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

import java.util.HashMap;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspContext;
import javax.servlet.ServletRequest;

import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * Factory class used to construct instances of {@link DataGridState} objects.  This
 * class is used by the data grid and other clients to obtain the state for a data grid
 * with a given name.
 * </p>
 * <p>
 * Data grid state information is scoped by a unique data grid String name.  This name
 * should be unique for a particular scope in a request.  For all factory methods that
 * take a data grid name as a parameter, the value of the <code>name</code> attribute
 * should match the value of the
 * {@link org.apache.beehive.netui.tags.databinding.datagrid.DataGrid#setName(String)}
 * attribute for the data grid whose state to lookup.
 * </p>
 */
public final class DataGridStateFactory {

    private static final String KEY = DataGridStateFactory.class.getName() + "REQUEST_KEY";
    private static final DataGridConfig DEFAULT_DATA_GRID_CONFIG = DataGridConfigFactory.getInstance();

    /**
     * Get an instance of a DataGridStateFactory given a {@link JspContext}.
     *
     * @param jspContext the current {@link JspContext}
     * @return an instance of the factory
     */
    public static final DataGridStateFactory getInstance(JspContext jspContext) {
        assert jspContext instanceof PageContext;
        return getInstance(((PageContext)jspContext).getRequest());
    }

    /**
     * Get an instance of a DataGridStateFactory given a {@link ServletRequest}.
     *
     * @param request the current {@link ServletRequest}
     * @return an instance of the factory
     */
    public static final DataGridStateFactory getInstance(ServletRequest request) {
        Object obj = request.getAttribute(KEY);
        if(obj != null) {
            assert obj instanceof DataGridStateFactory;
            return (DataGridStateFactory)obj;
        }
        else {
            DataGridStateFactory factory = new DataGridStateFactory(request);
            request.setAttribute(KEY, factory);
            return factory;
        }
    }

    private final ServletRequest _request;
    private final HashMap/*<String, DataGridStateCodec>*/ _cache;

    private DataGridStateFactory(ServletRequest request) {
        _request = request;
        _cache = new HashMap/*<String, DataGridStateCodec>*/();
    }

    /**
     * <p>
     * Lookup a {@link DataGridState} object given a data grid identifier.
     * </p>
     * <p>
     * This method will use the default {@link DataGridConfig} object when returning a data grid specific
     * implementation of the {@link DataGridState} object.  In order to specify a {@link DataGridConfig},
     * the {@link DataGridStateFactory#getDataGridState(String, DataGridConfig)} can be supplied
     * with a specific data grid configuration.
     * </p>
     *
     * @param name the name of a data grid.
     * @return the {@link DataGridState} for the data grid with the given name
     */
    public final DataGridState getDataGridState(String name) {
        return getDataGridState(name, DEFAULT_DATA_GRID_CONFIG);
    }

    /**
     * <p>
     * Lookup a {@link DataGridState} object given a data grid identifier and a specific
     * {@link DataGridConfig} object.
     * </p>
     *
     * @param name the name of the data grid
     * @param config the {@link DataGridConfig} object to use when creating the
     * grid's {@link DataGridState} object.
     * @return the data grid state object
     */
    public final DataGridState getDataGridState(String name, DataGridConfig config) {
        if(config == null)
            throw new IllegalArgumentException(Bundle.getErrorString("DataGridStateFactory_nullDataGridConfig"));

        DataGridStateCodec codec = lookupCodec(name, config);
        DataGridState state = codec.getDataGridState();
        return state;
    }

    /**
     * <p>
     * Lookup a {@link DataGridURLBuilder} object given a data grid identifier.
     * </p>
     * <p>
     * This method will use the default {@link DataGridConfig} object when returning a data grid specific
     * implementation of the {@link DataGridURLBuilder} object.  In order to specify a {@link DataGridConfig},
     * the {@link DataGridStateFactory#getDataGridURLBuilder(String, DataGridConfig)} can be supplied
     * with a specific data grid configuration.
     * </p>
     *
     * @param name the name of the data grid
     * @return the {@link DataGridURLBuilder} for the data grid with the given name
     */
    public final DataGridURLBuilder getDataGridURLBuilder(String name) {
        return getDataGridURLBuilder(name, DEFAULT_DATA_GRID_CONFIG);
    }

    /**
     * <p>
     * Lookup a {@link DataGridURLBuilder} object given a data grid identifier and a specific
     * {@link DataGridConfig} object.
     * </p>
     *
     * @param name the name of the data grid
     * @param config the {@link DataGridConfig} object to use when creating the
     * grid's {@link DataGridURLBuilder} object.
     * @return the URL builder for a data grid's state object
     */
    public final DataGridURLBuilder getDataGridURLBuilder(String name, DataGridConfig config) {
        if(config == null)
            throw new IllegalArgumentException(Bundle.getErrorString("DataGridStateFactory_nullDataGridConfig"));

        DataGridStateCodec codec = lookupCodec(name, config);
        DataGridURLBuilder builder = codec.getDataGridURLBuilder();
        return builder;
    }

    /**
     * <p>
     * Convenience method that allows a {@link DataGridState} object from a client to
     * be attached to the factory.  This allows subsequent calls to retrieve this same {@link DataGridState}
     * instance.
     * </p>
     *
     * @param name the name of the data grid
     * @param state the {@link DataGridState} object to attach
     */
    public final void attachDataGridState(String name, DataGridState state) {
        DataGridStateCodec codec = lookupCodec(name, DEFAULT_DATA_GRID_CONFIG);
        codec.setDataGridState(state);
    }

    private final DataGridStateCodec lookupCodec(String name, DataGridConfig config) {
        DataGridStateCodec codec = null;
        if(_cache.containsKey(name))
            codec = (DataGridStateCodec)_cache.get(name);
        else {
            assert config != null;
            codec = config.createStateCodec(_request, name);
            _cache.put(name, codec);
        }

        return codec;
    }
}
