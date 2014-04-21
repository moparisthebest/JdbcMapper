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

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ModuleConfig;

import java.io.Serializable;

/**
 * Stores information about a previously-displayed page, as well as its initialization data.
 * Used with
 * <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#currentPage Jpf.NavigateTo.currentPage}</code>
 * or <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousPage Jpf.NavigateTo.previousPage}</code>
 * on {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward Jpf.Forward},
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction Jpf.SimpleAction}, or
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward Jpf.ConditionalForward}.
 */
public class PreviousPageInfo
        extends PreviousInfo
        implements Serializable
{
    private ActionForward _forward;
    private String _mappingPath;
    private transient ActionMapping _mapping;
    private Object _clientState;

    
    /**
     * Constructor which accepts the ActionForward used to display the page, the ActionForm
     * used to initialize the page, and the associated ActionMapping, which represents the
     * action that forwarded to the page.
     * 
     * @param forward the ActionForward that contains the path to the page.
     * @param form the form that was present for the page when it was rendered initially.
     * @param mapping the ActionMapping associated with the action that forwarded to the page, or <code>null</code>
     *            if the page was requested directly.
     * @param queryString the query string from the request URI.
     */ 
    public PreviousPageInfo( ActionForward forward, ActionForm form, ActionMapping mapping, String queryString )
    {
        super( form, queryString );
        _mapping = mapping;
        _mappingPath = mapping != null ? mapping.getPath() : null;
        _forward = forward;
    }

    /**
     * Get information about the action that forwarded to the page.
     * <br>
     * <br>
     * Note that this information is transient.  If you place this object in the session, and then retrieve it after
     * a session failover has occurred (i.e., after this object has been serialized and deserialized), then this method
     * will return <code>null</code> unless you first call {@link #reinitialize}.
     *
     * @return an ActionMapping that contains information about the action that forwarded to this page, or
     *             <code>null</code> if the page was requested directly.
     */
    public ActionMapping getMapping()
    {
        return _mapping;
    }

    /**
     * Set information about the action that forwarded to the page.
     *
     * @param mapping an ActionMapping that contains information about the action that forwarded to this page.
     */
    public void setMapping( ActionMapping mapping )
    {
        _mapping = mapping;
    }

    /**
     * Reinitialize the stored ActionMapping and PageFlowController objects.  These are transient, and will be lost if
     * you place this object in the session, and then retrieve it after a session failover has occurred (i.e., after
     * this object has been serialized and deserialized).
     */
    public void reinitialize( PageFlowController pfc )
    {
        if ( _mapping == null && _mappingPath != null )
        {
            ModuleConfig mc = pfc.getModuleConfig();
            assert mc != null : "no ModuleConfig found for " + pfc.getClass().getName();
            _mapping = ( ActionMapping ) mc.findActionConfig( _mappingPath );
        }
        
        if ( _forward != null && _forward instanceof Forward )
        {
            ( ( Forward ) _forward ).reinitialize( pfc );
        }
    }
    
    /**
     * Get the object that was used to forward to the page.
     *
     * @return the ActionForward returned by the action that forwarded to this page.
     */
    public ActionForward getForward()
    {
        return _forward;
    }

    /**
     * Set the object that was used to forward to the page.
     *
     * @param forward the ActionForward returned by the action that forwarded to this page.
     */
    public void setForward( ActionForward forward )
    {
        _forward = forward;
    }

    /**
     * Get client state associated with the page (e.g., component tree state for a JSF page).
     */ 
    public Object getClientState()
    {
        return _clientState;
    }

    /**
     * Set client state associated with the page (e.g., component tree state for a JSF page).
     */ 
    public void setClientState( Object clientState )
    {
        _clientState = clientState;
    }
}
