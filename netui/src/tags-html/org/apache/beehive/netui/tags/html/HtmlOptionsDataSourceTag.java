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
package org.apache.beehive.netui.tags.html;

import javax.servlet.jsp.JspException;

/**
 * Abstract base class extending the <code>HtmlDefaultableDataSourceTag</code> and adding support
 * for the <code>optionsDataSource</code> attribute.
 */
abstract public class HtmlOptionsDataSourceTag extends HtmlDefaultableDataSourceTag
{
    /**
     * The value of the options data source.
     */
    protected Object _optionsDataSource;

    /**
     * Sets the options datasource value (an expression).
     * @param optionsDataSource the options datasource
     * @jsptagref.attributedescription Sets the options datasource value (an expression).
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>expression_options_datasource</i>
     * @netui:attribute required="false"  rtexprvalue="true" type="java.lang.Object"
     * description="Sets the options datasource value."
     */
    public void setOptionsDataSource(Object optionsDataSource)
            throws JspException
    {
        _optionsDataSource = optionsDataSource;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();
        _optionsDataSource = null;
    }
}
