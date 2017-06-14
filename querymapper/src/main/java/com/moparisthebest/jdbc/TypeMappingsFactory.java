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
package com.moparisthebest.jdbc;

import java.sql.Blob;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;

/**
 * Currently contains all types of type mappings. Implemented using singleton pattern.
 */
public final class TypeMappingsFactory {

    private final static TypeMappingsFactory _instance = new TypeMappingsFactory();
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
        return _instance;
    }

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_BYTE = 1;
    public static final int TYPE_SHORT = 2;
    public static final int TYPE_INT = 3;
    public static final int TYPE_LONG = 4;
    public static final int TYPE_FLOAT = 5;
    public static final int TYPE_DOUBLE = 6;
    public static final int TYPE_BOOLEAN = 7;
    public static final int TYPE_BYTE_OBJ = 8;
    public static final int TYPE_SHORT_OBJ = 9;
    public static final int TYPE_INT_OBJ = 10;
    public static final int TYPE_LONG_OBJ = 11;
    public static final int TYPE_FLOAT_OBJ = 12;
    public static final int TYPE_DOUBLE_OBJ = 13;
    public static final int TYPE_BOOLEAN_OBJ = 14;
    public static final int TYPE_BIG_DECIMAL = 15;
    public static final int TYPE_STRING = 16;
    public static final int TYPE_BYTES = 17;
    public static final int TYPE_SQLDATE = 18;
    public static final int TYPE_TIME = 19;
    public static final int TYPE_TIMESTAMP = 20;
    public static final int TYPE_STREAM = 21;
    public static final int TYPE_READER = 22;
    public static final int TYPE_CLOB = 23;
    public static final int TYPE_BLOB = 24;
    public static final int TYPE_ARRAY = 25;
    public static final int TYPE_REF = 26;
    public static final int TYPE_DATE = 27;
    public static final int TYPE_CALENDAR = 28;
    public static final int TYPE_STRUCT = 29;
    public static final int TYPE_XMLBEAN_ENUM = 30;
	public static final int TYPE_ENUM = 31;
    private static final int TYPE_MAX = TYPE_ENUM + 1; // should always reference the max

    private final Map<Class, Object> _primitiveDefaults;

    //
    // keys in this map are the class of the method's return type,
    // values are the set of constants defined above all prefixed with
    // TYPE_
    //
    private final Map<Class, Integer> _typeMap;

    /**
     * Constructor
     */
    protected TypeMappingsFactory() {

        _primitiveDefaults = new HashMap<Class, Object>();
        _primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        _primitiveDefaults.put(Integer.TYPE, 0);
        _primitiveDefaults.put(Long.TYPE, 0);
        _primitiveDefaults.put(Byte.TYPE, (byte) 0);
        _primitiveDefaults.put(Short.TYPE, (short) 0);
        _primitiveDefaults.put(Character.TYPE, '\u0000');
        _primitiveDefaults.put(Float.TYPE, 0.0f);
        _primitiveDefaults.put(Double.TYPE, 0.0d);

        // Class to internal enum
        _typeMap = new HashMap<Class, Integer>(TYPE_MAX * 2);
        _typeMap.put(Boolean.TYPE, TYPE_BOOLEAN);
        _typeMap.put(Integer.TYPE, TYPE_INT);
        _typeMap.put(Long.TYPE, TYPE_LONG);
        _typeMap.put(Byte.TYPE, TYPE_BYTE);
        _typeMap.put(Short.TYPE, TYPE_SHORT);
        _typeMap.put(Float.TYPE, TYPE_FLOAT);
        _typeMap.put(Double.TYPE, TYPE_DOUBLE);
        _typeMap.put(Boolean.class, TYPE_BOOLEAN_OBJ);
        _typeMap.put(Integer.class, TYPE_INT_OBJ);
        _typeMap.put(Long.class, TYPE_LONG_OBJ);
        _typeMap.put(Byte.class, TYPE_BYTE_OBJ);
        _typeMap.put(Short.class, TYPE_SHORT_OBJ);
        _typeMap.put(Float.class, TYPE_FLOAT_OBJ);
        _typeMap.put(Double.class, TYPE_DOUBLE_OBJ);
        _typeMap.put(String.class, TYPE_STRING);
        _typeMap.put(java.math.BigDecimal.class, TYPE_BIG_DECIMAL);
        _typeMap.put(byte[].class, TYPE_BYTES);
        _typeMap.put(java.sql.Timestamp.class, TYPE_TIMESTAMP);
        _typeMap.put(java.sql.Time.class, TYPE_TIME);
        _typeMap.put(java.sql.Date.class, TYPE_SQLDATE);
        _typeMap.put(java.sql.Ref.class, TYPE_REF);
        _typeMap.put(Blob.class, TYPE_BLOB);
        _typeMap.put(Clob.class, TYPE_CLOB);
        _typeMap.put(java.sql.Array.class, TYPE_ARRAY);
        _typeMap.put(java.sql.Struct.class, TYPE_STRUCT);
        _typeMap.put(java.io.Reader.class, TYPE_READER);
        _typeMap.put(java.io.InputStream.class, TYPE_STREAM);
        _typeMap.put(java.util.Date.class, TYPE_DATE);
        _typeMap.put(java.util.Calendar.class, TYPE_CALENDAR);
        _typeMap.put(java.util.GregorianCalendar.class, TYPE_CALENDAR);
        if (XMLBEANS_STRING_ENUM_ABSTRACT_BASE != null) {
            _typeMap.put(XMLBEANS_STRING_ENUM_ABSTRACT_BASE, TYPE_XMLBEAN_ENUM);
        }
        _typeMap.put(Enum.class, TYPE_ENUM);
    }

    /**
     * Get the type id (defined by this class) for the given class.
     * @param classType Class to get type of.
     * @return Type id of class.
     */
    public int getTypeId(Class classType) {

        if(classType == null)
            return TYPE_UNKNOWN;

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
}
