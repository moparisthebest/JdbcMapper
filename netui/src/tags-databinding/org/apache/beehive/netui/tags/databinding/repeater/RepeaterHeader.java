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
 * Renders the header of a {@link Repeater} tag. This header may contain any any HTML markup that
 * can be rendered to a page.  In the case of starting a table, ordered list, or unordered list,
 * the HTML elements &lt;table&gt;, &lt;ol&gt;, and &lt;ul&gt; could respectively be rendered inside
 * the body of this tag.
 * </p>
 * <p>
 * There is no data item present at the time that the &lt;netui-data:repeaterHeader> renders (because
 * the iteration of the &lt;netui-data:repeater> tag has not yet begun), so tags in the body can not
 * reference the <code>${container...}</code> JSP EL implicit object to access the current item in
 * the data set, though other databinding contexts are available.
 * </p>
 * <p>
 * The footer tag is rendered exactly once at the beginning of repeater rendering.
 * </p>
 * <p>
 * For example, the following sample renders the data set as an HTML table.  The table has two columns, "index" and "name",
 * and each iteration over the data set is rendered a row of the table.  The &lt;netui-data:repeaterHeader>
 * tag renders once, before the iteration has begun.  It renders an opening HTML table tag and
 * two header rows for the table.
 * <p/>
 * <pre>
 *    &lt;netui-data:repeater dataSource="pageFlow.myDataSet">
 *        <b>&lt;netui-data:repeaterHeader></b>
 *            &lt;table border="1">
 *                &lt;tr>
 *                    &lt;td>&lt;b>index&lt;/b>&lt;/td>
 *                    &lt;td>&lt;b>name&lt;/b>&lt;/td>
 *                &lt;/tr>
 *        <b>&lt;/netui-data:repeaterHeader></b>
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
 *        &lt;netui-data:repeaterFooter>
 *            &lt;/table>
 *        &lt;/netui-data:repeaterFooter>
 *    &lt;/netui-data:repeater>
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * Renders the header of a {@link Repeater} tag. This header may contain any any HTML markup that
 * can be rendered to a page.  In the case of starting a table, ordered list, or unordered list,
 * the HTML elements &lt;table&gt;, &lt;ol&gt;, and &lt;ul&gt; could respectively be rendered inside
 * the body of this tag.
 * </p>
 * <p>
 * There is no data item present at the time that the &lt;netui-data:repeaterHeader> renders (because
 * the iteration of the &lt;netui-data:repeater> tag has not yet begun), so tags in the body can not
 * reference the <code>${container...}</code> JSP EL implicit object to access the current item in
 * the data set, though other databinding contexts are available.
 * </p>
 * <p>
 * The footer tag is rendered exactly once at the beginning of repeater rendering.
 * </p>
 * @example
 * <p>
 * For example, the following sample renders the data set as an HTML table.  The table has two columns, "index" and "name",
 * and each iteration over the data set is rendered a row of the table.  The &lt;netui-data:repeaterHeader>
 * tag renders once, before the iteration has begun.  It renders an opening HTML table tag and
 * two header rows for the table.
 * <p/>
 * <pre>
 *    &lt;netui-data:repeater dataSource="pageFlow.myDataSet">
 *        <b>&lt;netui-data:repeaterHeader></b>
 *            &lt;table border="1">
 *                &lt;tr>
 *                    &lt;td>&lt;b>index&lt;/b>&lt;/td>
 *                    &lt;td>&lt;b>name&lt;/b>&lt;/td>
 *                &lt;/tr>
 *        <b>&lt;/netui-data:repeaterHeader></b>
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
 *        &lt;netui-data:repeaterFooter>
 *            &lt;/table>
 *        &lt;/netui-data:repeaterFooter>
 *    &lt;/netui-data:repeater>
 * </pre>
 * </p>
 * @netui:tag name="repeaterHeader" description="Render the header of the repeater."
 */
public class RepeaterHeader
        extends RepeaterComponent {

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "RepeaterHeader";
    }

    protected boolean shouldRender() {
        return getRepeater().getRenderState() == Repeater.HEADER;
    }
}
