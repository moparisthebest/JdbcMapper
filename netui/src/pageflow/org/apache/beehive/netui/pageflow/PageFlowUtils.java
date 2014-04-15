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

import org.apache.beehive.netui.core.urls.FreezableMutableURI;
import org.apache.beehive.netui.core.urls.MutableURI;
import org.apache.beehive.netui.core.urls.URIContext;
import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.core.urls.URLType;
import org.apache.beehive.netui.core.urltemplates.URLTemplatesFactory;
import org.apache.beehive.netui.pageflow.internal.ActionResultImpl;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.AdapterManager;
import org.apache.beehive.netui.pageflow.internal.PageFlowRequestWrapper;
import org.apache.beehive.netui.pageflow.internal.URIContextFactory;
import org.apache.beehive.netui.pageflow.internal.DefaultURLTemplatesFactory;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.pageflow.scoping.ScopedResponse;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.handler.StorageHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.internal.FileUtils;
import org.apache.beehive.netui.util.internal.ServletUtils;
import org.apache.beehive.netui.util.internal.concurrent.InternalConcurrentHashMap;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.UrlConfig;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.RequestUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Utility methods related to Page Flow.
 */ 
public class PageFlowUtils
        implements PageFlowConstants, InternalConstants
{
    private static final Logger _log = Logger.getInstance( PageFlowUtils.class );

    private static final String PREVENT_CACHE_ATTR = InternalConstants.ATTR_PREFIX + "preventCache";
    private static final String ACTION_URI_ATTR = ATTR_PREFIX + "_actionURI";
    private static final int    PAGEFLOW_EXTENSION_LEN = PAGEFLOW_EXTENSION.length();
    private static final String DEFAULT_AUTORESOLVE_EXTENSIONS[] = new String[]{ ACTION_EXTENSION, PAGEFLOW_EXTENSION };
    
    /** Map of Struts module prefix to Map of form-type-name to form-name. */
    private static Map/*< String, Map< String, List< String > > >*/ _formNameMaps =
            new InternalConcurrentHashMap/*< String, Map< String, List< String > > >*/();

    /**
     * Get the Struts module path for a URI.  This is the parent directory, relative to the web
     * application root, of the file referenced by the URI.
     * 
     * @param request the current HttpServletRequest.
     * @param requestURI the URI for which to get the Struts module path.
     */
    public static String getModulePath( HttpServletRequest request, String requestURI )
    {
        return getModulePathForRelativeURI( getRelativeURI( request, requestURI, null ) );
    }

    /**
     * Get the Struts module path for the current request URI.  This is the parent directory,
     * relative to the web application root, of the file referenced by the request URI.
     * 
     * @param request the current HttpServletRequest.
     */
    public static String getModulePath( HttpServletRequest request )
    {
        return getModulePathForRelativeURI( InternalUtils.getDecodedServletPath( request ) );
    }

    /**
     * Get the Struts module path for a URI that is relative to the web application root.
     * 
     * @param uri the URI for which to get the module path.
     */
    public static String getModulePathForRelativeURI( String uri )
    {
        if ( uri == null ) return null;
        assert uri.length() > 0;
        assert uri.charAt( 0 ) == '/' : uri;

        // Strip off the actual page name (e.g., some_page.jsp)
        int slash = uri.lastIndexOf( '/' );
        uri = uri.substring( 0, slash );

        return uri;
    }

    /**
     * Get the request URI, relative to the URI of the given PageFlowController.
     *
     * @param request the current HttpServletRequest.
     * @param relativeTo a PageFlowController to which the returned URI should be relative, or
     *        <code>null</code> if the returned URI should be relative to the webapp root.
     */
    public static String getRelativeURI( HttpServletRequest request, PageFlowController relativeTo )
    {
        if ( relativeTo == null ) return InternalUtils.getDecodedServletPath( request );
        return getRelativeURI( request, InternalUtils.getDecodedURI( request ), relativeTo );
    }

    /**
     * Get a URI relative to the URI of the given PageFlowController.
     *
     * @param request the current HttpServletRequest.
     * @param uri the URI which should be made relative.
     * @param relativeTo a PageFlowController to which the returned URI should be relative, or
     *        <code>null</code> if the returned URI should be relative to the webapp root.
     */
    public static String getRelativeURI( HttpServletRequest request, String uri, PageFlowController relativeTo )
    {
        String contextPath = request.getContextPath();
        if ( relativeTo != null ) contextPath += relativeTo.getModulePath();
        int overlap = uri.indexOf( contextPath + '/' );
        if ( overlap == -1 ) return null;
        return uri.substring( overlap + contextPath.length() );
    }

    /**
     * Get a URI for the "begin" action in the PageFlowController associated with the given
     * request URI.
     * 
     * @return a String that is the URI for the "begin" action in the PageFlowController associated
     * with the given request URI.
     */
    public static String getBeginActionURI( String requestURI )
    {
        // Translate this to a request for the begin action ("begin.do") for this PageFlowController.
        InternalStringBuilder retVal = new InternalStringBuilder();
        int lastSlash = requestURI.lastIndexOf( '/' );

        if ( lastSlash != -1 )
        {
            retVal.append( requestURI.substring( 0, lastSlash ) );
        }

        retVal.append( '/' ).append( BEGIN_ACTION_NAME ).append( ACTION_EXTENSION );
        return retVal.toString();
    }

    /**
     * Get the stack of nested page flows for the current user session.  Create and store an empty
     * stack if none exists.
     * 
     * @deprecated Use {@link PageFlowStack#get(HttpServletRequest, ServletContext)} instead.
     * 
     * @param request the current HttpServletRequest
     * @return a {@link PageFlowStack} of nested page flows ({@link PageFlowController}s) for the current user session.
     */
    public static Stack getPageFlowStack( HttpServletRequest request )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        return PageFlowStack.get( request, servletContext, true ).getLegacyStack();
    }
    
    /**
     * Destroys the stack of {@link PageFlowController}s that have invoked nested page flows.
     * 
     * @deprecated Use {@link PageFlowStack#destroy} instead.
     * 
     * @param request the current HttpServletRequest.
     */ 
    public static void destroyPageFlowStack( HttpServletRequest request )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        PageFlowStack.get( request, servletContext ).destroy( request );
    }
    
    /**
     * Get the {@link PageFlowController} that is nesting the current one.
     * @deprecated Use {@link #getNestingPageFlow(HttpServletRequest, ServletContext)} instead.
     * 
     * @param request the current HttpServletRequest.
     * @return the nesting {@link PageFlowController}, or <code>null</code> if the current one
     *         is not being nested.
     */ 
    public static PageFlowController getNestingPageFlow( HttpServletRequest request )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        return getNestingPageFlow( request, servletContext );
    }
    
    /**
     * Get the {@link PageFlowController} that is nesting the current one.
     * 
     * @param request the current HttpServletRequest.
     * @param servletContext the current ServletContext.
     * @return the nesting {@link PageFlowController}, or <code>null</code> if the current one
     *         is not being nested.
     */ 
    public static PageFlowController getNestingPageFlow( HttpServletRequest request, ServletContext servletContext )
    {
        PageFlowStack jpfStack = PageFlowStack.get( request, servletContext, false );
        
        if ( jpfStack != null && ! jpfStack.isEmpty() )
        {
            PageFlowController top = jpfStack.peek().getPageFlow();
            return top;
        }
        
        return null;
    }

    /**
     * Get the current {@link PageFlowController}.
     * 
     * @param request the current HttpServletRequest.
     * @param servletContext the current ServletContext.
     * @return the current PageFlowController from the user session, or <code>null</code> if there is none.
     */ 
    public static PageFlowController getCurrentPageFlow( HttpServletRequest request, ServletContext servletContext )
    {
        ActionResolver cur = getCurrentActionResolver( request, servletContext );

        if (cur != null && cur.isPageFlow()) {
            PageFlowController pfc = (PageFlowController) cur;
            pfc.reinitializeIfNecessary(request, null, servletContext);
            return pfc;
        }

        return null;
    }
    
    /**
    /**
     * Get the current PageFlowController.
     * @deprecated Use {@link #getCurrentPageFlow(HttpServletRequest, ServletContext)} instead.
     * 
     * @param request the current HttpServletRequest.
     * @return the current PageFlowController from the user session, or <code>null</code>
     *         if there is none.
     */ 
    public static PageFlowController getCurrentPageFlow( HttpServletRequest request )
    {
        ActionResolver cur = getCurrentActionResolver( request );
        return cur != null && cur.isPageFlow() ? ( PageFlowController ) cur : null;
    }
    
    /**
     * Get the current ActionResolver.
     * @deprecated Use {@link #getCurrentPageFlow(HttpServletRequest, ServletContext)} instead.
     * 
     * @return the current ActionResolver from the user session, or <code>null</code> if there is none.
     */ 
    public static ActionResolver getCurrentActionResolver( HttpServletRequest request )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        return getCurrentActionResolver( request, servletContext );
    }
    
    /**
     * Get the current ActionResolver.
     * @deprecated Use {@link #getCurrentPageFlow(HttpServletRequest, ServletContext)} instead.
     * 
     * 
     * @param request the current HttpServletRequest.
     * @param servletContext the current ServletContext.
     * @return the current ActionResolver from the user session, or <code>null</code> if there is none.
     */ 
    public static ActionResolver getCurrentActionResolver( HttpServletRequest request, ServletContext servletContext )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        
        //
        // First see if the current page flow is a long-lived, which is stored in its own attribute.
        //
        String currentLongLivedAttrName =
                ScopedServletUtils.getScopedSessionAttrName( CURRENT_LONGLIVED_ATTR, unwrappedRequest );
        String currentLongLivedModulePath = ( String ) sh.getAttribute( rc, currentLongLivedAttrName );
        
        if ( currentLongLivedModulePath != null )
        {
            return getLongLivedPageFlow( currentLongLivedModulePath, unwrappedRequest );
        }
        else
        {
            String currentJpfAttrName = ScopedServletUtils.getScopedSessionAttrName( CURRENT_JPF_ATTR, unwrappedRequest );
            return ( ActionResolver ) sh.getAttribute( rc, currentJpfAttrName );
        }
    }
    
    /**
     * Get the current {@link GlobalApp} instance.
     * 
     * @deprecated Use {@link #getSharedFlow} instead.
     * @param request the current HttpServletRequest.
     * @return the current {@link GlobalApp} from the user session, or <code>null</code> if none
     *         exists.
     */ 
    public static GlobalApp getGlobalApp( HttpServletRequest request )
    {
        SharedFlowController sf = getSharedFlow( InternalConstants.GLOBALAPP_CLASSNAME, request );
        return sf instanceof GlobalApp ? ( GlobalApp ) sf : null;
    }

    /**
     * Get the a map of shared flow name to shared flow instance, based on the names defined in the
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#sharedFlowRefs sharedFlowRefs} attribute
     * of the {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller} annotation on the
     * <strong>current page flow</strong>.
     * 
     * @param request the current HttpServletRequest, which is used to determine the current page flow.
     * @return a Map of shared flow name (string) to shared flow instance ({@link SharedFlowController}).
     */ 
    public static Map/*< String, SharedFlowController >*/ getSharedFlows( HttpServletRequest request )
    {
        Map/*< String, SharedFlowController >*/ sharedFlows = ImplicitObjectUtil.getSharedFlow( request );
        return sharedFlows != null ? sharedFlows : Collections.EMPTY_MAP;
    }
    
    /**
     * Get the shared flow with the given class name.
     * @deprecated Use {@link #getSharedFlow(String, HttpServletRequest, ServletContext)} instead.
     * 
     * @param sharedFlowClassName the class name of the shared flow to retrieve.
     * @param request the current HttpServletRequest.
     * @return the {@link SharedFlowController} of the given class name which is stored in the user session.
     */ 
    public static SharedFlowController getSharedFlow( String sharedFlowClassName, HttpServletRequest request )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        return getSharedFlow( sharedFlowClassName, request, servletContext );
    }
    
    /**
     * Get the shared flow with the given class name.
     * 
     * @param sharedFlowClassName the class name of the shared flow to retrieve.
     * @param request the current HttpServletRequest.
     * @return the {@link SharedFlowController} of the given class name which is stored in the user session.
     */ 
    public static SharedFlowController getSharedFlow( String sharedFlowClassName, HttpServletRequest request,
                                                      ServletContext servletContext )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName(InternalConstants.SHARED_FLOW_ATTR_PREFIX
                                                                      + sharedFlowClassName, request);
        SharedFlowController sf = (SharedFlowController) sh.getAttribute(rc, attrName);
        if (sf != null) {
            sf.reinitializeIfNecessary(request, null, servletContext);
        }
        return sf;
    }
    
    /**
     * Destroy the current {@link SharedFlowController} of the given class name.
     * @deprecated Use {@link #removeSharedFlow(String, HttpServletRequest, ServletContext)} instead.
     * @param sharedFlowClassName the class name of the current SharedFlowController to destroy.
     * @param request the current HttpServletRequest.
     */ 
    public static void removeSharedFlow( String sharedFlowClassName, HttpServletRequest request )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        removeSharedFlow( sharedFlowClassName, request, servletContext );
    }
    
    /**
     * Destroy the current {@link SharedFlowController} of the given class name.
     * @param sharedFlowClassName the class name of the current SharedFlowController to destroy.
     * @param request the current HttpServletRequest.
     */ 
    public static void removeSharedFlow( String sharedFlowClassName, HttpServletRequest request,
                                         ServletContext servletContext )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName(InternalConstants.SHARED_FLOW_ATTR_PREFIX
                                                                      + sharedFlowClassName, request);
        sh.removeAttribute(rc, attrName);
    }
    
    /**
     * Remove a "long-lived" page flow from the session. Once it is created, a long-lived page flow
     * is never removed from the session unless this method or {@link PageFlowController#remove} is
     * called.  Navigating to another page flow hides the current long-lived controller, but does not
     * remove it.
     * @deprecated Use {@link #removeLongLivedPageFlow(String, HttpServletRequest, ServletContext)} instead.
     */
    public static void removeLongLivedPageFlow( String modulePath, HttpServletRequest request )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        removeLongLivedPageFlow( modulePath, request, servletContext );
    }
    
    /**
     * Remove a "long-lived" page flow from the session. Once it is created, a long-lived page flow
     * is never removed from the session unless this method or {@link PageFlowController#remove} is
     * called.  Navigating to another page flow hides the current long-lived controller, but does not
     * remove it.
     */
    public static void removeLongLivedPageFlow( String modulePath, HttpServletRequest request,
                                                ServletContext servletContext )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        
        String attrName = InternalUtils.getLongLivedFlowAttr( modulePath );
        attrName = ScopedServletUtils.getScopedSessionAttrName( attrName, unwrappedRequest );
        sh.removeAttribute( rc, attrName );

        //
        // Now, if the current page flow is long-lived, remove the reference.
        //
        String currentLongLivedAttrName =
                ScopedServletUtils.getScopedSessionAttrName( CURRENT_LONGLIVED_ATTR, unwrappedRequest );
        String currentLongLivedModulePath =
                ( String ) sh.getAttribute( rc, currentLongLivedAttrName );
        
        if ( modulePath.equals( currentLongLivedModulePath ) )
        {
            sh.removeAttribute( rc, currentLongLivedAttrName );
        }
    }
    
    /**
     * Get the long-lived page flow instance associated with the given module (directory) path.
     * @deprecated Use {@link #getLongLivedPageFlow(String, HttpServletRequest, ServletContext)} instead.
     * 
     * @param modulePath the webapp-relative path to the directory containing the long-lived page flow.
     * @param request the current HttpServletRequest.
     * @return the long-lived page flow instance associated with the given module, or <code>null</code> if none is found.
     */ 
    public static PageFlowController getLongLivedPageFlow( String modulePath, HttpServletRequest request )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        return getLongLivedPageFlow( modulePath, request, servletContext );
    }
    
    /**
     * Get the long-lived page flow instance associated with the given module (directory) path.
     * 
     * @param modulePath the webapp-relative path to the directory containing the long-lived page flow.
     * @param request the current HttpServletRequest.
     * @param servletContext the current ServletContext.
     * @return the long-lived page flow instance associated with the given module, or <code>null</code> if none is found.
     */ 
    public static PageFlowController getLongLivedPageFlow( String modulePath, HttpServletRequest request,
                                                           ServletContext servletContext )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        
        String attrName = InternalUtils.getLongLivedFlowAttr( modulePath );
        attrName = ScopedServletUtils.getScopedSessionAttrName( attrName, unwrappedRequest );
        PageFlowController retVal =  ( PageFlowController ) sh.getAttribute( rc, attrName );
        return retVal;
    }
    
    /**
     * Make any form beans in the given {@link Forward} object available as attributets in the
     * request/session (as appropriate).
     * 
     * @param mapping the ActionMapping for the current Struts action being processed.
     * @param fwd the {@link Forward} object that contains the ActionForm instances to be
     *            made available in the request/session (as appropriate).
     * @param request the current HttpServletRequest.
     * @param overwrite if <code>false</code> a form from <code>fwd</code> will only be set
     *            in the request if there is no existing form with the same name.
     */
    public static void setOutputForms( ActionMapping mapping, Forward fwd, HttpServletRequest request, 
                                       boolean overwrite )
    {
        if ( fwd == null ) return;
        
        //
        // *If* there is a target action mapping, set output forms for it.
        //
        if ( mapping != null ) setOutputForms( mapping, fwd.getOutputForms(), request, overwrite );
        
        InternalUtils.setForwardedFormBean( request, fwd.getFirstOutputForm( request ) );
    }
    
    /**
     * Make any form beans in the given {@link Forward} object available as attributets in the
     * request/session (as appropriate).
     * 
     * @param mapping the ActionMapping for the current Struts action being processed.
     * @param fwd the {@link Forward} object that contains the ActionForm instances to be
     *            made available in the request/session (as appropriate).
     * @param request the current HttpServletRequest.
     */
    public static void setOutputForms( ActionMapping mapping, Forward fwd, HttpServletRequest request )
    {
        if ( fwd == null )
        {
            return;
        }

        if ( mapping != null )
        {
            setOutputForms( mapping, fwd.getOutputForms(), request );
        }

        InternalUtils.setForwardedFormBean( request, fwd.getFirstOutputForm( request ) );
    }

    /**
     * Make a set of form beans available as attributets in the request/session (as appropriate).
     * 
     * @param mapping the ActionMapping for the current Struts action being processed.
     * @param outputForms an array of ActionForm instances to be made available in the
     *            request/session (as appropriate).
     * @param request the current HttpServletRequest.
     */
    public static void setOutputForms( ActionMapping mapping, ActionForm[] outputForms,
                                       HttpServletRequest request )
    {
        setOutputForms( mapping, outputForms, request, true );
    }
    
    /**
     * Make a set of form beans available as attributets in the request/session (as appropriate).
     * 
     * @param mapping the ActionMapping for the current Struts action being processed.
     * @param outputForms an array of ActionForm instances to be made available in the
     *            request/session (as appropriate).
     * @param overwrite if <code>false</code> a form from <code>fwd</code> will only be set
     *            in the request if there is no existing form with the same name.
     * @param request the current HttpServletRequest.
     */
    public static void setOutputForms( ActionMapping mapping, ActionForm[] outputForms,
                                       HttpServletRequest request, boolean overwrite )
    {
        try
        {
            //
            // Now set any output forms in the request or session, as appropriate.
            //
            assert mapping.getScope() == null
                    || mapping.getScope().equals( "request" )
                    || mapping.getScope().equals( "session" )
                : mapping.getScope();

            
            for ( int i = 0; i < outputForms.length; ++i )
            {
                setOutputForm( mapping, outputForms[i], request, overwrite );
            }
        }
        catch ( Exception e )
        {
            _log.error( "Error while setting Struts form-beans", e );
        }
    }

    private static List/*< String >*/ getFormNamesFromModuleConfig( String formBeanClassName, ModuleConfig moduleConfig )
    {
        String modulePrefix = moduleConfig.getPrefix();
        Map/*< String, List< String > >*/ formNameMap = ( Map ) _formNameMaps.get( modulePrefix );  // map of form-type-name to form-name
        
        if ( formNameMap == null )
        {
            formNameMap = new HashMap/*< String, List< String > >*/();
            FormBeanConfig[] formBeans = moduleConfig.findFormBeanConfigs();
            
            for ( int j = 0; j < formBeans.length; ++j )
            {
                assert formBeans[j] != null;
                String formBeanType = InternalUtils.getFormBeanType( formBeans[j] );
                List/*< String >*/ formBeanNames = ( List ) formNameMap.get( formBeanType );
                
                if ( formBeanNames == null )
                {
                    formBeanNames = new ArrayList/*< String >*/();
                    formNameMap.put( formBeanType, formBeanNames );
                }
                
                formBeanNames.add( formBeans[j].getName() );
            }
            
            _formNameMaps.put( modulePrefix, formNameMap );
        }
        
        return ( List ) formNameMap.get( formBeanClassName );
    }
    
    /**
     * Make a form bean available as an attributet in the request/session (as appropriate).
     * 
     * @param mapping the ActionMapping for the current Struts action being processed.
     * @param form an ActionForm instance to be made available in the request/session
     *           (as appropriate).
     * @param overwrite if <code>false</code> a form from <code>fwd</code> will only be set
     *           in the request if there is no existing form with the same name.
     * @param request the current HttpServletRequest.
     */
    public static void setOutputForm( ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, boolean overwrite )
    {
        if ( form != null )
        {
            ModuleConfig moduleConfig = mapping.getModuleConfig();
            Class formClass = InternalUtils.unwrapFormBean( form ).getClass();
            
            //
            // Get the names of *all* form beans of the desired type, and blast out this instance under all those names.
            //
            List formNames = getFormNamesFromModuleConfig( formClass.getName(), moduleConfig );
            List additionalFormNames = null;
            
            //
            // formNames is a statically-scoped list.  Below, we create a dynamic list of form names that correspond
            // to *implemented interfaces* of the given form bean class.
            //
            Class[] interfaces = formClass.getInterfaces();
            for ( int i = 0; i < interfaces.length; i++ )
            {
                Class formInterface = interfaces[i];
                List toAdd = getFormNamesFromModuleConfig( formInterface.getName(), moduleConfig );
                if ( toAdd != null )
                {
                    if ( additionalFormNames == null ) additionalFormNames = new ArrayList();
                    additionalFormNames.addAll( toAdd );
                }
            }
            
            // Do the same for all superclasses of the form bean class.
            for (Class i = formClass.getSuperclass(); i != null; i = i.getSuperclass()) {
                List toAdd = getFormNamesFromModuleConfig( i.getName(), moduleConfig );
                if ( toAdd != null )
                {
                    if ( additionalFormNames == null ) additionalFormNames = new ArrayList();
                    additionalFormNames.addAll( toAdd );
                }
            }
            
            if ( formNames == null && additionalFormNames == null )
            {
                String formName = generateFormBeanName( formClass, request );
                InternalUtils.setFormInScope( formName, form, mapping, request, overwrite );
            }
            else
            {
                if ( formNames != null )
                {
                    for ( Iterator i = formNames.iterator(); i.hasNext(); )  
                    {
                        String formName = ( String ) i.next();
                        InternalUtils.setFormInScope( formName, form, mapping, request, overwrite );
                    }
                }
                
                if ( additionalFormNames != null )
                {
                    for ( Iterator i = additionalFormNames.iterator(); i.hasNext(); )  
                    {
                        String formName = ( String ) i.next();
                        InternalUtils.setFormInScope( formName, form, mapping, request, overwrite );
                    }
                }
            }
        }
    }
    
    /**
     * Get the name for the type of a ActionForm instance.  Use a name looked up from
     * the current Struts module, or, if none is found, create one.
     * 
     * @param formInstance the ActionForm instance whose type will determine the name.
     * @param request the current HttpServletRequest, which contains a reference to the current Struts module.
     * @return the name found in the Struts module, or, if none is found, a name that is either:
     *     <ul>
     *         <li>a camel-cased version of the base class name (minus any package or outer-class
     *             qualifiers, or, if that name is already taken,</li>
     *         <li>the full class name, with '.' and '$' replaced by '_'.</li>
     *     </ul>
     */ 
    public static String getFormBeanName( ActionForm formInstance, HttpServletRequest request )
    {
        return getFormBeanName( formInstance.getClass(), request );
    }

    /**
     * Get the name for an ActionForm type.  Use a name looked up from the current Struts module, or,
     * if none is found, create one.
     * 
     * @param formBeanClass the ActionForm-derived class whose type will determine the name.
     * @param request the current HttpServletRequest, which contains a reference to the current Struts module.
     * @return the name found in the Struts module, or, if none is found, a name that is either:
     *     <ul>
     *         <li>a camel-cased version of the base class name (minus any package or outer-class
     *             qualifiers, or, if that name is already taken,</li>
     *         <li>the full class name, with '.' and '$' replaced by '_'.</li>
     *     </ul>
     */ 
    public static String getFormBeanName( Class formBeanClass, HttpServletRequest request )
    {
        ModuleConfig moduleConfig = RequestUtils.getRequestModuleConfig( request );
        List/*< String >*/ names = getFormNamesFromModuleConfig( formBeanClass.getName(), moduleConfig );
        
        if ( names != null )
        {
            assert names.size() > 0;    // getFormNamesFromModuleConfig returns null or a nonempty list
            return ( String ) names.get( 0 );
        }
        
        return generateFormBeanName( formBeanClass, request );
    }

    /**
     * Create the name for a form bean type.
     * 
     * @param formBeanClass the class whose type will determine the name.
     * @param request the current HttpServletRequest, which contains a reference to the current Struts module.
     * @return the name found in the Struts module, or, if none is found, a name that is either:
     *     <ul>
     *         <li>a camel-cased version of the base class name (minus any package or outer-class
     *             qualifiers, or, if that name is already taken,</li>
     *         <li>the full class name, with '.' and '$' replaced by '_'.</li>
     *     </ul>
     */ 
    private static String generateFormBeanName( Class formBeanClass, HttpServletRequest request )
    {
        ModuleConfig moduleConfig = RequestUtils.getRequestModuleConfig( request );
        String formBeanClassName = formBeanClass.getName();
        
        //
        // A form-bean wasn't found for this type, so we'll create a name.  First try and create
        // name that is a camelcased version of the classname without all of its package/outer-class
        // qualifiers.  If one with that name already exists, munge the fully-qualified classname.
        //
        String formType = formBeanClassName;
        int lastQualifier = formType.lastIndexOf( '$' );

        if ( lastQualifier == -1 )
        {
            lastQualifier = formType.lastIndexOf( '.' );
        }

        String formName = formType.substring( lastQualifier + 1 );
        formName = Character.toLowerCase( formName.charAt( 0 ) ) + formName.substring( 1 );

        if ( moduleConfig.findFormBeanConfig( formName ) != null )
        {
            formName = formType.replace( '.', '_' ).replace( '$', '_' );
            assert moduleConfig.findFormBeanConfig( formName ) == null : formName;
        }
        
        return formName;        
    }
    
    /**
     * Get the class name of a {@link PageFlowController}, given the URI to it.
     * 
     * @param uri the URI to the {@link PageFlowController}, which should be relative to the
     *            web application root (i.e., it should not include the context path).
     */
    public static String getPageFlowClassName( String uri )
    {
        assert uri != null;
        assert uri.length() > 0;
        
        if ( uri.charAt( 0 ) == '/' ) uri = uri.substring( 1 );
        
        assert FileUtils.osSensitiveEndsWith( uri, PAGEFLOW_EXTENSION ) : uri;
        if ( FileUtils.osSensitiveEndsWith( uri, PAGEFLOW_EXTENSION ) ) 
        {
            uri = uri.substring( 0, uri.length() - PAGEFLOW_EXTENSION_LEN );
        }
        
        return uri.replace( '/', '.' );
    }
    
    /**
     * Get the class name of a {@link PageFlowController}, given the URI to it.
     * 
     * @deprecated Use {@link #getPageFlowClassName(String)} instead.
     * 
     * @param uri the URI to the {@link PageFlowController}, which should be relative to the
     *            web application root (i.e., it should not include the context path).
     */
    public static String getJpfClassName( String uri )
    {
        return getPageFlowClassName( uri );
    }
    
    /**
     * Get the URI for a {@link PageFlowController}, given its class name.
     * 
     * @param className the name of the {@link PageFlowController} class.
     * @return a String that is the URI for the {@link PageFlowController}, relative to the web
     *         application root (i.e., not including the context path).
     */
    public static String getPageFlowURI( String className )
    {
        return '/' + className.replace( '.', '/' ) + PAGEFLOW_EXTENSION;
    }
    
    /**
     * @deprecated Use {@link PageFlowActionServlet#getModuleConfPath} instead.
     * 
     * Get the path to the Struts module configration file (e.g.,
     * "/WEB-INF/classes/_pageflow/struts-config-someModule") for a given module
     * path (e.g., "someModule"), according to the PageFlow convention.
     * 
     * @param modulePath the Struts module path.
     * @return a String that is the path to the Struts configuration file, relative to the
     *         web application root.
     */ 
    public static String getModuleConfPath( String modulePath )
    {
        return new PageFlowActionServlet.DefaultModuleConfigLocator().getModuleConfigPath( modulePath );
    }
    

    /**
     * Get the most recent action URI that was processed by {@link FlowController#execute}.
     * 
     * @param request the current ServletRequest.
     * @return a String that is the most recent action URI.  This is only valid during a request
     *         that has been forwarded from the action URI.
     */ 
    public static String getActionURI( ServletRequest request )
    {
        return ( String ) request.getAttribute( ACTION_URI_ATTR );
    }
    
    /**
     * Sets the most recent action URI that was processed by {@link FlowController#execute}.
     */ 
    static void setActionURI( HttpServletRequest request )
    {
        request.setAttribute( ACTION_URI_ATTR, InternalUtils.getDecodedURI( request ) );
    }
    
    /**
     * Tell whether a web application resource requires a secure transport protocol.  This is
     * determined from web.xml; for example, the following block specifies that all resources under
     * /login require a secure transport protocol.
     * <pre>
     *    &lt;security-constraint&gt;
     *        &lt;web-resource-collection&gt;
     *          &lt;web-resource-name&gt;Secure PageFlow - begin&lt;/web-resource-name&gt; 
     *          &lt;url-pattern&gt;/login/*&lt;/url-pattern&gt;
     *        &lt;/web-resource-collection&gt;
     *        &lt;user-data-constraint&gt;
     *           &lt;transport-guarantee&gt;CONFIDENTIAL&lt;/transport-guarantee&gt;
     *        &lt;/user-data-constraint&gt;
     *    &lt;/security-constraint&gt;
     * </pre>
     * 
     * @param uri a webapp-relative URI for a resource.  There must not be query parameters or a scheme
     *            on the URI.
     * @param request the current request.
     * @return <code>Boolean.TRUE</code> if a transport-guarantee of <code>CONFIDENTIAL</code> or
     *         <code>INTEGRAL</code> is associated with the given resource; <code>Boolean.FALSE</code> 
     *         a transport-guarantee of <code>NONE</code> is associated with the given resource; or
     *         <code>null</code> if there is no transport-guarantee associated with the given resource.
     */ 
    public static SecurityProtocol getSecurityProtocol( String uri, ServletContext servletContext,
                                                        HttpServletRequest request )
    {
        return AdapterManager.getServletContainerAdapter( servletContext ).getSecurityProtocol( uri, request );
    }
    
    /**
     * @deprecated Use {@link #getSecurityProtocol(String, ServletContext, HttpServletRequest)} instead.
     */ 
    public static Boolean isSecureResource( String uri, ServletContext context )
    {
        // TODO: once DefaultServletContainerAdapter has this functionality, delegate to it.
        return null;
    }

    /**
     * Set a named action output, which corresponds to an input declared by the <code>pageInput</code> JSP tag.
     * The actual value can be read from within a JSP using the <code>"pageInput"</code> databinding context.
     * 
     * @deprecated Use {@link #addActionOutput} instead.
     * @param name the name of the action output.
     * @param value the value of the action output.
     * @param request the current ServletRequest.
     */
    public static void addPageInput( String name, Object value, ServletRequest request )
    {
        addActionOutput( name, value, request );
    }
    
    /**
     * Set a named action output, which corresponds to an input declared by the <code>pageInput</code> JSP tag.
     * The actual value can be read from within a JSP using the <code>"pageInput"</code> databinding context.
     * 
     * @param name the name of the action output.
     * @param value the value of the action output.
     * @param request the current ServletRequest.
     */
    public static void addActionOutput( String name, Object value, ServletRequest request )
    {
        Map map = InternalUtils.getActionOutputMap( request, true );
        
        if ( map.containsKey( name ) )
        {
            if ( _log.isWarnEnabled() )
            {
                _log.warn( "Overwriting action output\"" + name + "\"." );
            }
        }
        
        map.put( name, value );
    }
    
    /**
     * Get a named action output that was registered in the current request.
     * 
     * @deprecated Use {@link #getActionOutput} instead.
     * @param name the name of the action output.
     * @param request the current ServletRequest
     * @see #addActionOutput
     */ 
    public static Object getPageInput( String name, ServletRequest request )
    {
        return getActionOutput( name, request );
    }
    
    /**
     * Get a named action output that was registered in the current request.
     * 
     * @param name the name of the action output.
     * @param request the current ServletRequest
     * @see #addActionOutput
     */ 
    public static Object getActionOutput( String name, ServletRequest request )
    {
        Map map = InternalUtils.getActionOutputMap( request, false );
        return map != null ? map.get( name ) : null;
    }
    
    /**
     * Add a validation error that will be shown with the Errors and Error tags.
     * @deprecated Use {@link #addActionError(ServletRequest, String, String, Object[])} instead.
     * 
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the error message.
     * @param messageArgs an array of arguments for the error message.
     * @param request the current ServletRequest.
     */ 
    public static void addValidationError( String propertyName, String messageKey, Object[] messageArgs,
                                           ServletRequest request )
    {
        InternalUtils.addActionError( propertyName, new ActionMessage( messageKey, messageArgs ), request );
    }
    
    
    /**
     * Add a validation error that will be shown with the Errors and Error tags.
     * @deprecated Use {@link #addActionError(ServletRequest, String, String, Object[])} instead.
     * 
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the error message.
     * @param messageArg an argument for the error message.
     * @param request the current ServletRequest.
     */ 
    public static void addValidationError( String propertyName, String messageKey, Object messageArg,
                                           ServletRequest request )
    {
        addActionError( request, propertyName, messageKey, new Object[]{ messageArg } );
    }
    
    /**
     * Add a validation error that will be shown with the Errors and Error tags.
     * @deprecated Use {@link #addActionError(ServletRequest, String, String, Object[])} instead.
     * 
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the error message.
     * @param request the current ServletRequest.
     */ 
    public static void addValidationError( String propertyName, String messageKey, ServletRequest request )
    {
        addActionError( request, propertyName, messageKey );
    }
    
    /**
     * Add a property-related message that will be shown with the Errors and Error tags.
     * 
     * @param request the current ServletRequest.
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the message.
     * @param messageArgs zero or more arguments to the message.
     */ 
    public static void addActionError( ServletRequest request, String propertyName, String messageKey,
                                       Object[] messageArgs )
    {
        InternalUtils.addActionError( propertyName, new ActionMessage( messageKey, messageArgs ), request );
    }
    
    /**
     * Add a property-related message that will be shown with the Errors and Error tags.
     * 
     * @param request the current ServletRequest.
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the message.
     */ 
    public static void addActionError( ServletRequest request, String propertyName, String messageKey )
    {
        InternalUtils.addActionError( propertyName, new ActionMessage( messageKey, null ), request );
    }
    
    /**
     * Add a property-related message that will be shown with the Errors and Error tags.
     * 
     * @param request the current ServletRequest.
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the message.
     * @param messageArg an argument to the message
     */ 
    public static void addActionError( ServletRequest request, String propertyName, String messageKey,
                                       Object messageArg )
    {
        Object[] messageArgs = new Object[]{ messageArg };
        InternalUtils.addActionError( propertyName, new ActionMessage( messageKey, messageArgs ), request );
    }
    
    /**
     * Add a property-related message that will be shown with the Errors and Error tags.
     * 
     * @param request the current ServletRequest.
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the message.
     * @param messageArg1 the first argument to the message
     * @param messageArg2 the second argument to the message
     */ 
    public static void addActionError( ServletRequest request, String propertyName, String messageKey,
                                       Object messageArg1, Object messageArg2 )
    {
        Object[] messageArgs = new Object[]{ messageArg1, messageArg2 };
        InternalUtils.addActionError( propertyName, new ActionMessage( messageKey, messageArgs ), request );
    }
    
    /**
     * Add a property-related message that will be shown with the Errors and Error tags.
     * 
     * @param request the current ServletRequest.
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the message.
     * @param messageArg1 the first argument to the message
     * @param messageArg2 the second argument to the message
     * @param messageArg3 the third argument to the message
     */ 
    public static void addActionError( ServletRequest request, String propertyName, String messageKey,
                                       Object messageArg1, Object messageArg2, Object messageArg3 )
    {
        Object[] messageArgs = new Object[]{ messageArg1, messageArg2, messageArg3 };
        InternalUtils.addActionError( propertyName, new ActionMessage( messageKey, messageArgs ), request );
    }
    
    /**
     * Add a property-related message as an expression that will be evaluated and shown with the Errors and Error tags.
     * 
     * @param request the current ServletRequest.
     * @param propertyName the name of the property with which to associate this error.
     * @param expression the JSP 2.0-style expression (e.g., <code>${pageFlow.myProperty}</code>) or literal string
     *            that will be used as the message.
     * @param messageArgs zero or more arguments to the message.
     */ 
    public static void addActionErrorExpression( HttpServletRequest request, String propertyName, String expression,
                                                 Object[] messageArgs )
    {
        ExpressionMessage msg = new ExpressionMessage( expression, messageArgs );
        InternalUtils.addActionError( propertyName, msg, request );
    }
    
    /**
     * Resolve the given action to a URI by running an entire request-processing cycle on the given ScopedRequest
     * and ScopedResponse.
     * @exclude
     * 
     * @param context the current ServletContext
     * @param request the ServletRequest, which must be a {@link ScopedRequest}.
     * @param response the ServletResponse, which must be a {@link ScopedResponse}.
     * @param actionOverride if not <code>null</code>, this qualified action-path is used to construct an action
     *                       URI which is set as the request URI.  The action-path <strong>must</strong> begin with '/',
     *                       which makes it qualified from the webapp root.
     * @param autoResolveExtensions a list of URI extensions (e.g., ".do", ".jpf") that will be auto-resolved, i.e.,
     *                              on which this method will be recursively called.  If <code>null</code>, the
     *                              default extensions ".do" and ".jpf" will be used.
     */
    public static ActionResult strutsLookup( ServletContext context, ServletRequest request,
                                             HttpServletResponse response, String actionOverride,
                                             String[] autoResolveExtensions )
        throws Exception
    {
        ScopedRequest scopedRequest = ScopedServletUtils.unwrapRequest( request );
        ScopedResponse scopedResponse = ScopedServletUtils.unwrapResponse( response );
        assert scopedRequest != null : request.getClass().getName();
        assert scopedResponse != null : response.getClass().getName();
        assert request instanceof HttpServletRequest : request.getClass().getName();
        
        if ( scopedRequest == null )
        {
            throw new IllegalArgumentException( "request must be of type " + ScopedRequest.class.getName() );
        }
        if ( scopedResponse == null )
        {
            throw new IllegalArgumentException( "response must be of type " + ScopedResponse.class.getName() );
        }
        
        ActionServlet as = InternalUtils.getActionServlet( context );
        
        if ( actionOverride != null )
        {
            // The action must be fully-qualified with its module path.
            assert actionOverride.charAt( 0 ) == '/' : actionOverride;
            InternalStringBuilder uri = new InternalStringBuilder( scopedRequest.getContextPath() );
            uri.append( actionOverride );
            uri.append( PageFlowConstants.ACTION_EXTENSION );
            scopedRequest.setRequestURI( uri.toString() );
        }

        //
        // In case the request was already forwarded once, clear out the recorded forwarded-URI.  This
        // will allow us to tell whether processing the request actually forwarded somewhere.
        //
        scopedRequest.setForwardedURI( null );
        
        //
        // Now process the request.  We create a PageFlowRequestWrapper for pageflow-specific request-scoped info.
        //
        PageFlowRequestWrapper wrappedRequest = PageFlowRequestWrapper.wrapRequest( ( HttpServletRequest ) request );
        wrappedRequest.setScopedLookup( true );

        if (as != null)
        {
            as.doGet( wrappedRequest, scopedResponse );  // this just calls process() -- same as doPost()
        }
        else
        {
            // The normal servlet initialization has not completed yet
            // so rather than call doGet() directly, aquire the request
            // dispatcher from the unwrapped outer request and call
            // forward to trigger the servlet initialization.
            HttpServletRequest req = scopedRequest.getOuterRequest();
            RequestDispatcher rd = req.getRequestDispatcher(scopedRequest.getRequestURI());
            rd.forward(wrappedRequest, scopedResponse);
        }

        String returnURI;

        if ( ! scopedResponse.didRedirect() )
        {
            returnURI = scopedRequest.getForwardedURI();
            
            if ( autoResolveExtensions == null )
            {
                autoResolveExtensions = DEFAULT_AUTORESOLVE_EXTENSIONS;
            }
            
            if ( returnURI != null )
            {
                for ( int i = 0; i < autoResolveExtensions.length; ++i )
                {
                    if ( FileUtils.uriEndsWith( returnURI, autoResolveExtensions[i] ) )
                    {
                        scopedRequest.doForward();
                        return strutsLookup( context, wrappedRequest, scopedResponse, null, autoResolveExtensions );
                    }
                }
            }
        }
        else
        {
            returnURI = scopedResponse.getRedirectURI();
        }
        
        RequestContext requestContext = new RequestContext( scopedRequest, scopedResponse );
        Handlers.get( context ).getStorageHandler().applyChanges( requestContext );
        
        if ( returnURI != null )
        {
            return new ActionResultImpl( returnURI, scopedResponse.didRedirect(), scopedResponse.getStatusCode(),
                                         scopedResponse.getStatusMessage(), scopedResponse.isError() );
        }
        else
        {
            return null;
        }
    }
    
    /**
     * If the given request is a MultipartRequestWrapper (Struts class that doesn't extend
     * HttpServletRequestWrapper), return the wrapped request; otherwise, return the given request.
     * @exclude
     */ 
    public static HttpServletRequest unwrapMultipart( HttpServletRequest request )
    {
        if ( request instanceof MultipartRequestWrapper )
        {
            request = ( ( MultipartRequestWrapper ) request ).getRequest();
        }
        
        return request;
    }
    
    /**
     * Get or create the current {@link GlobalApp} instance.
     * @deprecated Use {@link #getGlobalApp} instead.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse
     * @return the current {@link GlobalApp} from the user session, or a newly-instantiated one
     *         (based on the user's Global.app file) if none was in the session.  Failing that,
     *         return <code>null</code>.
     */ 
    public static GlobalApp ensureGlobalApp( HttpServletRequest request, HttpServletResponse response )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        return ensureGlobalApp( request, response, servletContext );
    }
    
    /**
     * Get or create the current {@link GlobalApp} instance.
     * @deprecated Use {@link #getSharedFlow} instead.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse
     * @return the current {@link GlobalApp} from the user session, or a newly-instantiated one
     *         (based on the user's Global.app file) if none was in the session.  Failing that,
     *         return <code>null</code>.
     */ 
    public static GlobalApp ensureGlobalApp( HttpServletRequest request, HttpServletResponse response,
                                             ServletContext servletContext )
    {
        GlobalApp ga = getGlobalApp( request );
        
        if ( ga != null )
        {
            ga.reinitialize( request, response, servletContext );
        }
        else
        {
            ga = FlowControllerFactory.getGlobalApp( request, response, servletContext );
        }
        
        return ga;
    }
    
    /**
     * @deprecated This is an internal utility.  {@link InternalUtils#getBindingUpdateErrors} can be used, but it is
     *             not guaranteed to be supported in the future.
     */ 
    public static Map getBindingUpdateErrors( ServletRequest request )
    {
        return InternalUtils.getBindingUpdateErrors( request );
    }
    
    /**
     * @deprecated This is an internal utility.  {@link InternalUtils#ensureModuleConfig} can be used, but it is
     *             not guaranteed to be supported in the future.
     */ 
    public static ModuleConfig ensureModuleConfig( String modulePath, ServletRequest request, ServletContext context )
    {
        return InternalUtils.ensureModuleConfig( modulePath, context );
    }
    
    /**
     * @deprecated This will be removed with no replacement in a future release.
     */ 
    public static ModuleConfig getGlobalAppConfig( ServletContext servletContext )
    {
        return InternalUtils.getModuleConfig( GLOBALAPP_MODULE_CONTEXT_PATH, servletContext );
    }
    
    /**
     * @deprecated This is an internal utility.  {@link InternalUtils#getModuleConfig} can be used, but it is
     *             not guaranteed to be supported in the future.
     */ 
    public static ModuleConfig getModuleConfig( String modulePath, ServletContext context )
    {
        return InternalUtils.getModuleConfig( modulePath, context );
    }
    
    /**
     * Get the file extension from a file name.
     * @deprecated Use {@link FileUtils#getFileExtension} instead.
     * 
     * @param filename the file name.
     * @return the file extension (everything after the last '.'), or the empty string if there is no file extension.
     */ 
    public static String getFileExtension( String filename )
    {
        return FileUtils.getFileExtension( filename );
    }
    
    /**
     * @deprecated This is an internal utility.  {@link InternalUtils#getFlowControllerClassName} can be used, but it is
     *             not guaranteed to be supported in the future.
     */ 
    public static String getPageFlowClassName( String modulePath, ServletRequest request, ServletContext context )
    {
        return InternalUtils.getFlowControllerClassName( modulePath, request, context );
    }
    
    /**
     * @deprecated This method no longer has any effect, and will be removed without replacement in a future release.
     */ 
    public static boolean ensureAppDeployment( HttpServletRequest request, HttpServletResponse response,
                                               ServletContext servletContext )
    {
        return false;
    }
    
    /**
     * Tell whether a given URI is absolute, i.e., whether it contains a scheme-part (e.g., "http:").
     * @deprecated Use {@link FileUtils#isAbsoluteURI} instead.
     * 
     * @param uri the URI to test.
     * @return <code>true</code> if the given URI is absolute.
     */ 
    public static boolean isAbsoluteURI( String uri )
    {
        return FileUtils.isAbsoluteURI( uri );
    }
    
    /**
     * @deprecated Use {@link #getCurrentPageFlow} instead.
     */ 
    public static final PageFlowController ensureCurrentPageFlow( HttpServletRequest request,
                                                                  HttpServletResponse response,
                                                                  ServletContext servletContext )
    {
        try
        {
            FlowControllerFactory factory = FlowControllerFactory.get( servletContext );
            return factory.getPageFlowForRequest( new RequestContext( request, response ) );
        }
        catch ( InstantiationException e )
        {
            _log.error( "Could not instantiate PageFlowController for request " + request.getRequestURI(), e );
        }
        catch ( IllegalAccessException e )
        {
            _log.error( "Could not instantiate PageFlowController for request " + request.getRequestURI(), e );
        }
        
        return null;
    }
    
    /**
     * @deprecated Use {@link #getCurrentPageFlow} instead.
     */ 
    public static final PageFlowController ensureCurrentPageFlow( HttpServletRequest request,
                                                                  HttpServletResponse response )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request );
        
        if ( servletContext == null && _log.isWarnEnabled() )
        {
            _log.warn( "could not get ServletContext from request " + request );
        }
        
        return ensureCurrentPageFlow( request, response, servletContext );
    }
    
    /**
     * @deprecated This is an internal utility.  {@link InternalUtils#addBindingUpdateError} can be used, but it is
     *             not guaranteed to be supported in the future.
     */ 
    public static void addBindingUpdateError( ServletRequest request, String expression, String message, Throwable e )
    {
        InternalUtils.addBindingUpdateError( request, expression, message, e );
    }
    
    /**
     * @deprecated This is an internal utility.  {@link ServletUtils#dumpRequest} can be used, but it is
     *             not guaranteed to be supported in the future.
     */ 
    public static void dumpRequest( HttpServletRequest request, PrintStream output )
    {
        ServletUtils.dumpRequest( request, output );
    }
    
    /**
     * @deprecated This is an internal utility.  {@link ServletUtils#dumpServletContext} can be used, but it is
     *             not guaranteed to be supported in the future.
     */ 
    public static void dumpServletContext( ServletContext context, PrintStream output )
    {
        ServletUtils.dumpServletContext( context, output );
    }
    
    /**
     * @deprecated Use {@link ServletUtils#preventCache} instead.
     */ 
    public static void preventCache( HttpServletResponse response )
    {
        ServletUtils.preventCache( response );
    }
    
    /**
     * @deprecated This is an internal utility.  {@link InternalUtils#setCurrentActionResolver} can be used, but it is
     *             not guaranteed to be supported in the future.  This method will be removed in the next version.
     */ 
    public static void setCurrentActionResolver( ActionResolver resolver, HttpServletRequest request )
    {
        ServletContext servletContext = InternalUtils.getServletContext( request ); 
        InternalUtils.setCurrentActionResolver( resolver, request, servletContext );
    }

    /**
     * Create a raw action URI, which can be modified before being sent through the registered URL rewriting chain
     * using {@link URLRewriterService#rewriteURL}.  Use {@link #getRewrittenActionURI} to get a fully-rewritten URI.
     *
     * @param servletContext the current ServletContext.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param actionName the action name to convert into a MutableURI; may be qualified with a path from the webapp
     *            root, in which case the parent directory from the current request is <i>not</i> used.
     * @return a MutableURI for the given action, suitable for URL rewriting.
     * @throws URISyntaxException if there is a problem converting the action URI (derived from processing the given
     *             action name) into a MutableURI.
     */
    public static MutableURI getActionURI( ServletContext servletContext, HttpServletRequest request,
                                           HttpServletResponse response, String actionName )
            throws URISyntaxException
    {
        if ( actionName.length() < 1 ) throw new IllegalArgumentException( "actionName must be non-empty" );

        /*
         * The implementation for HttpServletRequest method getContextPath()
         * is not consistant across containers. The spec says the "container
         * does not decode this string." However, the string returned in
         * Tomcat is decoded. (See Tomcat bugzilla bug 39503) Also this method
         * returns a decoded string in some containers if the request is a
         * forward where the RequestDispatcher was acquired using a relative
         * path. It seems that it is only the space character in the context
         * path that causes us issues using the java.net.URI class in our
         * MutableURI.setURI(). So,... check for a decoded space and encode it.
         */
        String contextPath = request.getContextPath();
        if (contextPath.indexOf(' ') != -1) {
            contextPath = contextPath.replace(" ", "%20");
        }
        InternalStringBuilder actionURI = new InternalStringBuilder(contextPath);
        
        if ( actionName.charAt( 0 ) != '/' )
        {
            actionURI.append( InternalUtils.getModulePathFromReqAttr( request ) );
            actionURI.append( '/' );
        }
        
        actionURI.append( actionName );
        if ( ! actionName.endsWith( ACTION_EXTENSION ) ) actionURI.append( ACTION_EXTENSION );
        
        FreezableMutableURI uri = new FreezableMutableURI();
        uri.setEncoding( response.getCharacterEncoding() );
        uri.setPath( actionURI.toString() );
        return uri;
    }
    
    /**
     * Create a fully-rewritten URI given an action name and parameters.
     *
     * @param servletContext the current ServletContext.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param actionName the action name to convert into a fully-rewritten URI; may be qualified with a path from the
     *            webapp root, in which case the parent directory from the current request is <i>not</i> used.
     * @param params the additional parameters to include in the URI query.
     * @param fragment the fragment (anchor or location) for this url.
     * @param forXML flag indicating that the query of the uri should be written
     *               using the &quot;&amp;amp;&quot; entity, rather than the character, '&amp;'.
     * @return a fully-rewritten URI for the given action.
     * @throws URISyntaxException if there is a problem converting the action URI (derived
     *                            from processing the given action name) into a MutableURI.
     */
    public static String getRewrittenActionURI( ServletContext servletContext, HttpServletRequest request,
                                                HttpServletResponse response, String actionName, Map params,
                                                String fragment, boolean forXML )
            throws URISyntaxException
    {
        MutableURI uri = getActionURI( servletContext, request, response, actionName );
        if ( params != null ) uri.addParameters( params, false );
        if ( fragment != null ) uri.setFragment( uri.encode( fragment ) );

        boolean needsToBeSecure = needsToBeSecure( servletContext, request, uri.getPath(), true );
        URLRewriterService.rewriteURL( servletContext, request, response, uri, URLType.ACTION, needsToBeSecure );
        String key = getURLTemplateKey( URLType.ACTION, needsToBeSecure );
        URIContext uriContext = URIContextFactory.getInstance( forXML );

        return URLRewriterService.getTemplatedURL( servletContext, request, uri, key, uriContext );
    }

    /**
     * Create a fully-rewritten URI given a path and parameters.
     *
     * <p> Calls the rewriter service using a type of {@link URLType#RESOURCE}. </p>
     *
     * @param servletContext the current ServletContext.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param path the path to process into a fully-rewritten URI.
     * @param params the additional parameters to include in the URI query.
     * @param fragment the fragment (anchor or location) for this URI.
     * @param forXML flag indicating that the query of the uri should be written
     *               using the &quot;&amp;amp;&quot; entity, rather than the character, '&amp;'.
     * @return a fully-rewritten URI for the given action.
     * @throws URISyntaxException if there's a problem converting the action URI (derived
     *                            from processing the given action name) into a MutableURI.
     */
    public static String getRewrittenResourceURI( ServletContext servletContext, HttpServletRequest request,
                                                  HttpServletResponse response, String path, Map params,
                                                  String fragment, boolean forXML )
            throws URISyntaxException
    {
        return rewriteResourceOrHrefURL( servletContext, request, response, path, params, fragment, forXML, URLType.RESOURCE );
    }

    /**
     * Create a fully-rewritten URI given a path and parameters.
     *
     * <p> Calls the rewriter service using a type of {@link URLType#ACTION}. </p>
     *
     * @param servletContext the current ServletContext.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param path the path to process into a fully-rewritten URI.
     * @param params the additional parameters to include in the URI query.
     * @param fragment the fragment (anchor or location) for this URI.
     * @param forXML flag indicating that the query of the uri should be written
     *               using the &quot;&amp;amp;&quot; entity, rather than the character, '&amp;'.
     * @return a fully-rewritten URI for the given action.
     * @throws URISyntaxException if there's a problem converting the action URI (derived
     *                            from processing the given action name) into a MutableURI.
     */
    public static String getRewrittenHrefURI( ServletContext servletContext, HttpServletRequest request,
                                              HttpServletResponse response, String path, Map params,
                                              String fragment, boolean forXML )
            throws URISyntaxException
    {
        return rewriteResourceOrHrefURL( servletContext, request, response, path, params, fragment, forXML, URLType.ACTION );
    }

    private static String rewriteResourceOrHrefURL( ServletContext servletContext, HttpServletRequest request,
                                                    HttpServletResponse response, String path, Map params,
                                                    String fragment, boolean forXML, URLType urlType )
            throws URISyntaxException
    {
        boolean encoded = false;
        UrlConfig urlConfig = ConfigUtil.getConfig().getUrlConfig();

        if (urlConfig != null) {
            encoded = !urlConfig.isUrlEncodeUrls();
        }

        FreezableMutableURI uri = new FreezableMutableURI();
        uri.setEncoding( response.getCharacterEncoding() );
        uri.setURI( path, encoded );

        if ( params != null )
        {
            uri.addParameters( params, false );
        }

        if ( fragment != null )
        {
            uri.setFragment( uri.encode( fragment ) );
        }

        URIContext uriContext = URIContextFactory.getInstance( forXML );
        if ( uri.isAbsolute() )
        {
            return uri.getURIString( uriContext );
        }

        if ( path.length() != 0 && path.charAt( 0 ) !=  '/' )
        {
            String reqUri = request.getRequestURI();
            String reqPath = reqUri.substring( 0, reqUri.lastIndexOf( '/' ) + 1 );
            uri.setPath( reqPath + uri.getPath() );
        }

        boolean needsToBeSecure = needsToBeSecure( servletContext, request, uri.getPath(), true );
        URLRewriterService.rewriteURL( servletContext, request, response, uri, urlType, needsToBeSecure );
        String key = getURLTemplateKey( urlType, needsToBeSecure );

        return URLRewriterService.getTemplatedURL( servletContext, request, uri, key, uriContext );
    }

    /**
     * Tell whether a given URI should be written to be secure.
     * @param context          the current ServletContext.
     * @param request          the current HttpServletRequest.
     * @param uri              the URI to check.
     * @param stripContextPath if <code>true</code>, strip the webapp context path from the URI before
     *                         processing it.
     * @return <code>true</code> when:
     *         <ul>
     *         <li>the given URI is configured in the deployment descriptor to be secure (according to
     *         {@link SecurityProtocol}), or
     *         <li>the given URI is not configured in the deployment descriptor, and the current request
     *         is secure ({@link HttpServletRequest#isSecure} returns
     *         <code>true</code>).
     *         </ul>
     *         <code>false</code> when:
     *         <ul>
     *         <li>the given URI is configured explicitly in the deployment descriptor to be unsecure
     *         (according to {@link SecurityProtocol}), or
     *         <li>the given URI is not configured in the deployment descriptor, and the current request
     *         is unsecure ({@link HttpServletRequest#isSecure} returns
     *         <code>false</code>).
     *         </ul>
     */
    public static boolean needsToBeSecure(ServletContext context, ServletRequest request,
                                          String uri, boolean stripContextPath)
    {
        // Get the web-app relative path for security check
        String secureCheck = uri;
        if (stripContextPath) {
            String contextPath = ((HttpServletRequest) request).getContextPath();
            if (secureCheck.startsWith(contextPath)) {
                secureCheck = secureCheck.substring(contextPath.length());
            }
        }

        boolean secure = false;
        if (secureCheck.indexOf('?') > -1) {
            secureCheck = secureCheck.substring(0, secureCheck.indexOf('?'));
        }

        SecurityProtocol sp = getSecurityProtocol(secureCheck, context, (HttpServletRequest) request);
        if (sp.equals(SecurityProtocol.UNSPECIFIED)) {
            secure = request.isSecure();
        }
        else {
            secure = sp.equals(SecurityProtocol.SECURE);
        }

        return secure;
    }

    /**
     * Returns a key for the URL template type given the URL type and a
     * flag indicating a secure URL or not.
     *
     * @param urlType the type of URL (ACTION, RESOURCE).
     * @param needsToBeSecure indicates that the template should be for a secure URL.
     * @return the key/type of template to use.
     */
    public static String getURLTemplateKey( URLType urlType, boolean needsToBeSecure )
    {
        String key = URLTemplatesFactory.ACTION_TEMPLATE;
        if ( urlType.equals( URLType.ACTION ) )
        {
            if ( needsToBeSecure )
            {
                key = URLTemplatesFactory.SECURE_ACTION_TEMPLATE;
            }
            else
            {
                key = URLTemplatesFactory.ACTION_TEMPLATE;
            }
        }
        else if ( urlType.equals( URLType.RESOURCE ) )
        {
            if ( needsToBeSecure )
            {
                key = URLTemplatesFactory.SECURE_RESOURCE_TEMPLATE;
            }
            else
            {
                key = URLTemplatesFactory.RESOURCE_TEMPLATE;
            }
        }

        return key;
    }

    /**
     * Get an uninitialized instance of a container specific URLTemplatesFactory
     * from the ServletContainerAdapter. If none exists, this returns an instance
     * of {@link DefaultURLTemplatesFactory}. Caller should then set the known
     * and required tokens, call the {@link URLTemplatesFactory#load(javax.servlet.ServletContext)}
     * method and {@link URLTemplatesFactory#initServletContext(javax.servlet.ServletContext,
     * org.apache.beehive.netui.core.urltemplates.URLTemplatesFactory)}. 
     *
     * <p>
     * IMPORTANT NOTE - Always try to get the application instance from the ServletContext
     * by calling {@link URLTemplatesFactory#getURLTemplatesFactory(javax.servlet.ServletContext)}.
     * Then, if a new URLTemplatesFactory must be created, call this method.
     * </p>
     *
     * @param servletContext
     * @return a container specific implementation of URLTemplatesFactory, or
     *         {@link DefaultURLTemplatesFactory}.
     */
    public static URLTemplatesFactory createURLTemplatesFactory( ServletContext servletContext )
    {
        // get the URLTemplatesFactory from the containerAdapter.
        ServletContainerAdapter containerAdapter = AdapterManager.getServletContainerAdapter( servletContext );
        URLTemplatesFactory factory = (URLTemplatesFactory) containerAdapter.getFactory( URLTemplatesFactory.class, null, null );

        // if there's no URLTemplatesFactory, use our default impl.
        if ( factory == null )
        {
            factory = new DefaultURLTemplatesFactory();
        }

        return factory;
    }

    /**
     * Make sure that when this page is rendered, it will set headers in the response to prevent caching.
     * Because these headers are lost on server forwards, we set a request attribute to cause the headers
     * to be set right before the page is rendered.  This attribute can be read via
     * {@link #isPreventCache(javax.servlet.ServletRequest)}.
     *
     * @param request the servlet request
     */
    static void setPreventCache( ServletRequest request ) {
        assert request != null;
        request.setAttribute(PREVENT_CACHE_ATTR, Boolean.TRUE);
    }

    /**
     * Internal getter that reports whether this request should prevent caching.  This flag is set via the
     * {@link #setPreventCache(javax.servlet.ServletRequest)} attribute.
     * @param request the servlet request
     * @return <code>true</code> if the framework has set the prevent cache flag
     */
    static boolean isPreventCache(ServletRequest request) {
        assert request != null;
        return request.getAttribute(PREVENT_CACHE_ATTR) != null;
    }
}
