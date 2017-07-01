package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.util.ResultSetIterable;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
//IFJAVA8_START
import java.util.stream.Stream;
//IFJAVA8_END

public class ListQueryMapper extends QueryMapper {

	private static final InList defaultInList;

	static {
		InList def = null;
		try {
			final Class<?> ensureContext = Class.forName(System.getProperty("QueryMapper.defaultInList.class", "com.moparisthebest.jdbc.BindInList"));
			final Method method = ensureContext.getMethod(System.getProperty("QueryMapper.defaultInList.method", "instance"));
			def = (InList) method.invoke(null);
		} catch (Throwable e) {
			// NEVER ignore
			throw new RuntimeException(e);
		}
		defaultInList = def;
	}

	protected final QueryMapper delegate;
	protected final InList inList;

	public static final String inListReplace = "{inList}";

	private ListQueryMapper(Connection conn, String jndiName, Factory<Connection> factory, QueryMapper delegate, ResultSetMapper cm, InList inList) {
		this.inList = inList;
		this.delegate = delegate == null ? new QueryMapper(conn, jndiName, factory, cm) :
				(delegate instanceof ListQueryMapper ? ((ListQueryMapper)delegate).delegate : delegate);
	}

	public ListQueryMapper(QueryMapper delegate, InList inList) {
		this(null, null, null, delegate, null, inList);
	}

	public ListQueryMapper(QueryMapper delegate) {
		this(null, null, null, delegate, null, defaultInList);
	}

	public ListQueryMapper(Connection conn, InList inList) {
		this(conn, null, null, null, null, inList);
	}

	public ListQueryMapper(Connection conn, ResultSetMapper cm, InList inList) {
		this(conn, null, null, null, cm, inList);
	}

	public ListQueryMapper(Connection conn) {
		this(conn, defaultInList);
	}

	public ListQueryMapper(Connection conn, ResultSetMapper cm) {
		this(conn, cm, defaultInList);
	}

	public ListQueryMapper(String jndiName, InList inList) {
		this(null, jndiName, null, null, null, inList);
	}

	public ListQueryMapper(String jndiName, ResultSetMapper cm, InList inList) {
		this(null, jndiName, null, null, cm, inList);
	}

	public ListQueryMapper(String jndiName) {
		this(jndiName, defaultInList);
	}

	public ListQueryMapper(String jndiName, ResultSetMapper cm) {
		this(jndiName, cm, defaultInList);
	}

	public ListQueryMapper(Factory<Connection> factory, InList inList) {
		this(null, null, factory, null, null, inList);
	}

	public ListQueryMapper(Factory<Connection> factory, ResultSetMapper cm, InList inList) {
		this(null, null, factory, null, cm, inList);
	}

	public ListQueryMapper(Factory<Connection> factory) {
		this(factory, defaultInList);
	}

	public ListQueryMapper(Factory<Connection> factory, ResultSetMapper cm) {
		this(factory, cm, defaultInList);
	}

	// todo: get rid of wrap, cause, how do you know to close it or not? :'(
	public static ListQueryMapper wrap(final QueryMapper qm){
		return qm instanceof ListQueryMapper ? (ListQueryMapper)qm : new ListQueryMapper(qm);
	}

	public static ListQueryMapper wrap(final QueryMapper qm, final InList inList){
		return qm instanceof ListQueryMapper && ((ListQueryMapper)qm).inList == inList ? (ListQueryMapper)qm : new ListQueryMapper(qm, inList);
	}

	public <T> InList.InListObject inList(final String columnName, final Collection<T> values) throws SQLException {
		return this.inList.inList(delegate.conn, columnName, values);
	}

	// these update the database

	@Override
	public int executeUpdate(final PreparedStatement ps, final Object... bindObjects) throws SQLException {
		return delegate.executeUpdate(ps, bindObjects);
	}

	@Override
	public boolean executeUpdateSuccess(final PreparedStatement ps, final Object... bindObjects) throws SQLException {
		return delegate.executeUpdateSuccess(ps, bindObjects);
	}

	// these update the database using UpdateableDTOs

	@Override
	public int updateRows(final UpdateableDTO dto) throws SQLException {
		return delegate.updateRows(dto);
	}

	@Override
	public int updateRows(final Collection<UpdateableDTO> dtos) throws SQLException {
		return delegate.updateRows(dtos);
	}

	@Override
	public int updateRows(final UpdateableDTO[] dtos) throws SQLException {
		return delegate.updateRows(dtos);
	}

	@Override
	public int insertRows(final UpdateableDTO dto) throws SQLException {
		return delegate.insertRows(dto);
	}

	@Override
	public int insertRows(final Collection<UpdateableDTO> dtos) throws SQLException {
		return delegate.insertRows(dtos);
	}

	@Override
	public int insertRows(final UpdateableDTO[] dtos) throws SQLException {
		return delegate.insertRows(dtos);
	}

	// these grab ResultSets from the database

	@Override
	public ResultSet toResultSet(PreparedStatement ps, Object... bindObjects) throws SQLException {
		return delegate.toResultSet(ps, bindObjects);
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
		delegate.close();
	}

	// and these are standard

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final ListQueryMapper that = (ListQueryMapper) o;

		if (delegate != null ? !delegate.equals(that.delegate) : that.delegate != null) return false;
		if (inList != null ? !inList.equals(that.inList) : that.inList != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = delegate != null ? delegate.hashCode() : 0;
		result = 31 * result + (inList != null ? inList.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ListQueryMapper{" +
				"delegate=" + delegate +
				", inList=" + inList +
				"} " + super.toString();
	}

	// begin of ListQueryMapper specific methods

	private static StringBuilder recursiveReplace(final StringBuilder sb, final Object... bindObjects) {
		if (bindObjects != null && bindObjects.length > 0) {
			for (Object o : bindObjects) {
				if (o != null && o != QueryMapper.noBind) {
					if (o instanceof InList.InListObject) {
						final int startIndex = sb.indexOf(inListReplace);
						if (startIndex < -1)
							return sb; // we've replaced all, maybe an error? meh
						sb.replace(startIndex, startIndex + inListReplace.length(), o.toString());
					} else if (o instanceof Object[]) {
						recursiveReplace(sb, (Object[]) o);
					} else if (o instanceof Collection) {
						recursiveReplace(sb, ((Collection) o).toArray());
					}
				}
			}
		}
		return sb;
	}

	protected String prepareSql(final String sql, final Object... bindObjects) {
		return !sql.contains(inListReplace) ? sql : recursiveReplace(new StringBuilder(sql), bindObjects).toString();
	}

	@Override
	public int executeUpdate(final String sql, final Object... bindObjects) throws SQLException {
		return delegate.executeUpdate(prepareSql(sql, bindObjects), bindObjects);
	}

	@Override
	public boolean executeUpdateSuccess(final String sql, final Object... bindObjects) throws SQLException {
		return delegate.executeUpdateSuccess(prepareSql(sql, bindObjects), bindObjects);
	}

	@Override
	public ResultSet toResultSet(String sql, Object... bindObjects) throws SQLException {
		return delegate.toResultSet(prepareSql(sql, bindObjects), bindObjects);
	}

	// DO NOT EDIT BELOW THIS LINE, OR CHANGE THIS COMMENT, CODE AUTOMATICALLY GENERATED BY genQueryMapper.sh

	@Override
	public <T> T toObject(PreparedStatement ps, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toObject(ps, componentType, bindObjects);
	}

	@Override
	public <T> T toObject(String sql, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toObject(prepareSql(sql, bindObjects), componentType, bindObjects);
	}

	@Override
	public <T> ResultSetIterable<T> toResultSetIterable(PreparedStatement ps, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toResultSetIterable(ps, componentType, bindObjects);
	}

	@Override
	public <T> ResultSetIterable<T> toResultSetIterable(String sql, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toResultSetIterable(prepareSql(sql, bindObjects), componentType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> ResultSetIterable<Map<String, V>> toResultSetIterable(PreparedStatement ps, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toResultSetIterable(ps, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> ResultSetIterable<Map<String, V>> toResultSetIterable(String sql, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toResultSetIterable(prepareSql(sql, bindObjects), componentType, mapValType, bindObjects);
	}

	//IFJAVA8_START

	@Override
	public <T> Stream<T> toStream(PreparedStatement ps, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toStream(ps, componentType, bindObjects);
	}

	@Override
	public <T> Stream<T> toStream(String sql, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toStream(prepareSql(sql, bindObjects), componentType, bindObjects);
	}

	//IFJAVA8_END

	//IFJAVA8_START

	@Override
	public <T extends Map<String, V>, V> Stream<Map<String, V>> toStream(PreparedStatement ps, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toStream(ps, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> Stream<Map<String, V>> toStream(String sql, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toStream(prepareSql(sql, bindObjects), componentType, mapValType, bindObjects);
	}

	//IFJAVA8_END

	@Override
	public <T extends Map<String, V>, V> Map<String, V> toSingleMap(PreparedStatement ps, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toSingleMap(ps, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> Map<String, V> toSingleMap(String sql, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toSingleMap(prepareSql(sql, bindObjects), componentType, mapValType, bindObjects);
	}

	@Override
	public <V> Map<String, V> toSingleMap(PreparedStatement ps, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toSingleMap(ps, mapValType, bindObjects);
	}

	@Override
	public <V> Map<String, V> toSingleMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toSingleMap(prepareSql(sql, bindObjects), mapValType, bindObjects);
	}

	@Override
	public <T> T toType(PreparedStatement ps, TypeReference<T> typeReference, final Object... bindObjects) throws SQLException {
		return delegate.toType(ps, typeReference, bindObjects);
	}

	@Override
	public <T> T toType(String sql, TypeReference<T> typeReference, final Object... bindObjects) throws SQLException {
		return delegate.toType(prepareSql(sql, bindObjects), typeReference, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(PreparedStatement ps, final Class<T> collectionType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toCollection(ps, collectionType, componentType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(String sql, final Class<T> collectionType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toCollection(prepareSql(sql, bindObjects), collectionType, componentType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(PreparedStatement ps, T list, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toCollection(ps, list, componentType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(String sql, T list, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toCollection(prepareSql(sql, bindObjects), list, componentType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E> T toMap(PreparedStatement ps, T map, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMap(ps, map, mapKeyType, componentType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E> T toMap(String sql, T map, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMap(prepareSql(sql, bindObjects), map, mapKeyType, componentType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(PreparedStatement ps, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMapCollection(ps, returnType, mapKeyType, collectionType, componentType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(String sql, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMapCollection(prepareSql(sql, bindObjects), returnType, mapKeyType, collectionType, componentType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(PreparedStatement ps, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMapCollection(ps, map, mapKeyType, collectionType, componentType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(String sql, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMapCollection(prepareSql(sql, bindObjects), map, mapKeyType, collectionType, componentType, bindObjects);
	}

	@Override
	public <T> ListIterator<T> toListIterator(PreparedStatement ps, final Class<T> type, final Object... bindObjects) throws SQLException {
		return delegate.toListIterator(ps, type, bindObjects);
	}

	@Override
	public <T> ListIterator<T> toListIterator(String sql, final Class<T> type, final Object... bindObjects) throws SQLException {
		return delegate.toListIterator(prepareSql(sql, bindObjects), type, bindObjects);
	}

	@Override
	public <T> Iterator<T> toIterator(PreparedStatement ps, final Class<T> type, final Object... bindObjects) throws SQLException {
		return delegate.toIterator(ps, type, bindObjects);
	}

	@Override
	public <T> Iterator<T> toIterator(String sql, final Class<T> type, final Object... bindObjects) throws SQLException {
		return delegate.toIterator(prepareSql(sql, bindObjects), type, bindObjects);
	}

	@Override
	public <T> T[] toArray(PreparedStatement ps, final Class<T> type, final Object... bindObjects) throws SQLException {
		return delegate.toArray(ps, type, bindObjects);
	}

	@Override
	public <T> T[] toArray(String sql, final Class<T> type, final Object... bindObjects) throws SQLException {
		return delegate.toArray(prepareSql(sql, bindObjects), type, bindObjects);
	}

	@Override
	public <E> List<E> toList(PreparedStatement ps, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toList(ps, componentType, bindObjects);
	}

	@Override
	public <E> List<E> toList(String sql, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toList(prepareSql(sql, bindObjects), componentType, bindObjects);
	}

	@Override
	public <K, E> Map<K, E> toMap(PreparedStatement ps, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMap(ps, mapKeyType, componentType, bindObjects);
	}

	@Override
	public <K, E> Map<K, E> toMap(String sql, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMap(prepareSql(sql, bindObjects), mapKeyType, componentType, bindObjects);
	}

	@Override
	public <K, E extends List<C>, C> Map<K, E> toMapList(PreparedStatement ps, Class<K> mapKeyType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMapList(ps, mapKeyType, componentType, bindObjects);
	}

	@Override
	public <K, E extends List<C>, C> Map<K, E> toMapList(String sql, Class<K> mapKeyType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return delegate.toMapList(prepareSql(sql, bindObjects), mapKeyType, componentType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(PreparedStatement ps, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toCollectionMap(ps, collectionType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(String sql, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toCollectionMap(prepareSql(sql, bindObjects), collectionType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(PreparedStatement ps, T list, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toCollectionMap(ps, list, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(String sql, T list, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toCollectionMap(prepareSql(sql, bindObjects), list, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(PreparedStatement ps, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapMap(ps, returnType, mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(String sql, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapMap(prepareSql(sql, bindObjects), returnType, mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(PreparedStatement ps, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapMap(ps, map, mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(String sql, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapMap(prepareSql(sql, bindObjects), map, mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(PreparedStatement ps, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapCollectionMap(ps, returnType, mapKeyType, collectionType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(String sql, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapCollectionMap(prepareSql(sql, bindObjects), returnType, mapKeyType, collectionType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(PreparedStatement ps, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapCollectionMap(ps, map, mapKeyType, collectionType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(String sql, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapCollectionMap(prepareSql(sql, bindObjects), map, mapKeyType, collectionType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(PreparedStatement ps, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toListIteratorMap(ps, type, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(String sql, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toListIteratorMap(prepareSql(sql, bindObjects), type, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(PreparedStatement ps, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toIteratorMap(ps, type, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(String sql, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toIteratorMap(prepareSql(sql, bindObjects), type, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(PreparedStatement ps, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toArrayMap(ps, type, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(String sql, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toArrayMap(prepareSql(sql, bindObjects), type, mapValType, bindObjects);
	}

	@Override
	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(PreparedStatement ps, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toListMap(ps, componentType, mapValType, bindObjects);
	}

	@Override
	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(String sql, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toListMap(prepareSql(sql, bindObjects), componentType, mapValType, bindObjects);
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(PreparedStatement ps, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapMap(ps, mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(String sql, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapMap(prepareSql(sql, bindObjects), mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(PreparedStatement ps, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapListMap(ps, mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(String sql, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapListMap(prepareSql(sql, bindObjects), mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <V> ListIterator<Map<String, V>> toListIteratorMap(PreparedStatement ps, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toListIteratorMap(ps, mapValType, bindObjects);
	}

	@Override
	public <V> ListIterator<Map<String, V>> toListIteratorMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toListIteratorMap(prepareSql(sql, bindObjects), mapValType, bindObjects);
	}

	@Override
	public <V> Iterator<Map<String, V>> toIteratorMap(PreparedStatement ps, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toIteratorMap(ps, mapValType, bindObjects);
	}

	@Override
	public <V> Iterator<Map<String, V>> toIteratorMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toIteratorMap(prepareSql(sql, bindObjects), mapValType, bindObjects);
	}

	@Override
	public <V> List<Map<String, V>> toListMap(PreparedStatement ps, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toListMap(ps, mapValType, bindObjects);
	}

	@Override
	public <V> List<Map<String, V>> toListMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toListMap(prepareSql(sql, bindObjects), mapValType, bindObjects);
	}

	@Override
	public <K, V> Map<K, Map<String, V>> toMapMap(PreparedStatement ps, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapMap(ps, mapKeyType, mapValType, bindObjects);
	}

	@Override
	public <K, V> Map<K, Map<String, V>> toMapMap(String sql, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapMap(prepareSql(sql, bindObjects), mapKeyType, mapValType, bindObjects);
	}

	@Override
	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(PreparedStatement ps, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapListMap(ps, mapKeyType, mapValType, bindObjects);
	}

	@Override
	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(String sql, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return delegate.toMapListMap(prepareSql(sql, bindObjects), mapKeyType, mapValType, bindObjects);
	}

}

