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
package org.apache.beehive.netui.tags.databinding.datagrid;

import javax.servlet.jsp.JspException;

import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * Data grid tag used to render the grid's pager at a specific location in a JSP.  By default,
 * the data grid renders its pager in a fixed location.  In order to change the location of the pager,
 * this tag can be used to move the rendered pager or to render multiple pagers.  For example, to render
 * a pager in both the grid's header and footer, the tag can be used as:
 * <pre>
 *     &lt;netui-data:header>
 *         ....
 *         &lt;netui-data;renderPager/>
 *         ....
 *     &lt;/netui-data:header>
 *     &lt;netui-data:footer>
 *         ....
 *         &lt;netui-data;renderPager/>
 *         ....
 *     &lt;/netui-data:footer>
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * Data grid tag used to render the grid's pager at a specific location in a JSP.  By default,
 * the data grid renders its pager in a fixed location.  In order to change the location of the pager,
 * this tag can be used to move the rendered pager or to render multiple pagers.  For example, to render
 * a pager in both the grid's header and footer, the tag can be used as:
 * <pre>
 *     &lt;netui-data:header>
 *         ....
 *         &lt;netui-data;renderPager/>
 *         ....
 *     &lt;/netui-data:header>
 *     &lt;netui-data:footer>
 *         ....
 *         &lt;netui-data;renderPager/>
 *         ....
 *     &lt;/netui-data:footer>
 * </pre>
 * </p>
 *
 * @netui:tag name="renderPager" body-content="empty"
 *            description="Data grid tag used to render the grid's pager at a specific location in a JSP."
 */
public class RenderPager
        extends AbstractDataGridHtmlTag {

    /**
     * The tag's name; this is used for NetUI tag error reporting.
     * @return the tag's name
     */
    public String getTagName() {
        return "RenderPager";
    }

    /**
     * <p>
     * Tag rendering method that renders the data grid's registered
     * {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.PagerRenderer} into the
     * page's output.
     * </p>
     * @throws JspException when an error occurs rendering or no {@link DataGridTagModel} can be found.
     */
    public void doTag()
            throws JspException {

        DataGridTagModel dgm = DataGridUtil.getDataGridTagModel(getJspContext());
        if(dgm == null)
            throw new JspException(Bundle.getErrorString("DataGridTags_MissingDataGridModel"));

        String output = dgm.getPagerRenderer().render();
        if(output != null)
            write(output);
    }
}
