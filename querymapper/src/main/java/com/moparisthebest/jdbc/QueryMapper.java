package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapper;
import com.moparisthebest.jdbc.codegen.JdbcMapperFactory;
import com.moparisthebest.jdbc.util.ResultSetIterable;
import com.moparisthebest.jdbc.util.ResultSetUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.*;
//IFJAVA8_START
import java.time.*;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
//IFJAVA8_END

import static com.moparisthebest.jdbc.TryClose.tryClose;

public class QueryMapper implements JdbcMapper {

	public static final Object noBind = new Object();
	public static final ResultSetMapper defaultRsm = new ResultSetMapper();

	protected static final int[] ORACLE_SINGLE_COLUMN_INDEX = new int[]{1};

	/*IFJAVA6_START
	private static final Charset UTF_8 = Charset.forName("UTF-8");
	IFJAVA6_END*/

	protected final ResultSetMapper cm;
	protected final Connection conn;
	protected final boolean closeConn;

	protected QueryMapper(Connection conn, final String jndiName, Factory<Connection> factory, final ResultSetMapper cm) {
		this.cm = cm == null ? defaultRsm : cm;
		boolean closeConn = false;
		if(conn == null) {
			if(factory == null && jndiName != null)
				factory = JdbcMapperFactory.connectionFactory(jndiName);
			if (factory != null) {
				try {
					conn = factory.create();
					closeConn = true;
				} catch (SQLException e) {
					throw new RuntimeException("factory failed to create connection", e);
				}
			}
		}
		if (conn == null)
			throw new NullPointerException("Connection needs to be non-null for QueryMapper...");
		this.conn = conn;
		this.closeConn = closeConn;
	}

	public QueryMapper(Connection conn, ResultSetMapper cm) {
		this(conn, null, null, cm);
	}

	public QueryMapper(Connection conn) {
		this(conn, null);
	}

	public QueryMapper(String jndiName, ResultSetMapper cm) {
		this(null, jndiName, null, cm);
	}

	public QueryMapper(String jndiName) {
		this(jndiName, null);
	}

	public QueryMapper(Factory<Connection> factory, ResultSetMapper cm) {
		this(null, null, factory, cm);
	}

	public QueryMapper(Factory<Connection> factory) {
		this(factory, null);
	}

	/**
	 * Only meant to be called by implementing classes
	 */
	protected QueryMapper() {
		this.cm = null;
		this.conn = null;
		this.closeConn = false;
	}

	@Override
	public void close() {
		if (closeConn) {
			tryClose(conn);
		}
	}

	private static class StringWrapper {
		public final String s;

		private StringWrapper(String s) {
			this.s = s;
		}

		public String toString() {
			return s;
		}

		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof StringWrapper)) return false;
			StringWrapper that = (StringWrapper) o;
			return !(s != null ? !s.equals(that.s) : that.s != null);
		}

		public int hashCode() {
			return s != null ? s.hashCode() : 0;
		}
	}

	private static class ClobString extends StringWrapper {
		private ClobString(String s) {
			super(s);
		}
	}

	private static class BlobString extends StringWrapper {
		private final Charset charset;
		private BlobString(final String s, final Charset charset) {
			super(s);
			this.charset = charset;
		}
	}

	public static Object wrapClob(String s) {
		return new ClobString(s);
	}

	public static Object wrapBlob(String s) {
		return new BlobString(s, UTF_8);
	}

	public static Object wrapBlob(final String s, final Charset charset) {
		return new BlobString(s, charset == null ? UTF_8 : charset);
	}

	public static void setObject(final PreparedStatement ps, final int index, final Object o) throws SQLException {
		// we are going to put most common ones up top so it should execute faster normally
		if (o == null || o instanceof String || o instanceof Number)
			ps.setObject(index, o);
			// java.util.Date support, put it in a Timestamp
		else if (o instanceof java.util.Date)
			ps.setObject(index, o.getClass().equals(java.util.Date.class) ? new java.sql.Timestamp(((java.util.Date)o).getTime()) : o);
		//IFJAVA8_START// todo: other java.time types
		else if (o instanceof Instant)
			ps.setObject(index, java.sql.Timestamp.from((Instant)o));
		else if (o instanceof LocalDateTime)
			ps.setObject(index, java.sql.Timestamp.valueOf((LocalDateTime)o));
		else if (o instanceof LocalDate)
			ps.setObject(index, java.sql.Date.valueOf((LocalDate)o));
		else if (o instanceof LocalTime)
			ps.setObject(index, java.sql.Time.valueOf((LocalTime)o));
		else if (o instanceof ZonedDateTime)
			ps.setObject(index, java.sql.Timestamp.from(((ZonedDateTime)o).toInstant()));
		else if (o instanceof OffsetDateTime)
			ps.setObject(index, java.sql.Timestamp.from(((OffsetDateTime)o).toInstant()));
		else if (o instanceof OffsetTime)
			ps.setObject(index, java.sql.Time.valueOf(((OffsetTime)o).toLocalTime())); // todo: no timezone?

		//IFJAVA8_END
			// CLOB support
		else if (o instanceof Reader)
			ps.setClob(index, (Reader) o);
		else if (o instanceof ClobString)
			ps.setClob(index, ((ClobString) o).s == null ? null : new StringReader(((ClobString) o).s));
		else if (o instanceof java.sql.Clob)
			ps.setClob(index, (java.sql.Clob) o);
			// BLOB support
		else if (o instanceof byte[])
			ps.setBlob(index, new ByteArrayInputStream((byte[]) o));
		else if (o instanceof InputStream)
			ps.setBlob(index, (InputStream) o);
		else if (o instanceof File)
			try {
				ps.setBlob(index, new FileInputStream((File) o)); // todo: does this close this or leak a file descriptor?
			} catch (FileNotFoundException e) {
				throw new SQLException("File to Blob FileNotFoundException", e);
			}
		else if (o instanceof BlobString)
			ps.setBlob(index, ((BlobString) o).s == null ? null : new ByteArrayInputStream(((BlobString) o).s.getBytes(((BlobString) o).charset)));
		else if (o instanceof java.sql.Blob)
			ps.setBlob(index, (java.sql.Blob) o);
		else if (o instanceof ArrayInList.ArrayListObject)
			ps.setArray(index, ((ArrayInList.ArrayListObject) o).getArray());
		else if (o instanceof java.sql.Array)
			ps.setArray(index, (java.sql.Array) o);
		else if (o instanceof Enum)
			ps.setObject(index, ((Enum)o).name());
		else
			ps.setObject(index, o); // probably won't get here ever, but just in case...
		/*
		switch(ps.getParameterMetaData().getParameterType(index)){ // 'java.sql.SQLException: Unsupported feature', fully JDBC 3.0 compliant my ass, freaking oracle...
			case Types.CLOB:
				if(o instanceof String)
					ps.setObject(index, o);
				else if (o instanceof Reader)
					ps.setClob(index, (Reader) o);
				else if (o instanceof Clob)
					ps.setClob(index, (Clob) o);
				return;
			case Types.BLOB:
				if (o instanceof byte[])
					ps.setBlob(index, new ByteArrayInputStream((byte[])o));
				else if (o instanceof InputStream)
					ps.setBlob(index, (InputStream) o);
				else if (o instanceof File)
					try {
						ps.setBlob(index, new FileInputStream((File) o));
					} catch (FileNotFoundException e) {
						throw new SQLException("File to Blob FileNotFoundException", e);
					}
				else if (o instanceof Blob)
					ps.setBlob(index, (Blob) o);
				else if(o instanceof String)
					try{
						ps.setBlob(index, new ByteArrayInputStream(((String) o).getBytes("UTF-8")));
					}catch(UnsupportedEncodingException e){
						throw new SQLException("String to Blob UnsupportedEncodingException", e);
					}
				return;
			default:
				ps.setObject(index, o);
		}
		*/
	}

	public static int recursiveBind(final PreparedStatement ps, final Object... bindObjects) throws SQLException {
		return recursiveBind(ps, 0, bindObjects);
	}

	private static int recursiveBind(final PreparedStatement ps, int index, final Object... bindObjects) throws SQLException {
		if (bindObjects != null && bindObjects.length > 0) {
			for (Object o : bindObjects) {
				if (o != null) {
					if (o == InList.InListObject.inEmpty || o == InList.InListObject.notInEmpty || o == noBind) {
						continue; // ignore
					} else if (o instanceof BindInList.BindInListObject) {
						if (((BindInList.BindInListObject) o).getBindObjects() != null)
							index = recursiveBind(ps, index, ((BindInList.BindInListObject) o).getBindObjects());
						continue;
					} else if (o instanceof Object[]) {
						index = recursiveBind(ps, index, (Object[]) o);
						continue;
					} else if (o instanceof Collection) {
						index = recursiveBind(ps, index, ((Collection) o).toArray());
						continue;
					}
				}
				//System.out.printf("index: '%d' bound to '%s'\n", index+1, o);
				setObject(ps, ++index, o);
				//ps.setObject(++index, o);
			}
		}
		return index;
	}

	public static PreparedStatement bindStatement(final PreparedStatement ps, final Object... bindObjects) throws SQLException {
		recursiveBind(ps, bindObjects);
		return ps;
	}

	protected static PreparedStatement bind(final PreparedStatement ps, final Object... bindObjects) throws SQLException {
		return bindStatement(ps, bindObjects);
	}

	protected static ResultSet bindExecute(final PreparedStatement ps, final Object... bindObjects) throws SQLException {
		return bind(ps, bindObjects).executeQuery();
	}

	// these update the database

	public int executeUpdate(PreparedStatement ps, final Object... bindObjects) throws SQLException {
		return bind(ps, bindObjects).executeUpdate();
	}

	public boolean executeUpdateSuccess(PreparedStatement ps, final Object... bindObjects) throws SQLException {
		return this.executeUpdate(ps, bindObjects) >= 0;
	}

	public Long insertGetGeneratedKey(final PreparedStatement ps, final Object... bindObjects) throws SQLException {
		this.executeUpdate(ps, bindObjects);
		ResultSet rs = null;
		try {
			rs = ps.getGeneratedKeys();
			if(rs.next())
				return ResultSetUtil.getObjectLong(rs, 1);
		} finally {
			tryClose(rs);
		}
		return null;
	}

	public <T> T insertGetGeneratedKeyType(final PreparedStatement ps, final TypeReference<T> typeReference, final Object... bindObjects) throws SQLException {
		this.executeUpdate(ps, bindObjects);
		ResultSet rs = null;
		try {
			rs = ps.getGeneratedKeys();
			if(rs.next())
				return cm.toType(rs, typeReference);
		} finally {
			tryClose(rs);
		}
		return null;
	}

	public int executeUpdate(String sql, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.executeUpdate(ps, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public boolean executeUpdateSuccess(String sql, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.executeUpdateSuccess(ps, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	private Boolean oracleDatabase = null;
	public Long insertGetGeneratedKey(final String sql, final Object... bindObjects) throws SQLException {
		// this single function is somewhat database specific
		// sqlite/ms-sql/mysql works with either Statement.RETURN_GENERATED_KEYS or int[]{1}
		// oracle requires int[]{1} instead, failing on Statement.RETURN_GENERATED_KEYS
		// postgre requires Statement.RETURN_GENERATED_KEYS instead, failing on int[]{1}

		// so we lazily cache oracleDatabase just in this one function
		if(oracleDatabase == null)
			oracleDatabase = conn.isWrapperFor(OptimalInList.oracleConnection);

		PreparedStatement ps = null;
		try {
			ps = oracleDatabase ? conn.prepareStatement(sql, ORACLE_SINGLE_COLUMN_INDEX) : conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			return this.insertGetGeneratedKey(ps, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T> T insertGetGeneratedKeyType(final String sql, final TypeReference<T> typeReference, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			return this.insertGetGeneratedKeyType(ps, typeReference, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	// these update the database using UpdateableDTOs

	public int updateRows(UpdateableDTO dto) throws SQLException {
		return dto.updateRow(this);
	}

	public int updateRows(Collection<UpdateableDTO> dtos) throws SQLException {
		int count = 0;
		if (dtos != null)
			for (UpdateableDTO dto : dtos)
				count += dto.updateRow(this);
		return count;
	}

	public int updateRows(UpdateableDTO[] dtos) throws SQLException {
		return updateRows(Arrays.asList(dtos));
	}

	public int insertRows(UpdateableDTO dto) throws SQLException {
		return dto.insertRow(this);
	}

	public int insertRows(Collection<UpdateableDTO> dtos) throws SQLException {
		int count = 0;
		if (dtos != null)
			for (UpdateableDTO dto : dtos)
				count += dto.insertRow(this);
		return count;
	}

	public int insertRows(UpdateableDTO[] dtos) throws SQLException {
		return insertRows(Arrays.asList(dtos));
	}

	// these grab ResultSets from the database

	public ResultSet toResultSet(PreparedStatement ps, final Object... bindObjects) throws SQLException {
		return bindExecute(ps, bindObjects);
	}

	public ResultSet toResultSet(String sql, final Object... bindObjects) throws SQLException {
		return toResultSet(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, bindObjects);
	}
	
	public ResultSet toResultSet(String sql, int rsType, int rsConcurrency, final Object... bindObjects) throws SQLException {
		// works with StatementClosingResultSet
		boolean error = true;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql, rsType, rsConcurrency);
			rs = this.toResultSet(ps, bindObjects);
			error = false;
			return new StatementClosingResultSet(rs, ps);
		} finally {
			if (error) {
				tryClose(rs);
				tryClose(ps);
			}
		}
	}

	// these are handled specially and not generated because we need it to hold open PreparedStatement until the ResultSetIterable is closed

	@SuppressWarnings("unchecked")
	public <T> T toType(String sql, TypeReference<T> typeReference, final Object... bindObjects) throws SQLException {
		boolean error = true, closePs = true;
		PreparedStatement ps = null;
		ResultSet rs = null;
		T ret = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = this.toResultSet(ps, bindObjects);
			ret = cm.toType(rs, typeReference);
			if(ret instanceof ResultSetIterable) {
				ret = (T)((ResultSetIterable)ret).setPreparedStatementToClose(ps);
				closePs = false;
			}
			//IFJAVA8_START
			else if(ret instanceof Stream) {
				final PreparedStatement finalPs = ps;
				ret = (T)((Stream)ret).onClose(() -> tryClose(finalPs));
				closePs = false;
			}
			//IFJAVA8_END
			else if(ret instanceof ResultSet) {
				ret = (T)new StatementClosingResultSet(rs, ps);
				closePs = false;
			}
			error = false;
			return ret;
		} finally {
			if (error) {
				if(ret != null) {
					if(ret instanceof ResultSet)
						tryClose((ResultSet)ret);
					else if(ret instanceof Closeable)
						tryClose((Closeable)ret);
					//IFJAVA8_START
					else if(ret instanceof AutoCloseable)
						tryClose((AutoCloseable)ret);
					//IFJAVA8_END
				}
				tryClose(rs);
				tryClose(ps);
			} else if(closePs)
				tryClose(ps);
		}
	}

	public <T> ResultSetIterable<T> toResultSetIterable(String sql, Class<T> componentType, final Object... bindObjects) throws SQLException {
		boolean error = true;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSetIterable<T> ret = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = this.toResultSet(ps, bindObjects);
			ret = cm.toResultSetIterable(rs, componentType).setPreparedStatementToClose(ps);
			error = false;
			return ret;
		} finally {
			if (error) {
				tryClose(ret);
				tryClose(rs);
				tryClose(ps);
			}
		}
	}

	public <T extends Map<String, V>, V> ResultSetIterable<Map<String, V>> toResultSetIterable(String sql, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		boolean error = true;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSetIterable<Map<String, V>> ret = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = this.toResultSet(ps, bindObjects);
			ret = cm.toResultSetIterable(rs, componentType, mapValType).setPreparedStatementToClose(ps);
			error = false;
			return ret;
		} finally {
			if (error) {
				tryClose(ret);
				tryClose(rs);
				tryClose(ps);
			}
		}
	}

	//IFJAVA8_START
	public <T> Stream<T> toStream(String sql, Class<T> componentType, final Object... bindObjects) throws SQLException {
		boolean error = true;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Stream<T> ret = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = this.toResultSet(ps, bindObjects);
			final PreparedStatement finalPs = ps;
			ret = cm.toStream(rs, componentType).onClose(() -> tryClose(finalPs));
			error = false;
			return ret;
		} finally {
			if (error) {
				tryClose(ret);
				tryClose(rs);
				tryClose(ps);
			}
		}
	}

	public <T extends Map<String, V>, V> Stream<Map<String, V>> toStream(String sql, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		boolean error = true;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Stream<Map<String, V>> ret = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = this.toResultSet(ps, bindObjects);
			final PreparedStatement finalPs = ps;
			ret = cm.toStream(rs, componentType, mapValType).onClose(() -> tryClose(finalPs));
			error = false;
			return ret;
		} finally {
			if (error) {
				tryClose(ret);
				tryClose(rs);
				tryClose(ps);
			}
		}
	}
	//IFJAVA8_END

	// these are standard getters

	public ResultSetMapper getCustomResultSetMapper() {
		return cm;
	}

	public Connection getConnection() {
		return conn;
	}

	// DO NOT EDIT BELOW THIS LINE, OR CHANGE THIS COMMENT, CODE AUTOMATICALLY GENERATED BY genQueryMapper.sh

	public <T> T toObject(PreparedStatement ps, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return cm.toObject(bindExecute(ps, bindObjects), componentType);
	}

	public <T> T toObject(String sql, Class<T> componentType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toObject(ps, componentType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T> ResultSetIterable<T> toResultSetIterable(PreparedStatement ps, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return cm.toResultSetIterable(bindExecute(ps, bindObjects), componentType);
	}

	public <T extends Map<String, V>, V> ResultSetIterable<Map<String, V>> toResultSetIterable(PreparedStatement ps, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toResultSetIterable(bindExecute(ps, bindObjects), componentType, mapValType);
	}

	//IFJAVA8_START

	public <T> Stream<T> toStream(PreparedStatement ps, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return cm.toStream(bindExecute(ps, bindObjects), componentType);
	}

	//IFJAVA8_END

	//IFJAVA8_START

	public <T extends Map<String, V>, V> Stream<Map<String, V>> toStream(PreparedStatement ps, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toStream(bindExecute(ps, bindObjects), componentType, mapValType);
	}

	//IFJAVA8_END

	public <T extends Map<String, V>, V> Map<String, V> toSingleMap(PreparedStatement ps, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toSingleMap(bindExecute(ps, bindObjects), componentType, mapValType);
	}

	public <T extends Map<String, V>, V> Map<String, V> toSingleMap(String sql, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toSingleMap(ps, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <V> Map<String, V> toSingleMap(PreparedStatement ps, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toSingleMap(bindExecute(ps, bindObjects), mapValType);
	}

	public <V> Map<String, V> toSingleMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toSingleMap(ps, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T> T toType(PreparedStatement ps, TypeReference<T> typeReference, final Object... bindObjects) throws SQLException {
		return cm.toType(bindExecute(ps, bindObjects), typeReference);
	}

	public <T extends Collection<E>, E> T toCollection(PreparedStatement ps, final Class<T> collectionType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return cm.toCollection(bindExecute(ps, bindObjects), collectionType, componentType);
	}

	public <T extends Collection<E>, E> T toCollection(String sql, final Class<T> collectionType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toCollection(ps, collectionType, componentType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Collection<E>, E> T toCollection(PreparedStatement ps, T list, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return cm.toCollection(bindExecute(ps, bindObjects), list, componentType);
	}

	public <T extends Collection<E>, E> T toCollection(String sql, T list, Class<E> componentType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toCollection(ps, list, componentType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<K, E>, K, E> T toMap(PreparedStatement ps, T map, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return cm.toMap(bindExecute(ps, bindObjects), map, mapKeyType, componentType);
	}

	public <T extends Map<K, E>, K, E> T toMap(String sql, T map, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMap(ps, map, mapKeyType, componentType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(PreparedStatement ps, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return cm.toMapCollection(bindExecute(ps, bindObjects), returnType, mapKeyType, collectionType, componentType);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(String sql, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapCollection(ps, returnType, mapKeyType, collectionType, componentType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(PreparedStatement ps, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return cm.toMapCollection(bindExecute(ps, bindObjects), map, mapKeyType, collectionType, componentType);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(String sql, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapCollection(ps, map, mapKeyType, collectionType, componentType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T> ListIterator<T> toListIterator(PreparedStatement ps, final Class<T> type, final Object... bindObjects) throws SQLException {
		return cm.toListIterator(bindExecute(ps, bindObjects), type);
	}

	public <T> ListIterator<T> toListIterator(String sql, final Class<T> type, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toListIterator(ps, type, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T> Iterator<T> toIterator(PreparedStatement ps, final Class<T> type, final Object... bindObjects) throws SQLException {
		return cm.toIterator(bindExecute(ps, bindObjects), type);
	}

	public <T> Iterator<T> toIterator(String sql, final Class<T> type, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toIterator(ps, type, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T> T[] toArray(PreparedStatement ps, final Class<T> type, final Object... bindObjects) throws SQLException {
		return cm.toArray(bindExecute(ps, bindObjects), type);
	}

	public <T> T[] toArray(String sql, final Class<T> type, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toArray(ps, type, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <E> List<E> toList(PreparedStatement ps, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return cm.toList(bindExecute(ps, bindObjects), componentType);
	}

	public <E> List<E> toList(String sql, Class<E> componentType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toList(ps, componentType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <K, E> Map<K, E> toMap(PreparedStatement ps, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return cm.toMap(bindExecute(ps, bindObjects), mapKeyType, componentType);
	}

	public <K, E> Map<K, E> toMap(String sql, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMap(ps, mapKeyType, componentType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <K, E extends List<C>, C> Map<K, E> toMapList(PreparedStatement ps, Class<K> mapKeyType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return cm.toMapList(bindExecute(ps, bindObjects), mapKeyType, componentType);
	}

	public <K, E extends List<C>, C> Map<K, E> toMapList(String sql, Class<K> mapKeyType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapList(ps, mapKeyType, componentType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(PreparedStatement ps, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toCollectionMap(bindExecute(ps, bindObjects), collectionType, componentType, mapValType);
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(String sql, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toCollectionMap(ps, collectionType, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(PreparedStatement ps, T list, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toCollectionMap(bindExecute(ps, bindObjects), list, componentType, mapValType);
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(String sql, T list, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toCollectionMap(ps, list, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(PreparedStatement ps, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toMapMap(bindExecute(ps, bindObjects), returnType, mapKeyType, componentType, mapValType);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(String sql, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapMap(ps, returnType, mapKeyType, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(PreparedStatement ps, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toMapMap(bindExecute(ps, bindObjects), map, mapKeyType, componentType, mapValType);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(String sql, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapMap(ps, map, mapKeyType, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(PreparedStatement ps, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toMapCollectionMap(bindExecute(ps, bindObjects), returnType, mapKeyType, collectionType, componentType, mapValType);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(String sql, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapCollectionMap(ps, returnType, mapKeyType, collectionType, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(PreparedStatement ps, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toMapCollectionMap(bindExecute(ps, bindObjects), map, mapKeyType, collectionType, componentType, mapValType);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(String sql, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapCollectionMap(ps, map, mapKeyType, collectionType, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(PreparedStatement ps, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toListIteratorMap(bindExecute(ps, bindObjects), type, mapValType);
	}

	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(String sql, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toListIteratorMap(ps, type, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(PreparedStatement ps, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toIteratorMap(bindExecute(ps, bindObjects), type, mapValType);
	}

	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(String sql, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toIteratorMap(ps, type, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(PreparedStatement ps, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toArrayMap(bindExecute(ps, bindObjects), type, mapValType);
	}

	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(String sql, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toArrayMap(ps, type, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(PreparedStatement ps, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toListMap(bindExecute(ps, bindObjects), componentType, mapValType);
	}

	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(String sql, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toListMap(ps, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(PreparedStatement ps, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toMapMap(bindExecute(ps, bindObjects), mapKeyType, componentType, mapValType);
	}

	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(String sql, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapMap(ps, mapKeyType, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(PreparedStatement ps, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toMapListMap(bindExecute(ps, bindObjects), mapKeyType, componentType, mapValType);
	}

	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(String sql, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapListMap(ps, mapKeyType, componentType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <V> ListIterator<Map<String, V>> toListIteratorMap(PreparedStatement ps, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toListIteratorMap(bindExecute(ps, bindObjects), mapValType);
	}

	public <V> ListIterator<Map<String, V>> toListIteratorMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toListIteratorMap(ps, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <V> Iterator<Map<String, V>> toIteratorMap(PreparedStatement ps, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toIteratorMap(bindExecute(ps, bindObjects), mapValType);
	}

	public <V> Iterator<Map<String, V>> toIteratorMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toIteratorMap(ps, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <V> List<Map<String, V>> toListMap(PreparedStatement ps, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toListMap(bindExecute(ps, bindObjects), mapValType);
	}

	public <V> List<Map<String, V>> toListMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toListMap(ps, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <K, V> Map<K, Map<String, V>> toMapMap(PreparedStatement ps, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toMapMap(bindExecute(ps, bindObjects), mapKeyType, mapValType);
	}

	public <K, V> Map<K, Map<String, V>> toMapMap(String sql, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapMap(ps, mapKeyType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(PreparedStatement ps, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return cm.toMapListMap(bindExecute(ps, bindObjects), mapKeyType, mapValType);
	}

	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(String sql, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return this.toMapListMap(ps, mapKeyType, mapValType, bindObjects);
		} finally {
			tryClose(ps);
		}
	}

}

