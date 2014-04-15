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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating row mappers.
 * <p/>
 * Row mapper types supported by this factory include: HashMap, Map, Object, XmlObject. The factory determines the
 * proper row mapper to use by checking its List of RowMappers against the type of mapping requested.  When performing
 * the lookup, the factory attempts to find the most specific type match.  If a match can't be found the most general
 * type of RowMapper is returned, RowToObjectMapper.
 */
public final class RowMapperFactory {

    private static final HashMap<Class, Class<? extends RowMapper>> _rowMappings
            = new HashMap<Class, Class<? extends RowMapper>>();

    private static Class<? extends RowMapper> DEFAULT_OBJ_ROWMAPPING = RowToObjectMapper.class;

    private static Class XMLOBJ_CLASS = null;
    private static Class<? extends RowMapper> DEFAULT_XMLOBJ_ROWMAPPING = null;
    private final static Class[] _params = {ResultSet.class, Class.class, Calendar.class};

    static {

        _rowMappings.put(HashMap.class, RowToHashMapMapper.class);
        _rowMappings.put(Map.class, RowToMapMapper.class);

        try {
            XMLOBJ_CLASS = Class.forName("org.apache.xmlbeans.XmlObject");
            DEFAULT_XMLOBJ_ROWMAPPING = RowToXmlObjectMapper.class;
        } catch (ClassNotFoundException e) {
            // NOOP if apache xml beans not present
        }
    }

    /**
     * Get a RowMapper instance which knows how to map a ResultSet row to the given return type.
     *
     * @param rs              The ResultSet to map.
     * @param returnTypeClass The class to map a ResultSet row to.
     * @param cal             Calendar instance for mapping date/time values.
     * @return A RowMapper instance.
     */
    public static RowMapper getRowMapper(ResultSet rs, Class returnTypeClass, Calendar cal) {

        Class<? extends RowMapper> rm = _rowMappings.get(returnTypeClass);
        if (rm != null) {
            return getMapper(rm, rs, returnTypeClass, cal);
        }

        //
        // if we made it to here, check if the default XMLObject Mapper can be used,
        // otherwise use the default object mapper
        //
        if (XMLOBJ_CLASS != null && XMLOBJ_CLASS.isAssignableFrom(returnTypeClass)) {
            return getMapper(DEFAULT_XMLOBJ_ROWMAPPING, rs, returnTypeClass, cal);
        } else {
            return getMapper(DEFAULT_OBJ_ROWMAPPING, rs, returnTypeClass, cal);
        }
    }

    /**
     * Add a new row mapper to the list of available row mappers.  The getRowMapper method traverses the
     * list of mappers from beginning to end, checking to see if a mapper can handle the specified
     * returnTypeClass.  There is a default mapper which is used if a match cannot be found in the list.
     *
     * @param returnTypeClass Class which this mapper maps a row to.
     * @param rowMapperClass  The row mapper class.
     */
    public static void addRowMapping(Class returnTypeClass, Class<? extends RowMapper> rowMapperClass) {
        _rowMappings.put(returnTypeClass, rowMapperClass);
    }

    /**
     * Replace a row mapping.
     *
     * @param returnTypeClass Class which this mapper maps a row to.
     * @param rowMapperClass  The row mapper class.
     * @return if the mapper was replaced, false mapper for returnTypeClass was not found, no action taken.
     */
    public static Class<? extends RowMapper> replaceRowMapping(Class returnTypeClass, Class<? extends RowMapper> rowMapperClass) {
        return _rowMappings.put(returnTypeClass, rowMapperClass);
    }

    /**
     * remove the  row mapping for the specified class type.
     *
     * @param returnTypeClass
     * @return the RowMapper class which was removed, null if returnTypeClass did not match any of the registered
     *         row mappers.
     */
    public static Class<? extends RowMapper> removeRowMapping(Class returnTypeClass) {
        return _rowMappings.remove(returnTypeClass);
    }

    /**
     * Sets the rowmapper for Object.class
     *
     * @param rowMapperClass
     */
    public static Class <? extends RowMapper> setDefaultRowMapping(Class<? extends RowMapper> rowMapperClass) {
        Class<? extends RowMapper> ret = DEFAULT_OBJ_ROWMAPPING;
        DEFAULT_OBJ_ROWMAPPING = rowMapperClass;
        return ret;
    }

    /**
     * Sets the rowmapper for XmlObject.class
     *
     * @param rowMapperClass
     */
    public static Class<? extends RowMapper> setDefaultXmlRowMapping(Class mapToClass, Class<? extends RowMapper> rowMapperClass) {
        Class<? extends RowMapper> ret = DEFAULT_XMLOBJ_ROWMAPPING;
        DEFAULT_XMLOBJ_ROWMAPPING = rowMapperClass;
        XMLOBJ_CLASS = mapToClass;
        return ret;
    }

    /**
     * Create an instance of the RowMapper class.
     *
     * @param rowMapper
     * @param rs         ResultSet we are mapping from.
     * @param returnType Class to map rows to.
     * @param cal        Calendar instance for date/time values.
     * @return A RowMapper instance.
     */
    private static RowMapper getMapper(Class<? extends RowMapper> rowMapper, ResultSet rs, Class returnType, Calendar cal)
    {
        Constructor c = null;
        try {
            c = rowMapper.getDeclaredConstructor(_params);
            return (RowMapper) c.newInstance(new Object[]{rs, returnType, cal});
        } catch (NoSuchMethodException e) {
            throw new ControlException("Failure creating new instance of RowMapper, " + e.toString(), e);
        } catch (InstantiationException e) {
            throw new ControlException("Failure creating new instance of RowMapper, " + e.toString(), e);
        } catch (IllegalAccessException e) {
            throw new ControlException("Failure creating new instance of RowMapper, " + e.toString(), e);
        } catch (InvocationTargetException e) {
            throw new ControlException("Failure creating new instance of RowMapper, " + e.getCause().toString(), e);
        }
    }
}
