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

import javax.servlet.jsp.JspContext;

import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;

/**
 * <p>
 * Utility data grid tag that allows the page author to gain access to the {@link DataGridState} object
 * outside of the body of a &lt;netui-data:dataGrid&gt; tag.  When building UI for sorting, filtering, or paging,
 * this UI does not need to exist inside of the HTML table rendered by the data grid tags.  In order to render
 * UI for sorting, filtering, and paging, it is often necessary to gain access to the state exposed to a data
 * grid via the {@link DataGridState} object.  For example:
 * <pre>
 *   &lt;netui-data:getDataGridState gridName="employees" var="employeeGridState"/>
 *   &lt;c:if test="${pageScope.employeeGridState.sortModel.sorts} != null}">
 *     ... render UI when sorts are present ...
 *   &lt;/c:if>
 * </pre>
 * this will expose the "employees" data grid's list of
 * {@link org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel#getSorts()} to the JSP for access by
 * the JSP EL.
 * </p>
 * @jsptagref.tagdescription
 * <p>
 * Utility data grid tag that allows the page author to gain access to the {@link DataGridState} object
 * outside of the body of a &lt;netui-data:dataGrid&gt; tag.  When building UI for sorting, filtering, or paging,
 * this UI does not need to exist inside of the HTML table rendered by the data grid tags.  In order to render
 * UI for sorting, filtering, and paging, it is often necessary to gain access to the state exposed to a data
 * grid via the {@link DataGridState} object.  For example:
 * <pre>
 *   &lt;netui-data:getDataGridState gridName="employees" var="employeeGridState"/>
 *   &lt;c:if test="${pageScope.employeeGridState.sortModel.sorts} != null}">
 *     ... render UI when sorts are present ...
 *   &lt;/c:if>
 * </pre>
 * this will expose the "employees" data grid's list of
 * {@link org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel#getSorts()} to the JSP for access by
 * the JSP EL.
 * </p>
 *
 * @netui:tag name="getDataGridState" body-content="scriptless"
 *            description="Get a DataGridState object and add it to the PageContext under a given variable name"
 */
public class GetDataGridState
    extends AbstractSimpleTag {

    private String _var = null;
    private String _name = null;
    private DataGridConfig _config = null;

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public final String getTagName() {
        return "GetDataGridState";
    }

    /**
     * Set the data grid name whose {@link DataGridState} should be placed in the {@link JspContext}.
     *
     * @param name the name of the data grid
     * @jsptagref.attributedescription
     * Set the data grid name whose {@link DataGridState} should be placed in the {@link JspContext}.
     * @jsptagref.attributesyntaxvalue <i>string_gridName</i>
     * @netui:attribute name="name" required="true"
     *                  description="Set the name of the data grid whose DataGridState to lookup."
     */
    public void setGridName(String name) {
        _name = name;
    }

    /**
     * Set the name used to store the {@link DataGridState} object as a {@link JspContext} attribute.
     * @param var the name used to store the {@link DataGridState}
     * @jsptagref.attributedescription
     * Set the name used to store the {@link DataGridState} object as a {@link JspContext} attribute.
     * @jsptagref.attributesyntaxvalue <i>string_var</i>
     * @netui:attribute name="var" required="true"
     */
    public void setVar(String var) {
        _var = var;
    }

    /**
     * Set a {@link DataGridConfig} instance used to create a {@link DataGridState} object.  This attribute is
     * optional; when unset, the default {@link DataGridConfig} is used.
     * @param config the {@link DataGridConfig} used to create the {@link DataGridState}
     * @jsptagref.attributedescription
     * Set a {@link DataGridConfig} instance used to create a {@link DataGridState} object.  This attribute is
     * optional; when unset, the default {@link DataGridConfig} is used.
     * @jsptagref.attributesyntaxvalue <i>string_var</i>
     * @netui:attribute name="name" required="false"
     *                  description="Set an optional DataGridConfig instance used to create the DataGridState object."
     */
    public void setDataGridConfig(DataGridConfig config) {
        _config = config;
    }

    /**
     * Using a {@link DataGridConfig} object, access the {@link DataGridState} and place
     * it in the {@link JspContext} under the attribute key set via {@link #setVar(String)}
     */
    public void doTag() {
        JspContext jspContext = getJspContext();
        DataGridStateFactory factory = DataGridStateFactory.getInstance(jspContext);
        assert factory != null;

        DataGridState state = null;
        if(_config != null)
            state = factory.getDataGridState(_name, _config);
        else
            state = factory.getDataGridState(_name);

        jspContext.setAttribute(_var, state);
    }
}
