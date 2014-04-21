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
package org.apache.beehive.controls.spi.bean;

import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.beehive.controls.api.properties.PropertyMap;
import org.apache.beehive.controls.api.properties.BeanPropertyMap;
import org.apache.beehive.controls.api.properties.PropertyKey;
import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.ControlException;

/**
 * The SimpleControlFactory class is a default implementation of the 
 * <code>org.apache.beehive.controls.api.bean.ControlFactory</code> interface.  It
 * uses Java reflection to create new control instances.
 *
 * @see org.apache.beehive.controls.api.bean.Controls#instantiate
 * @see org.apache.beehive.controls.spi.bean.ControlFactory
 */
public class JavaControlFactory implements ControlFactory
{
    private static ConcurrentHashMap<Class, Constructor> _constructors = new ConcurrentHashMap<Class, Constructor>();

    private static final Properties _extImplBindings = new Properties();

    private static final String EXT_IMPL_BINDING_CONFIG = "controlbindings.properties";
    private static final String KEY_CONTROL_IMPLEMENTATION = "controlImplementation";

    static
    {
        InputStream is = JavaControlFactory.class.getClassLoader().getResourceAsStream( EXT_IMPL_BINDING_CONFIG );

        if (is != null) {
            try {
                _extImplBindings.load(is);
            }
            catch(IOException ignore) {
                // ignore...
            }
            finally {
                try{is.close();}
                catch(IOException ignore) {
                    // ignore...
                }
            }
        }
    }

    /**
     * Instantiates a new ControlBean of the requested class, using mechanisms provided
     * by a provider-specific JavaBeans framework.
     *
     * @param beanClass the ControlBean class to instantiate
     * @param props an initial set of client-specified properties to associate with the
     *              bean instance.   May be null.
     * @param context the containing ControlBeanContext for the bean, if nested inside of
     *                a container or other control.  May be null to use the current active
     *                execution context.
     * @param id the bean control ID.  Must be unique within the containing context.  If
     *           null, a unique identifier will be auto-generated.
     * @return a new ControlBean instance of the requested class.
     */
    public <T extends ControlBean> T instantiate(Class<T> beanClass,
                                                 PropertyMap props,
                                                 ControlBeanContext context,
                                                 String id)
    {
        String beanClassName = beanClass.getName();

        String extImplBinding = _extImplBindings.getProperty( beanClassName + "_" + id );
        if ( extImplBinding == null )
            extImplBinding = _extImplBindings.getProperty( beanClassName );

        if ( extImplBinding != null )
        {
            BeanPropertyMap bpm = props == null ? new BeanPropertyMap( beanClass ) : new BeanPropertyMap( props );
            PropertyKey propKey = new PropertyKey(org.apache.beehive.controls.api.properties.BaseProperties.class,
                                                  KEY_CONTROL_IMPLEMENTATION);

            bpm.setProperty( propKey, extImplBinding );
            props = bpm;
        }

        T ret = null;
        try
        {
            Constructor<T> ctor = _constructors.get(beanClass);
            if (ctor == null)
            {
                ctor = beanClass.getConstructor(ControlBeanContext.class,
                                                String.class,
                                                PropertyMap.class);

                _constructors.put(beanClass, ctor);
            }
            ret = ctor.newInstance(context, id, props);
        }
        catch (InvocationTargetException ite)
        {
            Throwable t = ite.getCause();
            throw new ControlException("ControlBean constructor exception", t);
        }
        catch (Exception e)
        {
            throw new ControlException("Exception creating ControlBean", e);
        }

        return ret;
    }
}
