package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.util.ResultSetIterable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
//IFJAVA8_START
import java.util.stream.Stream;
//IFJAVA8_END

public class NullQueryMapper extends QueryMapper {

	public static interface ThrowableHandler {
		void handle(Throwable e);
	}

	//IFJAVA8_START
	public static final ThrowableHandler safeHandler = e -> {throw new RuntimeException(e);}; // this is good, exceptions are propagated up stacks
	public static final ThrowableHandler verboseHandler = Throwable::printStackTrace; // this is bad, don't swallow exceptions!
	public static final ThrowableHandler quietHandler = e -> {}; // this is super bad
	//IFJAVA8_END

	/*IFJAVA6_START
	public static final ThrowableHandler safeHandler = new ThrowableHandler() {
		@Override
		public void handle(Throwable e) {
			throw new RuntimeException(e);
		}
	};
	public static final ThrowableHandler verboseHandler = new ThrowableHandler() {
		@Override
		public void handle(Throwable e) {
			e.printStackTrace();
		}
	};
	public static final ThrowableHandler quietHandler = new ThrowableHandler() {
		@Override
		public void handle(Throwable e) {

		}
	};
	IFJAVA6_END*/

	protected final ThrowableHandler handler;
	protected final QueryMapper delegate;
	protected final boolean closeDelegate;

	private NullQueryMapper(Connection conn, String jndiName, Factory<Connection> factory, QueryMapper delegate, ResultSetMapper cm, ThrowableHandler handler) {
		this.handler = handler == null ? safeHandler : handler;
		this.closeDelegate = delegate == null;
		this.delegate = this.closeDelegate ? new QueryMapper(conn, jndiName, factory, cm) : delegate;
	}

	private NullQueryMapper(Connection conn, String jndiName, Factory<Connection> factory, QueryMapper delegate, ResultSetMapper cm, boolean verbose) {
		this(conn, jndiName, factory, delegate, cm, verbose ? verboseHandler : quietHandler);
	}

	private NullQueryMapper(ThrowableHandler handler, QueryMapper delegate, boolean closeDelegate) {
		this.handler = handler;
		this.delegate = delegate;
		this.closeDelegate = closeDelegate;
	}

	public NullQueryMapper(QueryMapper delegate, boolean verbose) {
		this(null, null, null, delegate, null, verbose);
	}

	public NullQueryMapper(QueryMapper delegate) {
		this(null, null, null, delegate, null, true);
	}

	public NullQueryMapper(Connection conn, boolean verbose) {
		this(conn, null, null, null, null, verbose);
	}

	public NullQueryMapper(Connection conn, ResultSetMapper cm, boolean verbose) {
		this(conn, null, null, null, cm, verbose);
	}

	public NullQueryMapper(Connection conn) {
		this(conn, true);
	}

	public NullQueryMapper(Connection conn, ResultSetMapper cm) {
		this(conn, cm, true);
	}

	public NullQueryMapper(String jndiName, boolean verbose) {
		this(null, jndiName, null, null, null, verbose);
	}

	public NullQueryMapper(String jndiName, ResultSetMapper cm, boolean verbose) {
		this(null, jndiName, null, null, cm, verbose);
	}

	public NullQueryMapper(String jndiName) {
		this(jndiName, true);
	}

	public NullQueryMapper(String jndiName, ResultSetMapper cm) {
		this(jndiName, cm, true);
	}

	public NullQueryMapper(Factory<Connection> factory, boolean verbose) {
		this(null, null, factory, null, null, verbose);
	}

	public NullQueryMapper(Factory<Connection> factory, ResultSetMapper cm, boolean verbose) {
		this(null, null, factory, null, cm, verbose);
	}

	public NullQueryMapper(Factory<Connection> factory) {
		this(factory, true);
	}

	public NullQueryMapper(Factory<Connection> factory, ResultSetMapper cm) {
		this(factory, cm, true);
	}

	public static NullQueryMapper of(final Factory<QueryMapper> qmFactory, final ThrowableHandler handler) {
		try {
			return new NullQueryMapper(handler, qmFactory.create(), true);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	public static NullQueryMapper safe(final Factory<QueryMapper> qmFactory) {
		return of(qmFactory, safeHandler);
	}

	public static NullQueryMapper of(final QueryMapper qm, final ThrowableHandler handler) {
		try {
			return new NullQueryMapper(handler, qm, false);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	public static NullQueryMapper safe(final QueryMapper qm) {
		return of(qm, safeHandler);
	}

	@Override
	public <T> InList.InListObject inList(String columnName, Collection<T> values) {
		try {
			return delegate.inList(columnName, values);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> InList.InListObject notInList(String columnName, Collection<T> values) {
		try {
			return delegate.notInList(columnName, values);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	// these update the database

	@Override
	public int executeUpdate(PreparedStatement ps, Object... bindObjects) {
		try {
			return delegate.executeUpdate(ps, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return -1;
	}

	@Override
	public boolean executeUpdateSuccess(PreparedStatement ps, Object... bindObjects) {
		try {
			return delegate.executeUpdateSuccess(ps, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return false;
	}

	@Override
	public Long insertGetGeneratedKey(PreparedStatement ps, Object... bindObjects) {
		try {
			return delegate.insertGetGeneratedKey(ps, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> T insertGetGeneratedKeyType(PreparedStatement ps, TypeReference<T> typeReference, Object... bindObjects) {
		try {
			return delegate.insertGetGeneratedKeyType(ps, typeReference, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public int executeUpdate(String sql, Object... bindObjects) {
		try {
			return delegate.executeUpdate(sql, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return -1;
	}

	@Override
	public boolean executeUpdateSuccess(String sql, Object... bindObjects) {
		try {
			return delegate.executeUpdateSuccess(sql, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return false;
	}

	@Override
	public Long insertGetGeneratedKey(String sql, Object... bindObjects) {
		try {
			return delegate.insertGetGeneratedKey(sql, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> T insertGetGeneratedKeyType(String sql, TypeReference<T> typeReference, Object... bindObjects) {
		try {
			return delegate.insertGetGeneratedKeyType(sql, typeReference, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> T insertGetGeneratedKeyType(String sql, PreparedStatementFactory psf, TypeReference<T> typeReference, Object... bindObjects) {
		try {
			return delegate.insertGetGeneratedKeyType(sql, psf, typeReference, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

// these update the database using UpdateableDTOs

	@Override
	public int updateRows(UpdateableDTO dto) {
		try {
			return delegate.updateRows(dto);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return -1;
	}

	@Override
	public int updateRows(Collection<UpdateableDTO> dtos) {
		try {
			return delegate.updateRows(dtos);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return -1;
	}

	@Override
	public int updateRows(UpdateableDTO[] dtos) {
		try {
			return delegate.updateRows(dtos);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return -1;
	}

	@Override
	public int insertRows(UpdateableDTO dto) {
		try {
			return delegate.insertRows(dto);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return -1;
	}

	@Override
	public int insertRows(Collection<UpdateableDTO> dtos) {
		try {
			return delegate.insertRows(dtos);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return -1;
	}

	@Override
	public int insertRows(UpdateableDTO[] dtos) {
		try {
			return delegate.insertRows(dtos);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return -1;
	}

	// these grab ResultSets from the database

	@Override
	public ResultSet toResultSet(PreparedStatement ps, Object... bindObjects) {
		try {
			return delegate.toResultSet(ps, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public ResultSet toResultSet(String sql, Object... bindObjects) {
		try {
			return delegate.toResultSet(sql, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public ResultSet toResultSet(String sql, PreparedStatementFactory psf, Object... bindObjects) {
		try {
			return delegate.toResultSet(sql, psf, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

// these are standard getters

	@Override
	public ResultSetMapper getCustomResultSetMapper() {
		return delegate.getCustomResultSetMapper();
	}

	@Override
	public Connection getConnection() {
		return delegate.getConnection();
	}

	// these just delegate and change no functionality

	@Override
	public void close() {
		if(closeDelegate)
			delegate.close();
	}

	// and these are standard

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NullQueryMapper that = (NullQueryMapper) o;

		if (handler != null ? !handler.equals(that.handler) : that.handler != null) return false;
		return delegate != null ? delegate.equals(that.delegate) : that.delegate == null;
	}

	@Override
	public int hashCode() {
		int result = handler != null ? handler.hashCode() : 0;
		result = 31 * result + (delegate != null ? delegate.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "NullQueryMapper{" +
				"handler=" + handler +
				", delegate=" + delegate +
				"}";
	}

	// DO NOT EDIT BELOW THIS LINE, OR CHANGE THIS COMMENT, CODE AUTOMATICALLY GENERATED BY genQueryMapper.sh

	@Override
	public <T> T toObject(PreparedStatement query, Class<T> componentType, final Object... bindObjects) {
		try {
			return delegate.toObject(query, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> T toObject(String query, Class<T> componentType, final Object... bindObjects) {
		try {
			return delegate.toObject(query, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> ResultSetIterable<T> toResultSetIterable(PreparedStatement query, Class<T> componentType, final Object... bindObjects) {
		try {
			return delegate.toResultSetIterable(query, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> ResultSetIterable<T> toResultSetIterable(String query, Class<T> componentType, final Object... bindObjects) {
		try {
			return delegate.toResultSetIterable(query, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> ResultSetIterable<Map<String, V>> toResultSetIterable(PreparedStatement query, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toResultSetIterable(query, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> ResultSetIterable<Map<String, V>> toResultSetIterable(String query, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toResultSetIterable(query, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	//IFJAVA8_START

	@Override
	public <T> Stream<T> toStream(PreparedStatement query, Class<T> componentType, final Object... bindObjects) {
		try {
			return delegate.toStream(query, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> Stream<T> toStream(String query, Class<T> componentType, final Object... bindObjects) {
		try {
			return delegate.toStream(query, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	//IFJAVA8_END

	//IFJAVA8_START

	@Override
	public <T extends Map<String, V>, V> Stream<Map<String, V>> toStream(PreparedStatement query, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toStream(query, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> Stream<Map<String, V>> toStream(String query, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toStream(query, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	//IFJAVA8_END

	@Override
	public <T extends Map<String, V>, V> Map<String, V> toSingleMap(PreparedStatement query, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toSingleMap(query, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> Map<String, V> toSingleMap(String query, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toSingleMap(query, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <V> Map<String, V> toSingleMap(PreparedStatement query, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toSingleMap(query, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <V> Map<String, V> toSingleMap(String query, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toSingleMap(query, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> T toType(PreparedStatement query, TypeReference<T> typeReference, final Object... bindObjects) {
		try {
			return delegate.toType(query, typeReference, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> T toType(String query, TypeReference<T> typeReference, final Object... bindObjects) {
		try {
			return delegate.toType(query, typeReference, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(PreparedStatement query, final Class<T> collectionType, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toCollection(query, collectionType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(String query, final Class<T> collectionType, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toCollection(query, collectionType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(PreparedStatement query, T list, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toCollection(query, list, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(String query, T list, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toCollection(query, list, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E> T toMap(PreparedStatement query, T map, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toMap(query, map, mapKeyType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E> T toMap(String query, T map, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toMap(query, map, mapKeyType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(PreparedStatement query, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) {
		try {
			return delegate.toMapCollection(query, returnType, mapKeyType, collectionType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(String query, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) {
		try {
			return delegate.toMapCollection(query, returnType, mapKeyType, collectionType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(PreparedStatement query, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) {
		try {
			return delegate.toMapCollection(query, map, mapKeyType, collectionType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(String query, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) {
		try {
			return delegate.toMapCollection(query, map, mapKeyType, collectionType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> ListIterator<T> toListIterator(PreparedStatement query, final Class<T> type, final Object... bindObjects) {
		try {
			return delegate.toListIterator(query, type, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> ListIterator<T> toListIterator(String query, final Class<T> type, final Object... bindObjects) {
		try {
			return delegate.toListIterator(query, type, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> Iterator<T> toIterator(PreparedStatement query, final Class<T> type, final Object... bindObjects) {
		try {
			return delegate.toIterator(query, type, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> Iterator<T> toIterator(String query, final Class<T> type, final Object... bindObjects) {
		try {
			return delegate.toIterator(query, type, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> T[] toArray(PreparedStatement query, final Class<T> type, final Object... bindObjects) {
		try {
			return delegate.toArray(query, type, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T> T[] toArray(String query, final Class<T> type, final Object... bindObjects) {
		try {
			return delegate.toArray(query, type, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <E> List<E> toList(PreparedStatement query, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toList(query, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <E> List<E> toList(String query, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toList(query, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, E> Map<K, E> toMap(PreparedStatement query, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toMap(query, mapKeyType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, E> Map<K, E> toMap(String query, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) {
		try {
			return delegate.toMap(query, mapKeyType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, E extends List<C>, C> Map<K, E> toMapList(PreparedStatement query, Class<K> mapKeyType, Class<C> componentType, final Object... bindObjects) {
		try {
			return delegate.toMapList(query, mapKeyType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, E extends List<C>, C> Map<K, E> toMapList(String query, Class<K> mapKeyType, Class<C> componentType, final Object... bindObjects) {
		try {
			return delegate.toMapList(query, mapKeyType, componentType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(PreparedStatement query, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toCollectionMap(query, collectionType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(String query, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toCollectionMap(query, collectionType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(PreparedStatement query, T list, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toCollectionMap(query, list, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(String query, T list, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toCollectionMap(query, list, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(PreparedStatement query, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapMap(query, returnType, mapKeyType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(String query, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapMap(query, returnType, mapKeyType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(PreparedStatement query, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapMap(query, map, mapKeyType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(String query, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapMap(query, map, mapKeyType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(PreparedStatement query, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapCollectionMap(query, returnType, mapKeyType, collectionType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(String query, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapCollectionMap(query, returnType, mapKeyType, collectionType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(PreparedStatement query, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapCollectionMap(query, map, mapKeyType, collectionType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(String query, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapCollectionMap(query, map, mapKeyType, collectionType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(PreparedStatement query, final Class<T> type, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toListIteratorMap(query, type, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(String query, final Class<T> type, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toListIteratorMap(query, type, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(PreparedStatement query, final Class<T> type, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toIteratorMap(query, type, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(String query, final Class<T> type, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toIteratorMap(query, type, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(PreparedStatement query, final Class<T> type, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toArrayMap(query, type, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(String query, final Class<T> type, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toArrayMap(query, type, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(PreparedStatement query, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toListMap(query, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(String query, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toListMap(query, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(PreparedStatement query, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapMap(query, mapKeyType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(String query, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapMap(query, mapKeyType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(PreparedStatement query, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapListMap(query, mapKeyType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(String query, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapListMap(query, mapKeyType, componentType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <V> ListIterator<Map<String, V>> toListIteratorMap(PreparedStatement query, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toListIteratorMap(query, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <V> ListIterator<Map<String, V>> toListIteratorMap(String query, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toListIteratorMap(query, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <V> Iterator<Map<String, V>> toIteratorMap(PreparedStatement query, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toIteratorMap(query, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <V> Iterator<Map<String, V>> toIteratorMap(String query, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toIteratorMap(query, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <V> List<Map<String, V>> toListMap(PreparedStatement query, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toListMap(query, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <V> List<Map<String, V>> toListMap(String query, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toListMap(query, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, V> Map<K, Map<String, V>> toMapMap(PreparedStatement query, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapMap(query, mapKeyType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, V> Map<K, Map<String, V>> toMapMap(String query, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapMap(query, mapKeyType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(PreparedStatement query, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapListMap(query, mapKeyType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

	@Override
	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(String query, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) {
		try {
			return delegate.toMapListMap(query, mapKeyType, mapValType, bindObjects);
		} catch (Throwable e) {
			handler.handle(e);
		}
		return null;
	}

}

