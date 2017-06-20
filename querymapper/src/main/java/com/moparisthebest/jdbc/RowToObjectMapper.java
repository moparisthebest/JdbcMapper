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

import com.moparisthebest.jdbc.util.CaseInsensitiveHashMap;
import com.moparisthebest.jdbc.util.ResultSetUtil;

import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//IFJAVA8_START
import java.time.*;
//IFJAVA8_END

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
public class RowToObjectMapper<K, T> extends AbstractRowMapper<K, T> {

	private static final String SETTER_NAME_REGEX = "^(set)([A-Z_]\\w*+)";
	protected static final TypeMappingsFactory _tmf = TypeMappingsFactory.getInstance();
	public static final Pattern _setterRegex = Pattern.compile(SETTER_NAME_REGEX);

	public static final int TYPE_BOOLEAN = _tmf.getTypeId(Boolean.TYPE);//TypeMappingsFactory.TYPE_BOOLEAN; // not public? 
	public static final int TYPE_BOOLEAN_OBJ = _tmf.getTypeId(Boolean.class);//TypeMappingsFactory.TYPE_BOOLEAN_OBJ; // not public?

	protected boolean resultSetConstructor, constructorLoaded = false;
	protected Constructor<? extends T> constructor;
	protected final Class<? extends T> _returnTypeClass; // over-ride non-generic version of this in super class

	// only non-null when _returnTypeClass is an array, or a map
	protected final Class<?> componentType;
	protected final boolean returnMap;

	protected AccessibleObject[] _fields = null;
	protected int[] _fieldTypes, _fieldOrder;
	protected Class<? extends Enum>[] _fieldClasses;

	protected Object[] _args;

	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass) {
		this(resultSet, returnTypeClass, null, null);
	}

	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Class<?> mapValType) {
		this(resultSet, returnTypeClass, null, mapValType);
	}

	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal) {
		this(resultSet, returnTypeClass, cal, null);
	}


	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		this(resultSet, returnTypeClass, cal, mapValType, null);
	}


	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		this(resultSet, returnTypeClass, cal, mapValType, mapKeyType, false);
	}

	public RowToObjectMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType, boolean caseInsensitiveMap) {
		this(null, resultSet, returnTypeClass, cal, mapValType, mapKeyType, caseInsensitiveMap);
	}

	/**
	 * Create a new RowToObjectMapper.
	 *
	 * @param resultSet       ResultSet to map
	 * @param returnTypeClass Class to map to.
	 * @param cal             Calendar instance for date/time mappings.
	 */
	public RowToObjectMapper(String[] keys, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType, boolean caseInsensitiveMap) {
		super(keys, resultSet, returnTypeClass, cal, mapKeyType);
		returnMap = Map.class.isAssignableFrom(returnTypeClass);
		if(returnMap){
			Class<? extends T> rtc = ResultSetMapper.getConcreteClass(returnTypeClass, HashMap.class);
			if(caseInsensitiveMap && HashMap.class.equals(rtc)) {
				@SuppressWarnings("unchecked")
				final Class<? extends T> rtct = (Class<? extends T>) CaseInsensitiveHashMap.class;
				rtc = rtct;
			}
			_returnTypeClass = rtc;
			componentType = mapValType;
		}else{
			_returnTypeClass = returnTypeClass;
			// detect if we want an array back
			componentType = returnTypeClass.getComponentType();
		}
	}

	protected void lazyLoadConstructor() throws SQLException {
		if(constructorLoaded)
			return;
		// detect if returnTypeClass has a constructor that takes a ResultSet, if so, our job couldn't be easier...
		boolean resultSetConstructor = false;
		Constructor<? extends T> constructor = null;
		try {
			constructor = _returnTypeClass.getConstructor(ResultSet.class);
			if (!constructor.isAccessible())
				constructor.setAccessible(true);
			resultSetConstructor = true;
		} catch (Throwable e) {
			// if no resultSetConstructor find the constructor

			// use con.getParameterAnnotations(); for java 6? ugly
			//IFJAVA8_START
			// look for constructor with matching parameters first
			String[] keys = null;
			Map<String, Integer> strippedKeys = null;
			outer:
			for(final Constructor<?> con : _returnTypeClass.getConstructors()) {
				final Parameter[] params = con.getParameters();
				if(params.length == _columnCount) {
					if(!params[0].isNamePresent())
						break; // nothing to do here, compile with -params?
					// do this stuff only once
					if(keys == null) {
						keys = getKeysFromResultSet();
						strippedKeys = new HashMap<String, Integer>(keys.length * 2);
						for (int x = 1; x <= _columnCount; ++x) {
							final String key = keys[x];
							strippedKeys.put(key, x);
							strippedKeys.put(key.replaceAll("_", ""), x);
						}
						_fieldOrder = new int[keys.length];
						_fieldTypes = new int[keys.length];
						@SuppressWarnings("unchecked")
						final Class<? extends Enum>[] noWarnings = (Class<? extends Enum>[]) new Class[keys.length];
						_fieldClasses = noWarnings;
					}
					int count = 0;
					for(final Parameter param : params) {
						final Integer index = strippedKeys.get(param.getName().toUpperCase());
						if(index == null)
							continue outer;
						_fieldOrder[++count] = index;
						_fieldTypes[count] = _tmf.getTypeId(param.getType());
						if(_fieldTypes[count] == TypeMappingsFactory.TYPE_ENUM) {
							@SuppressWarnings("unchecked")
							final Class<? extends Enum> noWarnings = (Class<? extends Enum>) param.getType();
							_fieldClasses[count] = noWarnings;
						}
					}
					@SuppressWarnings("unchecked")
					final Constructor<? extends T> noWarnings = (Constructor<? extends T>)con;
					this._args = new Object[params.length];
					constructor = noWarnings;
				}
			}
			//IFJAVA8_END
			if(constructor == null)
				try {
					constructor = _returnTypeClass.getDeclaredConstructor();
					if (!constructor.isAccessible())
						constructor.setAccessible(true);
				} catch (Throwable e1) {
					// if column count is 2 or less, it might map directly to a type like a Long or something, or be a map which does
					// or if componentType is non-null, then we want an array like Long[] or String[]
					if(_columnCount > 2 && componentType == null)
						throw new MapperException("Exception when trying to get constructor for : "+_returnTypeClass.getName() + " Must have default no-arg constructor or one that takes a single ResultSet.", e1);
				}
		}
		this.resultSetConstructor = resultSetConstructor;
		this.constructor = constructor;
		if(this._args == null)
			this._args = new Object[1];
		this.constructorLoaded = true;
	}

	/**
	 * This returns a new instance of the Map class required by Map<String, Object>[]
	 * It lives in it's own method to minimize suppressed warnings and allow subclasses to override methods,
	 * like perhaps to implement the original beehive behavior of case-insensitive strings
	 */
	@SuppressWarnings({"unchecked"})
	private Map<String, Object> getMapImplementation() throws IllegalAccessException, InstantiationException {
		return (Map<String, Object>)_returnTypeClass.newInstance();
	}

	@Override
	public K getMapKey() throws SQLException {
		return this.extractColumnValue(1, _mapKeyType);
	}

	/**
	 * Do the mapping.
	 *
	 * @return An object instance.
	 */
	@SuppressWarnings({"unchecked"})
	public T mapRowToReturnType() throws SQLException {

		if(mapOnlySecondColumn)
			return this.extractColumnValue(2, _returnTypeClass);

		lazyLoadConstructor();

		if (resultSetConstructor)
			try {
				return constructor.newInstance(_resultSet);
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create instance of : "
						+ _returnTypeClass.getName() + " sending in a ResultSet object as a parameter", e);
			}

		if(_fieldOrder != null)
			try {
				for(int x = 1; x <= _columnCount; ++x)
					_args[x-1] = extractColumnValue(_fieldOrder[x], _fieldTypes[x], _fieldClasses[x]);
				//System.out.println("creating " + constructor + " sending in a objects: " + Arrays.toString(_args));
				return constructor.newInstance(_args);
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create instance of : "
						+ _returnTypeClass.getName() + " sending in a objects: " + Arrays.toString(_args), e);
			}

		if(returnMap) // we want a map
			try {
				final Map<String, Object> ret = getMapImplementation();
				final String[] keys = getKeysFromResultSet();
				final int columnLength = _columnCount+1;
				if(componentType != null && componentType != Object.class){ // we want a specific value type
					int typeId = _tmf.getTypeId(componentType);
					for(int x = 1; x < columnLength; ++x)
						ret.put(keys[x].toLowerCase(), extractColumnValue(x, typeId, componentType));
				} else // we want a generic object type
					for(int x = 1; x < columnLength; ++x)
						ret.put(keys[x].toLowerCase(), _resultSet.getObject(x));
				return _returnTypeClass.cast(ret);
			} catch (Throwable e) {
				throw new MapperException(e.getClass().getName() + " when trying to create a Map<String, "
						+ (componentType == null ? "java.lang.Object" : componentType.getName()) + "> from a ResultSet row" +
						", all columns must be of the map value type", e);
			}
		else if(componentType != null) // we want an array
			try {
				final Object ret = Array.newInstance(componentType, _columnCount);
				final int typeId = _tmf.getTypeId(componentType);
				for(int x = 0; x < _columnCount;)
					Array.set(ret, x, extractColumnValue(++x, typeId, componentType));
					//ret[x] = extractColumnValue(++x, typeId);
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
					return (T)extractColumnValue(1, typeId, _returnTypeClass);
				} else {
					// we still might want a single value (i.e. java.util.Date)
					Object val = extractColumnValue(1, typeId, _returnTypeClass);
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
				_args[0] = extractColumnValue(i, _fieldTypes[i], _fieldClasses[i]);
				//System.out.printf("field: '%s' obj: '%s' fieldType: '%s'\n", _fields[i], _args[0], _fieldTypes[i]);
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
								+ " is incompatible with the SQL format of column " + i + " '" + md.getColumnLabel(i)
								+ "' (" + md.getColumnTypeName(i)
								+ ") which returns objects of type " + _args[0].getClass().getName());
					} else {
						throw new MapperException("The declared Java type for method " + ((Method) f).getName()
								+ ((Method) f).getParameterTypes()[0].toString()
								+ " is incompatible with the SQL format of column " + i + " '" + md.getColumnLabel(i)
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
			return classType.cast(extractColumnValue(index, _tmf.getTypeId(classType), classType));
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
		if(_fieldTypes == null)
			_fieldTypes = new int[_fields.length];
		if(_fieldClasses == null) {
			@SuppressWarnings("unchecked")
			final Class<? extends Enum>[] noWarnings = (Class<? extends Enum>[]) new Class[_fields.length];
			_fieldClasses = noWarnings;
		}

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
			_fields[i] = f;
			if (f instanceof Field) {
				final Field field = (Field) f;
				_fields[i] = modField(field, i);
				_fieldTypes[i] = _tmf.getTypeId(field.getType());
				if(_fieldTypes[i] == TypeMappingsFactory.TYPE_ENUM) {
					@SuppressWarnings("unchecked")
					final Class<? extends Enum> noWarnings = (Class<? extends Enum>) field.getType();
					_fieldClasses[i] = noWarnings;
				}
			} else {
				_fieldTypes[i] = _tmf.getTypeId(((Method) f).getParameterTypes()[0]);
			}
		}
	}

	protected AccessibleObject modField(final Field field, final int index) {
		field.setAccessible(true);
		return field;
	}

	public static <T> T fixNull(Class<T> returnType) {
		return returnType.cast(_tmf.fixNull(returnType));
	}

	/**
	 * Determine if the given method is a java bean setter method.
	 * @param method Method to check
	 * @return True if the method is a setter method.
	 */
	protected boolean isSetterMethod(Method method) {
		Matcher matcher = _setterRegex.matcher(method.getName());
		if (matcher.matches()) {

			if (Modifier.isStatic(method.getModifiers())) return false;
			if (!Modifier.isPublic(method.getModifiers())) return false;
			if (!Void.TYPE.equals(method.getReturnType())) return false;

			// method parameter checks
			Class[] params = method.getParameterTypes();
			if (params.length != 1) return false;
			if (TypeMappingsFactory.TYPE_UNKNOWN == _tmf.getTypeId(params[0])) return false;

			return true;
		}
		return false;
	}

	/**
	 * Extract a column value from the ResultSet and return it as resultType.
	 *
	 * @param index The column index of the value to extract from the ResultSet.
	 * @param resultType The return type. Defined in TypeMappingsFactory.
	 * @return The extracted value
	 * @throws java.sql.SQLException on error.
	 */
	protected Object extractColumnValue(final int index, final int resultType, final Class<?> resultTypeClass) throws SQLException {
		try{
			switch (resultType) {
				case TypeMappingsFactory.TYPE_INT:
					return new Integer(_resultSet.getInt(index));
				case TypeMappingsFactory.TYPE_LONG:
					return new Long(_resultSet.getLong(index));
				case TypeMappingsFactory.TYPE_FLOAT:
					return new Float(_resultSet.getFloat(index));
				case TypeMappingsFactory.TYPE_DOUBLE:
					return new Double(_resultSet.getDouble(index));
				case TypeMappingsFactory.TYPE_BYTE:
					return new Byte(_resultSet.getByte(index));
				case TypeMappingsFactory.TYPE_SHORT:
					return new Short(_resultSet.getShort(index));
				case TypeMappingsFactory.TYPE_INT_OBJ:
				{
					int i = _resultSet.getInt(index);
					return _resultSet.wasNull() ? null : new Integer(i);
				}
				case TypeMappingsFactory.TYPE_LONG_OBJ:
				{
					long i = _resultSet.getLong(index);
					return _resultSet.wasNull() ? null : new Long(i);
				}
				case TypeMappingsFactory.TYPE_FLOAT_OBJ:
				{
					float i = _resultSet.getFloat(index);
					return _resultSet.wasNull() ? null : new Float(i);
				}
				case TypeMappingsFactory.TYPE_DOUBLE_OBJ:
				{
					double i = _resultSet.getDouble(index);
					return _resultSet.wasNull() ? null : new Double(i);
				}
				case TypeMappingsFactory.TYPE_BYTE_OBJ:
				{
					byte i = _resultSet.getByte(index);
					return _resultSet.wasNull() ? null : new Byte(i);
				}
				case TypeMappingsFactory.TYPE_SHORT_OBJ:
				{
					short i = _resultSet.getShort(index);
					return _resultSet.wasNull() ? null : new Short(i);
				}

				case TypeMappingsFactory.TYPE_BOOLEAN:
				case TypeMappingsFactory.TYPE_BOOLEAN_OBJ:
				{
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
				case TypeMappingsFactory.TYPE_STRING:
				case TypeMappingsFactory.TYPE_XMLBEAN_ENUM:
					return _resultSet.getString(index);
				case TypeMappingsFactory.TYPE_ENUM:
					@SuppressWarnings("unchecked")
					final Enum ret = Enum.valueOf((Class<? extends Enum>)resultTypeClass, _resultSet.getString(index));
					return ret;
				case TypeMappingsFactory.TYPE_BIG_DECIMAL:
					return _resultSet.getBigDecimal(index);
				case TypeMappingsFactory.TYPE_BYTES:
					return _resultSet.getBytes(index);
				case TypeMappingsFactory.TYPE_TIMESTAMP:
				{
					if (null == _cal)
						return _resultSet.getTimestamp(index);
					else
						return _resultSet.getTimestamp(index, _cal);
				}
				case TypeMappingsFactory.TYPE_TIME:
				{
					if (null == _cal)
						return _resultSet.getTime(index);
					else
						return _resultSet.getTime(index, _cal);
				}
				case TypeMappingsFactory.TYPE_SQLDATE:
				{
					if (null == _cal)
						return _resultSet.getDate(index);
					else
						return _resultSet.getDate(index, _cal);
				}
				case TypeMappingsFactory.TYPE_DATE:
				{
					// convert explicity to java.util.Date
					// 12918 |  knex does not return java.sql.Date properly from web service
					java.sql.Timestamp ts = (null == _cal) ? _resultSet.getTimestamp(index) : _resultSet.getTimestamp(index, _cal);
					if (null == ts)
						return null;
					return new java.util.Date(ts.getTime());
				}
				case TypeMappingsFactory.TYPE_CALENDAR:
				{
					java.sql.Timestamp ts = (null == _cal) ? _resultSet.getTimestamp(index) : _resultSet.getTimestamp(index, _cal);
					if (null == ts)
						return null;
					Calendar c = (null == _cal) ? Calendar.getInstance() : (Calendar) _cal.clone();
					c.setTimeInMillis(ts.getTime());
					return c;
				}
				case TypeMappingsFactory.TYPE_REF:
					return _resultSet.getRef(index);
				case TypeMappingsFactory.TYPE_BLOB:
					return _resultSet.getBlob(index);
				case TypeMappingsFactory.TYPE_CLOB:
					return _resultSet.getClob(index);
				case TypeMappingsFactory.TYPE_ARRAY:
					return _resultSet.getArray(index);
				case TypeMappingsFactory.TYPE_READER:
				case TypeMappingsFactory.TYPE_STREAM:
					throw new MapperException("streaming return types are not supported by the JdbcControl; use ResultSet instead");
				//IFJAVA8_START
				// start java.time support
				case TypeMappingsFactory.TYPE_INSTANT:
					return ResultSetUtil.getInstant(_resultSet, index, _cal);
				case TypeMappingsFactory.TYPE_LOCALDATETIME:
					return ResultSetUtil.getLocalDateTime(_resultSet, index, _cal);
				case TypeMappingsFactory.TYPE_LOCALDATE:
					return ResultSetUtil.getLocalDate(_resultSet, index, _cal);
				case TypeMappingsFactory.TYPE_LOCALTIME:
					return ResultSetUtil.getLocalTime(_resultSet, index, _cal);
				// todo: send in ZoneId here?
				case TypeMappingsFactory.TYPE_ZONEDDATETIME:
					return ResultSetUtil.getZonedDateTime(_resultSet, index, _cal);
				case TypeMappingsFactory.TYPE_OFFSETDATETIME:
					return ResultSetUtil.getOffsetDateTime(_resultSet, index, _cal);
				case TypeMappingsFactory.TYPE_OFFSETTIME:
					return ResultSetUtil.getOffsetTime(_resultSet, index, _cal);
				case TypeMappingsFactory.TYPE_YEAR:
					// done this way instead of of(int) because usually int->string database coercion is allowed and the other isn't?
					return Year.parse(_resultSet.getString(index));
				case TypeMappingsFactory.TYPE_ZONEID:
					return ZoneId.of(_resultSet.getString(index));
				case TypeMappingsFactory.TYPE_ZONEOFFSET:
					return ZoneOffset.of(_resultSet.getString(index));
				// end java.time support
				//IFJAVA8_END
				case TypeMappingsFactory.TYPE_STRUCT:
				case TypeMappingsFactory.TYPE_UNKNOWN:
					// JAVA_TYPE (could be any), or REF
					return _resultSet.getObject(index);
				default:
					throw new MapperException("internal error: unknown type ID: " + Integer.toString(resultType));
			}
		}catch(SQLException e){
			throw new SQLExceptionColumnNum(e, index);
		}
	}
}

