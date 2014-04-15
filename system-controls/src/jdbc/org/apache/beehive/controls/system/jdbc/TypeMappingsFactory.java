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

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.sql.Types;
import java.sql.Blob;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;

import org.apache.beehive.controls.api.ControlException;

/**
 * Currently contains all types of type mappings. Implemented using singleton pattern.
 */
public final class TypeMappingsFactory {

    /* @todo: refactor! */

    private static TypeMappingsFactory _instance;
    private static Class<?> XMLBEANS_STRING_ENUM_ABSTRACT_BASE = null;

    static {
        try {
            XMLBEANS_STRING_ENUM_ABSTRACT_BASE = Class.forName("org.apache.xmlbeans.StringEnumAbstractBase");
        } catch (ClassNotFoundException e) {
            // not an error, just means XmlBeans is not available
        }
    }

    /**
     * Get an instance of this class.
     * @return TypeMappingsFactory instance.
     */
    public static TypeMappingsFactory getInstance() {
        if (_instance == null) {
            _instance = new TypeMappingsFactory();
        }
        return _instance;
    }

    public static final int TYPE_UNKNOWN = 0;
    static final int TYPE_BYTE = 1;
    static final int TYPE_SHORT = 2;
    static final int TYPE_INT = 3;
    static final int TYPE_LONG = 4;
    static final int TYPE_FLOAT = 5;
    static final int TYPE_DOUBLE = 6;
    static final int TYPE_BOOLEAN = 7;
    static final int TYPE_BYTE_OBJ = 8;
    static final int TYPE_SHORT_OBJ = 9;
    static final int TYPE_INT_OBJ = 10;
    static final int TYPE_LONG_OBJ = 11;
    static final int TYPE_FLOAT_OBJ = 12;
    static final int TYPE_DOUBLE_OBJ = 13;
    static final int TYPE_BOOLEAN_OBJ = 14;
    static final int TYPE_BIG_DECIMAL = 15;
    static final int TYPE_STRING = 16;
    static final int TYPE_BYTES = 17;
    static final int TYPE_SQLDATE = 18;
    static final int TYPE_TIME = 19;
    static final int TYPE_TIMESTAMP = 20;
    static final int TYPE_STREAM = 21;
    static final int TYPE_READER = 22;
    static final int TYPE_CLOB = 23;
    static final int TYPE_BLOB = 24;
    static final int TYPE_ARRAY = 25;
    static final int TYPE_REF = 26;
    static final int TYPE_DATE = 27;
    static final int TYPE_CALENDAR = 28;
    static final int TYPE_STRUCT = 29;
    static final int TYPE_XMLBEAN_ENUM = 30;
    static final int TYPE_MAX = 31;

    private Map<Class, Object> _primitiveDefaults;

    //
    // keys in this map are the class of the method's return type,
    // values are the set of constants defined above all prefixed with
    // TYPE_
    //
    private Map<Class, Integer> _typeMap;
    private Map<Class, Integer> _typeSqlMap;

    /**
     * Map a string version of sql type to sql type (java.sql.Types).
     * example: "INTEGER" maps to java.sql.Types.INTEGER
     */
    private Map<String, Integer> _typeSqlNameMap;

    private static Method _methodMapGet;

    /**
     * Constructor
     */
    TypeMappingsFactory() {

        _primitiveDefaults = new HashMap<Class, Object>();
        _primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        _primitiveDefaults.put(Integer.TYPE, new Integer(0));
        _primitiveDefaults.put(Long.TYPE, new Long(0));
        _primitiveDefaults.put(Byte.TYPE, new Byte((byte) 0));
        _primitiveDefaults.put(Short.TYPE, new Short((short) 0));
        _primitiveDefaults.put(Character.TYPE, new Character('\u0000'));
        _primitiveDefaults.put(Float.TYPE, new Float(0.0f));
        _primitiveDefaults.put(Double.TYPE, new Double(0.0d));

        // Class to internal enum
        _typeMap = new HashMap<Class, Integer>(TYPE_MAX * 2);
        _typeMap.put(Boolean.TYPE, new Integer(TYPE_BOOLEAN));
        _typeMap.put(Integer.TYPE, new Integer(TYPE_INT));
        _typeMap.put(Long.TYPE, new Integer(TYPE_LONG));
        _typeMap.put(Byte.TYPE, new Integer(TYPE_BYTE));
        _typeMap.put(Short.TYPE, new Integer(TYPE_SHORT));
        _typeMap.put(Float.TYPE, new Integer(TYPE_FLOAT));
        _typeMap.put(Double.TYPE, new Integer(TYPE_DOUBLE));
        _typeMap.put(Boolean.class, new Integer(TYPE_BOOLEAN_OBJ));
        _typeMap.put(Integer.class, new Integer(TYPE_INT_OBJ));
        _typeMap.put(Long.class, new Integer(TYPE_LONG_OBJ));
        _typeMap.put(Byte.class, new Integer(TYPE_BYTE_OBJ));
        _typeMap.put(Short.class, new Integer(TYPE_SHORT_OBJ));
        _typeMap.put(Float.class, new Integer(TYPE_FLOAT_OBJ));
        _typeMap.put(Double.class, new Integer(TYPE_DOUBLE_OBJ));
        _typeMap.put(String.class, new Integer(TYPE_STRING));
        _typeMap.put(java.math.BigDecimal.class, new Integer(TYPE_BIG_DECIMAL));
        _typeMap.put(byte[].class, new Integer(TYPE_BYTES));
        _typeMap.put(java.sql.Timestamp.class, new Integer(TYPE_TIMESTAMP));
        _typeMap.put(java.sql.Time.class, new Integer(TYPE_TIME));
        _typeMap.put(java.sql.Date.class, new Integer(TYPE_SQLDATE));
        _typeMap.put(java.sql.Ref.class, new Integer(TYPE_REF));
        _typeMap.put(java.sql.Blob.class, new Integer(TYPE_BLOB));
        _typeMap.put(java.sql.Clob.class, new Integer(TYPE_CLOB));
        _typeMap.put(java.sql.Array.class, new Integer(TYPE_ARRAY));
        _typeMap.put(java.sql.Struct.class, new Integer(TYPE_STRUCT));
        _typeMap.put(java.io.Reader.class, new Integer(TYPE_READER));
        _typeMap.put(java.io.InputStream.class, new Integer(TYPE_STREAM));
        _typeMap.put(java.util.Date.class, new Integer(TYPE_DATE));
        _typeMap.put(java.util.Calendar.class, new Integer(TYPE_CALENDAR));
        _typeMap.put(java.util.GregorianCalendar.class, new Integer(TYPE_CALENDAR));
        if (XMLBEANS_STRING_ENUM_ABSTRACT_BASE != null) {
            _typeMap.put(XMLBEANS_STRING_ENUM_ABSTRACT_BASE, new Integer(TYPE_XMLBEAN_ENUM));
        }

        // Class to java.sql.Types
        _typeSqlMap = new HashMap<Class, Integer>(TYPE_MAX * 2);
        _typeSqlMap.put(Boolean.TYPE, new Integer(Types.BOOLEAN));
        _typeSqlMap.put(Integer.TYPE, new Integer(Types.INTEGER));
        _typeSqlMap.put(Long.TYPE, new Integer(Types.BIGINT));
        _typeSqlMap.put(Byte.TYPE, new Integer(Types.TINYINT));
        _typeSqlMap.put(Short.TYPE, new Integer(Types.SMALLINT));
        _typeSqlMap.put(Float.TYPE, new Integer(Types.REAL));
        _typeSqlMap.put(Double.TYPE, new Integer(Types.DOUBLE));
        _typeSqlMap.put(Boolean.class, new Integer(Types.BOOLEAN));
        _typeSqlMap.put(Integer.class, new Integer(Types.INTEGER));
        _typeSqlMap.put(Long.class, new Integer(Types.BIGINT));
        _typeSqlMap.put(Byte.class, new Integer(Types.TINYINT));
        _typeSqlMap.put(Short.class, new Integer(Types.SMALLINT));
        _typeSqlMap.put(Float.class, new Integer(Types.REAL));
        _typeSqlMap.put(Double.class, new Integer(Types.DOUBLE));
        _typeSqlMap.put(String.class, new Integer(Types.VARCHAR));
        _typeSqlMap.put(java.math.BigDecimal.class, new Integer(Types.DECIMAL));
        _typeSqlMap.put(byte[].class, new Integer(Types.VARBINARY));
        _typeSqlMap.put(java.sql.Timestamp.class, new Integer(Types.TIMESTAMP));
        _typeSqlMap.put(java.sql.Time.class, new Integer(Types.TIME));
        _typeSqlMap.put(java.sql.Date.class, new Integer(Types.DATE));
        _typeSqlMap.put(java.sql.Ref.class, new Integer(Types.REF));
        _typeSqlMap.put(java.sql.Blob.class, new Integer(Types.BLOB));
        _typeSqlMap.put(java.sql.Clob.class, new Integer(Types.CLOB));
        _typeSqlMap.put(java.sql.Array.class, new Integer(Types.ARRAY));
        _typeSqlMap.put(java.sql.Struct.class, new Integer(Types.STRUCT));
        _typeSqlMap.put(java.util.Date.class, new Integer(Types.TIMESTAMP));
        _typeSqlMap.put(java.util.Calendar.class, new Integer(Types.TIMESTAMP));
        _typeSqlMap.put(java.util.GregorianCalendar.class, new Integer(Types.TIMESTAMP));
        if (XMLBEANS_STRING_ENUM_ABSTRACT_BASE != null) {
            _typeSqlMap.put(XMLBEANS_STRING_ENUM_ABSTRACT_BASE, new Integer(Types.VARCHAR));
        }

        // String to java.sql.Types
        _typeSqlNameMap = new HashMap<String, Integer>(TYPE_MAX * 2);
        _typeSqlNameMap.put("BIT", new Integer(Types.BIT));
        _typeSqlNameMap.put("TINYINT", new Integer(Types.TINYINT));
        _typeSqlNameMap.put("SMALLINT", new Integer(Types.SMALLINT));
        _typeSqlNameMap.put("INTEGER", new Integer(Types.INTEGER));
        _typeSqlNameMap.put("BIGINT", new Integer(Types.BIGINT));
        _typeSqlNameMap.put("FLOAT", new Integer(Types.REAL));
        _typeSqlNameMap.put("REAL", new Integer(Types.REAL));
        _typeSqlNameMap.put("DOUBLE", new Integer(Types.DOUBLE));
        _typeSqlNameMap.put("NUMERIC", new Integer(Types.NUMERIC));
        _typeSqlNameMap.put("DECIMAL", new Integer(Types.DECIMAL));
        _typeSqlNameMap.put("CHAR", new Integer(Types.CHAR));
        _typeSqlNameMap.put("VARCHAR", new Integer(Types.VARCHAR));
        _typeSqlNameMap.put("LONGVARCHAR", new Integer(Types.LONGVARCHAR));
        _typeSqlNameMap.put("DATE", new Integer(Types.DATE));
        _typeSqlNameMap.put("TIME", new Integer(Types.TIME));
        _typeSqlNameMap.put("TIMESTAMP", new Integer(Types.TIMESTAMP));
        _typeSqlNameMap.put("BINARY", new Integer(Types.BINARY));
        _typeSqlNameMap.put("VARBINARY", new Integer(Types.VARBINARY));
        _typeSqlNameMap.put("LONGVARBINARY", new Integer(Types.LONGVARBINARY));
        _typeSqlNameMap.put("NULL", new Integer(Types.NULL));
        _typeSqlNameMap.put("OTHER", new Integer(Types.OTHER));
        _typeSqlNameMap.put("JAVA_OBJECT", new Integer(Types.JAVA_OBJECT));
        _typeSqlNameMap.put("DISTINCT", new Integer(Types.DISTINCT));
        _typeSqlNameMap.put("STRUCT", new Integer(Types.STRUCT));
        _typeSqlNameMap.put("ARRAY", new Integer(Types.ARRAY));
        _typeSqlNameMap.put("BLOB", new Integer(Types.BLOB));
        _typeSqlNameMap.put("CLOB", new Integer(Types.CLOB));
        _typeSqlNameMap.put("REF", new Integer(Types.REF));
        _typeSqlNameMap.put("DATALINK", new Integer(Types.DATALINK));
        _typeSqlNameMap.put("BOOLEAN", new Integer(Types.BOOLEAN));

        // some JAVA synonyms
        _typeSqlNameMap.put("BYTE", new Integer(Types.TINYINT));
        _typeSqlNameMap.put("SHORT", new Integer(Types.SMALLINT));
        _typeSqlNameMap.put("INT", new Integer(Types.INTEGER));
        _typeSqlNameMap.put("LONG", new Integer(Types.BIGINT));

        // cache the Map.get method for efficiency
        try {
            _methodMapGet = java.util.Map.class.getMethod("get", new Class[]{Object.class});
        } catch (NoSuchMethodException e) {
            throw new ControlException("Can not find java.util.Map.get(Object) method");
        }
    }

    /**
     * Convert a type string to its SQL Type int value.
     * @param type A String containing the SQL type name.
     * @return The SQL type, TYPE_UNKNOWN if cannot convert.
     */
    public int convertStringToSQLType(String type) {
        if (_typeSqlNameMap.containsKey(type.toUpperCase())) {
            return _typeSqlNameMap.get(type.toUpperCase());
        }
        return TYPE_UNKNOWN;
    }

    /**
     * Get the SQL type of a class, start at top level class an check all super classes until match is found.
     * @param classType Class to get SQL type of.
     * @return Types.OTHER if cannot find SQL type.
     */
    public int getSqlType(Class classType) {

        final Class origType = classType;
        while (classType != null) {
            Integer type = _typeSqlMap.get(classType);
            if (type != null) {
                return type.intValue();
            }
            classType = classType.getSuperclass();
        }

        //
        // special check for blobs/clobs they are interfaces not derived from
        //
        if (Blob.class.isAssignableFrom(origType)) {
            return _typeSqlMap.get(Blob.class).intValue();
        } else if (Clob.class.isAssignableFrom(origType)) {
            return _typeSqlMap.get(Clob.class).intValue();
        }

        return Types.OTHER;
    }

    /**
     * Get the SQL type for an object.
     * @param o Object to get SQL type of.
     * @return SQL type of the object, Types.OTHER if cannot classify.
     */
    public int getSqlType(Object o) {
        if (null == o) {
            return Types.NULL;
        }
        return getSqlType(o.getClass());
    }

    /**
     *
     * @param val
     * @param args
     * @return the type
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Object lookupType(Object val, Object[] args)
            throws IllegalAccessException, InvocationTargetException
    {
        return _methodMapGet.invoke(val, args);
    }

    /**
     * Get the type id (defined by this class) for the given class.
     * @param classType Class to get type of.
     * @return Type id of class.
     */
    public int getTypeId(Class classType) {

        final Class origType = classType;
        while (null != classType) {
            Integer typeObj = (Integer) _typeMap.get(classType);
            if (null != typeObj) {
                return typeObj.intValue();
            }
            classType = classType.getSuperclass();
        }

        //
        // special check for blobs/clobs they are interfaces not derived from
        //
        if (Blob.class.isAssignableFrom(origType)) {
            return _typeMap.get(Blob.class).intValue();
        } else if (Clob.class.isAssignableFrom(origType)) {
            return _typeMap.get(Clob.class).intValue();
        }

        return TYPE_UNKNOWN;
    }

   /**
    * Returns a primitive legal value as opposed to null if type is primitive.
    * @param type type to get null value for.
    * @return null value for specifed type.
    */
   public Object fixNull(Class type) {
       return type.isPrimitive() ? _primitiveDefaults.get(type) : null;
   }

   /**
     * Create an Object array for the given array.
     *
     * @param o An array.
     * @return A new object array.
     */
    public static Object[] toObjectArray(Object o) {

        Class clas = o.getClass().getComponentType();

        if (null == clas) return null;

        Object[] arr;

        if (clas == Boolean.TYPE) {
            boolean[] src = (boolean[]) o;
            arr = new Object[src.length];
            for (int i = 0; i < src.length; i++)
                arr[i] = new Boolean(src[i]);
        } else if (clas == Character.TYPE) {
            char[] src = (char[]) o;
            arr = new Object[src.length];
            for (int i = 0; i < src.length; i++)
                arr[i] = new Character(src[i]);
        } else if (clas == Byte.TYPE) {
            byte[] src = (byte[]) o;
            arr = new Object[src.length];
            for (int i = 0; i < src.length; i++)
                arr[i] = new Byte(src[i]);
        } else if (clas == Short.TYPE) {
            short[] src = (short[]) o;
            arr = new Object[src.length];
            for (int i = 0; i < src.length; i++)
                arr[i] = new Short(src[i]);
        } else if (clas == Integer.TYPE) {
            int[] src = (int[]) o;
            arr = new Object[src.length];
            for (int i = 0; i < src.length; i++)
                arr[i] = new Integer(src[i]);
        } else if (clas == Long.TYPE) {
            long[] src = (long[]) o;
            arr = new Object[src.length];
            for (int i = 0; i < src.length; i++)
                arr[i] = new Long(src[i]);
        } else if (clas == Float.TYPE) {
            float[] src = (float[]) o;
            arr = new Object[src.length];
            for (int i = 0; i < src.length; i++)
                arr[i] = new Float(src[i]);
        } else if (clas == Double.TYPE) {
            double[] src = (double[]) o;
            arr = new Object[src.length];
            for (int i = 0; i < src.length; i++)
                arr[i] = new Double(src[i]);
        } else {
            arr = (Object[]) o;
        }
        return arr;
    }
}
