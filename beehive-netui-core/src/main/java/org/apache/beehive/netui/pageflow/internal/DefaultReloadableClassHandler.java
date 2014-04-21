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
package org.apache.beehive.netui.pageflow.internal;

import org.apache.beehive.netui.pageflow.AutoRegisterActionServlet;
import org.apache.beehive.netui.pageflow.ServletContainerAdapter;
import org.apache.beehive.netui.pageflow.RequestContext;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.handler.ReloadableClassHandler;
import org.apache.beehive.netui.pageflow.handler.HandlerConfig;
import org.apache.beehive.netui.pageflow.handler.Handler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.util.internal.BouncyClassLoader;
import org.apache.beehive.netui.util.internal.cache.ClassLevelCache;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.internal.DiscoveryUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.beehive.netui.util.internal.concurrent.InternalConcurrentHashMap;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.struts.action.ActionServlet;


/**
 * Handler that can load classes through a classloader that gets dropped/refreshed when time stamps on binary files
 * change.
 */
public class DefaultReloadableClassHandler
        extends DefaultHandler
    implements ReloadableClassHandler
{
    private static final Logger _log = Logger.getInstance( DefaultReloadableClassHandler.class );
    private static final boolean _enabled = System.getProperty("pageflow.bouncy") != null; 
    
    private transient BouncyClassLoader _pageFlowClassLoader = null;
    
    public DefaultReloadableClassHandler( ServletContext servletContext )
    {
        init( null, null, servletContext );

    }

    public void init(HandlerConfig handlerConfig, Handler previousHandler, ServletContext servletContext)
    {
        super.init(handlerConfig, previousHandler, servletContext);
        
        // This feature is in prototype mode, and only enabled through a System property for now.
        if (_enabled) {
            _log.info("Reloadable classes are enabled.");
            createClassLoader(servletContext);
        }
    }

    public void reinit(ServletContext servletContext) {
        super.reinit(servletContext);

        if (_enabled && _pageFlowClassLoader == null) {
            createClassLoader(servletContext);
        }
    }

    private void createClassLoader(ServletContext servletContext) 
    {
        ServletContainerAdapter servletContainerAdapter = AdapterManager.getServletContainerAdapter( servletContext );
        
        if (servletContainerAdapter.isInProductionMode()) {
            _log.info("In production mode; reloadable classes disabled.");
            return;
        }
        
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        File[] classDirs = null;
        
        // TODO: make this configurable in netui-config.xml.  You should be able to specify absolute files
        // and also context-relative paths.
        {
            ArrayList classDirsList = new ArrayList();
            
            String path = servletContext.getRealPath( "/WEB-INF/reloadableClasses" );
            if ( path != null ) {
                File file = new File( path );
                if ( file.isDirectory() ) {
                    classDirsList.add(file);
                }
            }
            
            path = servletContext.getRealPath( "/WEB-INF/classes" );
            if ( path != null ) {
                File file = new File( path );
                if ( file.isDirectory() ) {
                    classDirsList.add(file);
                }
            }
            
            if (! classDirsList.isEmpty()) {
                classDirs = (File[]) classDirsList.toArray(new File[classDirsList.size()]);
            }
        }
        
        if ( classDirs != null )
        {
            _pageFlowClassLoader = new BouncyClassLoader( classDirs, contextClassLoader );
            
            StringBuffer message = new StringBuffer("Reloadable Page Flow classes enabled; using class directories ");
            for (int i = 0; i < classDirs.length; i++) {
                if (i != 0) {
                    message.append(", ");
                }
                message.append(classDirs[i]);
            }
            // TODO: remove the following println when this feature is done.
            System.out.println("*** " + message);
            _log.info(message);
        }
    }
    
    public Object newInstance( String className )
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return getRegisteredReloadableClassHandler().loadClass( className ).newInstance();
    }
    
    private static Map/*< String, Class >*/ _loadedClasses = new InternalConcurrentHashMap/*< String, Class >*/();
    private static class Null {}
    private static Class NULL_CLASS = Null.class;
    
    public Class loadCachedClass( String className )
    {
        Class clazz = ( Class ) _loadedClasses.get( className );
        
        if ( clazz != null )
        {
            return clazz != NULL_CLASS ? clazz : null;
        }
        else
        {
            try
            {
                clazz = getRegisteredReloadableClassHandler().loadClass( className );
                _loadedClasses.put( className, clazz );
                return clazz;
            }
            catch ( ClassNotFoundException e )
            {
                _loadedClasses.put( className, NULL_CLASS );
                return null;
            }
        }
    }
    
    public Class loadClass( String className )
        throws ClassNotFoundException
    {
        if ( _pageFlowClassLoader != null )
        {
            synchronized ( this )
            {
                return _pageFlowClassLoader.loadClass( className );
            }
        }
        
        return DiscoveryUtils.getClassLoader().loadClass( className );
    }

    public URL getResource(String name)
    {
        if (_pageFlowClassLoader != null) {
            synchronized (this) {
                return _pageFlowClassLoader.getResource(name);
            }
        }
        
        return DiscoveryUtils.getClassLoader().getResource(name);
    }

    public InputStream getResourceAsStream(String name) {
        if (_pageFlowClassLoader != null) {
            synchronized (this) {
                return _pageFlowClassLoader.getResourceAsStream(name);
            }
        }
        
        return DiscoveryUtils.getClassLoader().getResourceAsStream(name);
    }

    public void reloadClasses( RequestContext context )
    {
        if ( _pageFlowClassLoader == null )
        {
            return;
        }
        
        synchronized ( this )
        {
            if ( _pageFlowClassLoader.isStale() )
            {
                // TODO: remove the following println when this feature is done.
                System.out.println("*** Classes/resources modified; bouncing classloader.");
        
                _log.info( "Classes/resources modified; bouncing classloader." );
                
                // First go through the session and request and remove any attributes whose classes were loaded by the
                // stale classloader.
                ServletRequest request = context.getRequest();
                removeSessionAttributes(request);
                removeRequestAttributes(request);
                removeRequestAttributes(ScopedServletUtils.getOuterServletRequest(request));
                
                // Remove any deferred storage attributes.
                Handlers.get(getServletContext()).getStorageHandler().dropChanges(context);
                
                // Clear all caches of methods, etc.
                ClassLevelCache.clearAll();
                
                // Clear out all registered modules from the ActionServlet.
                ActionServlet actionServlet = InternalUtils.getActionServlet(getServletContext());
                
                if (actionServlet instanceof AutoRegisterActionServlet) {
                    ((AutoRegisterActionServlet) actionServlet).clearRegisteredModules();
                }
                
                // Bounce the classloader.
                init( getConfig(), getPreviousHandler(), getServletContext() );
            }
        }
    }
    
    private void removeSessionAttributes(ServletRequest request)
    {
        HttpSession session = InternalUtils.getHttpSession(request, false);

        if (session != null) {
            ArrayList attrsToRemove = new ArrayList();

            for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();) {
                String attrName = (String) e.nextElement();
                Object attr = session.getAttribute(attrName);
                if (attr.getClass().getClassLoader() == _pageFlowClassLoader) {
                    attrsToRemove.add(attrName);
                }
            }

            for (Iterator i = attrsToRemove.iterator(); i.hasNext();) {
                String attrName = (String) i.next();
                if (_log.isDebugEnabled()) {
                    _log.debug("Removing session attribute " + attrName + " because its ClassLoader is being bounced.");
                }

                session.removeAttribute(attrName);
            }
        }
    }
    
    private void removeRequestAttributes(ServletRequest request)
    {
        ArrayList attrsToRemove = new ArrayList();

        for (Enumeration e = request.getAttributeNames(); e.hasMoreElements();) {
            String attrName = (String) e.nextElement();
            Object attr = request.getAttribute(attrName);
            if (attr.getClass().getClassLoader() == _pageFlowClassLoader) {
                attrsToRemove.add(attrName);
            }
        }

        for (Iterator i = attrsToRemove.iterator(); i.hasNext();) {
            String attrName = (String) i.next();
            if (_log.isDebugEnabled()) {
                _log.debug("Removing request attribute " + attrName + " because its ClassLoader is being bounced.");
            }

            request.removeAttribute(attrName);
        }
    }
    
    public ClassLoader getClassLoader()
    {
        if ( _pageFlowClassLoader != null )
        {
            synchronized ( this )
            {
                return _pageFlowClassLoader;
            }
        }
        
        return _pageFlowClassLoader;
    }
    
    public boolean isReloadEnabled()
    {
        return _enabled;
    }
    
    public ReloadableClassHandler getRegisteredReloadableClassHandler()
    {
        return ( ReloadableClassHandler ) super.getRegisteredHandler();
    }
}
