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
package org.apache.beehive.netui.databinding.datagrid.runtime.config;

import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Enumeration;
import java.text.MessageFormat;

import org.apache.beehive.netui.databinding.datagrid.api.DataGridResourceProvider;

/**
 * <p>
 * Default implementation of the data grid's {@link DataGridResourceProvider} class.  This class returns
 * string resources from the default resource bundle:
 * <pre>
 *     org.apache.beehive.netui.databinding.datagrid.runtime.util.data-grid-default.properties
 * </pre>
 * If resource bundle chaining is enabled, this class will first search the user-provided resource
 * bundle.  If a message matching the key is found, it will be returned.  If no message matching a key is found,
 * the default resource bundle will be searched.  To enable chaining, set the property:
 * {@link DataGridResourceProvider#setEnableChaining(boolean)}.
 * </p>
 */
class DefaultDataGridResourceProvider
    extends DataGridResourceProvider {

    private static final Object VALUE_PLACEHOLDER = new Object();
    private static final String DEFAULT_RESOURCE_BUNDLE = "org.apache.beehive.netui.databinding.datagrid.runtime.util.data-grid-default";

    private ResourceBundle _defaultResourceBundle = null;
    private ResourceBundle _resourceBundle = null;
    private HashMap _resourceBundleKeys = null;

    /**
     * Default, package protected constructor.  This class should only be constructed via the
     * {@link DefaultDataGridResourceProvider}.
     */
    DefaultDataGridResourceProvider() {
    }

    /**
     * Get a message from the default properties file given a message key.  If chaining is enabled,
     * this method will first check the specified resource bundle and then fall back to the
     * default bundle.
     * @param key the key
     * @return the message
     */
    public String getMessage(String key) {
        return internalGetMessage(key);
    }

    /**
     * Format a message associated with the given key and with the given message arguments.
     * @param key the key
     * @param args the formatting arguments
     * @return the formatted message
     */
    public String formatMessage(String key, Object[] args) {
        String msg = internalFormatMessage(getMessage(key), args);
        return msg;
    }

    /**
     * Lookup a message given a message key.  If chaining is enabled via
     * {@link DataGridResourceProvider#setEnableChaining(boolean)}, the outer resource bundle will be searched,
     * first.  If no message matching the message key is found, the default resource bundle is searched.
     * @param key the key
     * @return the message.
     */
    private final String internalGetMessage(String key) {
        if(getResourceBundlePath() == null)
            return getDefaultMessage(key);
        else {
            /* ensure that the correct resource bundles are created */
            if(_resourceBundle == null) {
                _resourceBundle = createResourceBundle(getResourceBundlePath());
            }

            if(isEnableChaining() && _resourceBundleKeys == null) {
                Enumeration e = _resourceBundle.getKeys();
                while(e.hasMoreElements())
                    _resourceBundleKeys.put(e.nextElement(), VALUE_PLACEHOLDER);
            }

            if(!isEnableChaining() || _resourceBundleKeys.containsKey(key))
                return _resourceBundle.getString(key);
            else
                return _defaultResourceBundle.getString(key);
        }
    }

    /**
     * Get a message with the given key from the default message bundle.
     * @param key the key
     * @return the message
     */
    private String getDefaultMessage(String key) {
        if(_defaultResourceBundle == null)
            _defaultResourceBundle = createResourceBundle(DEFAULT_RESOURCE_BUNDLE);
        return _defaultResourceBundle.getString(key);
    }

}
