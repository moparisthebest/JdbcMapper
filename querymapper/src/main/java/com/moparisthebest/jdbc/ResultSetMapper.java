package com.moparisthebest.jdbc;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */

import com.moparisthebest.jdbc.util.ResultSetIterable;

import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
//IFJAVA8_START
import java.util.stream.Stream;
//IFJAVA8_END

/**
 * Default ResultSetMapper implementation for Objects.
 * <p/>
 * This class is heavily modified from org.apache.beehive.controls.system.jdbc.DefaultObjectResultSetMapper
 * <p/>
 * This uses CustomRowToObjectMapper to perform the actual mapping to individual classes, so look to that
 * implementation for details.
 * <p/>
 * This supports mapping to a single object, or collections of Objects, currently it supports:
 * 1. arrays
 * 2. anything implementing java.util.Collection with a public no-arg constructor
 * 3. anything implementing java.util.Map with a public no-arg constructor
 * a. The first column in the ResultSet will be the Map's key
 * b. If there are only two columns, the second will be the Map's value
 * c. If there are more than two columns, the value will be mapped to an object with the entire ResultSet in it,
 * including the key, just like returning a Collection would do
 * 4. Iterators returned by a Collection, or ListIterators returned by a List
 * <p/>
 * It has sensible default implementations to return if you specify an interface, look in Map 'interfaceToConcrete'
 * for the default values returned for specific interfaces.  If not specified in there, Map's default is HashMap,
 * while Collection's default is ArrayList.
 * <p/>
 * Anything else must be a concrete class with a public no-arg constructor to instantiate it
 *
 * @author Travis Burtrum (modifications from beehive)
 */
public class ResultSetMapper implements RowMapperProvider {

	public static final Map<Class, Class> interfaceToConcrete = Collections.unmodifiableMap(new HashMap<Class, Class>() {{
		// Collection's
		put(Collection.class, ArrayList.class);
		// List
		put(List.class, ArrayList.class);
		// Set's
		put(Set.class, HashSet.class);
		// SortedSet's
		put(SortedSet.class, TreeSet.class);
		put(NavigableSet.class, ConcurrentSkipListSet.class);
		// Queue's
		put(Queue.class, LinkedList.class);
		put(Deque.class, LinkedList.class);
		// BlockingQueue's
		put(BlockingQueue.class, LinkedBlockingQueue.class);
		put(BlockingDeque.class, LinkedBlockingDeque.class);

		// Map's
		put(Map.class, HashMap.class);
		// ConcurrentMap's
		put(ConcurrentMap.class, ConcurrentHashMap.class);
		put(ConcurrentNavigableMap.class, ConcurrentSkipListMap.class);
		// SortedMap's
		put(SortedMap.class, TreeMap.class);
		put(NavigableMap.class, TreeMap.class);
	}});

	@SuppressWarnings({"unchecked"})
	public static <T, E> Class<? extends T> getConcreteClass(final Class<T> returnType, final Class<E> defaultConcreteClass) {
		int classModifiers = returnType.getModifiers();
		if (Modifier.isInterface(classModifiers) || Modifier.isAbstract(classModifiers)) {
			Class<? extends T> concrete = (Class<? extends T>) interfaceToConcrete.get(returnType);
			return concrete == null ? (Class<? extends T>) defaultConcreteClass : concrete;
		}
		return returnType;
	}

	public static <T, E> T instantiateClass(final Class<T> returnType, final Class<E> defaultConcreteClass) {
		try {
			return getConcreteClass(returnType, defaultConcreteClass).newInstance();
		} catch (Throwable e) {
			throw new MapperException("Failed to instantiate class of type " + returnType.getName(), e);
		}
	}

	private final Calendar cal;
	private final int arrayMaxLength;

	public ResultSetMapper(Calendar cal, int arrayMaxLength) {
		this.cal = cal;
		this.arrayMaxLength = arrayMaxLength;
	}

	public ResultSetMapper() {
		this(-1);
	}

	public ResultSetMapper(int arrayMaxLength) {
		this(null, arrayMaxLength);
	}

	protected <T> T privToObject(ResultSet rs, Class<T> componentType, Calendar cal, Class<?> mapValType) {
		T ret = null;
		try {
			if (rs.next())
				ret = getRowMapper(rs, componentType, cal, mapValType, null).mapRowToReturnType();
		} catch (SQLException e) {
			// ignore // todo: this looks crazy dangerous look into it...
		}
		tryClose(rs);
		return ret == null ? RowToObjectMapper.fixNull(componentType) : ret;
	}

	protected <T> ResultSetIterable<T> privToResultSetIterable(ResultSet rs, Class<T> componentType, Calendar cal, Class<?> mapValType) {
		try {
			return ResultSetIterable.getResultSetIterable(rs,
					rs.next() ? getRowMapper(rs, componentType, cal, mapValType, null).getResultSetToObject() : null,
					cal);
		} catch (SQLException e) {
			throw new MapperException("cannot create ResultSetIterable", e);
		}
	}

	//IFJAVA8_START

	protected <T> Stream<T> privToStream(ResultSet rs, Class<T> componentType, Calendar cal, Class<?> mapValType) {
		try {
			return ResultSetIterable.getStream(rs,
					rs.next() ? getRowMapper(rs, componentType, cal, mapValType, null).getResultSetToObject() : null,
					cal);
		} catch (SQLException e) {
			throw new MapperException("cannot create Stream", e);
		}
	}

	//IFJAVA8_END

	protected <T extends Collection<E>, E> T privToCollection(ResultSet rs, final Class<T> collectionType, Class<E> componentType, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		return privToCollection(rs, instantiateClass(collectionType, ArrayList.class), componentType, arrayMaxLength, cal, mapValType);
	}

	/**
	 * Invoked when the return type of the method is an array type.
	 *
	 * @param rs             ResultSet to process.
	 * @param componentType  The class of object contained within the array
	 * @param arrayMaxLength The maximum size of array to create, a value of 0 indicates that the array
	 *                       size will be the same as the result set size (no limit).
	 * @param cal            A calendar instance to use for date/time values
	 * @return An array of the specified class type
	 */
	protected <T extends Collection<E>, E> T privToCollection(ResultSet rs, T list, Class<E> componentType, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		int numRows = 0;
		// dereference list in an attempt to gracefully avoid OutOfMemoryError's
		final SoftReference<T> softList = new SoftReference<T>(list);
		list = null;
		try {
			// a value of less than 1 indicates that all rows from the ResultSet should be included.
			final boolean unlimitedRows = arrayMaxLength < 1;

			final RowMapper<?, E> rowMapper = getRowMapper(rs, componentType, cal, mapValType, null);

			for (; (unlimitedRows || numRows != arrayMaxLength) && rs.next(); ++numRows) {
				E object = rowMapper.mapRowToReturnType();

				list = softList.get();
				if (list == null)
					throw new OutOfMemoryError();
				list.add(object);
				list = null;
			}

			if (!unlimitedRows)
				warnOnMaxLength(numRows, arrayMaxLength, rs);

			list = softList.get();
			if (list == null)
				throw new OutOfMemoryError();

			// if this list is Finishable, call finish()
			if (list instanceof Finishable)
				((Finishable) list).finish(rs);

			tryClose(rs);
			return list;
		} catch(OutOfMemoryError e){
			tryClose(rs);
			throw new MapperException(String.format("Too many rows (processed %d, max %d), ran out of memory.", numRows, arrayMaxLength), e);	
		} catch(Throwable e) {	
			if(list == null)
				list = softList.get();
			String columnName = "UNKNOWN";
			String columnType = "UNKNOWN";
			String returnedType = "UNKNOWN";
			int badColumn = 0;
			try {
				ResultSetMetaData md = rs.getMetaData();
				badColumn = e.getCause() instanceof SQLExceptionColumnNum ? ((SQLExceptionColumnNum)e.getCause()).getColumnNum() : 1;
				columnName = md.getColumnLabel(badColumn);
				columnType = md.getColumnTypeName(badColumn);
				returnedType = ((list != null && !list.isEmpty()) ? list.iterator().next() : rs.getObject(badColumn)).getClass().getName();
			} catch (Throwable t) {
				// ignore
			}
			tryClose(rs);
			// assuming no errors in resultSetObject() this can only happen
			// for single column result sets.
			throw new MapperException("The declared Java type for "
					+ String.format("%s<%s>"
					, (list != null ? list.getClass() : "UnknownCollection")
					, getComponentName(componentType, mapValType))
					+ " is incompatible with the SQL format of column " + badColumn + " '" + columnName
					+ "' (" + columnType + ") which returns objects of type " + returnedType,
					e);
		}
	}

	protected <T extends Map<K, E>, K, E> T privToMap(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		return privToMap(rs, instantiateClass(returnType, HashMap.class), mapKeyType, componentType, arrayMaxLength, cal, mapValType);
	}

	/**
	 * Invoked when the return type of the method is a Map type.
	 *
	 * @param rs             ResultSet to process.
	 * @param arrayMaxLength The maximum size of array to create, a value of 0 indicates that the array
	 *                       size will be the same as the result set size (no limit).
	 * @param cal            A calendar instance to use for date/time values
	 * @return An array of the specified class type
	 */
	protected <T extends Map<K, E>, K, E> T privToMap(ResultSet rs, T map, Class<K> mapKeyType, Class<E> componentType, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		int numRows = 0;
		// dereference map in an attempt to gracefully avoid OutOfMemoryError's
		final SoftReference<T> softMap = new SoftReference<T>(map);
		map = null;
		try {
			// a value of less than 1 indicates that all rows from the ResultSet should be included.
			final boolean unlimitedRows = arrayMaxLength < 1;

			final RowMapper<K, E> rowMapper = getRowMapper(rs, componentType, cal, mapValType, mapKeyType);

			for (; (unlimitedRows || numRows != arrayMaxLength) && rs.next(); ++numRows) {
				K key = rowMapper.getMapKey();
				E value = rowMapper.mapRowToReturnType();

				map = softMap.get();
				if (map == null)
					throw new OutOfMemoryError();
				map.put(key, value);
				map = null;
			}

			if (!unlimitedRows)
				warnOnMaxLength(numRows, arrayMaxLength, rs);

			map = softMap.get();
			if (map == null)
				throw new OutOfMemoryError();

			// if this map is Finishable, call finish()
			if (map instanceof Finishable)
				((Finishable) map).finish(rs);

			tryClose(rs);
			return map;
		} catch(OutOfMemoryError e){
			tryClose(rs);
			throw new MapperException(String.format("Too many rows (processed %d, max %d), ran out of memory.", numRows, arrayMaxLength), e);
		}catch (Throwable e) {
			if(map == null)
				map = softMap.get();
			String columnName = "UNKNOWN";
			String columnType = "UNKNOWN";
			String returnedType = "UNKNOWN"; 
			int badColumn = 0;
			try {
				ResultSetMetaData md = rs.getMetaData();
				badColumn = e.getCause() instanceof SQLExceptionColumnNum ? ((SQLExceptionColumnNum)e.getCause()).getColumnNum() : 1;
				columnName = md.getColumnLabel(badColumn);
				columnType = md.getColumnTypeName(badColumn);
				returnedType = ((map != null && !map.isEmpty()) ? map.values().iterator().next() : rs.getObject(badColumn)).getClass().getName();
			} catch (Throwable t) {
				// ignore
			}
			tryClose(rs);
			// assuming no errors in resultSetObject() this can only happen
			// for single column result sets.
			throw new MapperException("The declared Java type for "
					+ String.format("%s<%s, %s>"
					, (map != null ? map.getClass() : "UnknownMap")
					, mapKeyType != null ? mapKeyType.getName() : "UNKNOWN"
					, getComponentName(componentType, mapValType))
					+ " is incompatible with the SQL format of column "+ badColumn + " '" + columnName
					+ "' (" + columnType + ") which returns objects of type " + returnedType,
					e);
		}
	}

	protected <T extends Map<K, E>, K, E extends Collection<C>, C> T privToMapCollection(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		return privToMapCollection(rs, instantiateClass(returnType, HashMap.class), mapKeyType, collectionType, componentType, arrayMaxLength, cal, mapValType);
	}

	/**
	 * Invoked when the return type of the method is a Map type with a list for a value.
	 *
	 * @param rs             ResultSet to process.
	 * @param arrayMaxLength The maximum size of array to create, a value of 0 indicates that the array
	 *                       size will be the same as the result set size (no limit).
	 * @param cal            A calendar instance to use for date/time values
	 * @return An array of the specified class type
	 */
	protected <T extends Map<K, E>, K, E extends Collection<C>, C> T privToMapCollection(ResultSet rs, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		int numRows = 0;
		// dereference map in an attempt to gracefully avoid OutOfMemoryError's
		final SoftReference<T> softMap = new SoftReference<T>(map);
		map = null;
		try {
			// a value of less than 1 indicates that all rows from the ResultSet should be included.
			final boolean unlimitedRows = arrayMaxLength < 1;

			final RowMapper<K, C> rowMapper = getRowMapper(rs, componentType, cal, mapValType, mapKeyType);

			for (; (unlimitedRows || numRows != arrayMaxLength) && rs.next(); ++numRows) {
				K key = rowMapper.getMapKey();
				C value = rowMapper.mapRowToReturnType();

				map = softMap.get();
				if (map == null)
					throw new OutOfMemoryError();
				E list = map.get(key);
				if(list == null){
					list = instantiateClass(collectionType, ArrayList.class);
					map.put(key, list);
				}
				list.add(value);
				map = null;
			}

			if (!unlimitedRows)
				warnOnMaxLength(numRows, arrayMaxLength, rs);

			map = softMap.get();
			if (map == null)
				throw new OutOfMemoryError();

			// if this map is Finishable, call finish()
			if (map instanceof Finishable)
				((Finishable) map).finish(rs);

			tryClose(rs);
			return map;
		} catch(OutOfMemoryError e){
			tryClose(rs);
			throw new MapperException(String.format("Too many rows (processed %d, max %d), ran out of memory.", numRows, arrayMaxLength), e);
		}catch (Throwable e) {
			if(map == null)
				map = softMap.get();
			String columnName = "UNKNOWN";
			String columnType = "UNKNOWN";
			String returnedType = "UNKNOWN";
			int badColumn = 0;
			try {
				ResultSetMetaData md = rs.getMetaData();
				badColumn = e.getCause() instanceof SQLExceptionColumnNum ? ((SQLExceptionColumnNum)e.getCause()).getColumnNum() : 1;
				columnName = md.getColumnLabel(badColumn);
				columnType = md.getColumnTypeName(badColumn);
				returnedType = ((map != null && !map.isEmpty()) ? map.values().iterator().next() : rs.getObject(badColumn)).getClass().getName();
			} catch (Throwable t) {
				// ignore
			}
			tryClose(rs);
			// assuming no errors in resultSetObject() this can only happen
			// for single column result sets.
			throw new MapperException("The declared Java type for "
					+ String.format("%s<%s, %s>"
					, (map != null ? map.getClass() : "UnknownMap")
					, mapKeyType != null ? mapKeyType.getName() : "UNKNOWN"
					, getComponentName(componentType, mapValType))
					+ " is incompatible with the SQL format of column "+ badColumn + " '" + columnName
					+ "' (" + columnType + ") which returns objects of type " + returnedType,
					e);
		}
	}


	// methods using toMapCollection to return different types
	protected <T> ListIterator<T> privToListIterator(ResultSet rs, final Class<T> type, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		return privToList(rs, type, arrayMaxLength, cal, mapValType).listIterator();
	}

	protected <T> Iterator<T> privToIterator(ResultSet rs, final Class<T> type, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		return privToList(rs, type, arrayMaxLength, cal, mapValType).iterator();
	}

	@SuppressWarnings({"unchecked"})
	protected <T> T[] privToArray(ResultSet rs, final Class<T> type, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		List<T> list = privToList(rs, type, arrayMaxLength, cal, mapValType);
		return list.toArray((T[]) Array.newInstance(type, list.size()));
	}

	protected <E> List<E> privToList(ResultSet rs, Class<E> componentType, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		return privToCollection(rs, new ArrayList<E>(), componentType, arrayMaxLength, cal, mapValType);
	}

	protected <K, E> Map<K, E> privToMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		return privToMap(rs, new HashMap<K, E>(), mapKeyType, componentType, arrayMaxLength, cal, mapValType);
	}

	protected <K, E extends Collection<C>, C> Map<K, E> privToMapCollection(ResultSet rs, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, int arrayMaxLength, Calendar cal, Class<?> mapValType) {
		return privToMapCollection(rs, new HashMap<K, E>(), mapKeyType, collectionType, componentType, arrayMaxLength, cal, mapValType);
	}

	// fairly un-interesting methods below here
	public <K, T> RowMapper<K, T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		return new RowToObjectMapper<K, T>(resultSet, returnTypeClass, cal, mapValType, mapKeyType);
	}

	protected void warnOnMaxLength(final int numRows, final int arrayMaxLength, final ResultSet rs) {
		if (numRows < arrayMaxLength)
			return;
		int totalRows = numRows;
		try {
			if (rs.getType() != ResultSet.TYPE_FORWARD_ONLY) {
				rs.last();
				totalRows = rs.getRow();
			} else {
				totalRows = rs.getRow();
				while (rs.next())
					++totalRows;
			}
			if (totalRows == 0)
				totalRows = numRows;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.err.printf("This JdbcControl query returned %d rows, %s arrayMaxLength (%d), which you most likely never want to happen, investigate!!!!\n",
				totalRows, totalRows == numRows ? "equaling" : "exceeding", arrayMaxLength);
		Thread.dumpStack();
	}

	// overloaded helper methods

	@SuppressWarnings("unchecked")
	protected Object toType(final ResultSet rs, final Class returnType, final ParameterizedType type, final int arrayMaxLength, final Calendar cal) {
		if (returnType.isArray()) {
			return toArray(rs, returnType.getComponentType(), arrayMaxLength, cal);
		} else if (Collection.class.isAssignableFrom(returnType)) {
			final Type componentType = type.getActualTypeArguments()[0];
			if(componentType instanceof ParameterizedType) {
				final ParameterizedType parameterizedType = ((ParameterizedType) componentType);
				final Type rawType = parameterizedType.getRawType();
				if(rawType instanceof Class && Map.class.isAssignableFrom((Class)rawType)) {
					final Type[] mapTypes = parameterizedType.getActualTypeArguments();
					if (mapTypes.length == 2 && mapTypes[0].equals(String.class) && mapTypes[1] instanceof Class)
						return toCollectionMap(rs, returnType, (Class) rawType, (Class) mapTypes[1]);
				}
			}
			// if we didn't match above signature, try just a regular collection
			if(componentType instanceof Class) {
				return toCollection(rs, returnType, (Class) componentType, arrayMaxLength, cal);
			}
			// or give up...
			throw new MapperException("unknown Collection type to map...");
		} else if (Map.class.isAssignableFrom(returnType)) {
			Type[] types = type.getActualTypeArguments();
			if (types[1] instanceof ParameterizedType) { // for collectionMaps
				ParameterizedType pt = (ParameterizedType) types[1];
				Class collectionType = (Class) pt.getRawType();
				if (Collection.class.isAssignableFrom(collectionType))
					return toMapCollection(rs, returnType, (Class) types[0], collectionType, (Class) pt.getActualTypeArguments()[0], arrayMaxLength, cal);
			}
			return toMap(rs, com.moparisthebest.jdbc.ResultSetMapper.instantiateClass((Class<Map>)returnType, HashMap.class), (Class) types[0], (Class) types[1], arrayMaxLength, cal);
		} else if (Iterator.class.isAssignableFrom(returnType)) {
			if(ResultSetIterable.class.isAssignableFrom(returnType))
				return toResultSetIterable(rs, (Class) type.getActualTypeArguments()[0], cal);
			return ListIterator.class.isAssignableFrom(returnType) ?
					toListIterator(rs, (Class) type.getActualTypeArguments()[0], arrayMaxLength, cal) :
					toIterator(rs, (Class) type.getActualTypeArguments()[0], arrayMaxLength, cal);
		}
		//IFJAVA8_START
		else if (Stream.class.isAssignableFrom(returnType)) {
			return toStream(rs, (Class) type.getActualTypeArguments()[0], cal);
		}
		//IFJAVA8_END
		else if(ResultSet.class.isAssignableFrom(returnType)) {
			return rs; // odd, we didn't do much, but oh well
		} else {
			return toObject(rs, returnType, cal);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T toType(ResultSet rs, TypeReference<T> typeReference, int arrayMaxLength, Calendar cal) {
		return (T)this.toType(rs, typeReference.getRawType(), typeReference.getType(), arrayMaxLength, cal);
	}

	public <T> T toObject(ResultSet rs, Class<T> componentType, Calendar cal) {
		return privToObject(rs, componentType, cal, null);
	}

	public <T> ResultSetIterable<T> toResultSetIterable(ResultSet rs, Class<T> componentType, Calendar cal) {
		return privToResultSetIterable(rs, componentType, cal, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Map<String, V>, V> ResultSetIterable<Map<String, V>> toResultSetIterable(ResultSet rs, Class<T> componentType, Class<V> mapValType, Calendar cal) {
		return (ResultSetIterable<Map<String, V>>)privToResultSetIterable(rs, componentType, cal, mapValType);
	}

	//IFJAVA8_START

	public <T> Stream<T> toStream(ResultSet rs, Class<T> componentType, Calendar cal) {
		return privToStream(rs, componentType, cal, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Map<String, V>, V> Stream<Map<String, V>> toStream(ResultSet rs, Class<T> componentType, Class<V> mapValType, Calendar cal) {
		return (Stream<Map<String, V>>)privToStream(rs, componentType, cal, mapValType);
	}

	//IFJAVA8_END

	public <T extends Collection<E>, E> T toCollection(ResultSet rs, final Class<T> collectionType, Class<E> componentType, int arrayMaxLength, Calendar cal) {
		return privToCollection(rs, collectionType, componentType, arrayMaxLength, cal, null);
	}

	public <T extends Collection<E>, E> T toCollection(ResultSet rs, T list, Class<E> componentType, int arrayMaxLength, Calendar cal) {
		return privToCollection(rs, list, componentType, arrayMaxLength, cal, null);
	}

	public <T extends Map<K, E>, K, E> T toMap(ResultSet rs, T map, Class<K> mapKeyType, Class<E> componentType, int arrayMaxLength, Calendar cal) {
		return privToMap(rs, map, mapKeyType, componentType, arrayMaxLength, cal, null);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, int arrayMaxLength, Calendar cal) {
		return privToMapCollection(rs, returnType, mapKeyType, collectionType, componentType, arrayMaxLength, cal, null);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(ResultSet rs, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, int arrayMaxLength, Calendar cal) {
		return privToMapCollection(rs, map, mapKeyType, collectionType, componentType, arrayMaxLength, cal, null);
	}

	public <T> ListIterator<T> toListIterator(ResultSet rs, final Class<T> type, int arrayMaxLength, Calendar cal) {
		return privToListIterator(rs, type, arrayMaxLength, cal, null);
	}

	public <T> Iterator<T> toIterator(ResultSet rs, final Class<T> type, int arrayMaxLength, Calendar cal) {
		return privToIterator(rs, type, arrayMaxLength, cal, null);
	}

	public <T> T[] toArray(ResultSet rs, final Class<T> type, int arrayMaxLength, Calendar cal) {
		return privToArray(rs, type, arrayMaxLength, cal, null);
	}

	/**
	 * Returns a simple List of componentType
	 */
	public <E> List<E> toList(ResultSet rs, Class<E> componentType, int arrayMaxLength, Calendar cal) {
		return privToList(rs, componentType, arrayMaxLength, cal, null);
	}

	/**
	 * Returns a simple Map of mapKeyType -> componentType
	 */
	public <K, E> Map<K, E> toMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, int arrayMaxLength, Calendar cal) {
		return privToMap(rs, mapKeyType, componentType, arrayMaxLength, cal, null);
	}

	/**
	 * Returns a simple Map of mapKeyType -> List<componentType>
	 */
	@SuppressWarnings({"unchecked"})
	public <K, E extends List<C>, C> Map<K, E> toMapList(ResultSet rs, Class<K> mapKeyType, Class<C> componentType, int arrayMaxLength, Calendar cal) {
		return (Map<K, E>) privToMapCollection(rs, mapKeyType, List.class, componentType, arrayMaxLength, cal, null);
	}

	// map methods

	// the following 6 methods I can find no way to not require an unchecked cast on the object being returned
	// please find a way and let me know, until then, I won't provide overloaded methods for them
	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(ResultSet rs, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return privToCollection(rs, collectionType, componentType, arrayMaxLength, cal, mapValType);
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(ResultSet rs, T list, Class<E> componentType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return privToCollection(rs, list, componentType, arrayMaxLength, cal, mapValType);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return privToMap(rs, returnType, mapKeyType, componentType, arrayMaxLength, cal, mapValType);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(ResultSet rs, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return privToMap(rs, map, mapKeyType, componentType, arrayMaxLength, cal, mapValType);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return privToMapCollection(rs, returnType, mapKeyType, collectionType, componentType, arrayMaxLength, cal, mapValType);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(ResultSet rs, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return privToMapCollection(rs, map, mapKeyType, collectionType, componentType, arrayMaxLength, cal, mapValType);
	}

	public <T extends Map<String, V>, V> Map<String, V> toSingleMap(ResultSet rs, Class<T> componentType, Class<V> mapValType, Calendar cal) {
		return privToObject(rs, componentType, cal, mapValType);
	}

	@SuppressWarnings({"unchecked"})
	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(ResultSet rs, final Class<T> type, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return (ListIterator<Map<String, V>>) privToListIterator(rs, type, arrayMaxLength, cal, mapValType);
	}

	@SuppressWarnings({"unchecked"})
	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(ResultSet rs, final Class<T> type, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return (Iterator<Map<String, V>>) privToIterator(rs, type, arrayMaxLength, cal, mapValType);
	}

	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(ResultSet rs, final Class<T> type, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return privToArray(rs, type, arrayMaxLength, cal, mapValType);
	}

	@SuppressWarnings({"unchecked"})
	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(ResultSet rs, Class<E> componentType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return (List<Map<String, V>>) privToList(rs, componentType, arrayMaxLength, cal, mapValType);
	}

	@SuppressWarnings({"unchecked"})
	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return (Map<K, Map<String, V>>) privToMap(rs, mapKeyType, componentType, arrayMaxLength, cal, mapValType);
	}

	@SuppressWarnings({"unchecked"})
	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return (Map<K, List<Map<String, V>>>) privToMapCollection(rs, mapKeyType, List.class, componentType, arrayMaxLength, cal, mapValType);
	}

	// overloaded map methods that don't require you to specify the type of map you want

	@SuppressWarnings({"unchecked"})
	public <V> Map<String, V> toSingleMap(ResultSet rs, Class<V> mapValType, Calendar cal) {
		return toSingleMap(rs, Map.class, mapValType, cal);
	}

	@SuppressWarnings({"unchecked"})
	public <V> ListIterator<Map<String, V>> toListIteratorMap(ResultSet rs, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return toListIteratorMap(rs, Map.class, mapValType, arrayMaxLength, cal);
	}

	@SuppressWarnings({"unchecked"})
	public <V> Iterator<Map<String, V>> toIteratorMap(ResultSet rs, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return toIteratorMap(rs, Map.class, mapValType, arrayMaxLength, cal);
	}

	@SuppressWarnings({"unchecked"})
	public <V> List<Map<String, V>> toListMap(ResultSet rs, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return toListMap(rs, Map.class, mapValType, arrayMaxLength, cal);
	}

	@SuppressWarnings({"unchecked"})
	public <K, V> Map<K, Map<String, V>> toMapMap(ResultSet rs, Class<K> mapKeyType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return toMapMap(rs, mapKeyType, Map.class, mapValType, arrayMaxLength, cal);
	}

	@SuppressWarnings({"unchecked"})
	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(ResultSet rs, Class<K> mapKeyType, Class<V> mapValType, int arrayMaxLength, Calendar cal) {
		return this.toMapListMap(rs, mapKeyType, Map.class, mapValType, arrayMaxLength, cal);
	}

	// completely useless except inside this class

	private static String getComponentName(Class<?> componentType, Class<?> mapValType){
		final String componentName = componentType != null ? componentType.getName() : "UNKNOWN";
		if(Map.class.isAssignableFrom(componentType))
			return String.format("%s<java.lang.String, %s>",
					componentName, mapValType != null ? mapValType.getName() : "java.lang.Object");
		if(componentType != null && componentType.isArray())
			return componentType.getComponentType().getName()+"[]";
		return componentName;
	}

	protected void tryClose(ResultSet rs) {
		//todo: if (canCloseResultSet())
			TryClose.tryClose(rs);
	}

	// DO NOT EDIT BELOW THIS LINE, OR CHANGE THIS COMMENT, CODE AUTOMATICALLY GENERATED BY genQueryMapper.sh

	public <T> T toObject(ResultSet rs, Class<T> componentType) {
		return this.toObject(rs, componentType, cal);
	}

	public <T> ResultSetIterable<T> toResultSetIterable(ResultSet rs, Class<T> componentType) {
		return this.toResultSetIterable(rs, componentType, cal);
	}

	public <T extends Map<String, V>, V> ResultSetIterable<Map<String, V>> toResultSetIterable(ResultSet rs, Class<T> componentType, Class<V> mapValType) {
		return this.toResultSetIterable(rs, componentType, mapValType, cal);
	}

	//IFJAVA8_START

	public <T> Stream<T> toStream(ResultSet rs, Class<T> componentType) {
		return this.toStream(rs, componentType, cal);
	}

	//IFJAVA8_END

	//IFJAVA8_START

	public <T extends Map<String, V>, V> Stream<Map<String, V>> toStream(ResultSet rs, Class<T> componentType, Class<V> mapValType) {
		return this.toStream(rs, componentType, mapValType, cal);
	}

	//IFJAVA8_END

	public <T extends Map<String, V>, V> Map<String, V> toSingleMap(ResultSet rs, Class<T> componentType, Class<V> mapValType) {
		return this.toSingleMap(rs, componentType, mapValType, cal);
	}

	public <V> Map<String, V> toSingleMap(ResultSet rs, Class<V> mapValType) {
		return this.toSingleMap(rs, mapValType, cal);
	}

	public <T> T toType(ResultSet rs, TypeReference<T> typeReference) {
		return this.toType(rs, typeReference, arrayMaxLength, cal);
	}

	public <T> T toType(ResultSet rs, TypeReference<T> typeReference, int arrayMaxLength) {
		return this.toType(rs, typeReference, arrayMaxLength, cal);
	}

	public <T> T toType(ResultSet rs, TypeReference<T> typeReference, Calendar cal) {
		return this.toType(rs, typeReference, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E> T toCollection(ResultSet rs, final Class<T> collectionType, Class<E> componentType) {
		return this.toCollection(rs, collectionType, componentType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E> T toCollection(ResultSet rs, final Class<T> collectionType, Class<E> componentType, int arrayMaxLength) {
		return this.toCollection(rs, collectionType, componentType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E> T toCollection(ResultSet rs, final Class<T> collectionType, Class<E> componentType, Calendar cal) {
		return this.toCollection(rs, collectionType, componentType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E> T toCollection(ResultSet rs, T list, Class<E> componentType) {
		return this.toCollection(rs, list, componentType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E> T toCollection(ResultSet rs, T list, Class<E> componentType, int arrayMaxLength) {
		return this.toCollection(rs, list, componentType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E> T toCollection(ResultSet rs, T list, Class<E> componentType, Calendar cal) {
		return this.toCollection(rs, list, componentType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E> T toMap(ResultSet rs, T map, Class<K> mapKeyType, Class<E> componentType) {
		return this.toMap(rs, map, mapKeyType, componentType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E> T toMap(ResultSet rs, T map, Class<K> mapKeyType, Class<E> componentType, int arrayMaxLength) {
		return this.toMap(rs, map, mapKeyType, componentType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E> T toMap(ResultSet rs, T map, Class<K> mapKeyType, Class<E> componentType, Calendar cal) {
		return this.toMap(rs, map, mapKeyType, componentType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType) {
		return this.toMapCollection(rs, returnType, mapKeyType, collectionType, componentType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, int arrayMaxLength) {
		return this.toMapCollection(rs, returnType, mapKeyType, collectionType, componentType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, Calendar cal) {
		return this.toMapCollection(rs, returnType, mapKeyType, collectionType, componentType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(ResultSet rs, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType) {
		return this.toMapCollection(rs, map, mapKeyType, collectionType, componentType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(ResultSet rs, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, int arrayMaxLength) {
		return this.toMapCollection(rs, map, mapKeyType, collectionType, componentType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Collection<C>, C> T toMapCollection(ResultSet rs, T map, Class<K> mapKeyType, Class<E> collectionType, Class<C> componentType, Calendar cal) {
		return this.toMapCollection(rs, map, mapKeyType, collectionType, componentType, arrayMaxLength, cal);
	}

	public <T> ListIterator<T> toListIterator(ResultSet rs, final Class<T> type) {
		return this.toListIterator(rs, type, arrayMaxLength, cal);
	}

	public <T> ListIterator<T> toListIterator(ResultSet rs, final Class<T> type, int arrayMaxLength) {
		return this.toListIterator(rs, type, arrayMaxLength, cal);
	}

	public <T> ListIterator<T> toListIterator(ResultSet rs, final Class<T> type, Calendar cal) {
		return this.toListIterator(rs, type, arrayMaxLength, cal);
	}

	public <T> Iterator<T> toIterator(ResultSet rs, final Class<T> type) {
		return this.toIterator(rs, type, arrayMaxLength, cal);
	}

	public <T> Iterator<T> toIterator(ResultSet rs, final Class<T> type, int arrayMaxLength) {
		return this.toIterator(rs, type, arrayMaxLength, cal);
	}

	public <T> Iterator<T> toIterator(ResultSet rs, final Class<T> type, Calendar cal) {
		return this.toIterator(rs, type, arrayMaxLength, cal);
	}

	public <T> T[] toArray(ResultSet rs, final Class<T> type) {
		return this.toArray(rs, type, arrayMaxLength, cal);
	}

	public <T> T[] toArray(ResultSet rs, final Class<T> type, int arrayMaxLength) {
		return this.toArray(rs, type, arrayMaxLength, cal);
	}

	public <T> T[] toArray(ResultSet rs, final Class<T> type, Calendar cal) {
		return this.toArray(rs, type, arrayMaxLength, cal);
	}

	public <E> List<E> toList(ResultSet rs, Class<E> componentType) {
		return this.toList(rs, componentType, arrayMaxLength, cal);
	}

	public <E> List<E> toList(ResultSet rs, Class<E> componentType, int arrayMaxLength) {
		return this.toList(rs, componentType, arrayMaxLength, cal);
	}

	public <E> List<E> toList(ResultSet rs, Class<E> componentType, Calendar cal) {
		return this.toList(rs, componentType, arrayMaxLength, cal);
	}

	public <K, E> Map<K, E> toMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType) {
		return this.toMap(rs, mapKeyType, componentType, arrayMaxLength, cal);
	}

	public <K, E> Map<K, E> toMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, int arrayMaxLength) {
		return this.toMap(rs, mapKeyType, componentType, arrayMaxLength, cal);
	}

	public <K, E> Map<K, E> toMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, Calendar cal) {
		return this.toMap(rs, mapKeyType, componentType, arrayMaxLength, cal);
	}

	public <K, E extends List<C>, C> Map<K, E> toMapList(ResultSet rs, Class<K> mapKeyType, Class<C> componentType) {
		return this.toMapList(rs, mapKeyType, componentType, arrayMaxLength, cal);
	}

	public <K, E extends List<C>, C> Map<K, E> toMapList(ResultSet rs, Class<K> mapKeyType, Class<C> componentType, int arrayMaxLength) {
		return this.toMapList(rs, mapKeyType, componentType, arrayMaxLength, cal);
	}

	public <K, E extends List<C>, C> Map<K, E> toMapList(ResultSet rs, Class<K> mapKeyType, Class<C> componentType, Calendar cal) {
		return this.toMapList(rs, mapKeyType, componentType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(ResultSet rs, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType) {
		return this.toCollectionMap(rs, collectionType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(ResultSet rs, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength) {
		return this.toCollectionMap(rs, collectionType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(ResultSet rs, final Class<T> collectionType, Class<E> componentType, Class<V> mapValType, Calendar cal) {
		return this.toCollectionMap(rs, collectionType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(ResultSet rs, T list, Class<E> componentType, Class<V> mapValType) {
		return this.toCollectionMap(rs, list, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(ResultSet rs, T list, Class<E> componentType, Class<V> mapValType, int arrayMaxLength) {
		return this.toCollectionMap(rs, list, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Collection<E>, E extends Map<String, V>, V> T toCollectionMap(ResultSet rs, T list, Class<E> componentType, Class<V> mapValType, Calendar cal) {
		return this.toCollectionMap(rs, list, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType) {
		return this.toMapMap(rs, returnType, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength) {
		return this.toMapMap(rs, returnType, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, Calendar cal) {
		return this.toMapMap(rs, returnType, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(ResultSet rs, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType) {
		return this.toMapMap(rs, map, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(ResultSet rs, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength) {
		return this.toMapMap(rs, map, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, E>, K, E extends Map<String, V>, V> T toMapMap(ResultSet rs, T map, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, Calendar cal) {
		return this.toMapMap(rs, map, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType) {
		return this.toMapCollectionMap(rs, returnType, mapKeyType, collectionType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength) {
		return this.toMapCollectionMap(rs, returnType, mapKeyType, collectionType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(ResultSet rs, final Class<T> returnType, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, Calendar cal) {
		return this.toMapCollectionMap(rs, returnType, mapKeyType, collectionType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(ResultSet rs, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType) {
		return this.toMapCollectionMap(rs, map, mapKeyType, collectionType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(ResultSet rs, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength) {
		return this.toMapCollectionMap(rs, map, mapKeyType, collectionType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<K, C>, K, C extends Collection<E>, E extends Map<String, V>, V> T toMapCollectionMap(ResultSet rs, T map, Class<K> mapKeyType, Class<C> collectionType, Class<E> componentType, Class<V> mapValType, Calendar cal) {
		return this.toMapCollectionMap(rs, map, mapKeyType, collectionType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(ResultSet rs, final Class<T> type, Class<V> mapValType) {
		return this.toListIteratorMap(rs, type, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(ResultSet rs, final Class<T> type, Class<V> mapValType, int arrayMaxLength) {
		return this.toListIteratorMap(rs, type, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<String, V>, V> ListIterator<Map<String, V>> toListIteratorMap(ResultSet rs, final Class<T> type, Class<V> mapValType, Calendar cal) {
		return this.toListIteratorMap(rs, type, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(ResultSet rs, final Class<T> type, Class<V> mapValType) {
		return this.toIteratorMap(rs, type, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(ResultSet rs, final Class<T> type, Class<V> mapValType, int arrayMaxLength) {
		return this.toIteratorMap(rs, type, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<String, V>, V> Iterator<Map<String, V>> toIteratorMap(ResultSet rs, final Class<T> type, Class<V> mapValType, Calendar cal) {
		return this.toIteratorMap(rs, type, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(ResultSet rs, final Class<T> type, Class<V> mapValType) {
		return this.toArrayMap(rs, type, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(ResultSet rs, final Class<T> type, Class<V> mapValType, int arrayMaxLength) {
		return this.toArrayMap(rs, type, mapValType, arrayMaxLength, cal);
	}

	public <T extends Map<String, V>, V> Map<String, V>[] toArrayMap(ResultSet rs, final Class<T> type, Class<V> mapValType, Calendar cal) {
		return this.toArrayMap(rs, type, mapValType, arrayMaxLength, cal);
	}

	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(ResultSet rs, Class<E> componentType, Class<V> mapValType) {
		return this.toListMap(rs, componentType, mapValType, arrayMaxLength, cal);
	}

	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(ResultSet rs, Class<E> componentType, Class<V> mapValType, int arrayMaxLength) {
		return this.toListMap(rs, componentType, mapValType, arrayMaxLength, cal);
	}

	public <E extends Map<String, V>, V> List<Map<String, V>> toListMap(ResultSet rs, Class<E> componentType, Class<V> mapValType, Calendar cal) {
		return this.toListMap(rs, componentType, mapValType, arrayMaxLength, cal);
	}

	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType) {
		return this.toMapMap(rs, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength) {
		return this.toMapMap(rs, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <K, E extends Map<String, V>, V> Map<K, Map<String, V>> toMapMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, Calendar cal) {
		return this.toMapMap(rs, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType) {
		return this.toMapListMap(rs, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, int arrayMaxLength) {
		return this.toMapListMap(rs, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <K, E extends Map<String, V>, V> Map<K, List<Map<String, V>>> toMapListMap(ResultSet rs, Class<K> mapKeyType, Class<E> componentType, Class<V> mapValType, Calendar cal) {
		return this.toMapListMap(rs, mapKeyType, componentType, mapValType, arrayMaxLength, cal);
	}

	public <V> ListIterator<Map<String, V>> toListIteratorMap(ResultSet rs, Class<V> mapValType) {
		return this.toListIteratorMap(rs, mapValType, arrayMaxLength, cal);
	}

	public <V> ListIterator<Map<String, V>> toListIteratorMap(ResultSet rs, Class<V> mapValType, int arrayMaxLength) {
		return this.toListIteratorMap(rs, mapValType, arrayMaxLength, cal);
	}

	public <V> ListIterator<Map<String, V>> toListIteratorMap(ResultSet rs, Class<V> mapValType, Calendar cal) {
		return this.toListIteratorMap(rs, mapValType, arrayMaxLength, cal);
	}

	public <V> Iterator<Map<String, V>> toIteratorMap(ResultSet rs, Class<V> mapValType) {
		return this.toIteratorMap(rs, mapValType, arrayMaxLength, cal);
	}

	public <V> Iterator<Map<String, V>> toIteratorMap(ResultSet rs, Class<V> mapValType, int arrayMaxLength) {
		return this.toIteratorMap(rs, mapValType, arrayMaxLength, cal);
	}

	public <V> Iterator<Map<String, V>> toIteratorMap(ResultSet rs, Class<V> mapValType, Calendar cal) {
		return this.toIteratorMap(rs, mapValType, arrayMaxLength, cal);
	}

	public <V> List<Map<String, V>> toListMap(ResultSet rs, Class<V> mapValType) {
		return this.toListMap(rs, mapValType, arrayMaxLength, cal);
	}

	public <V> List<Map<String, V>> toListMap(ResultSet rs, Class<V> mapValType, int arrayMaxLength) {
		return this.toListMap(rs, mapValType, arrayMaxLength, cal);
	}

	public <V> List<Map<String, V>> toListMap(ResultSet rs, Class<V> mapValType, Calendar cal) {
		return this.toListMap(rs, mapValType, arrayMaxLength, cal);
	}

	public <K, V> Map<K, Map<String, V>> toMapMap(ResultSet rs, Class<K> mapKeyType, Class<V> mapValType) {
		return this.toMapMap(rs, mapKeyType, mapValType, arrayMaxLength, cal);
	}

	public <K, V> Map<K, Map<String, V>> toMapMap(ResultSet rs, Class<K> mapKeyType, Class<V> mapValType, int arrayMaxLength) {
		return this.toMapMap(rs, mapKeyType, mapValType, arrayMaxLength, cal);
	}

	public <K, V> Map<K, Map<String, V>> toMapMap(ResultSet rs, Class<K> mapKeyType, Class<V> mapValType, Calendar cal) {
		return this.toMapMap(rs, mapKeyType, mapValType, arrayMaxLength, cal);
	}

	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(ResultSet rs, Class<K> mapKeyType, Class<V> mapValType) {
		return this.toMapListMap(rs, mapKeyType, mapValType, arrayMaxLength, cal);
	}

	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(ResultSet rs, Class<K> mapKeyType, Class<V> mapValType, int arrayMaxLength) {
		return this.toMapListMap(rs, mapKeyType, mapValType, arrayMaxLength, cal);
	}

	public <K, V> Map<K, List<Map<String, V>>> toMapListMap(ResultSet rs, Class<K> mapKeyType, Class<V> mapValType, Calendar cal) {
		return this.toMapListMap(rs, mapKeyType, mapValType, arrayMaxLength, cal);
	}

}

