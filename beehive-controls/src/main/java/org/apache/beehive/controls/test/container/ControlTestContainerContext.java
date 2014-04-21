/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   $Header:$
*/
package org.apache.beehive.controls.test.container;

import java.io.InputStream;
import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextServiceProvider;

import org.apache.beehive.controls.runtime.bean.ControlContainerContext;
import org.apache.beehive.controls.runtime.bean.WebContextFactoryProvider;
import org.apache.beehive.controls.spi.context.ControlBeanContextFactory;

/**
 * ControlContainerContext implementation used to test controls in a standalone JVM.
 */
public class ControlTestContainerContext
    extends ControlContainerContext {

    private transient BeanContextServiceProvider _cbcFactoryProvider;

    public ControlTestContainerContext(){
        //
        // This sets the BeanContextServicesFactory instance on the ControlBeanContext and allows this
        // CCC object to be created with a BeanContextServicesDelegate of the type returned by this factory
        //
        super(WebContextFactoryProvider.WEB_CONTEXT_BCS_FACTORY);
    }

    /**
     * Called by BeanContextSupport superclass during construction and deserialization to
     * initialize subclass transient state
     */
    public void initialize()
    {
        super.initialize();
        // Register an *internal* service that is used to create ControlBeanContext objects for
        // children of this control container
        //
        _cbcFactoryProvider = WebContextFactoryProvider.getProvider();
        addService(ControlBeanContextFactory.class, _cbcFactoryProvider);
    }

    /**
     * Override ControlBeanContext.getService().  A control bean creates its bean context using the
     * ControlBeanContextFactory service provided by this context.  A control bean will attempt to create
     * its context before adding its self to this context as a child. This creates a chicken/egg problem since
     * only a child of a context may request a service from it.
     *
     * This method provides a way to crack the chicken/egg problem by first trying to get the service using the
     * control bean context's getService() method, and if that call returns null and the requested service is
     * the ControlBeanContextFactory then returning an instance of the service provider.
     *
     * @param serviceClass
     * @param selector
     * @return
     */
    public <T> T getService(Class<T> serviceClass, Object selector)
    {
        T service = super.getService(serviceClass, selector);
        if (service == null && serviceClass.equals(ControlBeanContextFactory.class)) {
            return (T)_cbcFactoryProvider.getService(this, this, serviceClass, selector);
        }
        return service;
    }

    public InputStream getResourceAsStream(String name, BeanContextChild bcc) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(name);
        return inputStream;
    }
}
