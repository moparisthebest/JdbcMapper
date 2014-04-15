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
package org.apache.beehive.controls.system.webservice.utils;

import javax.xml.namespace.QName;
import javax.xml.rpc.holders.Holder;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Utilities for moving values into and out of JAX-RPC holders.
 */
public class HolderUtils {

    private static Map<String, String> _holderMappings;

    static {
        _holderMappings = new HashMap<String, String>();
        _holderMappings.put(BigDecimal.class.getName(), "javax.xml.rpc.holders.BigDecimalHolder");
        _holderMappings.put(BigInteger.class.getName(), "javax.xml.rpc.holders.BigIntegerHolder");
        _holderMappings.put(boolean.class.getName(), "javax.xml.rpc.holders.BooleanHolder");
        _holderMappings.put(Boolean.class.getName(), "javax.xml.rpc.holders.BooleanWrapperHolder");
        _holderMappings.put(byte.class.getName(), "javax.xml.rpc.holders.ByteHolder");
        _holderMappings.put(Byte.class.getName(), "javax.xml.rpc.holders.ByteWrapperHolder");
        _holderMappings.put(Calendar.class.getName(), "javax.xml.rpc.holders.CalendarHolder");
        _holderMappings.put(double.class.getName(), "javax.xml.rpc.holders.DoubleHolder");
        _holderMappings.put(Double.class.getName(), "javax.xml.rpc.holders.DoubleWrapperHolder");
        _holderMappings.put(float.class.getName(), "javax.xml.rpc.holders.FloatHolder");
        _holderMappings.put(Float.class.getName(), "javax.xml.rpc.holders.FloatWrapperHolder");
        _holderMappings.put(Integer.class.getName(), "javax.xml.rpc.holders.IntegerWrapperHolder");
        _holderMappings.put(int.class.getName(), "javax.xml.rpc.holders.IntHolder");
        _holderMappings.put(long.class.getName(), "javax.xml.rpc.holders.LongHolder");
        _holderMappings.put(Long.class.getName(), "javax.xml.rpc.holders.LongWrapperHolder");
        _holderMappings.put(QName.class.getName(), "javax.xml.rpc.holders.QNameHolder");
        _holderMappings.put(short.class.getName(), "javax.xml.rpc.holders.ShortHolder");
        _holderMappings.put(Short.class.getName(), "javax.xml.rpc.holders.ShortWrapperHolder");
        _holderMappings.put(String.class.getName(), "javax.xml.rpc.holders.StringHolder");
    }

    /**
     * Attempt to map a class name to a JAX-RPC holder type.  If a match cannot be found map
     * to the GenericHolder class.
     *
     * @param className Name of class.
     * @return String Fully qualified name of JAX-RPC holder for class name.
     */
    public static String getHolderForClass(String className) {
        if (_holderMappings.containsKey(className)) {
            return _holderMappings.get(className);
        }
        else {
            return "org.apache.beehive.controls.system.webservice.utils.GenericHolder<" + className + ">";
        }
    }

    /**
     * Stuff a value into a holder class.
     *
     * @param holder The holder.
     * @param value  The value.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void stuffHolderValue(Holder holder, Object value)
            throws NoSuchFieldException, IllegalAccessException {

        Field valueField = holder.getClass().getField("value");
        Object curValueInHolder = valueField.get(holder);
        Class classOfValueFieldInHolder;

        if (curValueInHolder == null) {
            classOfValueFieldInHolder = valueField.getType();
        }
        else {
            classOfValueFieldInHolder = curValueInHolder.getClass();
        }

        if (value == null) {
            setFieldInObject(valueField, holder, null);
            return;
        }

        Object convertedValue = convertToHolderType(value, classOfValueFieldInHolder);
        setFieldInObject(valueField, holder, convertedValue);
    }

    /**
     * Type the class of the Holder's value field.
     *
     * @param t Holder type.
     * @return Class of the holder's value field.
     */
    public static Class getHoldersValueClass(Type t) {
        Class res = null;

        if (t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            Type raw = pt.getRawType();

            if (GenericHolder.class.isAssignableFrom((Class) raw)) {
                Type[] typeArgs = pt.getActualTypeArguments();
                if (typeArgs[0] instanceof GenericArrayType) {
                    Class arrayElementType = (Class) ((GenericArrayType) typeArgs[0]).getGenericComponentType();
                    res = Array.newInstance(arrayElementType, 0).getClass();
                }
                else {
                    res = (Class) typeArgs[0];
                }
            }
            else {
                throw new RuntimeException("Invalid parameterized type for holder: " + t);
            }
        }
        else if (t instanceof Class) {
            if (Holder.class.isAssignableFrom((Class) t)) {
                Field[] publicFields = ((Class) t).getFields();
                for (Field field : publicFields) {
                    if (field.getName().equals("value")) {
                        return field.getType();
                    }
                }
            }
            else {
                throw new RuntimeException("Invalid class.  Type: " + t + " is not a holder.");
            }
        }
        return res;
    }

    /**
     * Set the field value in the specified object.
     *
     * @param valueField Field to set.
     * @param destObject The destination object.
     * @param value      Field value.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static void setFieldInObject(Field valueField, Object destObject, Object value)
            throws IllegalArgumentException, IllegalAccessException {

        if (valueField.getType().isPrimitive()) {
            if (value == null) {
                // Don't need to set anything
            }
            else {
                valueField.set(destObject, value);
            }
        }
        else {
            valueField.set(destObject, value);
        }
    }

    /**
     * Convert value to the class of the holder field value.
     *
     * @param value                     Value to convert.
     * @param classOfValueFieldInHolder Target to convert value to.
     * @return The converted value, null if value cannot be converted.
     */
    private static Object convertToHolderType(Object value, Class classOfValueFieldInHolder) {

        if (classOfValueFieldInHolder == null || value == null) {
            return value;
        }

        // simple transformation
        if (classOfValueFieldInHolder.isAssignableFrom(value.getClass())) {
            return value;
        }

        // Calendar -> Date
        if (value instanceof Calendar && classOfValueFieldInHolder == Date.class) {
            return ((Calendar) value).getTime();
        }
        if (value instanceof Date && classOfValueFieldInHolder == Calendar.class) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) value);
            return calendar;
        }

        // Calendar -> java.sql.Date
        if (value instanceof Calendar && classOfValueFieldInHolder == java.sql.Date.class) {
            return new java.sql.Date(((Calendar) value).getTime().getTime());
        }

        // HashMap -> Hashtable
        if (value instanceof HashMap && classOfValueFieldInHolder == Hashtable.class) {
            return new Hashtable((HashMap) value);
        }

        // value -> array transformation
        if (classOfValueFieldInHolder.isArray() &&
                !classOfValueFieldInHolder.getComponentType().equals(Object.class) &&
                classOfValueFieldInHolder.getComponentType().isAssignableFrom(value.getClass())) {
            Object array = Array.newInstance(classOfValueFieldInHolder.getComponentType(), 1);
            Array.set(array, 0, value);
            return array;
        }

        // list -> array transformation
        if (classOfValueFieldInHolder.isArray() && value instanceof List) {
            return convertListToArray((List) value, classOfValueFieldInHolder);
        }

        // can't convert - return null
        return null;
    }

    /**
     * Convert a java.util.List implementation to an array.
     *
     * @param list      List to convert.
     * @param arrayType Array type to convert to.
     * @return Array of arrayTYpe, null if can't convert.
     */
    private static Object convertListToArray(List list, Class arrayType) {
        if (list.isEmpty()) {
            return Array.newInstance(arrayType, 0);
        }

        Class listComponentClazz = list.get(0).getClass();
        if (listComponentClazz.isAssignableFrom(arrayType.getComponentType())) {
            Object[] newArray = (Object[]) Array.newInstance(arrayType.getComponentType(), 1);
            return list.toArray(newArray);
        }

        return null;
    }
}
