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

import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.moparisthebest.jdbc.UpdateableDTO.YES;
import static com.moparisthebest.jdbc.UpdateableDTO.NO;

/**
 * Map a ResultSet row to an Object. This mapper uses Java reflection to perform the mapping.
 * <p/>
 * This class is modified from org.apache.beehive.controls.system.jdbc.RowToObjectMapper
 * <p/>
 * The column names are compared, case insensitive, and with and without underscores (_), to the set* methods
 * and fields of the given class to map the fields.  For example:
 * <p/>
 * USERID would prefer method setUserId(), and fall back to field userId if the method doesn't exist
 * USER_ID would act the same as the above, but also additionally try to match method setUser_Id(), or field user_id
 * <p/>
 * First, this class will look for a constructor that takes a ResultSet as a parameter, if it finds one, it will
 * instantiate it with that constructor, sending in the ResultSet.  Otherwise, this will only try to use public
 * setters, but will set fields regardless of specified access, even private fields.
 *
 * @author Travis Burtrum (modifications from beehive)
 */
public class RowToObjectMapper<T> extends RowMapper {

	public static final int TYPE_BOOLEAN = _tmf.getTypeId(Boolean.TYPE);//TypeMappingsFactory.TYPE_BOOLEAN; // not public? 
	public static final int TYPE_BOOLEAN_OBJ = _tmf.getTypeId(Boolean.class);//TypeMappingsFactory.TYPE_BOOLEAN_OBJ; // not public?

	protected final int _columnCount;
	protected final boolean resultSetConstructor;
	protected final Constructor<T> constructor;
	protected final Class<? extends T> _returnTypeClass; // over-ride non-generic version of this in super class
	
	// only non-null when _returnTypeClass is an array, or a map
	protected final Class<?> componentType;
	protected final boolean returnMap;

	protected AccessibleObject[] _fields;
	protected int[] _fieldTypes;

	protected final Object[] _args = new Object[1];

	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass) {
		this(resultSet, returnTypeClass, null, null);
	}

	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Class<?> mapValType) {
		this(resultSet, returnTypeClass, null, mapValType);
	}

	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal) {
		this(resultSet, returnTypeClass, cal, null);
	}

	/**
	 * Create a new RowToObjectMapper.
	 *
	 * @param resultSet       ResultSet to map
	 * @param returnTypeClass Class to map to.
	 * @param cal             Calendar instance for date/time mappings.
	 */
	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		super(resultSet, returnTypeClass, cal);
		returnMap = Map.class.isAssignableFrom(returnTypeClass);
		if(returnMap){
			_returnTypeClass = ResultSetMapper.getConcreteClass(returnTypeClass, HashMap.class);
			componentType = mapValType;
		}else{
			_returnTypeClass = returnTypeClass;
			// detect if we want an array back
			componentType = returnTypeClass.getComponentType();
		}

		_fields = null;

		try {
			_columnCount = resultSet.getMetaData().getColumnCount();
		} catch (SQLException e) {
			throw new MapperException("RowToObjectMapper: SQLException: " + e.getMessage(), e);
		}

		// detect if returnTypeClass has a constructor that takes a ResultSet, if so, our job couldn't be easier...
		boolean resultSetConstructor = false;
		Constructor<T> constructor = null;
		try {
			constructor = returnTypeClass.getConstructor(ResultSet.class);
			if (!constructor.isAccessible())
				constructor.setAccessible(true);
			resultSetConstructor = true;
		} catch (Throwable e) {
			// if no resultSetConstructor find the constructor
			try {
				constructor = returnTypeClass.getDeclaredConstructor();
				if (!constructor.isAccessible())
					constructor.setAccessible(true);
			} catch (Throwable e1) {
				if(_columnCount > 2) // if column count is 2 or less, it might map directly to a type like a Long or something, or be a map which does
					throw new MapperException("Exception when trying to get constructor for : "+returnTypeClass.getName() + " Must have default no-arg constructor or one that takes a single ResultSet.", e1);
			}
		}
		this.resultSetConstructor = resultSetConstructor;
		this.constructor = constructor;
	}

	/**
	 * This returns a new instance of the Map class required by Map<String, Object>[]
	 * It lives in it's own method to minimize suppressed warnings and allow subclasses to override methods,
	 * like perhaps to implement the original beehive behavior of case-insensitive strings
	 */
	@SuppressWarnings({"unchecked"})
	protected Map<String, Object> getMapImplementation() throws IllegalAccessException, InstantiationException {
		return (Map<String, Object>)_returnTypeClass.newInstance();
	}

	/**
	 * Do the mapping.
	 *
	 * @return An object instance.
	 */
	public T mapRowToReturnType() {

		if (resultSetConstructor)
			try {
				return constructor.newInstance(_resultSet);
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create instance of : "
						+ _returnTypeClass.getName() + " sending in a ResultSet object as a parameter", e);
			}  		
		
		if(returnMap) // we want a map
			try {
				final Map<String, Object> ret = getMapImplementation();
				final ResultSetMetaData md = _resultSet.getMetaData();
				final int columnLength = _columnCount+1;
				if(componentType != null && componentType != Object.class){ // we want a specific value type
					int typeId = _tmf.getTypeId(componentType);
					for(int x = 1; x < columnLength; ++x)
						ret.put(md.getColumnName(x).toLowerCase(), extractColumnValue(x, typeId));
				} else // we want a generic object type
					for(int x = 1; x < columnLength; ++x)
						ret.put(md.getColumnName(x).toLowerCase(), _resultSet.getObject(x));
				return _returnTypeClass.cast(ret);
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create a Map<String, "
						+ (componentType == null ? "java.lang.Object" : componentType.getName()) + "> from a ResultSet row" +
						", all columns must be of the map value type", e);
			}
		else if(componentType != null) // we want an array
			try {
				final Object[] ret = (Object[])Array.newInstance(componentType, _columnCount);
				final int typeId = _tmf.getTypeId(componentType);
				for(int x = 0; x < _columnCount;)
					ret[x] = extractColumnValue(++x, typeId);
				return _returnTypeClass.cast(ret);
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create a "
						+ componentType.getName() + "[] from a ResultSet row, all columns must be of that type", e);
			}


		T resultObject = null;

		// if the ResultSet only contains a single column we may be able to map directly
		// to the return type -- if so we don't need to build any structures to support
		// mapping
		if (_columnCount == 1) {

			final int typeId = _tmf.getTypeId(_returnTypeClass);

			try {
				if (typeId != TypeMappingsFactory.TYPE_UNKNOWN) {
					return _returnTypeClass.cast(extractColumnValue(1, typeId));
				} else {
					// we still might want a single value (i.e. java.util.Date)
					Object val = extractColumnValue(1, typeId);
					if (_returnTypeClass.isAssignableFrom(val.getClass())) {
						return _returnTypeClass.cast(val);
					}
				}
			} catch (Exception e) {
				throw new MapperException(e.getMessage(), e);
			}
		}

		if (_fields == null) {
			try {
				getFieldMappings();
			} catch (SQLException e) {
				throw new MapperException(e.getMessage(), e);
			}
		}

		try {
			resultObject = constructor.newInstance();
		} catch (Throwable e) {
			if(constructor == null) // then this is a different error
				throw new MapperException("Exception when trying to get constructor for : "+_returnTypeClass.getName() + " Must have default no-arg constructor or one that takes a single ResultSet.", e);
			throw new MapperException(e.getClass().getName() + " when trying to create instance of : "
					+ _returnTypeClass.getName(), e);
		}

		for (int i = 1; i < _fields.length; i++) {
			AccessibleObject f = _fields[i];

			try {
				_args[0] = extractColumnValue(i, _fieldTypes[i]);
				//System.out.printf("field: '%s' obj: '%s' fieldType: '%s'\n", _fields[i], _args[0], _fieldTypes[i]);
				// custom hacked-in support for enums, can do better when we scrap org.apache.beehive.controls.system.jdbc.TypeMappingsFactory
				if(_fieldTypes[i] == 0 && _args[0] instanceof String){
					Class<?> fieldType = f instanceof Field ? ((Field)f).getType() : ((Method)f).getParameterTypes()[0];
					if(Enum.class.isAssignableFrom(fieldType))
						_args[0] = Enum.valueOf((Class<? extends Enum>)fieldType, (String)_args[0]);
				}
				if (f instanceof Field) {
					((Field) f).set(resultObject, _args[0]);
				} else {
					((Method) f).invoke(resultObject, _args);
				}
			} catch (SQLException e) {
				throw new MapperException(e.getMessage(), e);
			} catch (IllegalArgumentException iae) {

				try {
					ResultSetMetaData md = _resultSet.getMetaData();
					if (f instanceof Field) {
						throw new MapperException("The declared Java type for field " + ((Field) f).getName()
								+ ((Field) f).getType().toString()
								+ " is incompatible with the SQL format of column " + i + " '" + md.getColumnName(i)
								+ "' (" + md.getColumnTypeName(i)
								+ ") which returns objects of type " + _args[0].getClass().getName());
					} else {
						throw new MapperException("The declared Java type for method " + ((Method) f).getName()
								+ ((Method) f).getParameterTypes()[0].toString()
								+ " is incompatible with the SQL format of column " + i + " '" + md.getColumnName(i)
								+ "' (" + md.getColumnTypeName(i)
								+ ") which returns objects of type " + _args[0].getClass().getName());
					}
				} catch (SQLException e) {
					throw new MapperException(e.getMessage(), e);
				}

			} catch (IllegalAccessException e) {
				if (f instanceof Field) {
					throw new MapperException("IllegalAccessException when trying to access field " + ((Field) f).getName(), e);
				} else {
					throw new MapperException("IllegalAccessException when trying to access method " + ((Method) f).getName(), e);
				}
			} catch (InvocationTargetException e) {
				throw new MapperException("InvocationTargetException when trying to access method " + ((Method) f).getName(), e);
			}
		}
		// if this resultObject is Finishable, call finish()
		if (resultObject instanceof Finishable)
			try {
				((Finishable) resultObject).finish(_resultSet);
			} catch (SQLException e) {
				throw new MapperException(e.getMessage(), e);
			}
		return resultObject;
	}

	/**
	 * Provided so we can extract a column value to use for the key of a map
	 *
	 * @param index
	 * @param classType
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({"unchecked"})
	public <E> E extractColumnValue(int index, Class<E> classType) throws SQLException {
			return classType.cast(extractColumnValue(index, _tmf.getTypeId(classType)));
	}

	/**
	 * Provided so we know whether to map all values to a type, or just the second one
	 *
	 * @return
	 */
	public int getColumnCount() {
		return this._columnCount;
	}

	/**
	 * Build the structures necessary to do the mapping
	 *
	 * @throws SQLException on error.
	 */
	protected void getFieldMappings()
			throws SQLException {

		final String[] keys = getKeysFromResultSet();
		//System.out.println("keys: "+ Arrays.toString(keys));

		// added this to handle stripping '_' from keys
		Map<String, String> strippedKeys = new HashMap<String, String>();
		for (final String key : keys) {
			String strippedKey = key;
			if (key != null) {
				strippedKey = key.replaceAll("_", "");
				if (key.equals(strippedKey))
					continue;
				strippedKeys.put(strippedKey, key);
			}
		}
		//System.out.println("strippedKeys: "+strippedKeys);
		//
		// find fields or setters for return class
		//
		HashMap<String, AccessibleObject> mapFields = new HashMap<String, AccessibleObject>(_columnCount * 2);
		for (int i = 1; i <= _columnCount; i++) {
			mapFields.put(keys[i], null);
		}

		// public methods
		Method[] classMethods = _returnTypeClass.getMethods();
		for (Method m : classMethods) {
			//System.out.printf("method: '%s', isSetterMethod: '%s'\n", m, isSetterMethod(m));
			if (isSetterMethod(m)) {
				String fieldName = m.getName().substring(3).toUpperCase();
				//System.out.println("METHOD-fieldName1: "+fieldName);
				if (!mapFields.containsKey(fieldName)) {
					fieldName = strippedKeys.get(fieldName);
					if (fieldName == null)
						continue;
					//System.out.println("METHOD-fieldName2: "+fieldName);
				}
				final AccessibleObject field = mapFields.get(fieldName);
				// check for overloads
				if (field == null) {
					mapFields.put(fieldName, m);
				} else {
					// fix for 'overloaded' methods when it comes to stripped keys, we want the exact match
					final String thisName = m.getName().substring(3).toUpperCase();
					final String previousName = ((Method) field).getName().substring(3).toUpperCase();
					//System.out.printf("thisName: '%s', previousName: '%s', mapFields.containsKey(thisName): %b, strippedKeys.containsKey(previousName): %b\n", thisName, previousName, mapFields.containsKey(thisName), strippedKeys.containsKey(previousName));
					if(mapFields.containsKey(thisName) && strippedKeys.containsKey(previousName)) {
						mapFields.put(fieldName, m);
					} else if (!mapFields.containsKey(previousName) || !strippedKeys.containsKey(thisName)) {
						throw new MapperException("Unable to choose between overloaded methods '" + m.getName()
								+ "' and '" + ((Method) field).getName() + "' for field '" + fieldName + "' on the '" + _returnTypeClass.getName() + "' class. Mapping is done using "
								+ "a case insensitive comparison of SQL ResultSet columns to field "
								+ "names and public setter methods on the return class. Columns are also "
								+ "stripped of '_' and compared if no match is found with them.");
					}
					// then the 'overloaded' method is already correct
				}
			}
		}

		// fix for 8813: include inherited and non-public fields
		for (Class clazz = _returnTypeClass; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
			//System.out.println("searching for fields in class: "+clazz.getName());
			Field[] classFields = clazz.getDeclaredFields();
			//System.out.println("fields in class: "+Arrays.toString(classFields));
			for (Field f : classFields) {
				if (Modifier.isStatic(f.getModifiers())) continue;
				//if (!Modifier.isPublic(f.getModifiers())) continue;  // commented this out to work on all types of fields
				String fieldName = f.getName().toUpperCase();
				//System.out.println("fieldName: "+fieldName);
				if (!mapFields.containsKey(fieldName)) {
					fieldName = strippedKeys.get(fieldName);
					if (fieldName == null)
						continue;
				}
				final AccessibleObject field = mapFields.get(fieldName);
				if (field == null) {
					mapFields.put(fieldName, f);
				} else if(field instanceof Field) {
					// fix for 'overloaded' fields when it comes to stripped keys, we want the exact match
					final String thisName = f.getName().toUpperCase();
					final String previousName = ((Field) field).getName().toUpperCase();
					//System.out.printf("thisName: '%s', previousName: '%s', mapFields.containsKey(thisName): %b, strippedKeys.containsKey(previousName): %b\n", thisName, previousName, mapFields.containsKey(thisName), strippedKeys.containsKey(previousName));
					if(mapFields.containsKey(thisName) && strippedKeys.containsKey(previousName)) {
						mapFields.put(fieldName, f);
					} else if (!mapFields.containsKey(previousName) || !strippedKeys.containsKey(thisName)) {
						throw new MapperException("Unable to choose between overloaded fields '" + f.getName()
								+ "' and '" + ((Field) field).getName() + "' for field '" + fieldName + "' on the '" + _returnTypeClass.getName() + "' class. Mapping is done using "
								+ "a case insensitive comparison of SQL ResultSet columns to field "
								+ "names and public setter methods on the return class. Columns are also "
								+ "stripped of '_' and compared if no match is found with them.");
					}
					// then the 'overloaded' field is already correct
				}
			}
		}

		// finally actually init the fields array
		_fields = new AccessibleObject[_columnCount + 1];
		_fieldTypes = new int[_columnCount + 1];

		for (int i = 1; i < _fields.length; i++) {
			AccessibleObject f = mapFields.get(keys[i]);
			if (f == null) {
				throw new MapperException("Unable to map the SQL column '" + keys[i]
						+ "' to a field on the '" + _returnTypeClass.getName() +
						"' class. Mapping is done using a case insensitive comparison of SQL ResultSet "
						+ "columns to field "
						+ "names and public setter methods on the return class. Columns are also "
						+ "stripped of '_' and compared if no match is found with them.");
			}
			f.setAccessible(true);
			_fields[i] = f;
			if (f instanceof Field) {
				_fieldTypes[i] = _tmf.getTypeId(((Field) f).getType());
			} else {
				_fieldTypes[i] = _tmf.getTypeId(((Method) f).getParameterTypes()[0]);
			}
		}
	}

	@Override
	protected Object extractColumnValue(int index, int resultType) throws SQLException {
		try{
			if (resultType != TYPE_BOOLEAN && resultType != TYPE_BOOLEAN_OBJ)
				return super.extractColumnValue(index, resultType);
			else {
				// do some special handling to convert a database string to a boolean
				boolean ret;
				try {
					// try to get an actual boolean from the database
					ret = _resultSet.getBoolean(index);
					// null seems to get returned as false above, so String code won't run if its null
					if (_resultSet.wasNull())
						if (resultType == TYPE_BOOLEAN_OBJ)
							return null; // only return null for Boolean object
						else
							throw new MapperException(String.format("Implicit conversion of database string to boolean failed on column '%d'. Returned string needs to be 'Y' or 'N' and was instead 'null'. If you want to accept null values, make it an object Boolean instead of primitive boolean.", index));
				} catch (SQLException e) {
					// if we are here, it wasn't a boolean or null, so try to grab a string instead
					String bool = _resultSet.getString(index);//.toUpperCase(); // do we want it case-insensitive?
					ret = YES.equals(bool);
					if (!ret && !NO.equals(bool))
						throw new MapperException(String.format("Implicit conversion of database string to boolean failed on column '%d'. Returned string needs to be 'Y' or 'N' and was instead '%s'.", index, bool));
					//throw e;
				}
				return ret ? Boolean.TRUE : Boolean.FALSE;
			}
		}catch(SQLException e){
			throw new SQLExceptionColumnNum(e, index);
		}
	}

	public static <T> T fixNull(Class<T> returnType) {
		return returnType.cast(_tmf.fixNull(returnType));
	}
}

