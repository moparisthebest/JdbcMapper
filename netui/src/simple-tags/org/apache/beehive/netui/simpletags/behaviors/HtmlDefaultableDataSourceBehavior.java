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
package org.apache.beehive.netui.simpletags.behaviors;

abstract public class HtmlDefaultableDataSourceBehavior extends HtmlDataSourceBehavior 
{
    protected Object _defaultValue;           // A String that contains or references the data to render for the default value of this tag.

     /**
      * Sets the default value (can be an expression).
      * @param defaultValue the default value
      * @jsptagref.attributedescription <p><b>Use in &lt;netui:checkBoxGroup>, &lt;netui:checkBox>,
      * &lt;netui:radioButtonGroup>, and &lt;netui:select> tags</b></p>
      * <p>Sets the preselected value or values.
      * <p>The <code>defaultValue</code> attribute takes either a String literal or
      * a data binding expression.
      * <p>If the <code>defaultValue</code> attribute has a String value (or if
      * the data binding expression points to a String), then a single value
      * will be preselected.
      * <p>If the <code>defaultValue</code> attribute points to a String[] object
      * (or any object which can be iterated over), then
      * multiple values will be preselected.
      *
      *
      * <p><b>Use in &lt;netui:textArea> and &lt;netui:textBox> tags</b></p>
      * <p>Sets the initial display text.</p>
      * <p>The <code>defaultValue</code> attribute takes either a String literal or
      * a data binding expression that points to a String.
      * @jsptagref.databindable Read / Write
      * @jsptagref.attributesyntaxvalue <i>string_or_expression_default</i>
      * @netui:attribute required="false" rtexprvalue="true"  type="java.lang.Object"
      * description="Sets the default value of the control which is used if the object bound to is null."
      */
     public void setDefaultValue(Object defaultValue)
     {
         _defaultValue = defaultValue;
     }
}
