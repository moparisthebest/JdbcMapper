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
package org.apache.beehive.netui.pageflow;

import org.apache.struts.action.ActionForm;

import java.io.Serializable;

/**
 * Base class for previous-page and previous-action information.
 */ 
public abstract class PreviousInfo
        implements Serializable
{
    private ActionForm _form;
    private String _queryString;

    protected PreviousInfo( ActionForm form, String queryString )
    {
        _form = form;
        _queryString = queryString;
    }

    /**
     * Get the form bean that was used to initialize the previous page or action.
     *
     * @return the previous ActionForm instance, or <code>null</code> if there was none.
     */
    public ActionForm getForm()
    {
        return _form;
    }

    /**
     * Get the query string from the previous page or action request.
     * 
     * @return the previous query string, or <code>null</code> if there was none.
     */ 
    public String getQueryString()
    {
        return _queryString;
    }

    void setForm( ActionForm form )
    {
        _form = form;
    }

    void setQueryString( String queryString )
    {
        _queryString = queryString;
    }
}
