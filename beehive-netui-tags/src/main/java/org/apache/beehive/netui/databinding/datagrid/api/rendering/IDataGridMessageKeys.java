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
package org.apache.beehive.netui.databinding.datagrid.api.rendering;

/**
 * <p>
 * This interfaces exposes a set of message keys that are used by the data grid when
 * looking-up message values for text and other strings during rendering.
 * </p>
 * <p>
 * Custom resource bundles implemented with Java property files can be exposed via a
 * {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridResourceProvider}.  Such property files
 * should contain message keys whose values match the values of the messages keys defined here.
 * </p>
 */
public interface IDataGridMessageKeys {

    /**
     * <p>
     * String key for the message displayed when no data is available in the data set to which
     * a data grid is bound.
     * <br/>
     * <b>Default value:</b><i>No data to display</i>
     */
    String DATAGRID_MSG_NODATA = "datagrid.msg.nodata";

    /**
     * <p>
     * String key for the root resource path used when building a URL to resources such as images rendered
     * in a data grid.
     * <br/>
     * <b>Default value:</b><i>resources/beehive/version1/images</i>
     * </p>
     */
    String DATAGRID_RESOURCE_PATH = "datagrid.resource.path";

    /**
     * <p>
     * String key for the name of an image used when rendering the ascending sort direction.
     * <br/>
     * <b>Default value:</b><i>/sortdown.gif</i>
     * </p>
     */
    String SORT_ASC_IMAGE_PATH = "sort.asc.img";

    /**
     * <p>
     * String key for the name of an image used when rendering the descending sort direction.
     * <br/>
     * <b>Default value:</b><i>/sortup.gif</i>
     * </p>
     */
    String SORT_DESC_IMAGE_PATH = "sort.desc.img";

    /**
     * <p>
     * String key for the name of an image used when rendering the no sort direction.
     * <br/>
     * <b>Default value:</b><i>/sortup.gif</i>
     * </p>
     */
    String SORT_NONE_IMAGE_PATH = "sort.none.img";

    /**
     * <p>
     * String key for the text displayed when rendering a link for paging to the first page of a data set.
     * <br/>
     * <b>Default value:</b><i>First</i>
     * </p>
     */
    String PAGER_MSG_FIRST = "pager.msg.first";

    /**
     * <p>
     * String key for the text displayed when rendering a link for paging to the logical next page of a data set.
     * <br/>
     * <b>Default value:</b><i>Next</i>
     * </p>
     */
    String PAGER_MSG_NEXT = "pager.msg.next";

    /**
     * <p>
     * String key for the text displayed when rendering a link for paging to the logical previous page of a data set.
     * <br/>
     * <b>Default value:</b><i>Previous</i>
     * </p>
     */
    String PAGER_MSG_PREVIOUS = "pager.msg.previous";

    /**
     * <p>
     * String key for the text displayed when rendering a link for paging to the last page of a data set.
     * <br/>
     * <b>Default value:</b><i>Last</i>
     * </p>
     */
    String PAGER_MSG_LAST = "pager.msg.last";

    /**
     * <p>
     * String key for the text displayed in a pager label when showing Page # of #.
     * <br/>
     * <b>Default value:</b><i>Page {0} of {1}</i>
     * The <code>{0}</code> is substituted with a 1-based value for the current page.  The <code>{1}</code> is
     * substituted with a 1-based value for the last page.
     * </p>
     */
    String PAGER_FMT_BANNER = "pager.fmt.banner";
}
