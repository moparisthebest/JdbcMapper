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
package org.apache.beehive.netui.pageflow.faces.internal;

import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.struts.action.ActionForm;

import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletRequest;

/**
 * Internal class used in JSF/Page Flow integration.  This exists to pass form beans from JSF pages to Page Flow
 * actions, and to abort event processing if {@link PageFlowNavigationHandler} forwarded to an action.
 * 
 * @see org.apache.beehive.netui.pageflow.faces.PageFlowApplicationFactory
 */ 
public class PageFlowActionListener
        implements ActionListener
{
    private ActionListener _delegate;
    
    public PageFlowActionListener( ActionListener delegate )
    {
        _delegate = delegate;
    }
    
    public void processAction( ActionEvent event ) throws AbortProcessingException
    {
        Object submitFormBean = event.getComponent().getAttributes().get( "submitFormBean" );
        FacesContext context = FacesContext.getCurrentInstance();
        Object request = context.getExternalContext().getRequest();

        if (submitFormBean != null && request instanceof ServletRequest) {
            ValueBinding binding = context.getApplication().createValueBinding( "#{" + submitFormBean + '}' );
            Object beanInstance = binding.getValue( context );
            ActionForm wrappedFormBean = InternalUtils.wrapFormBean( beanInstance );
            InternalUtils.setForwardedFormBean( ( ServletRequest ) request, wrappedFormBean );
        }
        
        _delegate.processAction( event );

        // Tell the faces process cycle that we're done... stop the life cycle
        if (request instanceof ServletRequest) {
            ServletRequest servletRequest = (ServletRequest) request;
            String actionURI = (String) servletRequest.getAttribute(PageFlowNavigationHandler.ALREADY_FORWARDED_ATTR);

            if (actionURI != null) {
                throw new AbortProcessingException("PageFlowNavigationHandler forwarded to: " + actionURI);
            }
        }
    }
}
