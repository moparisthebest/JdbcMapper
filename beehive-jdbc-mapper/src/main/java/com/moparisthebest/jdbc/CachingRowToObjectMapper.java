package com.moparisthebest.jdbc;

import java.lang.reflect.AccessibleObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CachingRowToObjectMapper<T> extends RowToObjectMapper<T> {

	protected static final Map<Integer, FieldMapping> cache = new HashMap<Integer, FieldMapping>();

	protected final int thisHash;
	protected final String[] keys;

	public CachingRowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		super(resultSet, returnTypeClass, cal, mapValType);
		try {
			keys = super.getKeysFromResultSet();
			//System.out.printf("keys: %d, %s: %d\n", Arrays.hashCode(keys), _returnTypeClass, _returnTypeClass.hashCode());
			thisHash = Arrays.hashCode(keys) ^ _returnTypeClass.hashCode();
		} catch (SQLException e) {
			throw new MapperException("CachingRowToObjectMapper: SQLException: " + e.getMessage(), e);
		}
	}

	@Override
	protected String[] getKeysFromResultSet() throws SQLException {
		return keys;
	}

	@Override
	protected void getFieldMappings() throws SQLException {
		FieldMapping fm = cache.get(thisHash);
		if (fm == null) {
			//System.out.printf("cache miss, hashcode: %d\n", thisHash);
			// generate and put into cache
			super.getFieldMappings();
			synchronized (cache) {
				// I *think* we only need to synchronize here, instead of around the get as well
				// it may allow some leaks (field mappings being generated more than once)
				// but the performance benefits of not having this entire method synchronized
				// statically probably outweighs the negatives
				cache.put(thisHash, new FieldMapping(_fields, _fieldTypes));
			}
		} else {
			//System.out.printf("cache hit, hashcode: %d\n", thisHash);
			// load from cache
			_fields = fm._fields;
			_fieldTypes = fm._fieldTypes;
		}
	}

	private static class FieldMapping {
		public final AccessibleObject[] _fields;
		public final int[] _fieldTypes;

		private FieldMapping(AccessibleObject[] _fields, int[] _fieldTypes) {
			this._fields = _fields;
			this._fieldTypes = _fieldTypes;
		}
	}
}
