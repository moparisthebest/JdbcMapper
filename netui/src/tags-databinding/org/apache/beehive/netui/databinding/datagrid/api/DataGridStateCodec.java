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

import javax.servlet.ServletRequest;

/**
 * <p>
 * This abstract base class acts as a service that exposes state information to a
 * data grid.
 * </p>
 * <p>
 * Implementations of the DataGridStateCodec should be request scoped and are not
 * meant to be serialized.  Implementations can hold references to the {@link ServletRequest}.
 * In order to maintain a data grid's state across a request in a Java object,
 * the {@link DataGridState} object should be used.
 * </p>
 */
public abstract class DataGridStateCodec {

    private ServletRequest _request;
    private String _gridName;

    /**
     * Set the {@link ServletRequest}.  The ServletRequest can be used by implementations to
     * discover information contained in request URL or searched for request attributes.
     *
     * @param request the current request
     */
    public void setServletRequest(ServletRequest request) {
        _request = request;
    }

    /**
     * Get the current servlet request with which this DataGridStateCodec is associated.
     *
     * @return the {@link ServletRequest}
     */
    public ServletRequest getServletRequest() {
        return _request;
    }

    /**
     * Set the data grid name with which this DataGridStateCodec is associated.
     *
     * @param gridName the data grid's name
     */
    public void setGridName(String gridName) {
        _gridName = gridName;
    }

    /**
     * Get the data grid name with which this DataGridStateCodec is associated.
     *
     * @return the data grid's name
     */
    public String getGridName() {
        return _gridName;
    }

    /**
     * Get the {@link DataGridState} for a data grid.  This object contains the state
     * which the data grid will use during rendering.
     *
     * @return the current {@link DataGridState} object
     */
    public abstract DataGridState getDataGridState();

    /**
     * Set the @{link DataGridState} object.  This allows a client to apply a prior
     * {@link DataGridState} object in order to explicitly set the data grid's state
     * to a previously create set of objects.
     *
     * @param state the new {@link DataGridState}
     */
    public abstract void setDataGridState(DataGridState state);

    /**
     * Get a {@link DataGridURLBuilder} which can build be used to build URLs for
     * a data grid's current state.
     *
     * @return the {@link DataGridURLBuilder} for the data grid's state
     */
    public abstract DataGridURLBuilder getDataGridURLBuilder();
}
