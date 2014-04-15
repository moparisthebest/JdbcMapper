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

import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.TemplateCellModel;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;

/**
 * <p>
 * Data grid cell that renders the content contained inside of its body inside of an HTML &lt;td&gt;
 * tag.  The body of the template cell can contain any JSP markup that is legal to nest inside of
 * a {@link javax.servlet.jsp.tagext.SimpleTag}.
 * </p>
 * <p>
 * The templateCell can be used to render HTML UI that is not supported with other data grid cell types.  For example,
 * to build UI that will POST data to a form, the NetUI {@link org.apache.beehive.netui.tags.html.TextBox} tag can
 * be used as:
 * <pre>
 *     &lt;netui-data:templateCell>
 *          &lt;netui:textBox dataSource="container.item.name"/>
 *     &lt;/netui-data:templateCell>
 * </pre>
 * </p>
 * <p>
 * The set of JSP implicit objects available to the body include:
 * <ul>
 * <li><code>dataGridModel</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel}
 * for the cell's containing data grid.</li>
 * <li><code>container</code> -- the {@link org.apache.beehive.netui.script.common.IDataAccessProvider} instance
 * that exposes the current data item and the current item's index</li>
 * </ul>
 * </p>
 * <p>
 * The HTML events, core attributes, and internationalization JSP tag attributes are applied to the
 * &lt;td&gt;.
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * Data grid cell that renders the content contained inside of its body inside of an HTML &lt;td&gt;
 * tag.  The body of the template cell can contain any JSP markup that is legal to nest inside of
 * a {@link javax.servlet.jsp.tagext.SimpleTag}.
 * </p>
 * <p>
 * The templateCell can be used to render HTML UI that is not supported with other data grid cell types.  For example,
 * to build UI that will POST data to a form, the NetUI {@link org.apache.beehive.netui.tags.html.TextBox} tag can
 * be used as:
 * <pre>
 *     &lt;netui-data:templateCell>
 *          &lt;netui:textBox dataSource="container.item.name"/>
 *     &lt;/netui-data:templateCell>
 * </pre>
 * </p>
 * <p>
 * The set of JSP implicit objects available to the body include:
 * <ul>
 * <li><code>dataGridModel</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel}
 * for the cell's containing data grid.</li>
 * <li><code>container</code> -- the {@link org.apache.beehive.netui.script.common.IDataAccessProvider} instance
 * that exposes the current data item and the current item's index</li>
 * </ul>
 * </p>
 * <p>
 * The HTML events, core attributes, and internationalization JSP tag attributes are applied to the
 * &lt;td&gt;.
 * </p>
 *
 * @netui:tag name="templateCell" body-content="scriptless"
 *            description="Data grid cell that renders the content resulting from evaluating its body"
 */
public class TemplateCell
    extends AbstractHtmlTableCell {

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "TemplateCell";
    }

    /**
     * Render the contents of this cell.  For this tag, the result of executing the
     * {@link javax.servlet.jsp.tagext.JspFragment} is added to the output stream.
     * @param appender the {@link AbstractRenderAppender} used for output
     * @param jspFragmentOutput the result of evaluating the body of the tag.
     */
    protected void renderDataCellContents(AbstractRenderAppender appender, String jspFragmentOutput) {
        appender.append(jspFragmentOutput);
    }

    /**
     * Implementation of {@link AbstractHtmlTableCell#internalGetCellModel()} that exposes the
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.TemplateCellModel}
     * which is storing state for this tag.
     * @return this tag's template cell model
     */
    protected CellModel internalGetCellModel() {
        return new TemplateCellModel();
    }
}
