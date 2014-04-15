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
 * Stores information about a recent action execution within a pageflow -- used with
 * Used with
 * <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousAction Jpf.NavigateTo.previousAction}</code>
 * on {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward Jpf.Forward},
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction Jpf.SimpleAction}, or
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward Jpf.ConditionalForward}.
 */
public class PreviousActionInfo
        extends PreviousInfo
        implements Serializable
{
    private String _actionURI;

    
    /**
     * Constructor which accepts an ActionForm and action URI.
     * 
     * @param form the form that was passed to the action.
     * @param actionURI the URI that was used to execute the action.
     * @param queryString the query string from the previous action URI.
     */ 
    public PreviousActionInfo( ActionForm form, String actionURI, String queryString )
    {
        super( form, queryString );
        _actionURI = actionURI;
    }

    /**
     * Get the URI that was used to execute the action.
     * 
     * @return the String URI that was used to execute the action.
     */ 
    public String getActionURI()
    {
        return _actionURI;
    }

    /**
     * Set the URI that was used to execute the action.
     * 
     * @param actionURI the URI that was used to execute the action.
     */ 
    public void setActionURI( String actionURI )
    {
        _actionURI = actionURI;
    }
}
