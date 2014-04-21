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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Abstract base class for all row mappers.
 *
 * RowMappers are used to map the contents of a row in a ResultSet to the return type of an annotated method.
 * Supported RowMapper types include: HashMap, Map, Object, XmlObject.  When a ResultSetMapper is ready to
 * map a ResultSet row to an object, it requests a RowMapper for the return type of the method from the
 * RowMapperFactory.
 *
 */
public abstract class RowMapper {

    private static final String SETTER_NAME_REGEX = "^(set)([A-Z_]\\w*+)";
    protected static final TypeMappingsFactory _tmf = TypeMappingsFactory.getInstance();
    protected static final Pattern _setterRegex = Pattern.compile(SETTER_NAME_REGEX);

    /** ResultSet to map. */
    protected final ResultSet _resultSet;

    /** Calendar instance for date/time mappings. */
    protected final Calendar _cal;

    /** Class to map ResultSet Rows to. */
    protected final Class<?> _returnTypeClass;

    /**
     * Create a new RowMapper for the specified ResultSet and return type Class.
     * @param resultSet ResultSet to map
     * @param returnTypeClass Class to map ResultSet rows to.
     * @param cal Calendar instance for date/time values.
     */
    protected RowMapper(ResultSet resultSet, Class<?> returnTypeClass, Calendar cal) {
        _resultSet = resultSet;
        _returnTypeClass = returnTypeClass;
        _cal = cal;
    }

    /**
     * Map a ResultSet row to the return type class
     * @return An instance of class.
     */
    public abstract Object mapRowToReturnType();


    /**
     * Build a String array of column names from the ResultSet.
     * @return A String array containing the column names contained within the ResultSet.
     * @throws SQLException on error
     */
    protected String[] getKeysFromResultSet() throws SQLException {

        String[] keys;
        final ResultSetMetaData md = _resultSet.getMetaData();
        final int columnCount = md.getColumnCount();

        keys = new String[columnCount + 1];
        for (int i = 1; i <= columnCount; i++) {
            keys[i] = md.getColumnName(i).toUpperCase();
        }
        return keys;
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
    protected Object extractColumnValue(int index, int resultType) throws SQLException {

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
            case TypeMappingsFactory.TYPE_BOOLEAN:
                return _resultSet.getBoolean(index) ? Boolean.TRUE : Boolean.FALSE;
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
            case TypeMappingsFactory.TYPE_BOOLEAN_OBJ:
                {
                    boolean i = _resultSet.getBoolean(index);
                    return _resultSet.wasNull() ? null : (i ? Boolean.TRUE : Boolean.FALSE);
                }
            case TypeMappingsFactory.TYPE_STRING:
            case TypeMappingsFactory.TYPE_XMLBEAN_ENUM:
                return _resultSet.getString(index);
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
                throw new ControlException("streaming return types are not supported by the JdbcControl; use ResultSet instead");
            case TypeMappingsFactory.TYPE_STRUCT:
            case TypeMappingsFactory.TYPE_UNKNOWN:
                // JAVA_TYPE (could be any), or REF
                return _resultSet.getObject(index);
            default:
                throw new ControlException("internal error: unknown type ID: " + Integer.toString(resultType));
        }
    }
}
