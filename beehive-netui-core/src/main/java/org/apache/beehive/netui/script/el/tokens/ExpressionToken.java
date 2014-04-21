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
package org.apache.beehive.netui.script.el.tokens;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.beehive.netui.util.internal.cache.PropertyCache;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.script.el.util.ParseUtils;

/**
 *
 */
public abstract class ExpressionToken {

    private static final Logger LOGGER = Logger.getInstance(ArrayIndexToken.class);
    private static final PropertyCache PROPERTY_CACHE = new PropertyCache();

    public abstract Object read(Object object);

    public abstract void write(Object object, Object value);

    public abstract String getTokenString();

    /**
     * Lookup the <code>key</code> in the <code>map</code>.
     * @param map the map
     * @param key the key
     * @return the value found at <code>map.get(key)</code> or <code>null</code> if no value was found
     */
    protected final Object mapLookup(Map map, Object key) {
        LOGGER.trace("get value from Map");
        return map.get(key);
    }

    /**
     * Get a JavaBean property from the given <code>value</code>
     * @param value the JavaBean
     * @param propertyName the property name
     * @return the value of the property from the object <code>value</code>
     */
    protected final Object beanLookup(Object value, Object propertyName) {
        LOGGER.trace("get JavaBean property : " + propertyName);
        return ParseUtils.getProperty(value, propertyName.toString(), PROPERTY_CACHE);
    }

    /**
     * Get the value in a {@link List} at <code>index</code>.
     * @param list the List
     * @param index the index
     * @return the value returned from <code>list.get(index)</code>
     */
    protected final Object listLookup(List list, int index) {
        LOGGER.trace("get value in List index " + index);
        return list.get(index);
    }

    /**
     * Get the value from <code>array</code> at <code>index</code>
     * @param array the array
     * @param index the index
     * @return the value returned from <code>Array.get(array, index)</code>
     */
    protected final Object arrayLookup(Object array, int index) {
        LOGGER.trace("get value from array index " + index);
        return Array.get(array, index);
    }

    /**
     * Update the value of <code>key</code> in <code>map</code>
     * @param map the map
     * @param key the key
     * @param value the value
     */
    protected final void mapUpdate(Map map, Object key, Object value) {
        Object o = map.get(key);
        /*
          If a value exists in map.get(key), convert the "value" parameter into
          the type of map.get(key).  It's a best guess as to what the type of the
          Map _should_ be without any further reflective information about the
          types contained in the map.
         */
        if(o != null) {
            Class type = o.getClass();
            value = ParseUtils.convertType(value, type);
        }

        map.put(key, value);
    }

    protected final void arrayUpdate(Object array, int index, Object value) {
        Object converted = value;

        Class elementType = array.getClass().getComponentType();
        if(!elementType.isAssignableFrom(value.getClass())) {
            converted = ParseUtils.convertType(value, elementType);
        }

        try {
            Array.set(array, index, converted);
        } catch(Exception e) {
            String msg = "An error occurred setting a value at index \"" + index + "\" on an array with component types \"" +
                elementType + "\".  Cause: " + e.toString();
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Update a {@link List} with the Object <code>value</code> at <code>index</code>.
     * @param list the List
     * @param index the index
     * @param value the new value
     */
    protected final void listUpdate(List list, int index, Object value) {
        Object converted = value;

        if(list.size() > index) {
            Object o = list.get(index);
            // can only convert types when there is an item in the currently requested place
            if(o != null) {
                Class itemType = o.getClass();
                converted = ParseUtils.convertType(value, itemType);
            }

            list.set(index, value);
        } else {
            // @note: not sure that this is the right thing.  Question is whether or not to insert nulls here to fill list up to "index"
            // @update: List doesn't guarantee that implementations will accept nulls.  So, we can't rely on that as a solution.
            // @update: this is an unfortunate but necessary solution...unless the List has enough elements to 
            // accomodate the new item at a particular index, this must be an error case.  The reasons are this:
            // 1) can't fill the list with nulls, List implementations are allowed to disallow them
            // 2) can't just do an "add" to the list -- in processing [0] and [1] on an empty list, [1] may get processed first. 
            //    this will go into list slot [0].  then, [0] gets processed and simply overwrites the previous because it's 
            //    already in the list
            // 3) can't go to a mixed approach because there's no metadata about what has been done and no time to build
            //    something that is apt to be complicated and exposed to the user
            // so...
            // the ultimate 8.1sp2 functionality is to simply disallow updating a value in a list that doesn't exist.  that 
            // being said, it is still possible to simply add to the list.  if {actionForm.list[42]} inserts into the 42'nd 
            // item, {actionForm.list} will just do an append on POST since there is no index specified.  this fix does 
            // not break backwards compatability because it will work on full lists and is completely broken now on empty 
            // lists, so changing this just gives a better exception message that "ArrayIndexOutOfBounds".  :)
            // 
            // September 2, 2003
            // ekoneil@apache.com
            // 
            String msg = "An error occurred setting a value at index \"" +
                index +
                "\" because the list is " +
                (list != null ? (" of size " + list.size()) : "null") + ".  " +
                "Be sure to allocate enough items in the List to accomodate any updates which may occur against the list.";

            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Update a JavaBean property named <code>identifier</code> with the given <code>value</code>.
     * @param bean the JavaBean
     * @param identifier the property name
     * @param value the new value
     */
    protected final void beanUpdate(Object bean, Object identifier, Object value) {
        LOGGER.trace("Update bean \"" + bean + "\" property \"" + identifier + "\"");

        String propertyName = identifier.toString();

        Class beanType = bean.getClass();
        Class propType = PROPERTY_CACHE.getPropertyType(beanType, propertyName);
        // Get the type of the JavaBean property given reflected information from the JavaBean's type
        if(propType != null) {
            try {
                // The type of the JavaBean property is a List.  To update it, get the List and
                // append the value to the end of the List.
                if(List.class.isAssignableFrom(propType)) {
                    Method listGetter = PROPERTY_CACHE.getPropertyGetter(beanType, propertyName);
                    if(listGetter != null) {
                        List list = (List)listGetter.invoke(bean, (Object[])null);
                        applyValuesToList(value, list);
                        return;
                    }
                }
                // The JavaBean is an Object, so set the Bean's property with the given value
                else {
                    Method setter = PROPERTY_CACHE.getPropertySetter(beanType, propertyName);

                    if(setter != null) {
                        LOGGER.trace("Set property via setter method: [" + setter + "]");

                        Class targetType = setter.getParameterTypes()[0];
                        Object converted = ParseUtils.convertType(value, targetType);

                        setter.invoke(bean, new Object[]{converted});
                        return;
                    }
                }
            }
            catch(Exception e) {
                String msg = "Could not update proprety named \"" + propertyName + "\" on bean of type \"" + beanType + "\".  Cause: " + e;
                LOGGER.error(msg, e);
                throw new RuntimeException(msg, e);
            }
        }

        String msg = "Could not update expression because a public JavaBean setter for the property \"" + identifier + "\" could not be found.";
        LOGGER.error(msg);
        throw new RuntimeException(msg);
    }

    /**
     * Attempt to convert a String indexString into an integer index.
     * @param indexString the index string
     * @return the converted integer
     */
    protected final int parseIndex(String indexString) {
        try {
            return Integer.parseInt(indexString);
        } catch(Exception e) {
            String msg = "Error converting \"" + indexString + "\" into an integer.  Cause: " + e;
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * Set a list of values on a {@link List}.  The behavior of this method is different given
     * the type of <code>value</code>:<br/>
     * - value is java.lang.String[]: add each item in the String[] to the list
     * - value is a String: add the value to the list
     * - otherwise, add the value to the end of the list
     * @param value the value to apply to a list
     * @param list the {@link List}
     */
    private static void applyValuesToList(Object value, List list) {
        if(list == null) {
            String msg = "Can not add a value to a null java.util.List";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }

        if(value instanceof String[]) {
            String[] ary = (String[])value;
            for(int i = 0; i < ary.length; i++)
                list.add(ary[i]);
        }
        else if(value instanceof String)
            list.add(value);
        // types that are not String[] or String are just set on the object
        else list.add(value);
    }
}
