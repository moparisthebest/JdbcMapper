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

import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PreviousPageInfo;
import org.apache.beehive.netui.pageflow.FacesBackingBean;
import org.apache.beehive.netui.pageflow.FacesBackingBeanFactory;
import org.apache.beehive.netui.pageflow.RequestContext;
import org.apache.beehive.netui.pageflow.internal.PageFlowRequestWrapper;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.util.internal.FileUtils;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Locale;
import java.io.IOException;
import java.io.Serializable;


/**
 * Internal class used in JSF/Page Flow integration.  Delegates in all cases except:
 * <ul>
 *     <li>
 *         {@link #restoreView}, which prevents view restoration if we're in a request forwarded by
 *         {@link PageFlowNavigationHandler}.
 *     </li>
 *     <li>
 *         {@link #createView}, which integrates with the "navigateTo" feature in Page Flow to save/restore the
 *         component tree.
 *     </li>
 * </ul>
 * 
 * @see org.apache.beehive.netui.pageflow.faces.PageFlowApplicationFactory
 */ 
class PageFlowViewHandler
        extends ViewHandler
{
    private ViewHandler _delegate;

    public PageFlowViewHandler( ViewHandler delegate )
    {
        _delegate = delegate;
    }
    
    public Locale calculateLocale(FacesContext context)
    {
        return _delegate.calculateLocale( context );
    }

    public String calculateRenderKitId(FacesContext context)
    {
        return _delegate.calculateRenderKitId( context );
    }

    private static class PageClientState implements Serializable
    {
        private UIViewRoot _viewRoot;
        private FacesBackingBean _backingBean;

        public PageClientState( UIViewRoot viewRoot, FacesBackingBean backingBean )
        {
            _viewRoot = viewRoot;
            _backingBean = backingBean;
        }

        public UIViewRoot getViewRoot()
        {
            return _viewRoot;
        }

        public FacesBackingBean getBackingBean()
        {
            return _backingBean;
        }
    }
    
    private static void setBackingBean( ServletRequest request, ServletResponse response, ServletContext servletContext )
    {
        if ( request instanceof HttpServletRequest )
        {
            FacesBackingBeanFactory factory = FacesBackingBeanFactory.get( servletContext );
            FacesBackingBean fbb = factory.getFacesBackingBeanForRequest( new RequestContext( request, response ) );
            
            if ( fbb != null )
            {
                ImplicitObjectUtil.loadFacesBackingBean( request, fbb );
            }
            else
            {
                ImplicitObjectUtil.unloadFacesBackingBean( request );
            }
        }
    }
    
    public UIViewRoot createView(FacesContext context, String viewId)
    {
        ExternalContext externalContext = context.getExternalContext();
        Object request = externalContext.getRequest();
        HttpServletRequest httpRequest = null;
        
        if ( request instanceof HttpServletRequest )
        {
            //
            // If this is a navigateTo=Jpf.NavigateTo.currentPage or a navigateTo=Jpf.NavigateTo.previousPage,
            // see if we've saved view state from the original page.  If so, just restore that.
            //
            httpRequest = ( HttpServletRequest ) request;
            PageFlowRequestWrapper rw = PageFlowRequestWrapper.unwrap( httpRequest );

            if ( rw != null )
            {
                PreviousPageInfo prevPageInfo = rw.getPreviousPageInfo( true );
                
                if ( prevPageInfo != null )
                {
                    Object clientState = prevPageInfo.getClientState();
                    
                    if ( clientState != null && clientState instanceof PageClientState )
                    {
                        PageClientState pcs = ( PageClientState ) clientState;
                        FacesBackingBean fbb = pcs.getBackingBean();
                        ServletContext servletContext = ( ServletContext ) externalContext.getContext();
                        
                        if ( fbb != null )
                        {
                            HttpServletResponse httpResponse = ( HttpServletResponse ) externalContext.getResponse();
                            fbb.restore( httpRequest, httpResponse, servletContext );
                        }
                        else
                        {
                            InternalUtils.removeCurrentFacesBackingBean( httpRequest, servletContext );
                        }
                        
                        return pcs.getViewRoot();
                    }
                }
            }
            
            //
            // Create/restore the backing bean that corresponds to this request.
            //
            HttpServletResponse response = ( HttpServletResponse ) externalContext.getResponse();
            ServletContext servletContext = ( ServletContext ) externalContext.getContext();
            setBackingBean( httpRequest, response, servletContext );
        }
        
        UIViewRoot viewRoot = _delegate.createView( context, viewId );
        savePreviousPageInfo( httpRequest, externalContext, viewId, viewRoot );
        return viewRoot;
    }

    public String getActionURL(FacesContext context, String viewId)
    {
        return _delegate.getActionURL( context, viewId );
    }

    public String getResourceURL(FacesContext context, String path)
    {
        return _delegate.getResourceURL( context, path );
    }

    public void renderView(FacesContext context, UIViewRoot viewToRender)
        throws IOException, FacesException
    {
        // Create/restore the backing bean that corresponds to this request.
        ExternalContext externalContext = context.getExternalContext();
        Object request = externalContext.getRequest();

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            ServletContext servletContext = (ServletContext) externalContext.getContext();
            setBackingBean(httpRequest, response, servletContext);
        }

        _delegate.renderView(context, viewToRender);
    }

    /**
     * If we are in a request forwarded by {@link PageFlowNavigationHandler}, returns <code>null</code>; otherwise,
     * delegates to the base ViewHandler.
     */ 
    public UIViewRoot restoreView(FacesContext context, String viewId)
    {
        ExternalContext externalContext = context.getExternalContext();
        Object request = externalContext.getRequest();
        HttpServletRequest httpRequest = null;
        
        if ( request instanceof HttpServletRequest )
        {
            httpRequest = ( HttpServletRequest ) request;
            
            //
            // If we did a forward in PageFlowNavigationHandler, don't try to restore the view.
            //
            if ( httpRequest.getAttribute( PageFlowNavigationHandler.ALREADY_FORWARDED_ATTR ) != null )
            {
                return null;
            }
            
            //
            // Create/restore the backing bean that corresponds to this request.
            //
            HttpServletResponse response = ( HttpServletResponse ) externalContext.getResponse();
            ServletContext servletContext = ( ServletContext ) externalContext.getContext();
            setBackingBean( httpRequest, response, servletContext );
            
        }
        
        UIViewRoot viewRoot = _delegate.restoreView( context, viewId );
        savePreviousPageInfo( httpRequest, externalContext, viewId, viewRoot );
        return viewRoot;
    }

    private static void savePreviousPageInfo( HttpServletRequest request, ExternalContext externalContext,
                                              String viewID, UIViewRoot viewRoot )
    {
        //
        // Save the current view state in the PreviousPageInfo structure of the current page flow.
        //
        if ( request != null )
        {
            ServletContext servletContext = ( ServletContext ) externalContext.getContext();
            PageFlowController curPageFlow = PageFlowUtils.getCurrentPageFlow( request, servletContext );
            
            if ( curPageFlow != null && ! curPageFlow.isPreviousPageInfoDisabled() )
            {
                //
                // Only save the previous page info if the JSF view-ID is the same as the current forward path.
                // Note that we strip the file extension from the view-ID -- different JSF implementations give
                // us different things (foo.jsp vs. foo.faces).
                //
                viewID = FileUtils.stripFileExtension( viewID );
                String currentForwardPath = FileUtils.stripFileExtension( curPageFlow.getCurrentForwardPath() );
                if ( viewID.equals( currentForwardPath ) )
                {
                    PreviousPageInfo prevPageInfo = curPageFlow.theCurrentPageInfo();
                    FacesBackingBean backingBean = InternalUtils.getFacesBackingBean( request, servletContext );
                    prevPageInfo.setClientState( new PageClientState( viewRoot, backingBean ) );
                }
            }
        }
    }
    
    public void writeState(FacesContext context) throws IOException
    {
        _delegate.writeState( context );
    }
}
