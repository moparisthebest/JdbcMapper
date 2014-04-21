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

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.properties.PropertyMap;

/**
 * The ControlFactory interface defines a service provider interface for integrating
 * an external JavaBean instantation/configuration framework with the Controls runtime.
 */
public interface ControlFactory
{
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
                                                 String id);
}
