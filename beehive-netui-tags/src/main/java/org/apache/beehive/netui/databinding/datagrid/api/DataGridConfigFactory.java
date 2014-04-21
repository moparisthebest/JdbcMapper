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

import org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultDataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.ExtensionUtil;

/**
 * <p>
 * A factory for creating instances of a {@link DataGridConfig}.
 * </p>
 */
public final class DataGridConfigFactory {

    private static final DataGridConfig DEFAULT_DATA_GRID_CONFIG = new DefaultDataGridConfig();
    /* do not construct */
    private DataGridConfigFactory() {
    }

    /**
     * Create an instance of the default {@link DataGridConfig} object.  When no other configuration
     * information is available, clients can use this method to create an instance that
     * provides sufficient defaults for data grid rendering.
     *
     * @return the default data grid config object.
     */
    public static final DataGridConfig getInstance() {
        return DEFAULT_DATA_GRID_CONFIG;
    }

    /**
     * Create an instance of a {@link DataGridConfig} object given a {@link Class} object.
     * The given class must extend the {@link DataGridConfig} base class.
     *
     * @param clazz the class to instantiate
     * @return the new {@link DataGridConfig} instance
     */
    public static final DataGridConfig getInstance(Class clazz) {
        DataGridConfig config = (DataGridConfig)ExtensionUtil.instantiateClass(clazz, DataGridConfig.class);
        return config;
    }
}
