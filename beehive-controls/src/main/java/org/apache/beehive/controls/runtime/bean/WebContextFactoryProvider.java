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
package org.apache.beehive.controls.runtime.bean;

import java.beans.beancontext.BeanContextServices;
import java.beans.beancontext.BeanContextServiceProvider;
import java.util.Iterator;
import java.util.Collections;

import org.apache.beehive.controls.spi.context.ControlBeanContextFactory;
import org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport;

/**
 * <p>
 * This class acts as a ControlBeanContextFactoryProvider that exposes this factory as a contextual service
 * from inside of a ControlBeanContext.
 * </p>
 * <p>
 * <b>Note:</b> This class, the service provider, and the contextual service it provides are considerd an implementation
 * detail and <b>should not</b> be used from user code.
 * </p>
 */
public class WebContextFactoryProvider
    implements BeanContextServiceProvider {

    private static final WebContextFactoryProvider theProvider = new WebContextFactoryProvider();
    private static final WebControlBeanContextFactory theFactory = new WebControlBeanContextFactory();

    public static final ControlBeanContext.BeanContextServicesFactory WEB_CONTEXT_BCS_FACTORY =
        new WebContextBeanContextServicesFactory();

    public static BeanContextServiceProvider getProvider() {
        return theProvider;
    }

    private WebContextFactoryProvider() {
    }

    public Object getService(BeanContextServices bcs, Object requestor, Class serviceClass, Object serviceSelector) {
        return theFactory;
    }

    public void releaseService(BeanContextServices bcs, Object requestor, Object service) {
    }

    public Iterator getCurrentServiceSelectors(BeanContextServices bcs, Class serviceClass) {
        return Collections.EMPTY_LIST.iterator();
    }

    /**
     * <p>
     * {@link ControlBeanContextFactory} implementation that provides a {@link ControlBeanContext} object
     * used for web-tier control containment.
     * </p>
     * <p>
     * <b>Note:</b> This factory is considerd an implementation detail and <b>should not</b> be referenced from user code.
     * </p>
     */
    /*package*/ static class WebControlBeanContextFactory
        implements ControlBeanContextFactory {

        public org.apache.beehive.controls.api.context.ControlBeanContext instantiate
            (org.apache.beehive.controls.api.bean.ControlBean controlBean) {

            if(!(controlBean instanceof ControlBean))
                throw new IllegalArgumentException("The ControlBean of type \"" +
                    controlBean.getClass().getName() +
                    "\" is unsupported.  The ControlBean must extend " +
                    ControlBean.class.getName());

            ControlBean runtimeControlBean = (ControlBean)controlBean;

            return new ControlBeanContext(runtimeControlBean, WEB_CONTEXT_BCS_FACTORY);
        }
    }

    /*package*/ static class WebContextBeanContextServicesFactory
        extends ControlBeanContext.BeanContextServicesFactory {
        protected BeanContextServices instantiate(ControlBeanContext controlBeanContext) {
            return new ControlBeanContextServicesSupport(controlBeanContext);

            /* The java implementation of the BeanContext support classes,
               not currently used by Beehive due to performance issues.

               return new java.beans.beancontext.BeanContextServicesSupport(controlBeanContext);
             */
        }
    }
}
