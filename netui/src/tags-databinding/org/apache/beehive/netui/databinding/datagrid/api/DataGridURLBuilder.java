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

import java.util.Map;

/**
 * <p>
 * This abstract class provides methods that are used to build maps of URL parameters
 * containing current ahd future data grid state. These are useful for building
 * anchors and other kinds of requests that when clicked / submitted can move to
 * the next or previous page of data or change the state of sorts or filters for
 * a data grid.
 * </p>
 * <p>
 * Subclasses are free to implement the contents of the parameter map with whatever
 * parameter key / values that make sense for a particular data grid.  For example,
 * some data grids may encode sort, filter, and paging information in the URL.  Others
 * may add information about row selection and not add sort / filter parameters.  Be
 * sure to check the documentation for a specific DataGridURLBuilder subclass to
 * find out what specific parameters are available in the parameter maps.
 * </p>
 * <p>
 * Parameter maps produced by methods on this class should contain key / value pairs
 * where the key is of type <code>String</code> and the values are of type
 * <code>String[]</code>.  The parameter maps should also include all of the
 * additional "current" URL parameters in order to maintain the "current" view
 * state and modifying only state associated with a single data grid.  Subclasses are
 * free to change this behavior.
 * </p>
 */
public abstract class DataGridURLBuilder {

    /**
     * Get a {@link Map} containing the current state of the data grid.
     * @return the parameter map
     */
    public abstract Map getQueryParams();

    /**
     * Get a parameter map that contains the grid's current state with a
     * value that will set the current to the first page.
     *
     * @return the parameter map
     */
    public abstract Map getQueryParamsForFirstPage();

    /**
     * Get a parameter map that contains the grid's current state with a
     * value that will set the current page to the previous page.
     *
     * @return the parameter map
     */
    public abstract Map getQueryParamsForPreviousPage();

    /**
     * Get a parameter map that contains the grid's current state with a
     * value that will set the current page to the next page.
     *
     * @return the parameter map
     */
    public abstract Map getQueryParamsForNextPage();

    /**
     * Get a parameter map that contains the grid's current state with a
     * value that will set the current page to the last page.
     *
     * @return the parameter map
     */
    public abstract Map getQueryParamsForLastPage();

    /**
     * Get a String array that contains the values which can be used to
     * reach any page in the data grid.
     *
     * @return an array of the query parameter values for each page
     */
    public abstract String[] getPagerParamValues();

    /**
     * Get the String for the pager query parameter key.
     *
     * @return the query parameter key for accessing the current page from the URL
     */
    public abstract String getPagerRowQueryParamKey();

    /**
     * Get a parameter map that contains the grid's current state with the
     * sort matching the given <code>sortExpression</code> switched to the
     * next available sort direction.
     *
     * @param sortExpression the sort expression whose sort value to change
     * @return the parameter map
     */
    public abstract Map buildSortQueryParamsMap(String sortExpression);
}
