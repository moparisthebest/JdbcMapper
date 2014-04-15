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

import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.internal.PageFlowRequestWrapper;
import org.apache.beehive.netui.pageflow.internal.AdapterManager;
import org.apache.beehive.netui.pageflow.config.PageFlowControllerConfig;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.handler.ReloadableClassHandler;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.internal.DiscoveryUtils;
import org.apache.beehive.netui.util.internal.concurrent.InternalConcurrentHashMap;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.PageFlowConfig;
import org.apache.beehive.netui.util.config.bean.ModuleConfigLocatorConfig;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.commons.digester.Digester;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ModuleConfigFactory;
import org.apache.struts.config.impl.ModuleConfigImpl;
import org.apache.struts.util.RequestUtils;
import org.xml.sax.InputSource;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * ActionServlet that automatically registers requested Struts modules based on a set of module configuration file
 * locators.  The user may specify {@link ModuleConfigLocator} classes in /WEB-INF/beehive-netui-config.xml using the
 * <code>&lt;module-config-locators&gt;</code> element.
 */
public class AutoRegisterActionServlet extends ActionServlet
{
    /**
     * @deprecated To register {@link ModuleConfigLocator}s, use the <code>module-config-locators</code> element
     *             in <code>pageflow-config</code> of /WEB-INF/netui-config.xml.
     */ 
    public static String MODULE_CONFIG_LOCATOR_CLASS_ATTR = "moduleConfigLocators";
    
    private static final Logger _log = Logger.getInstance( AutoRegisterActionServlet.class );

    private static final ModuleConfig NONEXISTANT_MODULE_CONFIG = new NonexistantModuleConfig();

    /** Map of module-path to ModuleConfig */
    private Map/*< String, ModuleConfig >*/ _registeredModules =
        new InternalConcurrentHashMap/*< String, ModuleConfig >*/();

    private transient Digester _cachedConfigDigester = null;
    private Map _configParams = null;
    private ModuleConfigLocator[] _moduleConfigLocators = null;
    private ReloadableClassHandler _rch;
    
    public void init()
        throws ServletException
    {
        _rch = Handlers.get(getServletContext()).getReloadableClassHandler();
        setupModuleConfigLocators();
        super.init();
    }
    
    private void setupModuleConfigLocators()
    {
        ModuleConfigLocator[] defaultLocators = getDefaultModuleConfigLocators();
        ArrayList/*< ModuleConfigLocator >*/ locators = new ArrayList/*< ModuleConfigLocator >*/();
        
        for ( int i =0; i < defaultLocators.length; ++i )
        {
            locators.add( defaultLocators[i] );
        }
        
        //
        // Look for ModuleConfigLocators in beehive-netui-config.xml.
        //
        PageFlowConfig pfConfig = ConfigUtil.getConfig().getPageFlowConfig();
        
        if ( pfConfig != null )
        {
            ModuleConfigLocatorConfig[] mcLocators = pfConfig.getModuleConfigLocators();
            
            if ( mcLocators != null )
            {
                for ( int i = 0; i < mcLocators.length; i++ )
                {
                    addModuleConfigLocator( mcLocators[i].getLocatorClass().trim(), locators );
                }
            }
        }
        
        //
        // Look for ModuleConfigLocators specified in web.xml (deprecated method for specifying them).
        //
        String configLocatorList = getServletConfig().getInitParameter( MODULE_CONFIG_LOCATOR_CLASS_ATTR );
        
        if ( configLocatorList != null )
        {
            if ( _log.isWarnEnabled() )
            {
                _log.warn( "Found module-config-locators list in context-parameter " + MODULE_CONFIG_LOCATOR_CLASS_ATTR
                           + ", which is deprecated.  Please use the <module-config-locators> element in "
                           + InternalConstants.NETUI_CONFIG_PATH + " instead." );
            }
            
            String[] configLocatorClassNames = configLocatorList.split( "," );
            
            for ( int i = 0; i < configLocatorClassNames.length; ++i )
            {
                addModuleConfigLocator( configLocatorClassNames[i].trim(), locators );
            }
        }
        
        _moduleConfigLocators = ( ModuleConfigLocator[] ) locators.toArray( new ModuleConfigLocator[locators.size()] ); 
    }
    
    private static void addModuleConfigLocator( String locatorClassName, ArrayList/*< ModuleConfigLocator >*/ locators )
    {
        try
        {
            Class locatorClass = DiscoveryUtils.loadImplementorClass( locatorClassName, ModuleConfigLocator.class );
            
            if ( locatorClass != null )  // previous call will log an error if it can't find the class
            {
                ModuleConfigLocator locator = ( ModuleConfigLocator ) locatorClass.newInstance();
                locators.add( locator );
            }
        }
        catch ( IllegalAccessException e )
        {
            _log.error( "Could not create an instance of specified module-config-locator " + locatorClassName, e );
        }
        catch ( InstantiationException e )
        {
            _log.error( "Could not create an instance of specified module-config-locator " + locatorClassName, e );
        }
    }
    
    /**
     * Get the base list of ModuleConfigLocators, to specify locations for auto-registered Struts modules.  By default,
     * this list is empty; derived classes may override to provide locators, or the user may specify them using
     * the "moduleConfigLocators" init-parameter.  When an unrecognized Struts module is requested, each registered
     * ModuleConfigLocator is queried for a possible path to the configuration file for the module.  If the
     * configuration file is found, the module is auto-registered against the file.
     */
    protected ModuleConfigLocator[] getDefaultModuleConfigLocators()
    {
        return new ModuleConfigLocator[0];
    }
    
    /**
     * Get the current list of registered ModuleConfigLocators.
     * 
     * @return an array of registered ModuleConfigLocators.
     * @see #getDefaultModuleConfigLocators
     */ 
    public ModuleConfigLocator[] getModuleConfigLocators()
    {
        return _moduleConfigLocators;
    }
    
    /**
     * Interface for specifying alternate locations for auto-registered Struts modules.
     * 
     * @see AutoRegisterActionServlet#getDefaultModuleConfigLocators
     */ 
    public static interface ModuleConfigLocator
    {
        /**
         * Get the webapp-relative path to a Struts module config file, based on the module name.
         * 
         * @param moduleName the name of the Struts module, e.g., "someModule" or "some/other/module".
         * @return the webapp-relative path the the Struts module config file.
         */ 
        public String getModuleConfigPath( String moduleName );
    }
    
    /**
     * Get the webapp-relative path to the Struts module configration file for a given module path,
     * based on registered ModuleConfigLocators.
     * 
     * @param modulePath the Struts module path.
     * @return a String that is the path to the Struts configuration file, relative to the web application root,
     *         or <code>null</code> if no appropriate configuration file is found.
     * @see #getDefaultModuleConfigLocators
     */ 
    public String getModuleConfPath( String modulePath )
    {
        if ( _moduleConfigLocators != null )
        {
            for ( int i = 0; i < _moduleConfigLocators.length; ++i )
            {
                ModuleConfigLocator locator = _moduleConfigLocators[i];
                String moduleConfigPath = locator.getModuleConfigPath( modulePath );
                
                try
                {
                    if ( getConfigResource( moduleConfigPath ) != null ) return moduleConfigPath;
                }
                catch ( MalformedURLException e )
                {
                    _log.error( "ModuleConfigLocator " + locator.getClass().getName()
                                + " returned an invalid path: " + moduleConfigPath + '.', e );
                }
            }
        }
        
        return null;
    }
    
    private boolean isAutoLoadModulePath( String modulePath, String prefix )
    {
        if ( _moduleConfigLocators != null )
        {
            for ( int i = 0; i < _moduleConfigLocators.length; ++i )
            {
                ModuleConfigLocator locator = _moduleConfigLocators[i];
                if ( modulePath.equals( locator.getModuleConfigPath( prefix ) ) ) return true;
            }
        }
        
        return false;
    }
    
    //
    // For cases where the servlet is serialized/deserialized, we'll hold onto ServletConfig
    // attributes, since the ServletConfig reference is transient.  Then, we'll return these
    // cached values if necessary in getInitParameter(), getInitParameterNames().
    //
    private void writeObject( ObjectOutputStream stream )
        throws IOException
    {
        if ( _log.isInfoEnabled() )
        {
            _log.info( "serializing ActionServlet " + this );
        }
        
        if ( _configParams != null )
        {
            stream.writeObject( _configParams );
        }
        else
        {
            ServletConfig servletConfig = getServletConfig();
            assert servletConfig != null;
            HashMap params = new HashMap();
            
            for ( Enumeration e = servletConfig.getInitParameterNames(); e.hasMoreElements(); )
            {
                String name = ( String ) e.nextElement();
                params.put( name, servletConfig.getInitParameter( name ) );
            }
            
            stream.writeObject( params );
        }
    }
    
    // See comments on writeObject.
    private void readObject( ObjectInputStream stream )
        throws IOException, ClassNotFoundException
    {
        if ( _log.isInfoEnabled() ) _log.info( "deserializing ActionServlet " + this );
        _configParams = ( Map ) stream.readObject();
    }

    public String getInitParameter( String s )
    {
        if ( getServletConfig() == null )
        {
            assert _configParams != null;   // see comments on writeObject
            return ( String ) _configParams.get( s );
        }
        
        return super.getInitParameter( s );
    }

    public Enumeration getInitParameterNames()
    {
        if ( getServletConfig() == null )
        {
            assert _configParams != null;   // see comments on writeObject
            return Collections.enumeration( _configParams.keySet() );
        }
        
        return super.getInitParameterNames();
    }

    /**
     * This method is almost exactly the same as the base class initModuleConfig.  The only difference
     * is that it does not throw an UnavailableException if a module configuration file is missing or
     * invalid.
     */
    protected ModuleConfig initModuleConfig( String prefix, String paths ) throws ServletException
    {

        if ( _log.isDebugEnabled() )
        {
            _log.debug( "Initializing module path '" + prefix + "' configuration from '" + paths + '\'' );
        }

        // Parse the configuration for this module
        ModuleConfig moduleConfig = null;
        InputStream input = null;
        String mapping;
        
        try
        {
            ModuleConfigFactory factoryObject =
                    ModuleConfigFactory.createFactory();
            moduleConfig = factoryObject.createModuleConfig( prefix );

            // Support for module-wide ActionMapping type override
            mapping = getServletConfig().getInitParameter( "mapping" );
            if ( mapping != null )
            {
                moduleConfig.setActionMappingClass( mapping );
            }

            // Configure the Digester instance we will use
            Digester digester = initConfigDigester();

            // Process each specified resource path
            while ( paths.length() > 0 )
            {
                digester.push( moduleConfig );
                String path;
                int comma = paths.indexOf( ',' );
                if ( comma >= 0 )
                {
                    path = paths.substring( 0, comma ).trim();
                    paths = paths.substring( comma + 1 );
                }
                else
                {
                    path = paths.trim();
                    paths = "";
                }
                if ( path.length() < 1 )
                {
                    break;
                }
                
                URL url = getConfigResource( path );
                
                //
                // THIS IS THE MAIN DIFFERENCE: we're doing a null-check here.
                //
                if ( url != null )
                {
                    URLConnection conn = url.openConnection();
                    conn.setUseCaches(false);
                    InputStream in = conn.getInputStream();
                    
                    try {
                        InputSource is = new InputSource(in);
                        is.setSystemId(url.toString());
                        input = getConfigResourceAsStream( path );
                        is.setByteStream( input );
                        
                        // also, we're not letting it fail here either.
                        try
                        {
                            digester.parse( is );
                            getServletContext().setAttribute( Globals.MODULE_KEY + prefix, moduleConfig );
                        }
                        catch ( Exception e )
                        {
                            _log.error( Bundle.getString( "PageFlow_Struts_ModuleParseError", path ), e );
                        }
                        input.close();
                    } finally {
                        in.close();
                    }
                }
                else
                {
                    //
                    // Special case.  If this is the default (root) module and the module path is one that's normally
                    // generated by the page flow compiler, then we don't want to error out if it's missing, since
                    // this probably just means that there's no root-level page flow.  Set up a default, empty,
                    // module config.
                    //
                    if ( prefix.equals( "" ) && isAutoLoadModulePath( path, prefix ) )
                    {
                        if ( _log.isInfoEnabled() )
                        {
                            _log.info( "There is no root module at " + path + "; initializing a default module." );
                        }
                        
                        //
                        // Set the ControllerConfig to a MissingRootModuleControllerConfig.  This is used by
                        // PageFlowRequestProcessor.
                        //
                        moduleConfig.setControllerConfig( new MissingRootModuleControllerConfig() );
                    }
                    else
                    {
                        _log.error( Bundle.getString( "PageFlow_Struts_MissingModuleConfig", path ) );
                    }
                }
            }

        }
        catch ( Throwable t )
        {
            _log.error( internal.getMessage( "configParse", paths ), t );
            throw new UnavailableException( internal.getMessage( "configParse", paths ) );
        }
        finally
        {
            if ( input != null )
            {
                try
                {
                    input.close();
                }
                catch ( IOException e )
                {
                    // ignore
                }
            }
        }

        // Force creation and registration of DynaActionFormClass instances
        // for all dynamic form beans we will be using
        FormBeanConfig fbs[] = moduleConfig.findFormBeanConfigs();
        for ( int i = 0; i < fbs.length; i++ )
        {
            if ( fbs[i].getDynamic() )
            {
                DynaActionFormClass.createDynaActionFormClass( fbs[i] );
            }
        }

        // Special handling for the default module (for
        // backwards compatibility only, will be removed later)
        if ( prefix.length() < 1 )
        {
            defaultControllerConfig( moduleConfig );
            defaultMessageResourcesConfig( moduleConfig );
        }

        // Return the completed configuration object
        //config.freeze();  // Now done after plugins init
        return moduleConfig;

    }

    static class MissingRootModuleControllerConfig extends ControllerConfig
    {
        public MissingRootModuleControllerConfig()
        {
            setProcessorClass( PageFlowRequestProcessor.class.getName() );
        }
    }

    /**
     * Get a resource URL for a module configuration file.  By default, this looks in the ServletContext
     * and in the context classloader.
     * 
     * @param path the path to the resource.
     * @return an URL for the resource, or <code>null</code> if the resource is not found.
     * @throws MalformedURLException
     */ 
    protected URL getConfigResource( String path )
        throws MalformedURLException
    {
        URL resource = getServletContext().getResource( path );
        if ( resource != null ) return resource;
        if ( path.startsWith( "/" ) ) path = path.substring( 1 );
        return _rch.getResource( path );
    }

    /**
     * Get a resource stream for a module configuration file.  By default, this looks in the ServletContext
     * and in the context classloader.
     * 
     * @param path the path to the resource.
     * @return an InputStream for the resource, or <code>null</code> if the resource is not found.
     */ 
    protected InputStream getConfigResourceAsStream( String path )
    {
        InputStream stream = getServletContext().getResourceAsStream( path );
        if ( stream != null ) return stream;
        if ( path.startsWith( "/" ) ) path = path.substring( 1 );
        return _rch.getResourceAsStream( path );
    }
    
    /**
     * Register a Struts module, initialized by the given configuration file.
     * 
     * @param modulePath the module path, starting at the webapp root, e.g., "/info/help".
     * @param configFilePath the path, starting at the webapp root, to the module configuration
     *        file (e.g., "/WEB-INF/my-generated-struts-config-info-help.xml").
     * @return the Struts ModuleConfig that was initialized.
     */
    protected synchronized ModuleConfig registerModule( String modulePath, String configFilePath )
        throws ServletException
    {
        if ( _log.isInfoEnabled() )
        {
            _log.info( "Dynamically registering module " + modulePath + ", config XML " + configFilePath );
        }
        
        if ( _log.isInfoEnabled() )
        {
            InternalStringBuilder msg = new InternalStringBuilder( "Dynamically registering module " ).append( modulePath );
            _log.info( msg.append( ", config XML " ).append( configFilePath ).toString() );
        }

        if ( _cachedConfigDigester == null )
        {
            _cachedConfigDigester = initConfigDigester();
        }

        configDigester = _cachedConfigDigester;
        ModuleConfig ac = initModuleConfig( modulePath, configFilePath );
        initModuleMessageResources( ac );
        initModuleDataSources( ac );
        initModulePlugIns( ac );
        ac.freeze();
        configDigester = null;

        // If this is a FlowController module, make a callback to the event reporter.
        ControllerConfig cc = ac.getControllerConfig();
        if ( cc instanceof PageFlowControllerConfig )
        {
            PageFlowControllerConfig pfcc = ( PageFlowControllerConfig ) cc;
            PageFlowEventReporter er = AdapterManager.getServletContainerAdapter( getServletContext() ).getEventReporter();
            er.flowControllerRegistered( modulePath, pfcc.getControllerClass(), ac );
        }
        
        // Initialize any delegating action configs or exception handler configs.
        InternalUtils.initDelegatingConfigs(ac, getServletContext());
        
        if ( _log.isDebugEnabled() )
        {
            _log.debug( "Finished registering module " + modulePath + ", config XML " + configFilePath );
        }
        
        return ac;
    }

    /**
     * This override of the base class process() registers a Struts module on the fly if the
     * config file can be found in our standard place (named in our standard way), regardless
     * of whether the module is configured in web.xml.
     */
    protected void process( HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        //
        // First wrap the request with an object that contains request-scoped values that our runtime uses.  This
        // is faster than sticking everything into attributes on the request (then basically reading from a HashMap).
        //
        PageFlowRequestWrapper requestWrapper = PageFlowRequestWrapper.wrapRequest( request );
        request = requestWrapper;
        
        ServletContext servletContext = getServletContext();
        String modulePath = PageFlowUtils.getModulePathForRelativeURI( InternalUtils.getDecodedServletPath( request ) );
        ModuleConfig registeredApp;
        
        //
        // Get the registered Struts module for the request.
        //
        registeredApp = getModuleConfig( modulePath, request, response );
        
        //
        // If we've dynamically registered a module, then we need to override the base process() behavior to select the
        // module.  Note that we don't want to synchronize the call to process().
        //
        if ( registeredApp != null )
        {
            //
            // Try to select the appropriate Struts module and delegate to its RequestProcessor.
            //
            ModuleConfig moduleConfig = InternalUtils.selectModule( modulePath, request, servletContext );
            
            // If this module came from an abstract page flow controller class, send an error.
            ControllerConfig cc = moduleConfig.getControllerConfig();
            if (cc instanceof PageFlowControllerConfig && ((PageFlowControllerConfig) cc).isAbstract()) {
                InternalUtils.sendDevTimeError( "PageFlow_AbstractPageFlow", null, HttpServletResponse.SC_NOT_FOUND, 
                                                request, response, servletContext,
                                                new Object[]{ modulePath } );
                return;
            }
                    
            RequestProcessor requestProcessor = getRequestProcessor( moduleConfig );
            requestProcessor.process( request, response );
        }
        else
        {
            //
            // Here, we're checking to see if this was a module that was registered externally by Struts (not by this
            // servlet).  This is the same as the base process() behavior, but it checks for a missing
            // module-configuration.
            //
            ModuleConfig moduleConfig = null;
            
            if ( InternalUtils.getModuleConfig( RequestUtils.getModuleName( request, servletContext ), servletContext ) != null )
            {
                String modulePrefix = RequestUtils.getModuleName( request, servletContext );
                moduleConfig = InternalUtils.selectModule( modulePrefix, request, servletContext );
            }
            
            String servletPath = InternalUtils.getDecodedServletPath( request );
            RequestProcessor rp = moduleConfig != null ? getRequestProcessor( moduleConfig ) : null;
            
            if ( rp != null && moduleCanHandlePath( moduleConfig, rp, servletPath ) )
            {
                rp.process( request, response );
            }
            else
            {
                //
                // Initialize the ServletContext in the request.  Often, we need access to the ServletContext when we only
                // have a ServletRequest.
                //
                InternalUtils.setServletContext( request, getServletContext() );
                
                if ( processUnhandledAction( request, response, servletPath ) ) return;
                
                String originalServletPath = requestWrapper.getOriginalServletPath();
                if ( originalServletPath != null )
                {
                    servletPath = originalServletPath;
                    modulePath = PageFlowUtils.getModulePathForRelativeURI( originalServletPath );
                }
                
                if ( _log.isErrorEnabled() )
                {
                    InternalStringBuilder msg = new InternalStringBuilder( "No module configuration registered for " );
                    msg.append( servletPath ).append( " (module path " ).append( modulePath ).append( ")." );
                    _log.error( msg.toString() );
                }

                //
                // If we're not in production mode, send a diagnostic on the response; otherwise, simply send a 404.
                //
                if ( modulePath.length() == 0 ) modulePath = "/";
                InternalUtils.sendDevTimeError( "PageFlow_NoModuleConf", null, HttpServletResponse.SC_NOT_FOUND, 
                                                request, response, servletContext,
                                                new Object[]{ servletPath, modulePath } );
            }
        }
    }
 
    /**
     * Tell whether the given module can handle the given path.  By default, this is always true.
     */
    protected boolean moduleCanHandlePath( ModuleConfig moduleConfig, RequestProcessor rp, String servletPath )
    {
        return true;
    }
    
    /**
     * @exclude
     */    
    protected Digester initConfigDigester() throws ServletException
    {
        _cachedConfigDigester = super.initConfigDigester();
        return _cachedConfigDigester;
    }
    
    public void destroy()
    {
        _registeredModules.clear();
        super.destroy();
    }

    /**
     * Get the Struts ModuleConfig for the given module path.
     * @deprecated Use {@link #ensureModuleRegistered} instead.
     * 
     * @param modulePath the module path, from the request URI.
     * @param request the current ServletRequest
     * @param response the current HttpServletResponse
     * @return the Struts ModuleConfig that corresponds with <code>modulePath</code>
     * @throws IOException
     * @throws ServletException
     */ 
    protected ModuleConfig getModuleConfig( String modulePath, ServletRequest request, ServletResponse response )
        throws IOException, ServletException
    {
        return ensureModuleRegistered(modulePath);
    }
    
    /**
     * Ensures that the Struts module for the given path is registered (dynamically, if necessary).
     * @deprecated Use #ensureModuleRegistered(String) instead.
     * 
     * @param modulePath the module path, from the request URI.
     * @param request the current ServletRequest
     * @throws IOException
     * @throws ServletException
     */ 
    public ModuleConfig ensureModuleRegistered( String modulePath, ServletRequest request )
        throws IOException, ServletException
    {
        return ensureModuleRegistered(modulePath);
    }
    
    public ModuleConfig ensureModuleRegistered( String modulePath )
            throws IOException, ServletException
    {
        //
        // Dynamically register the Struts module, if appropriate.  If we've already
        // tried to register it (_registeredModules.containsKey( modulePath )), don't
        // try again.
        //
        // Note that two threads could potentially get in here at the same time, and
        // both will register the module.  This is OK -- reads from _registeredModules
        // are consistent, and the worst that will happen is that the module will get
        // registered with a valid ModuleConfig a few times.
        //
        ModuleConfig mc = ( ModuleConfig ) _registeredModules.get( modulePath );
        
        if ( mc == null )
        {
            //
            // See if there's an explicit initialization for this module in
            // the webapp configuration.  If there is, we'll use that.
            //
            mc = ( ModuleConfig ) getServletContext().getAttribute( Globals.MODULE_KEY + modulePath );

            if ( mc == null )
            {
                //
                // If we find the Struts config file for this module, we can dynamically
                // register it.
                //
                String moduleConfPath = getModuleConfPath( modulePath );
                
                if ( moduleConfPath != null )
                {
                    mc = registerModule( modulePath, moduleConfPath );
                }
            }

            if ( mc == null )
            {
                _registeredModules.put( modulePath, NONEXISTANT_MODULE_CONFIG );
                                                   // ConcurrentHashMap doesn't allow null values
                return null;
            }
            else
            {
                _registeredModules.put( modulePath, mc );
                getServletContext().setAttribute( Globals.MODULE_KEY + modulePath, mc );
                return mc;
            }
        }
        else
        {
            if (mc.getPrefix() == null) {
                assert mc instanceof NonexistantModuleConfig : mc.getClass().getName();
                return null;
            }
            
            return mc;
        }
    }
    
    private static class NonexistantModuleConfig extends ModuleConfigImpl
    {
        public NonexistantModuleConfig()
        {
            super( null );
        }
    }
    
    //-----------------------------------------------------------------------------------------------------------
    // The following methods (defaultControllerConfig, defaultMessageResourcesConfig, defaultFormBeansConfig,
    // defaultForwardsConfig, defaultMappingsConfig) were copied straight from the Struts ActionServlet.java
    // (they're private, not protected).
    
    /**
     * Perform backwards-compatible configuration of the default module's
     * controller configuration from servlet initialization parameters (as
     * were used in Struts 1.0).
     *
     * @param config The ModuleConfig object for the default module
     *
     * @since Struts 1.1
     * @deprecated Will be removed in a release after Struts 1.1.
     */
    private void defaultControllerConfig(ModuleConfig config) {

        String value = null;
        ControllerConfig cc = config.getControllerConfig();
        
        value = getServletConfig().getInitParameter("bufferSize");
        if (value != null) {
            cc.setBufferSize(Integer.parseInt(value));
        }
        
        value = getServletConfig().getInitParameter("content");
        if (value != null) {
            cc.setContentType(value);
        }
        
        value = getServletConfig().getInitParameter("locale");        
        // must check for null here 
        if (value != null) {
            if ("true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value)) {
                cc.setLocale(true);
            } else {
                cc.setLocale(false);
            }
        } 
        
        value = getServletConfig().getInitParameter("maxFileSize");
        if (value != null) {
            cc.setMaxFileSize(value);
        }
        
        value = getServletConfig().getInitParameter("nocache");
        if (value != null) {
            if ("true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value)) {
                cc.setNocache(true);
            } else {
                cc.setNocache(false);
            }
        }
        
        value = getServletConfig().getInitParameter("multipartClass");
        if (value != null) {
            cc.setMultipartClass(value);
        }
        
        value = getServletConfig().getInitParameter("tempDir");
        if (value != null) {
            cc.setTempDir(value);
        }

    }

    /**
     * Perform backwards-compatible configuration of the default module's
     * message resources configuration from servlet initialization parameters
     * (as were used in Struts 1.0).
     *
     * @param config The ModuleConfig object for the default module
     *
     * @since Struts 1.1
     * @deprecated Will be removed in a release after Struts 1.1.
     */
    private void defaultMessageResourcesConfig(ModuleConfig config) {

        String value = null;

        MessageResourcesConfig mrc =
            config.findMessageResourcesConfig(Globals.MESSAGES_KEY);
        if (mrc == null) {
            mrc = new MessageResourcesConfig();
            mrc.setKey(Globals.MESSAGES_KEY);
            config.addMessageResourcesConfig(mrc);
        }
        value = getServletConfig().getInitParameter("application");
        if (value != null) {
            mrc.setParameter(value);
        }
        value= getServletConfig().getInitParameter("factory");
        if (value != null) {
            mrc.setFactory(value);
        }
        value = getServletConfig().getInitParameter("null");
        if (value != null) {
            if (value.equalsIgnoreCase("true") ||
                value.equalsIgnoreCase("yes")) {
                mrc.setNull(true);
            } else {
                mrc.setNull(false);
            }
        }
    }
    
    /**
     * Clear the internal map of registered modules.
     * 
     * @exclude
     */ 
    public void clearRegisteredModules()
    {
        ServletContext servletContext = getServletContext();
        
        for ( Iterator ii = _registeredModules.keySet().iterator(); ii.hasNext(); )  
        {
            String modulePrefix = ( String ) ii.next();
            servletContext.removeAttribute( Globals.MODULE_KEY + modulePrefix );
            servletContext.removeAttribute( Globals.REQUEST_PROCESSOR_KEY + modulePrefix );
        }
        
        _registeredModules.clear();
    }
    
    /**
     * Last chance to handle an unhandled action URI.
     * @return <code>true</code> if this method handled it (by forwarding somewhere or writing to the response).
     */ 
    protected boolean processUnhandledAction( HttpServletRequest request, HttpServletResponse response, String uri )
            throws IOException, ServletException
    {
        return false;
    }
}
