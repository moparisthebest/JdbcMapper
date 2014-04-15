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
package org.apache.beehive.netui.databinding.datagrid.api;

import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;

/**
 * <p>
 * This abstract class provides an abstraction used for obtaining messages and strings used
 * during data grid rendering.
 * </p>
 */
public abstract class DataGridResourceProvider {

    private boolean _enableChaining = false;
    private String _resourceBundlePath = null;
    private Locale _locale = null;

    /**
     * Abstract method used to obtain a message {@link String} given a <code>key</code>
     *
     * @param key the key used to lookup a {@link String}
     * @return the returned String if a message is found matching the key
     */
    public abstract String getMessage(String key);

    /**
     * Abstract method used to format a pattern given a pattern / message key and an array of
     * arguments.
     *
     * @param key the key to use when looking up a message to format
     * @param args the arguments to use when formatting a message
     * @return the formatted message if a message is found matching the key
     */
    public abstract String formatMessage(String key, Object[] args);

    /**
     * Accessor for determining if implementations are chaining enabled.  When chaining
     * is enabled, subclasses must use any nested DataGridResourceProvider instances to
     * lookup messages.  When chaining is enabled, the default messages for the data grid
     * will be returned.  When chaining is disabled, implementations are free to
     * hide message keys.
     *
     * @return <code>true</code> if chaining is enabled; <code>false</code> otherwise
     */
    public boolean isEnableChaining() {
        return _enableChaining;
    }

    /**
     * Setter for enabling or disabling chaining
     *
     * @param enableChaining the new chaining enabled value
     */
    public void setEnableChaining(boolean enableChaining) {
        _enableChaining = enableChaining;
    }

    /**
     * Set the {@link Locale} in which a message {@link String} should be looked up.
     *
     * @param locale the {@link Locale} to use
     */
    public void setLocale(Locale locale) {
        _locale = locale;
    }

    /**
     * Accessor for obtaining the {@link Locale} used when looking up messages.
     *
     * @return the {@link Locale} used for message lookup or <code>null</code> if no {@link Locale} was set
     */
    public Locale getLocale() {
        return _locale;
    }

    /**
     * Set the path used for creating a {@link ResourceBundle} object.
     *
     * @param resourceBundlePath the path to a resource bundle
     */
    public void setResourceBundlePath(String resourceBundlePath) {
        _resourceBundlePath = resourceBundlePath;
    }

    /**
     * Accessor for obtaining the path to the resource bundle used by a DataGridResourceProvider
     * implementation.
     *
     * @return the path to the {@link ResourceBundle}
     */
    public String getResourceBundlePath() {
        return _resourceBundlePath;
    }

    protected ResourceBundle createResourceBundle(String path) {
        ResourceBundle rb = ResourceBundle.getBundle(path, getLocale(), Thread.currentThread().getContextClassLoader());
        return rb;
    }

    /**
     * Internal convenience method that is used to format a message given a pattern
     * and a set of arguments.
     *
     * @param pattern the pattern to format
     * @param args the arguments to use when formatting
     * @return the formatted string
     */
    protected String internalFormatMessage(String pattern, Object[] args) {
        /* todo: perf -- could the MessageFormat objects be cached? */
        MessageFormat format = new MessageFormat(pattern);
        String msg = format.format(args).toString();
        return msg;
    }
}
