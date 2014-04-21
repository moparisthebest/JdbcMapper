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
package org.apache.beehive.controls.api.bean;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.apache.beehive.controls.api.properties.PropertyMap;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.spi.bean.ControlFactory;
import org.apache.beehive.controls.spi.bean.JavaControlFactory;
import org.apache.commons.discovery.tools.DiscoverClass;

/**
 * Helper class for using controls.  Includes static methods to help instantiate controls, and initialize
 * declarative control clients.
 */
public class Controls
{
    final private static String DEFAULT_FACTORY_CLASS = JavaControlFactory.class.getName();

    /**
     * Factory method for instantiating controls.  Controls instantiated using this method will be associated with the
     * current thread-local ControlBeanContext (possibly none), and have an auto-generated ID.
     *
     * @param cl the classloader used to load the ControlBean.  If null, the system classloader will be used.
     * @param beanName the fully qualified name of the ControlBean class.
     * @param props an optional PropertyMap containing initial property values for the control.  May be null.
     * @return an instance of the specified ControlBean.
     * @throws ClassNotFoundException
     */
    public static ControlBean instantiate( ClassLoader cl,
                                           String beanName,
                                           PropertyMap props )
        throws ClassNotFoundException
    {
        return instantiate( cl, beanName, props, null, null );
    }

    /**
     * Factory method for instantiating controls.
     *
     * @param cl the classloader used to load the ControlBean.  If null, the system classloader will be used.
     * @param beanName the fully qualified name of the ControlBean class.
     * @param props an optional PropertyMap containing initial property values for the control.  May be null.
     * @param cbc the ControlBeanContext that will nest the created control.  If null, the thread-local context
     *            (possibly none) will be used.
     * @param id a unique ID for the created control.  If null, an ID will be auto-generated.
     * @return an instance of the specified ControlBean.
     * @throws ClassNotFoundException
     */
    public static ControlBean instantiate( ClassLoader cl,
                                           String beanName,
                                           PropertyMap props,
                                           ControlBeanContext cbc,
                                           String id )
        throws ClassNotFoundException
    {
        Class beanClass = ( cl == null ) ? Class.forName( beanName ) : cl.loadClass( beanName );
        return instantiate(beanClass, props, cbc, id);
    }

    /**
     * Factory method for instantiating controls.
     *
     * @param beanClass the ControlBean class to instantiate
     * @param props an optional PropertyMap containing initial property values for the control.  
     * may be null.
     * @param context the ControlBeanContext that will nest the created control.  If null, the 
     * thread-local context (possibly none) will be used.
     * @param id a unique ID for the created control.  If null, an ID will be auto-generated.
     * @return an instance of the specified ControlBean.
     */
    public static <T extends ControlBean> T instantiate( Class<T> beanClass,
                                                         PropertyMap props,
                                                         ControlBeanContext context,
                                                         String id )
    {
        try
        {
            DiscoverClass discoverer = new DiscoverClass();
            Class factoryClass = discoverer.find( ControlFactory.class, DEFAULT_FACTORY_CLASS );
            ControlFactory factory = (ControlFactory)factoryClass.newInstance();
            return factory.instantiate( beanClass, props, context, id );
        }
        catch ( Exception e )
        {
            throw new ControlException( "Exception creating ControlBean", e );
        }
    }

    /**
     * Helper method for initializing instances of declarative control clients (objects that use controls via @Control
     * and @EventHandler annotations).  This method runs the client-specific generated ClientInitializer class to do
     * its initialization work.
     *
     * @param cl the classloader used to load the ClientInitializer.  If null, defaults to the classloader used to
     *           load the client object being initialized.
     * @param client the client object being initialized.
     * @param cbc the ControlBeanContext to be associated with the client object (that will nest the controls the client
     *            defines).  If null, the thread-local context (possibly none) will be used.
     * @throws ControlException
     * @throws ClassNotFoundException
     */
    public static void initializeClient( ClassLoader cl, Object client, ControlBeanContext cbc )
        throws ClassNotFoundException
    {
        Class clientClass = client.getClass();
        String clientName = clientClass.getName();

        if ( cl == null )
            cl = clientClass.getClassLoader();

        String initName = clientName + "ClientInitializer";
        Class initClass = cl.loadClass( initName );

        try
        {
            Method m = initClass.getMethod( "initialize", ControlBeanContext.class, clientClass );
            m.invoke(null, cbc, client );
        }
        catch ( Throwable e )
        {
            if ( e instanceof InvocationTargetException )
            {
                if ( e.getCause() != null )
                {
                    e = e.getCause();
                }
            }
                
            throw new ControlException( "Exception trying to run client initializer: " + e.getClass().getName() + ", " +
                                        e.getMessage(), e );
        }
    }
}
