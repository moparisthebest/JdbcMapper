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

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;

import org.apache.beehive.netui.pageflow.config.PageFlowActionForward;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.internal.AdapterManager;
import org.apache.beehive.netui.pageflow.internal.PageFlowRequestWrapper;
import org.apache.beehive.netui.pageflow.handler.ReloadableClassHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.util.internal.FileUtils;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * An object of this type is returned from an action methods in a {@link PageFlowController} to
 * determine the next URI to be displayed.  It is constructed on the name of a forward defined
 * by the @{@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward} annotation, and resolves to the URI
 * specified in that forward.
 */ 
public class Forward extends ActionForward
{
    private static final ActionForm[] EMPTY_ACTION_FORM_ARRAY = new ActionForm[0];
    
    public static final int RETURN_TO_NONE = 0;
    public static final int RETURN_TO_CURRENT_PAGE = 1;
    public static final int RETURN_TO_PREVIOUS_PAGE = 2;
    public static final int RETURN_TO_PREVIOUS_ACTION = 3;
    
    /**
     * @deprecated Use {@link #RETURN_TO_CURRENT_PAGE} or {@link #RETURN_TO_PREVIOUS_PAGE} instead.
     */ 
    public static final int RETURN_TO_PAGE = -1;
    
    /**
     * @deprecated Use {@link #RETURN_TO_PREVIOUS_ACTION} instead.
     */ 
    public static final int RETURN_TO_ACTION = -2;
    
    private static final Logger _log = Logger.getInstance( Forward.class );
    
    private static final String RETURN_TO_CURRENT_PAGE_STR = "currentPage";
    private static final String RETURN_TO_PREVIOUS_PAGE_STR = "previousPage";
    private static final String RETURN_TO_PAGE_LEGACY_STR = "page";
    private static final String RETURN_TO_PREVIOUS_ACTION_STR = "previousAction";
    private static final String RETURN_TO_ACTION_LEGACY_STR = "action";
    
    private static final Map/*< String, Class >*/ PRIMITIVE_TYPES = new HashMap/*< String, Class >*/();
    
    static
    {
        PRIMITIVE_TYPES.put( "boolean", boolean.class );
        PRIMITIVE_TYPES.put( "byte", byte.class );
        PRIMITIVE_TYPES.put( "char", char.class );
        PRIMITIVE_TYPES.put( "double", double.class );
        PRIMITIVE_TYPES.put( "float", float.class );
        PRIMITIVE_TYPES.put( "int", int.class );
        PRIMITIVE_TYPES.put( "long", long.class );
        PRIMITIVE_TYPES.put( "short", short.class );
    }

    private List _outputForms;

    private boolean _isNestedReturn = false;
    private boolean _init = false;
    private transient ActionMapping _mapping = null;         // will be reinitialized as necessary by PreviousPageInfo
    private transient FlowController _flowController = null; // will be reinitialized as necessary by PreviousPageInfo
    private transient ServletContext _servletContext = null; // will be reinitialized as necessary by PreviousPageInfo
    private String _mappingPath;
    private InternalStringBuilder _queryString;
    private boolean _explicitPath = false;
    private String _outputFormBeanType = null;
    private Map _actionOutputs = null;
    private int _returnToType;
    private boolean _redirectSpecifiedOnAnnotation = false;
    private boolean _redirectSetThroughMethod = false;
    private boolean _restoreQueryString = false;
    private boolean _externalRedirect = false;
    private boolean _outsidePageFlowDirectory = false;
    private String _relativeTo;
    
    /**
     * An alternate ModuleConfig from which to resolve forwards if they are not resolved
     * from the stored ActionMapping (and its stored ModuleConfig).
     */ 
    private ModuleConfig _altModuleConfig;
    
    
    /**
     * Construct based on an initializer Forward.  This is a framework-invoked constructor that should not normally
     * be called directly.
     */ 
    protected Forward( Forward init )
    {
        _outputForms = init._outputForms;
        _init = init._init;
        _mapping = init._mapping;
        _mappingPath = init._mappingPath;
        _queryString = init._queryString;
        _explicitPath = init._explicitPath;
        _flowController = init._flowController;
        _servletContext = init._servletContext;
        _outputFormBeanType = init._outputFormBeanType;
        _actionOutputs = init._actionOutputs;
        _returnToType = init._returnToType;
        _restoreQueryString = init._restoreQueryString;
        _externalRedirect = init._externalRedirect;
        _redirectSpecifiedOnAnnotation = init._redirectSpecifiedOnAnnotation;
        _redirectSetThroughMethod = init._redirectSetThroughMethod;
    }
    
    /**
     * Construct based on a given request.  This is a framework-invoked constructor that should not normally
     * be called directly.
     */ 
    protected Forward( HttpServletRequest request )
    {
        setPath( InternalUtils.getDecodedServletPath( request ) );
        setContextRelative( true );
        _explicitPath = true;        
    }
    
    /**
     * Constructor which accepts the name of a forward defined by the
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}
     * annotation.  The values returned from {@link #getPath}, {@link #getRedirect} and
     * {@link #contextRelative} are resolved from this forward.
     * 
     * @param forwardName the name of the forward
     *            ({@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}) to resolve.
     */ 
    public Forward( String forwardName )
    {
        setName( forwardName );
    }

    /**
     * Constructor which accepts the name of a forward defined by the
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}
     * annotation.  The values returned from {@link #getPath}, {@link #getRedirect} and
     * {@link #contextRelative} are resolved from this forward.  Also accepts a form bean
     * to make available in the request (or user session, as appropriate).
     * 
     * @param forwardName the name of the forward
     * ({@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}) to resolve.
     * @param outputFormBean a form bean instance to make available in the request (or user session, as appropriate).
     *            See {@link #addOutputForm} for details about how this manifests itself.
     */ 
    public Forward( String forwardName, Object outputFormBean )
    {
        this( forwardName );
        
        if ( outputFormBean != null )
        {
            addOutputForm( outputFormBean );
        }
    }
    
    /**
     * Constructor which accepts the name of a forward defined by the
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}
     * annotation.  The values returned from {@link #getPath}, {@link #getRedirect} and
     * {@link #contextRelative} are resolved from this forward.  Also accepts a named action output
     * to make available in the request, through {@link PageFlowUtils#getActionOutput}..
     * 
     * @param forwardName the name of the forward
     * ({@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}) to resolve.
     * @param actionOutputName the name of a action output to make available in the request.
     * @param actionOutputValue the action output object to make available in the request.
     */ 
    public Forward( String forwardName, String actionOutputName, Object actionOutputValue )
    {
        this( forwardName );
        addActionOutput( actionOutputName, actionOutputValue );
    }

    /**
     * Constructs a Forward that returns the given URI for {@link #getPath}.  By default
     * the Forward will cause server forward (not a browser redirect); to change this, use
     * {@link #setRedirect}.
     * 
     * @param uri the URI to return for {@link #getPath}.
     */ 
    public Forward( URI uri )
    {
        setPath( uri.toString() );
        setContextRelative( uri.getPath().startsWith( "/" ) );
        if ( uri.isAbsolute() ) super.setRedirect( true );
        _explicitPath = true;
    }
    
    /**
     * Constructs a Forward that returns the given URI for {@link #getPath}.
     * 
     * @param uri the URI to return for {@link #getPath}.
     * @param doRedirect set to <code>true</code> if this Forward should cause a browser redirect;
     *                 <code>false</code> if it should cause a server forward.
     */ 
    public Forward( URI uri, boolean doRedirect )
    {
        if ( ! doRedirect && uri.isAbsolute() )
        {
            throw new IllegalStateException( "Redirect value cannot be set to false for an absolute URI." );
        }
        
        setPath( uri.toString() );
        setRedirect( doRedirect );
        setContextRelative( uri.getPath().startsWith( "/" ) );
        _explicitPath = true;
    }
    
    /**
     * Construct based on the given <strong>webapp-relative</strong> path.  This is a framework-invoked constructor that
     * should not normally be called directly.
     */ 
    protected Forward( String path, boolean doRedirect )
    {
        setPath( path );
        setContextRelative( true );
        setRedirect( doRedirect );
        _explicitPath = true;
    }
    
    /**
     * Constructs a Forward that returns the given URL for {@link #getPath}.  Because the URL path
     * is absolute by nature, this Forward will cause a browser redirect.
     * 
     * @param url the URL to return for {@link #getPath}.
     */ 
    public Forward( URL url )
    {
        setPath( url.toString() );
        super.setRedirect( true );
        _explicitPath = true;
    }
    
    /**
     * Internal.  Initialize from an existing Struts ActionForward.
     */ 
    protected Forward( ActionForward initFwd, ServletContext servletContext )
    {
        _servletContext = servletContext;
        setName( initFwd.getName() );
        initFrom( initFwd );
    }
    
    /**
     * Set whether the URI resolved by this Forward should be redirected to.
     * 
     * @param doRedirect if <code>true</code>, the controller will send a browser redirect to
     *                 the URI for this Forward; otherwise, it will do a server forward to
     *                 the URI.
     */ 
    public void setRedirect( boolean doRedirect )
    {
        super.setRedirect( doRedirect );
        _redirectSetThroughMethod = true;
    }
    
    /**
     * Tell whether the URI resolved by this Forward should be redirected to.
     * 
     * @return <code>true</code> if the controller will send a browser redirect to the URI for
     *         this Forward; <code>false</code> if it will do a server forward to the URI.
     */ 
    public boolean isRedirect()
    {
        return super.getRedirect();
    }
    
    /**
     * Add a form bean that will be made available in the request (or user session, as
     * appropriate) if this Forward is returned by an action method in a {@link PageFlowController}.
     * Specifically, each form bean is stored as a request attribute with a name determined by
     * {@link PageFlowUtils#getFormBeanName}.
     * 
     * @param formBean the form bean instance to add.
     */ 
    public final void addOutputForm( Object formBean )
    {
        assert formBean != null : "The output form bean may not me null.";
        
        if ( formBean == null ) throw new IllegalArgumentException( "The output form bean may not be null." );
        if ( _outputForms == null ) _outputForms = new ArrayList();
        
        //
        // Throw an exception if this is a redirect, and if there was an output form added.  Output forms are carried
        // in the request, and will be lost on redirects.
        //
        if ( _init && getRedirect() )
        {
            String actionPath = _mappingPath != null ? _mappingPath : "";
            String descrip = getName() != null ? getName() : getPath();
            PageFlowException ex =  new IllegalRedirectOutputFormException( descrip, actionPath, _flowController,
                                                                            formBean.getClass().getName() );
            InternalUtils.throwPageFlowException( ex );
        }

        _outputForms.add( InternalUtils.wrapFormBean( formBean ) );
    }
    
    /**
     * Get all form-beans attached to this forward through {@link #addOutputForm} or {@link #Forward(String, Object)}.
     * 
     * @return an array of ActionForm instances that are attached to this forward.
     */ 
    public final ActionForm[] getOutputForms()
    {
        if ( _outputForms == null ) return EMPTY_ACTION_FORM_ARRAY;
        return ( ActionForm[] ) _outputForms.toArray( EMPTY_ACTION_FORM_ARRAY );
    }

    /**
     * Get the first output form bean that was added to this Forward.
     */ 
    public ActionForm getFirstOutputForm( HttpServletRequest request )
    {
        if ( _outputForms == null || _outputForms.size() == 0 )
        {
            if ( _outputFormBeanType != null )
            {
                try
                {
                    if ( _log.isDebugEnabled() )
                    {
                        _log.debug( "Creating form bean of type " + _outputFormBeanType );
                    }
                    
                    ServletContext servletContext = InternalUtils.getServletContext( request );
                    ReloadableClassHandler rch = Handlers.get( servletContext ).getReloadableClassHandler();
                    Object formBean = rch.newInstance( _outputFormBeanType );
                    ActionForm wrappedFormBean = InternalUtils.wrapFormBean( formBean );
                    addOutputForm( wrappedFormBean );
                    return wrappedFormBean;
                }
                catch ( Exception e )
                {
                    _log.error( "Could not create form bean instance of " + _outputFormBeanType, e );
                }                
            }
            
            return null;
        }
        else
        {
            return ( ActionForm ) _outputForms.get( 0 );
        }
    }
    
    /**
     * Tell whether {@link #getPath} will be successful, i.e., whether one of the following two
     * conditions is met:
     *     <ul>
     *         <li>the name around which this object was constructed resolves to a path defined
     *             by a {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}, or</li>
     *         <li>this object was constructed around an explicit path, by
     *             {@link #Forward(URI)} or {@link #Forward(URL)}.</li>
     *     </ul>
     * 
     * @return <code>true</code> if this forward does resolve to a URI path.
     */ 
    public boolean doesResolve()
    {
        if ( _explicitPath )
        {
            return true;
        }
        
        assert _mapping != null || _altModuleConfig != null : "PageFlow.Forward.doesResolve() called outside of request";
        return findForward( getName() ) != null;
    }

    /**
     * Resolves the forward with the given name, from the stored ActionMapping if possible, or
     * from the stored alternate ModuleConfig as a last resort.
     * 
     * @param forwardName the name of the forward to resolve.
     * @return the resolved ActionForward, or <code>null</code> if none is found.
     */ 
    protected ActionForward findForward( String forwardName )
    {
        ActionForward fwd = _mapping != null ? _mapping.findForward( forwardName ) : null;
        
        if ( fwd != null )
        {
            return fwd;
        }
        else if ( _altModuleConfig != null )
        {
            return ( ActionForward ) _altModuleConfig.findForwardConfig( forwardName );
        }
        
        return null;
    }
    
    /**
     * Set an alternate ModuleConfig from which to resolve forwards if they are not resolved
     * from the stored ActionMapping (and its stored ModuleConfig).
     */ 
    public void setAlternateModuleConfig( ModuleConfig mc )
    {
        _altModuleConfig = mc;
    }
    
    private void init()
    {
        if ( ! _init )
        {
            if ( _mappingPath == null && _altModuleConfig == null )
            {
                throw new IllegalStateException( "Forward is not initialized.  Use initialize()." );
            }
            
            ActionForward fwd = findForward( getName() );

            if ( fwd == null )
            {
                PageFlowException ex = new UnresolvableForwardException( getName(), _mappingPath, _flowController );
                InternalUtils.throwPageFlowException( ex );
            }

            initFrom( fwd );
            
            //
            // Throw an exception if this is a redirect, and if there was an output form or an action output added.
            // Output forms and action outputs are carried in the request, and will be lost on redirects.
            //
            if ( getRedirect() )
            {
                if ( _actionOutputs != null && ! _actionOutputs.isEmpty() )
                {
                    PageFlowException ex =
                            new IllegalActionOutputException( getName(), _mappingPath, _flowController,
                                                             ( String ) _actionOutputs.keySet().iterator().next() );
                    InternalUtils.throwPageFlowException( ex );
                }
                
                if ( _outputForms != null && ! _outputForms.isEmpty() )
                {
                    PageFlowException ex =
                            new IllegalRedirectOutputFormException( getName(), _mappingPath, _flowController,
                                                                    _outputForms.get( 0 ).getClass().getName() );
                    InternalUtils.throwPageFlowException( ex );
                }
            }
        }
    }
    
    private void initFrom( ActionForward fwd )
    {
        setContextRelative( fwd.getContextRelative() );

        //
        // Add query params to the path.
        //
        path = fwd.getPath();
        if ( _queryString != null ) path += _queryString.toString();
        
        if ( fwd instanceof PageFlowActionForward )
        {
            PageFlowActionForward fc = ( PageFlowActionForward ) fwd;
            _isNestedReturn = fc.isNestedReturn();
            _outputFormBeanType = fc.getReturnFormType();
            _redirectSpecifiedOnAnnotation = fc.hasExplicitRedirectValue();
            _restoreQueryString = fc.isRestoreQueryString();
            _externalRedirect = fc.isExternalRedirect();
            _relativeTo = fc.getRelativeTo();

            Class returnFormClass = null;
            
            if ( _outputFormBeanType != null )
            {
                try
                {
                    ReloadableClassHandler rch = Handlers.get(_servletContext).getReloadableClassHandler();
                    returnFormClass = rch.loadClass( _outputFormBeanType );
                }
                catch ( ClassNotFoundException e )
                {
                    // This should never happen -- the JPF compiler ensures that it's a valid class.
                    assert false : e;
                }
            }
            
            if ( fc.isReturnToPage() || fc.isReturnToAction() )
            {
                String fwdPath = fc.getPath();
                
                if ( fwdPath.equals( RETURN_TO_PREVIOUS_PAGE_STR ) )
                {
                    _returnToType = RETURN_TO_PREVIOUS_PAGE;
                }
                else if ( fwdPath.equals( RETURN_TO_CURRENT_PAGE_STR ) )
                {
                    _returnToType = RETURN_TO_CURRENT_PAGE;
                }
                else if ( fwdPath.equals( RETURN_TO_PAGE_LEGACY_STR ) )
                {
                    _returnToType = RETURN_TO_PAGE;  // legacy
                }
                else if ( fwdPath.equals( RETURN_TO_PREVIOUS_ACTION_STR ) )
                {
                    _returnToType = RETURN_TO_PREVIOUS_ACTION;
                }
                else if ( fwdPath.equals( RETURN_TO_ACTION_LEGACY_STR ) )
                {
                    _returnToType = RETURN_TO_ACTION;  // legacy
                }
                else
                {
                    assert false : "invalid return-to type for forward " + fc.getName() + ": " + fwdPath;
                    _returnToType = RETURN_TO_CURRENT_PAGE;
                }
            }
            
            String retFormMember = fc.getReturnFormMember();

            if ( retFormMember != null )
            {
                try
                {
                    assert _flowController != null;  // should be set in initialize()
                    Field field = _flowController.getClass().getDeclaredField( retFormMember );
                    returnFormClass = field.getType();
                    if ( ! Modifier.isPublic( field.getModifiers() ) ) field.setAccessible( true );
                    Object form = field.get( _flowController );
                    
                    if ( form != null )
                    {
                        if ( _log.isDebugEnabled() )
                        {
                            _log.debug( "using member " + retFormMember + " for Forward " + getName() );
                        }
                        
                        addOutputForm( form );
                    }
                    else
                    {
                        if ( _log.isInfoEnabled() )
                        {
                            _log.info( "returnFormMember " + retFormMember + " was null." );
                        }
                    }
                }
                catch ( NoSuchFieldException e )
                {
                    assert false : "could not find field " + retFormMember; // compiler should catch this
                }
                catch ( IllegalAccessException e )
                {
                    assert false;   // should not get here -- field is accessible.
                }
            }
            
            //
            // Make sure that if there's currently an output form, that it confirms to the return-form-type.
            //
            if ( returnFormClass != null && _outputForms != null && _outputForms.size() > 0 )
            {
                Object outputForm = InternalUtils.unwrapFormBean( ( ActionForm ) _outputForms.get( 0 ) );
                
                if ( ! returnFormClass.isInstance( outputForm ) )
                {
                    PageFlowException ex = 
                            new IllegalOutputFormTypeException( getName(), _mappingPath, _flowController,
                                                                outputForm.getClass().getName(),
                                                                returnFormClass.getName() );
                    InternalUtils.throwPageFlowException( ex );
                }
            }
            
            checkActionOutputs( fc );
        }
        
        if ( ! _redirectSetThroughMethod ) setRedirect( fwd.getRedirect() );
        
        _init = true;
    }

    /**
     * Make sure required action outputs are present, and are of the right type (only make the latter check when not
     * in production mode
     */
    private void checkActionOutputs( PageFlowActionForward fc )
    {
        PageFlowActionForward.ActionOutput[] actionOutputs = fc.getActionOutputs();
        boolean doExpensiveChecks =
                _actionOutputs != null
                && ! AdapterManager.getServletContainerAdapter( _servletContext ).isInProductionMode();
        
        for ( int i = 0; i < actionOutputs.length; ++i )
        {
            PageFlowActionForward.ActionOutput actionOutput = actionOutputs[i];
                
            if ( ! actionOutput.getNullable() )
            {
                String actionOutputName = actionOutput.getName();
                if ( _actionOutputs == null || ! _actionOutputs.containsKey(actionOutputName) )
                {
                    PageFlowException ex =
                        new MissingActionOutputException( _mappingPath, _flowController, actionOutput.getName(), getName() );
                    InternalUtils.throwPageFlowException( ex );
                }
                else if ( _actionOutputs.get(actionOutputName) == null )
                {
                    PageFlowException ex =
                            new NullActionOutputException( _mappingPath, _flowController, actionOutput.getName(), getName() );
                    InternalUtils.throwPageFlowException( ex );
                }
            }
                
            //
            // If we're *not* in production mode, do some (expensive) checks to ensure that the types for the
            // action outputs match their declared types.
            //
            if ( doExpensiveChecks )
            {
                Object actualActionOutput = _actionOutputs.get( actionOutput.getName() );
                    
                if ( actualActionOutput != null )
                {
                    String expectedTypeName = actionOutput.getType();
                    int expectedArrayDims = 0;
                    
                    while ( expectedTypeName.endsWith( "[]" ) )
                    {
                        ++expectedArrayDims;
                        expectedTypeName = expectedTypeName.substring( 0, expectedTypeName.length() - 2 );
                    }
                    
                    Class expectedType = ( Class ) PRIMITIVE_TYPES.get( expectedTypeName );
                    
                    if ( expectedType == null )
                    {
                        try
                        {
                            ReloadableClassHandler rch = Handlers.get(_servletContext).getReloadableClassHandler();
                            expectedType = rch.loadClass( expectedTypeName );
                        }
                        catch ( ClassNotFoundException e )
                        {
                            _log.error( "Could not load expected action output type " + expectedTypeName
                                        + " for action output '" + actionOutput.getName() + "' on forward '"
                                        + fc.getName() + "'; skipping type check." );
                            continue;
                        }
                    }
                    
                    Class actualType = actualActionOutput.getClass();
                    int actualArrayDims = 0;
                    InternalStringBuilder arraySuffix = new InternalStringBuilder();
                    
                    while ( actualType.isArray() && actualArrayDims <= expectedArrayDims )
                    {
                        ++actualArrayDims;
                        arraySuffix.append( "[]" );
                        actualType = actualType.getComponentType();
                    }
                        
                    if ( actualArrayDims != expectedArrayDims || ! expectedType.isAssignableFrom( actualType ) )
                    {
                        PageFlowException ex =
                                new MismatchedActionOutputException( _mappingPath, _flowController,
                                                                     actionOutput.getName(), getName(),
                                                                     expectedTypeName,
                                                                     actualType.getName() + arraySuffix );
                        InternalUtils.throwPageFlowException( ex );
                    }
                }
            }
        }
    }
                
    /**
     * Set the current ActionMapping and associated FlowController.  Normally, this method is called
     * by the framework, but you can use it to initialize the Forward object in order to call {@link #getPath}.
     * 
     * @deprecated Use {@link #initialize(ActionMapping, FlowController, ServletRequest)} instead.
     * @param mapping the current ActionMapping; this can be obtained from {@link FlowController#getMapping}.
     * @param flowController the object in which to look for referenced return-form members.
     */ 
    public void initialize( ActionMapping mapping, FlowController flowController )
    {
        _mapping = mapping;
        _mappingPath = mapping != null ? mapping.getPath() : null;
        _flowController = flowController;
        _servletContext = flowController.getServletContext();
    }
    
    /**
     * Set the current ActionMapping and associated FlowController.  Normally, this method is called
     * by the framework, but you can use it to initialize the Forward object in order to call {@link #getPath}.
     * 
     * @param mapping the current ActionMapping; this can be obtained from {@link FlowController#getMapping}.
     * @param flowController the object in which to look for referenced return-form members.
     */ 
    public void initialize( ActionMapping mapping, FlowController flowController, ServletRequest request )
    {
        _mapping = mapping;
        _mappingPath = mapping != null ? mapping.getPath() : null;
        _flowController = flowController;
        _servletContext = flowController.getServletContext();
    }
    
    /**
     * Set the path to be returned by {@link #getPath}.  This overrides any path or forward name
     * set in a constructor.
     * 
     * @param contextRelativePath the path to be returned by {@link #getPath}.
     */ 
    public void setPath( String contextRelativePath )
    {
        path = contextRelativePath;
        _init = true;
    }
    
    boolean isExplicitPath()
    {
        return _explicitPath;
    }
    
    /**
     * Tell whether this Forward was configured explicitly (through
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward#redirect &#64;Jpf.Forward(redirect=...)},
     * {@link #setRedirect}, or {@link #Forward(URI, boolean)}) to perform a redirect.  Otherwise, a redirect is
     * implied by a URI that does not resolve to the current server.
     */ 
    public boolean hasExplicitRedirectValue()
    {
        return _redirectSetThroughMethod || _redirectSpecifiedOnAnnotation;
    }

    /**
     * Get the URI path associated with this object.  Resolve it from the name of a forward
     * ({@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}) if necessary.
     * 
     * @return a String that is the URI path.
     * @see #Forward(String)
     * @see #Forward(String, Object)
     * @see #Forward(URI)
     * @see #Forward(URL)
     * @see #setPath
     */ 
    public String getPath()
    {
        init();
        return super.getPath();
    }
    
    /**
     * Tell whether returning this forward from an action method will cause a return from
     * a nested {@link PageFlowController}.
     * 
     * @return <code>true</code> if this forward will cause a return from nesting.
     */ 
    public boolean isNestedReturn()
    {
        init();
        return _isNestedReturn;
    }
    
    /**
     * Tell whether returning this forward from an action method will cause a previous page
     * to be displayed.
     * 
     * @return <code>true</code> if returning this forward from an action method will cause
     *         a previous page to be displayed.
     */ 
    public boolean isReturnToPage()
    {
        init();
        return _returnToType == RETURN_TO_PREVIOUS_PAGE || _returnToType == RETURN_TO_CURRENT_PAGE
                   || _returnToType == RETURN_TO_PAGE;
    }

    /**
     * Tell whether returning this forward from an action method will cause the previous action
     * to be re-run.
     * 
     * @return <code>true</code> if returning this forward from an action method will cause the
     * previous action to be re-run, i.e., whether the URI returned by {@link #getPath} will end
     * in "<i>previous-action-name</i>.do".
     */ 
    public boolean isReturnToAction()
    {
        init();
        return _returnToType == RETURN_TO_PREVIOUS_ACTION || _returnToType == RETURN_TO_ACTION;
    }

    /**
     * Tell whether this is a redirect to a URI outside of the current web application.
     */ 
    public boolean isExternalRedirect()
    {
        return _externalRedirect;
    }

    /**
     * Specify that this is a redirect to a URI outside of the current web application.
     */ 
    public void setExternalRedirect( boolean externalRedirect )
    {
        _externalRedirect = externalRedirect;
        if ( externalRedirect ) setRedirect( true );
    }

    /**
     * Tell whether this Forward will restore the original query string on the page restored when a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward},
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction},
     * or {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward}
     * with <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousAction Jpf.NavigateTo.previousAction}
     * </code> is used.
     */ 
    public boolean doesRestoreQueryString()
    {
        init();
        return _restoreQueryString;
    }
    
    /**
     * Set whether this Forward will restore the original query string query string on the page restored when a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward},
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction},
     * or {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward}
     * with <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousAction Jpf.NavigateTo.previousAction}
     * </code> is used.
     */ 
    public void setRestoreQueryString( boolean doesRestoreQueryString )
    {
        _restoreQueryString = doesRestoreQueryString;
    }
    
    /**
     * Tell whether the URI returned by {@link #getPath} is for a page flow.
     * 
     * @return <code>true</code> if the URI returned by {@link #getPath} is for a page flow, i.e., 
     * if it ends in ".jpf".
     */ 
    public boolean forwardsToPageFlow()
    {
        return FileUtils.osSensitiveEndsWith( getPath(), PageFlowConstants.PAGEFLOW_EXTENSION );
    }
    
    /**
     * Set the query string that will be appended to the URI returned by {@link #getPath}.
     * 
     * @param queryString the query string that will be appended to the URI.  If this string does not
     *            start with <code>'?'</code>, then this character will be prepended; if the string is
     *            <code>null</code>, the query string will be removed.
     */
    public void setQueryString( String queryString )
    {
        if ( queryString == null || queryString.length() == 0 )
        {
            _queryString = null;
        }
        else if ( queryString.charAt( 0 ) == '?' )
        {
            _queryString = new InternalStringBuilder( queryString );
        }
        else
        {
            _queryString = new InternalStringBuilder( "?" ).append( queryString );
        }
    }
    
    /**
     * Get the query string that will be appended to the URI returned by {@link #getPath}.
     * 
     * @return the query string that will be appended to the URI, or <code>null</code> if there
     *         is no query string.
     */
    public String getQueryString()
    {
        return _queryString != null ? _queryString.toString() : null;
    }
    
    /**
     * Add a query parameter to the URI returned by {@link #getPath}.
     * 
     * @param paramName the name of the query parameter.
     * @param value the value of the query parameter, or <code>null</code> if there is no value.
     */ 
    public void addQueryParam( String paramName, String value )
    {
        if ( _queryString == null )
        {
            _queryString = new InternalStringBuilder( "?" );
        }
        else
        {
            _queryString.append( '&' );
        }
        
        _queryString.append( paramName );
        
        if ( value != null )
        {
            _queryString.append( '=' ).append( value );
        }
    }
    
    /**
     * Add a query parameter with no value to the URI returned by {@link #getPath}.
     * 
     * @param paramName the name of the query parameter.
     */ 
    public final void addQueryParam( String paramName )
    {
        addQueryParam( paramName, null );
    }
    
    /**
     * Adds an action output that will be made available in the request, through {@link PageFlowUtils#getActionOutput}.
     * 
     * @deprecated Use {@link #addActionOutput} instead.
     * @param paramName the name of the action output.
     * @param value the action output value.
     */ 
    public void addPageInput( String paramName, Object value )
    {
        addActionOutput( paramName, value );
    }
    
    /**
     * Adds an action output that will be made available in the request, through {@link PageFlowUtils#getActionOutput}.
     * 
     * @param paramName the name of the action output.
     * @param value the action output value.
     */ 
    public void addActionOutput( String paramName, Object value )
    {
        if ( paramName == null || paramName.length() == 0 )
        {
            throw new IllegalArgumentException( "An action output name may not be null or empty." );
        }
        
        if ( _actionOutputs == null )
        {
            _actionOutputs = new HashMap();
        }
        
        //
        // Throw an exception if this is a redirect, and if there was an action output.  Action outputs are carried
        // in the request, and will be lost on redirects.
        //
        if ( _init && getRedirect() )
        {
            String actionPath = _mappingPath != null ? _mappingPath : "";
            String descrip = getName() != null ? getName() : getPath();
            PageFlowException ex = new IllegalActionOutputException( descrip, actionPath, _flowController, paramName );
            InternalUtils.throwPageFlowException( ex );
        }
        
        _actionOutputs.put( paramName, value );
    }
    
    /**
     * Get all action outputs that have been set on this Forward.
     * 
     * @deprecated Use {@link #getActionOutputs} instead.
     * @return a Map of name/value pairs representing all action outputs.
     * @see #addActionOutput
     */ 
    public Map getPageInputs()
    {
        return getActionOutputs();
    }
    
    /**
     * Get all action outputs that have been set on this Forward.
     * 
     * @return a Map of name/value pairs representing all action outputs.
     * @see #addActionOutput
     */ 
    public Map getActionOutputs()
    {
        return _actionOutputs;
    }
    
    /**
     * Get the type of return, if this is a <code>return-to</code> type.
     * 
     * @return one of the following values: {@link #RETURN_TO_CURRENT_PAGE}, {@link #RETURN_TO_PREVIOUS_PAGE},
     *         {@link #RETURN_TO_PAGE}, {@link #RETURN_TO_PREVIOUS_ACTION}, {@link #RETURN_TO_ACTION}, or
     *         {@link #RETURN_TO_NONE} if this Forward is not a <code>return-to</code> type.
     * @see #isReturnToAction
     * @see #isReturnToPage
     */ 
    public int getReturnToType()
    {
        return _returnToType;
    }
    
    /**
     * Get the type of return as a String, if this is a <code>return-to</code> type.
     * 
     * @return one of the following values: <code>currentPage</code>, <code>previousPage</code>, <code>page</code>,
     *         (deprecated), <code>previousAction</code>, <code>action</code> (deprecated), or <code>null</code>
     *         if this is not a <code>return-to</code> type.
     * @see #isReturnToAction
     * @see #isReturnToPage
     */ 
    public String getReturnToTypeAsString()
    {
        switch ( _returnToType )
        {
            case RETURN_TO_CURRENT_PAGE:
                return RETURN_TO_CURRENT_PAGE_STR;
            
            case RETURN_TO_PREVIOUS_PAGE:
                return RETURN_TO_PREVIOUS_PAGE_STR;
            
            case RETURN_TO_PAGE:
                return RETURN_TO_PAGE_LEGACY_STR;
                
            case RETURN_TO_PREVIOUS_ACTION:
                return RETURN_TO_PREVIOUS_ACTION_STR;
            
            case RETURN_TO_ACTION:
                return RETURN_TO_ACTION_LEGACY_STR;
        }
        
        return null;
    }
    
    void reinitialize( FlowController fc )
    {
        _flowController = fc;
        _servletContext = fc.getServletContext();
        
        if ( _mapping == null && _mappingPath != null )
        {
            ModuleConfig mc = fc.getModuleConfig();
            assert mc != null : "no ModuleConfig found for " + fc.getClass().getName();
            _mapping = ( ActionMapping ) mc.findActionConfig( _mappingPath );
        }
    }

    /**
     * Tell whether this Forward is relative to a particular directory path
     * (as is the case when inheriting local paths from base classes). If
     * this is true, it implies the forward is an inherited global forward,
     * with a &quot;relativeTo&quot; path given in a &lt;set-property&gt;
     * in the forward config.
     * 
     * <p>
     * This is a framework-invoked method that should not normally be called
     * directly.
     * </p>
     */ 
    public boolean hasRelativeToPath()
    {
        return _relativeTo != null;
    }

    /**
     * If this is a local path, change it so it's relative to the given path prefix, and remember that we did it in
     * a flag (_outsidePageFlowDirectory).  This is a framework-invoked method that should not normally be called
     * directly.
     */
    public void initializeRelativePath(ServletRequest request, String relativeTo) 
    {
        if ( relativeTo == null )
        {
            relativeTo = _relativeTo;
        }

        if ( relativeTo == null ) return;

        assert ! relativeTo.endsWith("/") : relativeTo;
        String path = getPath();
         
        // If this is a local (relative) path, prefix it with the given 'relativeTo' path, and save a flag.
        // We save this flag in a local variable because the Forward object may be saved/restored for use again.
        if (! getContextRelative() && ! FileUtils.isAbsoluteURI(path)) {
            assert path.startsWith("/") : path;
            setPath(relativeTo + path);
            setContextRelative(true);
            _outsidePageFlowDirectory = true;
        }
        
        // Store a flag in the request that will prevent another page flow from being initialized implicitly because
        // we're hitting a path within it.  We want to stay in the context of the current page flow.
        if (_outsidePageFlowDirectory) {
            PageFlowRequestWrapper.get(request).setStayInCurrentModule(true);
        }
    }
}
