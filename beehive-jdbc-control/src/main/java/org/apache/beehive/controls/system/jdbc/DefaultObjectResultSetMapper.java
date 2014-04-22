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
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.system.jdbc.JdbcControl.SQL;

import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Default ResultSetMapper implementation for Objects.
 */
public class DefaultObjectResultSetMapper extends AbstractResultSetMapper {

    /**
     * static reference to the TypeMappingsFactory for this class
     */
    protected static final TypeMappingsFactory _tmf = TypeMappingsFactory.getInstance();

    /**
     * Map the ResultSet to the method's return type. The object type returned is defined by the return type of the method.
     *
     * @param context   A ControlBeanContext instance, see Beehive controls javadoc for additional information
     * @param m         Method assoicated with this call.
     * @param resultSet Result set to map.
     * @param cal       A Calendar instance for time/date value resolution.
     * @return The Object resulting from the ResultSet
     */
    public Object mapToResultType(ControlBeanContext context, Method m, ResultSet resultSet, Calendar cal) {

        final Class returnType = m.getReturnType();
        final boolean isArray = returnType.isArray();

        try {
            if (isArray) {
                final SQL methodSQL = context.getMethodPropertySet(m, SQL.class);
                return arrayFromResultSet(resultSet, methodSQL.arrayMaxLength(), returnType, cal);
            } else {
                if (!resultSet.next()) {
                    return _tmf.fixNull(m.getReturnType());
                }
                return RowMapperFactory.getRowMapper(resultSet, returnType, cal).mapRowToReturnType();
            }
        } catch (SQLException e) {
            throw new ControlException(e.getMessage(), e);
        }
    }

    //
    // ////////////////////////////////// PRIVATE METHODS //////////////////////////////////////////
    //

    /**
     * Invoked when the return type of the method is an array type.
     *
     * @param rs         ResultSet to process.
     * @param maxRows    The maximum size of array to create, a value of 0 indicates that the array
     *                   size will be the same as the result set size (no limit).
     * @param arrayClass The class of object contained within the array
     * @param cal        A calendar instance to use for date/time values
     * @return An array of the specified class type
     * @throws SQLException On error.
     */
    protected Object arrayFromResultSet(ResultSet rs, int maxRows, Class arrayClass, Calendar cal)
            throws SQLException {

        ArrayList<Object> list = new ArrayList<Object>();
        Class componentType = arrayClass.getComponentType();
        RowMapper rowMapper = RowMapperFactory.getRowMapper(rs, componentType, cal);

        // a value of zero indicates that all rows from the resultset should be included.
        if (maxRows == 0) {
            maxRows = -1;
        }

        int numRows;
        boolean hasMoreRows = rs.next();
        for (numRows = 0; numRows != maxRows && hasMoreRows; numRows++) {
            list.add(rowMapper.mapRowToReturnType());
            hasMoreRows = rs.next();
        }

        Object array = Array.newInstance(componentType, numRows);
        try {
            for (int i = 0; i < numRows; i++) {
                Array.set(array, i, list.get(i));
            }
        } catch (IllegalArgumentException iae) {
            ResultSetMetaData md = rs.getMetaData();
            // assuming no errors in resultSetObject() this can only happen
            // for single column result sets.
            throw new ControlException("The declared Java type for array " + componentType.getName()
                                       + "is incompatible with the SQL format of column " + md.getColumnName(1)
                                       + md.getColumnTypeName(1) + "which returns objects of type + "
                                       + list.get(0).getClass().getName());
        }
        return array;
    }
}
