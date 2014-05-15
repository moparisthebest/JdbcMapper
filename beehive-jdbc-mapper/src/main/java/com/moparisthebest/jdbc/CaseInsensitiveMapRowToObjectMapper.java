package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mopar on 5/15/14.
 */
public class CaseInsensitiveMapRowToObjectMapper<T> extends RowToObjectMapper<T> {
	public CaseInsensitiveMapRowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass) {
		super(resultSet, returnTypeClass);
	}

	public CaseInsensitiveMapRowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Class<?> mapValType) {
		super(resultSet, returnTypeClass, mapValType);
	}

	public CaseInsensitiveMapRowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal) {
		super(resultSet, returnTypeClass, cal);
	}

	public CaseInsensitiveMapRowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		super(resultSet, returnTypeClass, cal, mapValType);
	}

	@Override
	protected Map<String, Object> getMapImplementation() throws IllegalAccessException, InstantiationException {
		if(HashMap.class.equals(_returnTypeClass))
			return new HashMap<String, Object>(){
				@Override
				public Object get(Object key) {
					return super.get(key instanceof String ? ((String)key).toLowerCase() : key);
				}

				@Override
				public boolean containsKey(Object key) {
					return super.containsKey(key instanceof String ? ((String)key).toLowerCase() : key);
				}
			};
		return super.getMapImplementation();
	}
}
