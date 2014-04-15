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
package org.apache.beehive.netui.tags.databinding.repeater;

/**
 * <p>
 * Renders the footer of a {@link Repeater} tag.  This header may contain any HTML markup that can
 * be rendered to a page.  In the case of closing a table, ordered list, or unordered list, the elements
 * &lt;/table&gt;, &lt;/ol&gt;, and &lt;/ul&gt; could respectively be rendered inside the body of
 * the &lt;netui-data:repeaterFooter> tag.
 * </p>
 * <p>
 * There is no data item present at the time that the &lt;netui-data:repeaterFooter> renders
 * (because the iteration of the &lt;netui-data:repeater> tag has ended), so tags in the body can not
 * reference the <code>${container...}</code> JSP EL implicit object to access the current item in
 * the data set, though other databinding contexts are available.
 * </p>
 * <p>
 * The header tag is rendered exactly once at the end of repeater rendering.
 * </p>
 * <p>
 * For example, the following sample renders the data set as an HTML table.  The table has two columns,
 * "index" and "name", and each iteration over the data set is rendered a row of the table.  The
 * &lt;netui-data:repeaterFooter> tag renders once, after the iteration is complete.  It renders a
 * closing HTML table tag.
 * <br/>
 * <pre>
 *    &lt;netui-data:repeater dataSource="pageFlow.myDataSet">
 *        &lt;netui-data:repeaterHeader>
 *            &lt;table border="1">
 *                &lt;tr>
 *                    &lt;td>&lt;b>index&lt;/b>&lt;/td>
 *                    &lt;td>&lt;b>name&lt;/b>&lt;/td>
 *                &lt;/tr>
 *        &lt;/netui-data:repeaterHeader>
 *        &lt;netui-data:repeaterItem>
 *            &lt;tr>
 *                &lt;td>
 *                    &lt;netui:span value="${container.index}" />
 *                &lt;/td>
 *                &lt;td>
 *                    &lt;netui:span value="${container.item}" />
 *                &lt;/td>
 *            &lt;/tr>
 *        &lt;/netui-data:repeaterItem>
 *        <b>&lt;netui-data:repeaterFooter></b>
 *            &lt;/table>
 *        <b>&lt;/netui-data:repeaterFooter></b>
 *    &lt;/netui-data:repeater>
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * Renders the footer of a {@link Repeater} tag.  This header may contain any HTML markup that can
 * be rendered to a page.  In the case of closing a table, ordered list, or unordered list, the elements
 * &lt;/table&gt;, &lt;/ol&gt;, and &lt;/ul&gt; could respectively be rendered inside the body of
 * the &lt;netui-data:repeaterFooter> tag.
 * </p>
 * <p>
 * There is no data item present at the time that the &lt;netui-data:repeaterFooter> renders
 * (because the iteration of the &lt;netui-data:repeater> tag has ended), so tags in the body can not
 * reference the <code>${container...}</code> JSP EL implicit object to access the current item in
 * the data set, though other databinding contexts are available.
 * </p>
 * <p>
 * The header tag is rendered exactly once at the end of repeater rendering.
 * </p>
 * @example
 * <p>
 * For example, the following sample renders the data set as an HTML table.  The table has two columns,
 * "index" and "name", and each iteration over the data set is rendered a row of the table.  The
 * &lt;netui-data:repeaterFooter> tag renders once, after the iteration is complete.  It renders a
 * closing HTML table tag.
 * <br/>
 * <pre>
 *    &lt;netui-data:repeater dataSource="pageFlow.myDataSet">
 *        &lt;netui-data:repeaterHeader>
 *            &lt;table border="1">
 *                &lt;tr>
 *                    &lt;td>&lt;b>index&lt;/b>&lt;/td>
 *                    &lt;td>&lt;b>name&lt;/b>&lt;/td>
 *                &lt;/tr>
 *        &lt;/netui-data:repeaterHeader>
 *        &lt;netui-data:repeaterItem>
 *            &lt;tr>
 *                &lt;td>
 *                    &lt;netui:span value="${container.index}" />
 *                &lt;/td>
 *                &lt;td>
 *                    &lt;netui:span value="${container.item}" />
 *                &lt;/td>
 *            &lt;/tr>
 *        &lt;/netui-data:repeaterItem>
 *        <b>&lt;netui-data:repeaterFooter></b>
 *            &lt;/table>
 *        <b>&lt;/netui-data:repeaterFooter></b>
 *    &lt;/netui-data:repeater>
 * </pre>
 * </p>
 *
 * @netui:tag name="repeaterFooter" description="Render the footer of a repeater tag."
 */
public class RepeaterFooter
        extends RepeaterComponent {

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "RepeaterFooter";
    }

    public boolean shouldRender() {
        return getRepeater().getRenderState() == Repeater.FOOTER;
    }
}
