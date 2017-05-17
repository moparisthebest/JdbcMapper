package com.moparisthebest.jdbc;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

public class CachingRowToObjectMapper<T> extends RowToObjectMapper<T> {

	protected final Map<ResultSetKey, FieldMapping<T>> cache;
	protected final ResultSetKey keys;

	public CachingRowToObjectMapper(final Map<ResultSetKey, FieldMapping<?>> cache, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		super(resultSet, returnTypeClass, cal, mapValType);
		@SuppressWarnings("unchecked")
		final Map<ResultSetKey, FieldMapping<T>> genericCache = (Map<ResultSetKey, FieldMapping<T>>) (Object) cache; // ridiculous ain't it?
		this.cache = genericCache;
		try {
			keys = new ResultSetKey(super.getKeysFromResultSet(), _returnTypeClass);
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
	protected void getFieldMappings() throws SQLException {
		final FieldMapping<T> fm = cache.get(keys);
		if (fm == null) {
			//System.out.printf("cache miss, keys: %s\n", keys);
			// generate and put into cache
			super.getFieldMappings();
			cache.put(keys, new FieldMapping<T>(_fields, _fieldTypes, resultSetConstructor, constructor));
		} else {
			//System.out.printf("cache hit, keys: %s\n", keys);
			// load from cache
			_fields = fm._fields;
			_fieldTypes = fm._fieldTypes;
			resultSetConstructor = fm.resultSetConstructor;
			constructor = fm.constructor;
			constructorLoaded = true;
		}
	}

	public static class ResultSetKey {
		protected final String[] keys;
		protected final Class<?> returnTypeClass;

		public ResultSetKey(final String[] keys, final Class<?> returnTypeClass) {
			this.keys = keys;
			this.returnTypeClass = returnTypeClass;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			if (!(o instanceof ResultSetKey)) return false;

			final ResultSetKey that = (ResultSetKey) o;

			return Arrays.equals(keys, that.keys) && (returnTypeClass != null ? returnTypeClass.equals(that.returnTypeClass) : that.returnTypeClass == null);
		}

		@Override
		public int hashCode() {
			int result = Arrays.hashCode(keys);
			result = 31 * result + (returnTypeClass != null ? returnTypeClass.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "ResultSetKey{" +
					"keys=" + Arrays.toString(keys) +
					", returnTypeClass=" + returnTypeClass +
					'}';
		}
	}

	static class FieldMapping<T> {
		public final AccessibleObject[] _fields;
		public final int[] _fieldTypes;
		public final boolean resultSetConstructor;
		public final Constructor<? extends T> constructor;

		public FieldMapping(final AccessibleObject[] _fields, final int[] _fieldTypes, final boolean resultSetConstructor, final Constructor<? extends T> constructor) {
			this._fields = _fields;
			this._fieldTypes = _fieldTypes;
			this.resultSetConstructor = resultSetConstructor;
			this.constructor = constructor;
		}
	}
}
