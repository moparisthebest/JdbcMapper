package com.moparisthebest.jdbc;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

/**
 * Maps same as RowToObjectMapper except caches constructor and field mappings
 */
public class CachingRowToObjectMapper<K, T> extends RowToObjectMapper<K, T> {

	protected final Map<ResultSetKey, FieldMapping<T>> cache;
	protected final ResultSetKey keys;

	public CachingRowToObjectMapper(final Map<ResultSetKey, FieldMapping<?>> cache, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		super(resultSet, returnTypeClass, cal, mapValType, mapKeyType);
		@SuppressWarnings("unchecked")
		final Map<ResultSetKey, FieldMapping<T>> genericCache = (Map<ResultSetKey, FieldMapping<T>>) (Object) cache; // ridiculous ain't it?
		this.cache = genericCache;
		try {
			keys = new ResultSetKey(super.getKeysFromResultSet(), _returnTypeClass, _mapKeyType);
			//System.out.printf("keys: %s\n", keys);
		} catch (SQLException e) {
			throw new MapperException("CachingRowToObjectMapper: SQLException: " + e.getMessage(), e);
		}
	}

	@Override
	protected String[] getKeysFromResultSet() throws SQLException {
		return keys.keys;
	}

	@Override
	protected void lazyLoadConstructor() throws SQLException {
		final FieldMapping<T> fm = cache.get(keys);
		if (fm == null) {
			//System.out.printf("cache miss, keys: %s\n", keys);
			// generate and put into cache
			super.lazyLoadConstructor();
			cache.put(keys, new FieldMapping<T>(_fields, _fieldTypes, _fieldOrder, _fieldClasses, resultSetConstructor, constructor));
		} else {
			//System.out.printf("cache hit, keys: %s\n", keys);
			// load from cache
			_fields = fm._fields;
			_fieldTypes = fm._fieldTypes;
			_fieldOrder = fm._fieldOrder;
			_fieldClasses = fm._fieldClasses;
			resultSetConstructor = fm.resultSetConstructor;
			constructor = fm.constructor;
			_args = _fieldOrder == null ? new Object[1] : new Object[_columnCount];
			constructorLoaded = true;
		}
	}

	@Override
	protected void getFieldMappings() throws SQLException {
		final FieldMapping<T> fm = cache.get(keys);
		// todo: could cache fm from lazyLoadConstructor and only set _fields if non-final, but race? investigate
		if (fm == null || fm._fields == null) {
			//System.out.printf("cache miss, keys: %s\n", keys);
			// generate and put into cache
			super.getFieldMappings();
			cache.put(keys, new FieldMapping<T>(_fields, _fieldTypes, _fieldOrder, _fieldClasses, resultSetConstructor, constructor));
		} else {
			//System.out.printf("cache hit, keys: %s\n", keys);
			// load from cache
			_fields = fm._fields;
			_fieldTypes = fm._fieldTypes;
			_fieldOrder = fm._fieldOrder;
			_fieldClasses = fm._fieldClasses;
			resultSetConstructor = fm.resultSetConstructor;
			constructor = fm.constructor;
			_args = _fieldOrder == null ? new Object[1] : new Object[_columnCount];
			constructorLoaded = true;
		}
	}

	public static class ResultSetKey {
		protected final String[] keys;
		protected final Class<?> returnTypeClass, mapKeyType;

		public ResultSetKey(final String[] keys, final Class<?> returnTypeClass, final Class<?> mapKeyType) {
			this.keys = keys;
			this.returnTypeClass = returnTypeClass;
			this.mapKeyType = mapKeyType;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			if (!(o instanceof ResultSetKey)) return false;

			final ResultSetKey that = (ResultSetKey) o;

			// Probably incorrect - comparing Object[] arrays with Arrays.equals
			if (!Arrays.equals(keys, that.keys)) return false;
			if (returnTypeClass != null ? !returnTypeClass.equals(that.returnTypeClass) : that.returnTypeClass != null)
				return false;
			return mapKeyType != null ? mapKeyType.equals(that.mapKeyType) : that.mapKeyType == null;
		}

		@Override
		public int hashCode() {
			int result = Arrays.hashCode(keys);
			result = 31 * result + (returnTypeClass != null ? returnTypeClass.hashCode() : 0);
			result = 31 * result + (mapKeyType != null ? mapKeyType.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "ResultSetKey{" +
					"keys=" + Arrays.toString(keys) +
					", returnTypeClass=" + returnTypeClass +
					", mapKeyType=" + mapKeyType +
					'}';
		}
	}

	static class FieldMapping<T> {
		public final AccessibleObject[] _fields;
		public final int[] _fieldTypes, _fieldOrder;
		public final Class<? extends Enum>[] _fieldClasses;
		public final boolean resultSetConstructor;
		public final Constructor<? extends T> constructor;

		public FieldMapping(final AccessibleObject[] _fields, final int[] _fieldTypes, final int[] _fieldOrder, final Class<? extends Enum>[] _fieldClasses, final boolean resultSetConstructor, final Constructor<? extends T> constructor) {
			this._fields = _fields;
			this._fieldTypes = _fieldTypes;
			this._fieldOrder = _fieldOrder;
			this._fieldClasses = _fieldClasses;
			this.resultSetConstructor = resultSetConstructor;
			this.constructor = constructor;
		}
	}
}
