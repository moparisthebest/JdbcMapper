package org.apache.beehive.samples.spring.factory;

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

import java.beans.beancontext.BeanContext;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;

import org.apache.beehive.controls.api.properties.PropertyMap;
import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.spi.bean.JavaControlFactory;
import org.apache.beehive.controls.runtime.servlet.ServletBeanContext;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The SpringControlFactory class is an implementation of the 
 * <code>org.apache.beehive.controls.api.bean.ControlFactory</code> interface, that
 * enables to be created and initialized from a Spring application context.
 * <p>
 * The SpringControlFactory class extends the basic Java instantation control factory
 * class.  If no Spring bean definition exists for the requested control id/class
 * combination, then basic Java instantation will be used.
 * <p>
 *
 * @see org.apache.beehive.controls.spi.bean.ControlFactory
 * @see org.apache.beehive.controls.spi.bean.JavaControlFactory
 */
public class SpringControlFactory extends JavaControlFactory
{
    /**
     * Instantiates a new ControlBean of the requested class, using mechanisms provided
     * by the Spring bean container.
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
        T bean = null;
        try
        {
            BeanFactory beanFactory = getBeanFactory(context);

            //
            // Look for a match in the bean factory based upon id
            //
            if (id != null) 
            {
                String fullID = null;
                if (context != null && context.getControlBean() != null)
                {
                    String parentID = context.getControlBean().getControlID();
                    if (parentID != null)
                        fullID = parentID + ControlBean.IDSeparator + id;
                }

                //
                // Check absolute ID first, then relative ID for a match
                //
                if (fullID != null && beanFactory.containsBean(fullID))
                    bean = (T)beanFactory.getBean(fullID, beanClass);
                else if (beanFactory.containsBean(id))
                    bean = (T)beanFactory.getBean(id, beanClass);
            }

            //
            // Look for a match based upon bean class name
            //
            String beanClassName = beanClass.getName();
            if (bean == null && beanFactory.containsBean(beanClassName))
                bean = (T)beanFactory.getBean(beanClassName, beanClass);

            if (bean != null)
            {
                //
                // Add the bean to the context passed to the constructor
                //
                if (context != null)
                    context.add(bean);

                //
                // Spring doesn't provide any mechanism to pass dynamic constructor args
                // to Spring bean factory methods.
                //
                if (props != null)
                    throw new ControlException("Providing a PropertyMap to the bean constructor is not supported by SpringControlFactory");
            }
            else
            {
                //
                // Fall back to using standard Java instantation if no Spring configuration
                // could be found.
                // TODO: Have a configuration option where this results in a failure, to avoid
                // masking misconfiguration issues if the expectation is that all usage of
                // Controls is configured via Spring.
                //
                bean = super.instantiate(beanClass, props, context, id);
            }
        }
        catch (Exception e)
        {
            throw new ControlException("Exception creating ControlBean", e);
        }

        return bean;
    }

    /**
     * Returns the Spring BeanFactory instance that should be associated with the current
     * ControlBeanContext.  Returns null if no context could be located.
     *
     * This is marked 'protected' to enable alternative implementations to overrride the
     * behavior for how a factory will be obtained.
     */
    protected BeanFactory getBeanFactory(ControlBeanContext context)
    {
        BeanFactory beanFactory = null;

        //
        // Currently, the thread contex loader is used as the key for a class loader to
        // bean factory cache.   This mirrors the behavior of ClassPathXmlApplicationContext
        // below, which is used to load the BeanFactory.
        //
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        SoftReference<BeanFactory> bfRef = _beanFactoryCache.get(cl);
        if (bfRef != null)
            beanFactory = bfRef.get();
        if (beanFactory != null)
            return beanFactory;

        //
        // If no context was provided to the constructor, associate with the local
        // thread context
        //
        if (context == null)
            context = ControlThreadContext.getContext();

        //
        // Use special handling of the webapp case, to enable the 'standard' Spring webapp
        // config (WEB-INF/applicationContext.xml, or whatever has been configured in the
        // loader servlet) to be used.
        //
        while (context != null && !(context instanceof ServletBeanContext))
        {
            BeanContext parentContext = context.getBeanContext();
            if (parentContext instanceof ControlBeanContext)
                context = (ControlBeanContext)parentContext;
            else
                context = null;
        }
        if (context != null)
        {
            ServletContext servletContext = ((ServletBeanContext)context).getServletContext();
            if (servletContext != null)
                beanFactory = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        }
    
        //
        // Otherwise, simply use a ClassLoader-based search for applicationContext.xml
        //
        if (beanFactory == null)
        {
            beanFactory = new ClassPathXmlApplicationContext(
                                                        new String[] {"applicationContext.xml"});
        }

        //
        // Write the resulting bean factory back (or null) into the bean factory cache
        //
        _beanFactoryCache.put(new SoftReference<ClassLoader>(cl), 
                              new SoftReference<BeanFactory>(beanFactory));
        return beanFactory;
    }

    //
    // Cache the mapping from a given class loader to the associated BeanFactory to use
    // when instantiating beans within it.
    //
    ConcurrentHashMap<SoftReference<ClassLoader>,SoftReference<BeanFactory>> _beanFactoryCache =
        new ConcurrentHashMap<SoftReference<ClassLoader>,SoftReference<BeanFactory>>();
}
