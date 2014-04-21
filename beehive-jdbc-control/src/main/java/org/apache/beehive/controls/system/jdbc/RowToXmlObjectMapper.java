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

package org.apache.beehive.controls.system.jdbc;

import org.apache.beehive.controls.api.ControlException;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Maps a ResultSet row to an XmlObject.
 */
public class RowToXmlObjectMapper extends RowMapper {

    private final int _columnCount;
    private final SchemaType _schemaType;

    private SetterMethod[] _setterMethods;
    private final Object[] _args = new Object[1];

    /**
     * Create a new RowToXmlObjectMapper.
     *
     * @param resultSet       ResultSet to map
     * @param returnTypeClass Class to map to.
     * @param cal             Calendar instance for date/time mappings.
     * @throws SQLException on error.
     */
    RowToXmlObjectMapper(ResultSet resultSet, Class returnTypeClass, Calendar cal)
            throws SQLException {
        super(resultSet, returnTypeClass, cal);

        _columnCount = resultSet.getMetaData().getColumnCount();
        _schemaType = getSchemaType(_returnTypeClass);
        _setterMethods = null;
    }

    /**
     * map a row from the ResultSet to an XmlObject instance
     *
     * @return An XmlObject instance.
     */
    public Object mapRowToReturnType() {

        Object resultObject = null;
        if (_columnCount == 1) {

            final int typeId = _tmf.getTypeId(_returnTypeClass);

            try {
                if (typeId != TypeMappingsFactory.TYPE_UNKNOWN) {
                    return extractColumnValue(1, typeId);
                } else {
                    // we still might want a single value (i.e. java.util.Date)
                    Object val = extractColumnValue(1, typeId);
                    if (_returnTypeClass.isAssignableFrom(val.getClass())) {
                        return val;
                    }
                }
            } catch (SQLException e) {
                throw new ControlException(e.getMessage(), e);
            }
        }

        if (_setterMethods == null) {
            try {
                getResultSetMappings();
            } catch (SQLException e) {
                throw new ControlException(e.getMessage(), e);
            }
        }

        resultObject = XmlObject.Factory.newInstance(new XmlOptions().setDocumentType(_schemaType));

        for (int i = 1; i < _setterMethods.length; i++) {
            Method setterMethod = _setterMethods[i].getSetter();
            Object resultValue = null;

            try {
                resultValue = extractColumnValue(i, _setterMethods[i].getParameterType());

                // if the setter is for an xmlbean enum type, convert the extracted resultset column
                // value to the proper xmlbean enum type. All xmlbean enums are derived from the class
                // StringEnumAbstractBase
                if (_setterMethods[i].getParameterType() == TypeMappingsFactory.TYPE_XMLBEAN_ENUM) {
                    Class parameterClass = _setterMethods[i].getParameterClass();
                    Method m = parameterClass.getMethod("forString", new Class[]{String.class});
                    resultValue = m.invoke(null, new Object[]{resultValue});
                }

                _args[0] = resultValue;
                setterMethod.invoke(resultObject, _args);

                if (_setterMethods[i].getNilable() != null) {
                    if (_resultSet.wasNull()) {
                        _setterMethods[i].getNilable().invoke(resultObject, (Object[]) null);
                    }
                }
            } catch (SQLException se) {
                throw new ControlException(se.getMessage(), se);
            } catch (IllegalArgumentException iae) {
                try {
                    ResultSetMetaData md = _resultSet.getMetaData();
                    throw new ControlException("The declared Java type for method " + setterMethod.getName()
                                               + setterMethod.getParameterTypes()[0].toString()
                                               + " is incompatible with the SQL format of column " + md.getColumnName(i).toString()
                                               + md.getColumnTypeName(i).toString()
                                               + " which returns objects of type " + resultValue.getClass().getName());
                } catch (SQLException e) {
                    throw new ControlException(e.getMessage(), e);
                }
            } catch (IllegalAccessException e) {
                throw new ControlException("IllegalAccessException when trying to access method " + setterMethod.getName(), e);
            } catch (NoSuchMethodException e) {
                throw new ControlException("NoSuchMethodException when trying to map schema enum value using Enum.forString().", e);
            } catch (InvocationTargetException e) {
                throw new ControlException("IllegalInvocationException when trying to access method " + setterMethod.getName(), e);
            }
        }
        return resultObject;
    }


// ///////////////////////////////////////////////// private methods /////////////////////////////////////////////////

    /**
     * Build the necessary structures to do the mapping
     *
     * @throws SQLException
     */
    private void getResultSetMappings() throws SQLException {

        //
        // special case for XmlObject, find factory class
        //
        if (_schemaType.isDocumentType()) {
            return;
        }

        final String[] keys = getKeysFromResultSet();

        //
        // find setters for return class
        //
        HashMap<String, Method> mapFields = new HashMap<String, Method>(_columnCount * 2);
        for (int i = 1; i <= _columnCount; i++) {
            mapFields.put(keys[i], null);
        }

        // public methods
        Method[] classMethods = _returnTypeClass.getMethods();
        for (Method method : classMethods) {

            if (isSetterMethod(method)) {
                final String fieldName = method.getName().substring(3).toUpperCase();
                if (mapFields.containsKey(fieldName)) {
                    mapFields.put(fieldName, method);
                }
            }
        }

        // finally actually init the fields array
        _setterMethods = new SetterMethod[_columnCount + 1];

        for (int i = 1; i < _setterMethods.length; i++) {
            Method setterMethod = mapFields.get(keys[i]);
            if (setterMethod == null) {
                throw new ControlException("Unable to map the SQL column " + keys[i]
                                           + " to a field on the " + _returnTypeClass.getName() +
                                           " class. Mapping is done using a case insensitive comparision of SQL ResultSet "
                                           + "columns to public setter methods on the return class.");
            }

            _setterMethods[i] = new SetterMethod(setterMethod);
        }
    }

    /**
     * Build a String array of column names from the ResultSet.
     *
     * @return A String array containing the column names contained within the ResultSet.
     * @throws SQLException on error
     */
    protected String[] getKeysFromResultSet()
            throws SQLException {

        String[] keys = super.getKeysFromResultSet();

        // check schemaProperty mapping names for more accurate column->field mapping
        SchemaProperty[] props = _schemaType.getElementProperties();
        for (int i = 0; i < props.length; i++) {

            int col = -1;
            try {
                col = _resultSet.findColumn(props[i].getName().getLocalPart());
            } catch (SQLException x) {
            }

            if (col > 0) {
                keys[col] = props[i].getJavaPropertyName().toUpperCase();
            }
        }
        return keys;
    }

    /**
     * Get the SchemaType for the specified class.
     *
     * @param returnType Class to get the SchemaType for.
     * @return SchemaType
     */
    private SchemaType getSchemaType(Class returnType) {
        SchemaType schemaType = null;
        if (XmlObject.class.isAssignableFrom(returnType)) {
            try {
                Field f = returnType.getField("type");
                if (SchemaType.class.isAssignableFrom(f.getType()) && Modifier.isStatic(f.getModifiers())) {
                    schemaType = (SchemaType) f.get(null);
                }
            } catch (NoSuchFieldException x) {
            } catch (IllegalAccessException x) {
            }
        }
        return schemaType;
    }

    // /////////////////////////////////////////////INNER CLASSES/////////////////////////////////////////////////

    /**
     * Helper class which contains setter method information.
     */
    private final class SetterMethod {
        private final Method _setter;
        private final int _parameterType;
        private final Class _parameterClass;
        private final Method _nilable;

        /**
         * Create a new setter method.
         *
         * @param setter Method instance.
         */
        SetterMethod(Method setter) {
            _setter = setter;
            _parameterClass = _setter.getParameterTypes()[0];
            _parameterType = _tmf.getTypeId(_parameterClass);
            _nilable = isNilable();
        }

        /**
         * Return the setter method.
         *
         * @return Method
         */
        Method getSetter() { return _setter; }

        /**
         * Return the class of the setter method's paramater.
         *
         * @return Class of the setter method's param.
         */
        Class getParameterClass() { return _parameterClass; }

        /**
         * Get the type of the methods paramter.
         * Type is defined by the TypeMappingsFactory, prefixed by TYPE_.
         *
         * @return int type.
         */
        int getParameterType() { return _parameterType; }

        /**
         * Get the nilable method for this setter.
         *
         * @return Method.
         */
        Method getNilable() { return _nilable; }

        /**
         * This takes care of the special case for xml beans return types.
         * since they return primitive types even if they are nillable, we
         * must explicitly call setNil after getting the column out of the resultSet.
         * for that, we need to keep track of each field's setNil method.
         * if the field is not nillable, it will not have a setNil method, and
         * that array index will be null.
         *
         * @return Method
         */
        private Method isNilable() {
            try {
                return _returnTypeClass.getMethod("setNil" + _setter.getName().substring(3), new Class[]{});
            } catch (NoSuchMethodException e) {
                // NOOP - just means there is no setNil
            }
            return null;
        }
    }
}
