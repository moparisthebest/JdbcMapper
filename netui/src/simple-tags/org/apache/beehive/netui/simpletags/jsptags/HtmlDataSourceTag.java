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
package org.apache.beehive.netui.simpletags.jsptags;

import org.apache.beehive.netui.simpletags.behaviors.HtmlDataSourceBehavior;

/**
 * Abstract Base class adding support for the <code>dataSource</code> attribute.
 */
abstract public class HtmlDataSourceTag
        extends HtmlFocusBaseTag
{
    /**
     * Sets the tag's data source (can be an expression).
     * @param dataSource the data source
     * @jsptagref.attributedescription <p>The <code>dataSource</code> attribute determines both
     * (1) the source of populating data for the tag and
     * (2) the object to which the tag submits data.
     *
     * <p>For example, assume that the Controller file (= JPF file) contains
     * a Form Bean with the property foo.  Then the following &lt;netui:textBox> tag will
     * (1) draw populating data from the Form Bean's foo property and (2)
     * submit user defined data to the same property.
     *
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;netui:textBox dataSource="actionForm.foo" /></code>
     *
     * <p>The <code>dataSource</code> attribute takes either a data binding expression or
     * the name of a Form Bean property.  In the
     * above example, <code>&lt;netui:textBox dataSource="foo" /></code>
     * would have the exactly same behavior.
     *
     * <p>When the tag is used to submit data, the data binding expression must
     * refer to a Form Bean property.
     * In cases where the tag is not used to submit data, but is used for
     * displaying data only, the data
     * binding expression need not refer to a Form Bean property.  For example,
     * assume that myIterativeData is a member variable on
     * the Controller file ( = JPF file).  The following &lt;netui-data:repeater>
     * tag draws its data from myIterativeData.
     *
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;netui-data:repeater dataSource="pageFlow.myIterativeData"></code>
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>expression_datasource</i>
     * @netui:attribute required="true"
     * description="The <code>dataSource</code> attribute determines both
     * the source of populating data for the tag and
     * the object to which the tag submits data."
     */
    public void setDataSource(String dataSource)
    {
        ((HtmlDataSourceBehavior) _behavior).setDataSource(dataSource);
    }
}
