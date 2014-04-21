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
package org.apache.beehive.netui.core.urls;

/**
 * This is a class that acts as a data structure for the information for an AJAX request.
 * An AJAX URL has the following shape commandPrefix]component-directed-command.xhr?[component-parameters]&ajaxParameter
 */
public class AjaxUrlInfo
{
    /**
     * When an AJAX request is made, this is the name of the hidden field or URL paramter
     * that contains the Ajax Paramter property.
     */
    public static final String AJAX_PARAMTER_NAME = "netuiajax";

    private String _commandPrefix;
    private String _ajaxParameter;

    public AjaxUrlInfo(String commandPrefix, String ajaxParameter)
    {
        _commandPrefix = commandPrefix;
        _ajaxParameter = ajaxParameter;
    }

    /**
     * Property getter for the Command Prefix.
     * @return return the prefix to the AJAX command
     */
    public String getCommandPrefix()
    {
        return _commandPrefix;
    }

    /**
     * Property getter for the AjaxParamter.  This paramter must be URL encoded
     * when added to a URL.
     * @return return the AJAX Paramter
     */
    public String getAjaxParameter()
    {
        return _ajaxParameter;
    }
}
