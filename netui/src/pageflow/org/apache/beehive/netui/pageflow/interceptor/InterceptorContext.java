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
package org.apache.beehive.netui.pageflow.interceptor;

import org.apache.beehive.netui.util.config.bean.CustomPropertyConfig;
import org.apache.beehive.netui.util.internal.DiscoveryUtils;
import org.apache.beehive.netui.util.logging.Logger;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Context object that encapsulates the configuration for an entire interceptor chain.
 * </p>
 */
public class InterceptorContext
    implements Serializable
{
    private static final Logger _log = Logger.getInstance( InterceptorContext.class );

    private Object _resultOverride;
    private Interceptor _overridingInterceptor;

    public void setResultOverride( Object newResult, Interceptor interceptor )
    {
        _resultOverride = newResult;
        _overridingInterceptor = interceptor;
    }

    public boolean hasResultOverride()
    {
        return _overridingInterceptor != null;
    }

    public Object getResultOverride()
    {
        return _resultOverride;
    }

    public Interceptor getOverridingInterceptor()
    {
        return _overridingInterceptor;
    }

    /**
     * Add an {@link Interceptor} to the list of interceptors.
     * @param configBeans an array of JavaBeans that configure a set of interceptors
     * @param interceptorsList the list of {@link Interceptor} instances
     * @param baseClassOrInterface
     */
    protected static void addInterceptors( org.apache.beehive.netui.util.config.bean.InterceptorConfig[] configBeans,
                                           List/*< Interceptor >*/ interceptorsList,
                                           Class baseClassOrInterface )
    {
        if ( configBeans != null )
        {
            for ( int i = 0; i < configBeans.length; i++ )
            {
                org.apache.beehive.netui.util.config.bean.InterceptorConfig configBean = configBeans[i];
                String className = configBean.getInterceptorClass();
                InterceptorConfig config = new InterceptorConfig( className );
                CustomPropertyConfig[] customProps = configBean.getCustomProperties();

                if ( customProps != null )
                {
                    for ( int j = 0; j < customProps.length; j++ )
                    {
                        CustomPropertyConfig customProp = customProps[j];
                        config.addCustomProperty( customProp.getName(), customProp.getValue() );
                    }
                }
                
                addInterceptor( config, baseClassOrInterface, interceptorsList );
            }
        }
    }
    
    /**
     * Instantiates an interceptor, based on the class name in the given InterceptorConfig, and adds it to the
     * given collection of interceptors.
     * 
     * @param config the InterceptorConfig used to determine the interceptor class.
     * @param baseClassOrInterface the required base class or interface.  May be <code>null</code>.
     * @param interceptors the List of interceptors to which to add.
     * @return an initialized Interceptor, or <code>null</code> if an error occurred.
     */ 
    protected static Interceptor addInterceptor( InterceptorConfig config,
                                                 Class baseClassOrInterface,
                                                 List/*< Interceptor >*/ interceptors )
    {
        Interceptor interceptor = createInterceptor( config, baseClassOrInterface );
        if ( interceptor != null )
            interceptors.add( interceptor );
        return interceptor;
    }
    
    /**
     * Instantiates an interceptor using the class name in the given InterceptorConfig.
     * 
     * @param config the {@link InterceptorConfig} used to determine the {@link Interceptor} class.
     * @param baseClassOrInterface the required base class or interface.  May be <code>null</code>.
     * @return an initialized Interceptor, or <code>null</code> if an error occurred.
     */ 
    protected static Interceptor createInterceptor( InterceptorConfig config, Class baseClassOrInterface )
    {
        assert Interceptor.class.isAssignableFrom( baseClassOrInterface )
                : baseClassOrInterface.getName() + " cannot be assigned to " + Interceptor.class.getName();
        
        ClassLoader cl = DiscoveryUtils.getClassLoader();
        String className = config.getInterceptorClass();
        
        try
        {
            Class interceptorClass = cl.loadClass( className );
            
            if ( ! baseClassOrInterface.isAssignableFrom( interceptorClass ) )
            {
                _log.error( "Interceptor " + interceptorClass.getName() + " does not implement or extend "
                            + baseClassOrInterface.getName() );
                return null;
            }
            
            Interceptor interceptor = ( Interceptor ) interceptorClass.newInstance();
            interceptor.init( config );
            return interceptor;
        }
        catch ( ClassNotFoundException e )
        {
            _log.error( "Could not find interceptor class " + className, e );
        }
        catch ( InstantiationException e )
        {
            _log.error( "Could not instantiate interceptor class " + className, e );
        }
        catch ( IllegalAccessException e )
        {
            _log.error( "Could not instantiate interceptor class " + className, e );
        }
        
        return null;
    }
    
}
