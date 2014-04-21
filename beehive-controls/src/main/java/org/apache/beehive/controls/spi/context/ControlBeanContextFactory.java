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
package org.apache.beehive.controls.spi.context;

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.context.ControlBeanContext;

/**
 * The ControlBeanContextFactory defines a service provider interface for providing implementations
 * of factories to create a {@link org.apache.beehive.controls.api.context.ControlBeanContext}
 * object.  This factory is <b>only</b> used to create ControlBeanContexts that are associated
 * with a ControlBean instance; it is not used to create
 * {@link org.apache.beehive.controls.api.context.ControlContainerContext} objects.
 */
public interface ControlBeanContextFactory {

    /**
     * Instantiate a {@link ControlBeanContext} object that will be associated with the
     * provided {@link ControlBean}.
     *
     * @param controlBean
     * @return the ControlBeanContext instance
     */
    public <T extends ControlBeanContext> T instantiate(ControlBean controlBean);
}
