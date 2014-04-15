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
package org.apache.beehive.netui.databinding.datagrid.api.filter;

/**
 * <p>
 * A filter operation is an abstract representation of an operation that can be performed when filtering a data set.
 * The operations that can be performed are not provided here; rather subclasses and external utilities can create
 * FilterOperation instances that map to concrete operations for query languages like XQuery or SQL.  The
 * process for performing this mapping is left to filtering utilities.
 * </p>
 */
public class FilterOperation
    implements java.io.Serializable {

    private int _id;
    private String _abbreviation;
    private String _resourceKey;
    private FilterOperationHint _operationHint;

    /**
     * Construct a FilterOperation given a set of metadata about the filter.
     * @param id the operation identifier
     * @param abbreviation the abbreviation for the filter
     * @param resourceKey a resource key used to lookup a readable String for a filter operation
     * @param operationHint a hint for the operation that classifies the operation into a language independent
     *                      operation category.
     */
    public FilterOperation(int id, String abbreviation, String resourceKey, FilterOperationHint operationHint) {
        _id = id;
        _abbreviation = abbreviation;
        _operationHint = operationHint;
        _resourceKey = resourceKey;
    }

    /**
     * Get the filter operation's id.
     * @return the id
     */
    public int getId() {
        return _id;
    }

    /**
     * Get the filter operation's abbreviation
     * @return the abbreviation
     */
    public String getAbbreviation() {
        return _abbreviation;
    }

    /**
     * Get the filter operation's resource key.  Discoverying the readable text for an operation is left
     * to the client or other filtering utilities.
     * @return the resource key
     */
    public String getResourceKey() {
        return _resourceKey;
    }

    /**
     * Get the filter operation's {@link FilterOperationHint}.
     * @return the operation hint
     */
    public FilterOperationHint getOperationHint() {
        return _operationHint;
    }
}
