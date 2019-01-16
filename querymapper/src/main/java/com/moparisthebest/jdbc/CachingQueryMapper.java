package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.util.ResultSetIterable;

import java.sql.*;
import java.util.*;
//IFJAVA8_START
import java.util.stream.Stream;
//IFJAVA8_END

import static com.moparisthebest.jdbc.TryClose.tryClose;

/**
 * This class caches the PreparedStatement's it creates for the strings you send in, then closes them when the close() method is called.
 * Since PreparedStatement is not thread-safe, this class cannot be either.  Be sure to call it from only a single thread
 * or synchronize around it.
 */
public class CachingQueryMapper extends QueryMapper {

	protected final Map<String, PreparedStatement> cache;

	protected CachingQueryMapper(Connection conn, String jndiName, Factory<Connection> factory, ResultSetMapper cm, final int maxEntries) {
		super(conn, jndiName, factory, cm);
		if (maxEntries > 0) { // we want a limited cache
			final float loadFactor = 0.75f; // default for HashMaps
			// if we set the initialCapacity this way, nothing should ever need re-sized
			final int initialCapacity = ((int) Math.ceil(maxEntries / loadFactor)) + 1;
			cache = new LinkedHashMap<String, PreparedStatement>(initialCapacity, loadFactor, true) {
				@Override
				protected boolean removeEldestEntry(Map.Entry<String, PreparedStatement> eldest) {
					final boolean remove = size() > maxEntries;
					if(remove){
						//System.out.printf("closing PreparedStatement '%s' with key '%s'\n", eldest.getValue(), eldest.getKey());
						tryClose(eldest.getValue());
					}
					return remove;
				}
			};
		} else
			cache = new HashMap<String, PreparedStatement>();
	}

	protected CachingQueryMapper(Connection conn, String jndiName, Factory<Connection> factory, ResultSetMapper cm) {
		this(conn, jndiName, factory, cm, 20); // default size of 20
	}

	public CachingQueryMapper(Connection conn, ResultSetMapper cm, final int maxEntries) {
		this(conn, null, null, cm, maxEntries);
	}

	public CachingQueryMapper(Connection conn, final int maxEntries) {
		this(conn, null, null, null, maxEntries);
	}

	public CachingQueryMapper(String jndiName, ResultSetMapper cm, final int maxEntries) {
		this(null, jndiName, null, cm, maxEntries);
	}

	public CachingQueryMapper(String jndiName, final int maxEntries) {
		this(null, jndiName, null, null, maxEntries);
	}

	public CachingQueryMapper(Factory<Connection> factory, ResultSetMapper cm, final int maxEntries) {
		this(null, null, factory, cm, maxEntries);
	}

	public CachingQueryMapper(Factory<Connection> factory, final int maxEntries) {
		this(null, null, factory, null, maxEntries);
	}

	public CachingQueryMapper(Connection conn, ResultSetMapper cm) {
		this(conn, null, null, cm);
	}

	public CachingQueryMapper(Connection conn) {
		this(conn, null, null, null);
	}

	public CachingQueryMapper(String jndiName, ResultSetMapper cm) {
		this(null, jndiName, null, cm);
	}

	public CachingQueryMapper(String jndiName) {
		this(null, jndiName, null, null);
	}

	public CachingQueryMapper(Factory<Connection> factory, ResultSetMapper cm) {
		this(null, null, factory, cm);
	}

	public CachingQueryMapper(Factory<Connection> factory) {
		this(null, null, factory, null);
	}

	/**
	 * This could perhaps end up caching something it shouldn't, as psf could return a different PreparedStatement
	 * for a given sql, we are going to assume this would never happen, if it could, don't use CachingQueryMapper or
	 * override this method, whatever you want.
	 */
	protected PreparedStatement getPreparedStatement(final String sql, final PreparedStatementFactory psf, final Object... bindObjects) throws SQLException {
		final String preparedSql = prepareInListSql(sql, bindObjects);
		PreparedStatement ps = cache.get(preparedSql);
		if (ps == null) {
			//System.out.println("cache miss");
			ps = psf.prepareStatement(conn, preparedSql);
			cache.put(preparedSql, ps);
		}
		//else System.out.println("cache hit");
		return ps;
	}

	public void clearCache(boolean close) {
		//System.out.println("cache size: "+cache.size());
		for (PreparedStatement ps : cache.values())
			tryClose(ps);
		if (close)
			super.close();
		else
			cache.clear();
	}

	public void clearCache() {
		this.clearCache(false);
	}

	@Override
	public void close() {
		this.clearCache(true);
	}

	@Override
	public int executeUpdate(String sql, Object... bindObjects) throws SQLException {
		return super.executeUpdate(getPreparedStatement(sql, bindObjects), bindObjects);
	}

	@Override
	public boolean executeUpdateSuccess(String sql, Object... bindObjects) throws SQLException {
		return super.executeUpdateSuccess(getPreparedStatement(sql, bindObjects), bindObjects);
	}

	@Override
	public Long insertGetGeneratedKey(String sql, Object... bindObjects) throws SQLException {
		return super.insertGetGeneratedKey(getPreparedStatement(sql, getSingleColumnPreparedStatementFactory(), bindObjects), bindObjects);
	}

	@Override
	public <T> T insertGetGeneratedKeyType(String sql, final PreparedStatementFactory psf, TypeReference<T> typeReference, Object... bindObjects) throws SQLException {
		return super.insertGetGeneratedKeyType(getPreparedStatement(sql, psf, bindObjects), typeReference, bindObjects);
	}

// these grab ResultSets from the database
	
	@Override
	public ResultSet toResultSet(final String sql, final PreparedStatementFactory psf, final Object... bindObjects) throws SQLException {
		return super.toResultSet(getPreparedStatement(sql, psf, bindObjects), bindObjects);
	}
	
	// DO NOT EDIT BELOW THIS LINE, OR CHANGE THIS COMMENT, CODE AUTOMATICALLY GENERATED BY genQueryMapper.sh

	@Override
	public <T> T toObject(String sql, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return super.toObject(getPreparedStatement(sql, bindObjects), componentType, bindObjects);
	}

	@Override
	public <T> ResultSetIterable<T> toResultSetIterable(String sql, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return super.toResultSetIterable(getPreparedStatement(sql, bindObjects), componentType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> ResultSetIterable<Map<String, V>> toResultSetIterable(String sql, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toResultSetIterable(getPreparedStatement(sql, bindObjects), componentType, mapValType, bindObjects);
	}

	//IFJAVA8_START

	@Override
	public <T> Stream<T> toStream(String sql, Class<T> componentType, final Object... bindObjects) throws SQLException {
		return super.toStream(getPreparedStatement(sql, bindObjects), componentType, bindObjects);
	}

	//IFJAVA8_END

	//IFJAVA8_START

	@Override
	public <T extends Map<String, V>, V> Stream<Map<String, V>> toStream(String sql, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toStream(getPreparedStatement(sql, bindObjects), componentType, mapValType, bindObjects);
	}

	//IFJAVA8_END

	@Override
	public <T extends Map<String, V>, V> Map<String, V> toSingleMap(String sql, Class<T> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toSingleMap(getPreparedStatement(sql, bindObjects), componentType, mapValType, bindObjects);
	}

	@Override
	public <V> Map<String, V> toSingleMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toSingleMap(getPreparedStatement(sql, bindObjects), mapValType, bindObjects);
	}

	@Override
	public <T> T toType(String sql, TypeReference<T> typeReference, final Object... bindObjects) throws SQLException {
		return super.toType(getPreparedStatement(sql, bindObjects), typeReference, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(String sql, final Class<T> collectionType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return super.toCollection(getPreparedStatement(sql, bindObjects), collectionType, componentType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E> T toCollection(String sql, T list, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return super.toCollection(getPreparedStatement(sql, bindObjects), list, componentType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E> T toMap(String sql, T map, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return super.toMap(getPreparedStatement(sql, bindObjects), map, mapKeyType, componentType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(String sql, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return super.toMapCollection(getPreparedStatement(sql, bindObjects), returnType, mapKeyType, collectionType, componentType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(String sql, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return super.toMapCollection(getPreparedStatement(sql, bindObjects), map, mapKeyType, collectionType, componentType, bindObjects);
	}

	@Override
	public <T> ListIterator<T> toListIterator(String sql, final Class<T> type, final Object... bindObjects) throws SQLException {
		return super.toListIterator(getPreparedStatement(sql, bindObjects), type, bindObjects);
	}

	@Override
	public <T> Iterator<T> toIterator(String sql, final Class<T> type, final Object... bindObjects) throws SQLException {
		return super.toIterator(getPreparedStatement(sql, bindObjects), type, bindObjects);
	}

	@Override
	public <T> T[] toArray(String sql, final Class<T> type, final Object... bindObjects) throws SQLException {
		return super.toArray(getPreparedStatement(sql, bindObjects), type, bindObjects);
	}

	@Override
	public <E> List<E> toList(String sql, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return super.toList(getPreparedStatement(sql, bindObjects), componentType, bindObjects);
	}

	@Override
	public <K, E> Map<K, E> toMap(String sql, Class<K> mapKeyType, Class<E> componentType, final Object... bindObjects) throws SQLException {
		return super.toMap(getPreparedStatement(sql, bindObjects), mapKeyType, componentType, bindObjects);
	}

	@Override
	public <K, E extends List<C>, C> Map<K, E> toMapList(String sql, Class<K> mapKeyType, Class<C> componentType, final Object... bindObjects) throws SQLException {
		return super.toMapList(getPreparedStatement(sql, bindObjects), mapKeyType, componentType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(String sql, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toCollectionMap(getPreparedStatement(sql, bindObjects), collectionType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(String sql, T list, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toCollectionMap(getPreparedStatement(sql, bindObjects), list, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(String sql, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toMapMap(getPreparedStatement(sql, bindObjects), returnType, mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(String sql, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toMapMap(getPreparedStatement(sql, bindObjects), map, mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(String sql, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toMapCollectionMap(getPreparedStatement(sql, bindObjects), returnType, mapKeyType, collectionType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(String sql, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toMapCollectionMap(getPreparedStatement(sql, bindObjects), map, mapKeyType, collectionType, componentType, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(String sql, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toListIteratorMap(getPreparedStatement(sql, bindObjects), type, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(String sql, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toIteratorMap(getPreparedStatement(sql, bindObjects), type, mapValType, bindObjects);
	}

	@Override
	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(String sql, final Class<T> type, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toArrayMap(getPreparedStatement(sql, bindObjects), type, mapValType, bindObjects);
	}

	@Override
	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(String sql, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toListMap(getPreparedStatement(sql, bindObjects), componentType, mapValType, bindObjects);
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(String sql, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toMapMap(getPreparedStatement(sql, bindObjects), mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(String sql, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toMapListMap(getPreparedStatement(sql, bindObjects), mapKeyType, componentType, mapValType, bindObjects);
	}

	@Override
	public <V> ListIterator<Map<String, V>> toListIteratorMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toListIteratorMap(getPreparedStatement(sql, bindObjects), mapValType, bindObjects);
	}

	@Override
	public <V> Iterator<Map<String, V>> toIteratorMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toIteratorMap(getPreparedStatement(sql, bindObjects), mapValType, bindObjects);
	}

	@Override
	public <V> List<Map<String, V>> toListMap(String sql, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toListMap(getPreparedStatement(sql, bindObjects), mapValType, bindObjects);
	}

	@Override
	public <K, V> Map<K, Map<String, V>> toMapMap(String sql, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toMapMap(getPreparedStatement(sql, bindObjects), mapKeyType, mapValType, bindObjects);
	}

	@Override
	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(String sql, Class<K> mapKeyType, Class<V> mapValType, final Object... bindObjects) throws SQLException {
		return super.toMapListMap(getPreparedStatement(sql, bindObjects), mapKeyType, mapValType, bindObjects);
	}

}

