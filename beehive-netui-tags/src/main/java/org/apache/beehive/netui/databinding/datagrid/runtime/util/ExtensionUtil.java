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
package org.apache.beehive.netui.databinding.datagrid.runtime.util;

import org.apache.beehive.netui.databinding.datagrid.api.exceptions.DataGridExtensionException;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.Bundle;

/**
 * Internal method used by the data grid to instantiate classes.  This class should not be used elsewhere.
 */
public final class ExtensionUtil {

    private static final Logger LOGGER = Logger.getInstance(ExtensionUtil.class);

    private ExtensionUtil() {
    }

    /**
     * Utility method that helps instantiate a class used to extend the data grid.
     *
     * @param className the name of a class to instantiate
     * @param assignableFrom the type that should be assignable from an instance of type <code>className</code>
     * @return an instance of the given class
     * @throws org.apache.beehive.netui.databinding.datagrid.api.exceptions.DataGridExtensionException
     *          when an error occurs creating an instance of the class
     */
    public static Object instantiateClass(String className, Class assignableFrom) {
        if(className == null)
            throw new IllegalArgumentException(Bundle.getErrorString("DataGridUtil_CantCreateClass"));

        Class clazz = null;
        try {
            clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
        }
        catch(Exception e) {
            assert e instanceof IllegalAccessException ||
                    e instanceof InstantiationException ||
                    e instanceof ClassNotFoundException : "Caught exception of unexpected type: " + e.getClass().getName();

            String msg = Bundle.getErrorString("DataGridUtil_CantInstantiateClass", new Object[]{e});
            LOGGER.error(msg, e);
            throw new DataGridExtensionException(msg, e);
        }

        return instantiateClass(clazz, assignableFrom);
    }

    /**
     * Utility method that helps instantiate a class used to extend the data grid.
     *
     * @param clazz the name of a class to instantiate
     * @param assignableFrom the type that should be assignable from an instance of type <code>className</code>
     * @return an instance of the given class
     * @throws org.apache.beehive.netui.databinding.datagrid.api.exceptions.DataGridExtensionException
     *          when an error occurs creating an instance of the class
     */
    public static Object instantiateClass(Class clazz, Class assignableFrom) {
        if(clazz == null)
            throw new IllegalArgumentException(Bundle.getErrorString("DataGridUtil_CantCreateClass"));

        try {
            Object obj = clazz.newInstance();

            if(assignableFrom == null || assignableFrom.isAssignableFrom(clazz))
                return obj;
            else
                throw new DataGridExtensionException(Bundle.getErrorString("DataGridUtil_InvalidParentClass", new Object[]{clazz.getName(), assignableFrom}));
        }
        catch(Exception e) {
            assert
                    e instanceof DataGridExtensionException ||
                    e instanceof IllegalAccessException ||
                    e instanceof InstantiationException ||
                    e instanceof ClassNotFoundException : "Caught exception of unexpected type " + e.getClass().getName();

            String msg = Bundle.getErrorString("DataGridUtil_CantInstantiateClass", new Object[]{e});
            LOGGER.error(msg, e);
            throw new DataGridExtensionException(msg, e);
        }
    }
}
