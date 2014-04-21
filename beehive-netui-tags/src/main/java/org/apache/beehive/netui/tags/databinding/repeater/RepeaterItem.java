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
 * This tag is used to render an individual item in the data set as it is iterated over by
 * the {@link Repeater} tag.  As an individual item is being iterated over, it is available using the
 * <code>${container.item}</code> JSP EL expression. The &lt;netui-data:repeaterItem> tag can only be
 * uesd when directly contained by a &lt;netui-data:repeater> tag.
 * </p>
 * <p>
 * By default, the &lt;netui-data:repeaterItem> renders its body exactly once for each of the items in the
 * &lt;netui-data:repeater> tag's data set.
 * </p>
 * For example, the following sample renders the data set as an HTML table.  The &lt;netui-data:repeaterItem> tag
 * renders a new row in the table for each item in the data set.
 * </p>
 * <pre>
 *    &lt;netui-data:repeater dataSource="pageFlow.myDataSet">
 *        &lt;netui-data:repeaterHeader>
 *            &lt;table border="1">
 *                &lt;tr>
 *                    &lt;td>&lt;b>index&lt;/b>&lt;/td>
 *                    &lt;td>&lt;b>name&lt;/b>&lt;/td>
 *                &lt;/tr>
 *        &lt;/netui-data:repeaterHeader>
 *        <b>&lt;netui-data:repeaterItem></b>
 *            &lt;tr>
 *                &lt;td>
 *                    &lt;netui:span value="${container.index}" />
 *                &lt;/td>
 *                &lt;td>
 *                    &lt;netui:span value="${container.item}" />
 *                &lt;/td>
 *            &lt;/tr>
 *        <b>&lt;/netui-data:repeaterItem></b>
 *        &lt;netui-data:repeaterFooter>
 *            &lt;/table>
 *        &lt;/netui-data:repeaterFooter>
 *    &lt;/netui-data:repeater>
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * This tag is used to render an individual item in the data set as it is iterated over by
 * the {@link Repeater} tag.  As an individual item is being iterated over, it is available using the
 * <code>${container.item}</code> JSP EL expression. The &lt;netui-data:repeaterItem> tag can only be
 * uesd when directly contained by a &lt;netui-data:repeater> tag.
 * </p>
 * <p>
 * By default, the &lt;netui-data:repeaterItem> renders its body exactly once for each of the items in the
 * &lt;netui-data:repeater> tag's data set.
 * </p>
 * @example
 * For example, the following sample renders the data set as an HTML table.  The &lt;netui-data:repeaterItem> tag
 * renders a new row in the table for each item in the data set.
 * </p>
 * <pre>
 *    &lt;netui-data:repeater dataSource="pageFlow.myDataSet">
 *        &lt;netui-data:repeaterHeader>
 *            &lt;table border="1">
 *                &lt;tr>
 *                    &lt;td>&lt;b>index&lt;/b>&lt;/td>
 *                    &lt;td>&lt;b>name&lt;/b>&lt;/td>
 *                &lt;/tr>
 *        &lt;/netui-data:repeaterHeader>
 *        <b>&lt;netui-data:repeaterItem></b>
 *            &lt;tr>
 *                &lt;td>
 *                    &lt;netui:span value="${container.index}" />
 *                &lt;/td>
 *                &lt;td>
 *                    &lt;netui:span value="${container.item}" />
 *                &lt;/td>
 *            &lt;/tr>
 *        <b>&lt;/netui-data:repeaterItem></b>
 *        &lt;netui-data:repeaterFooter>
 *            &lt;/table>
 *        &lt;/netui-data:repeaterFooter>
 *    &lt;/netui-data:repeater>
 * </pre>
 * </p>
 * @netui:tag name="repeaterItem" description="Render each data item in the data set rendered by the repeater"
 */
public class RepeaterItem
    extends RepeaterComponent {

    /**
     * Get the name of this tag.  This is used to identify the type of this tag for reporting tag errors.
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "RepeaterItem";
    }

    protected boolean shouldRender() {
        return getRepeater().getRenderState() == Repeater.ITEM;
    }
}
