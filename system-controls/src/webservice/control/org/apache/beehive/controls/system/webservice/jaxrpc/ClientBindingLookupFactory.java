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

package org.apache.beehive.controls.system.webservice.jaxrpc;

import org.apache.beehive.controls.system.webservice.ServiceControl;
import org.apache.beehive.webservice.utils.databinding.BindingLookupFactory;
import org.apache.beehive.webservice.utils.databinding.BindingLookupStrategy;
import org.apache.beehive.webservice.utils.databinding.SchemaTypesLookupStrategy;
import org.apache.beehive.webservice.utils.exception.BindingConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class ClientBindingLookupFactory {

    private static final Log LOG = LogFactory.getLog(ClientBindingLookupFactory.class);
    private static final BindingLookupStrategy DEFAULT_STRATEGY = new SchemaTypesLookupStrategy();

    public BindingLookupStrategy builtInTypes() {
        return ClientBindingLookupFactory.DEFAULT_STRATEGY;
    }

    public BindingLookupStrategy getInstance(ServiceControl.ServiceFactoryProviderType hint) {

        BindingLookupStrategy strategy;
        Class factoryClass = null;

        switch (hint) {
            case APACHE_AXIS:
            case DEFAULT:
                try {
                    factoryClass = Class.forName("org.apache.beehive.webservice.utils.databinding.AxisBindingLookupFactory");
                }
                catch (ClassNotFoundException e) {
                    throw new BindingConfigurationException("Unable to locate binding lookup factory for AXIS 1.x", e);
                }
                break;
            default:
                break;
        }

        if (factoryClass != null) {
            strategy = createStrategy(factoryClass);
        }
        else {
            strategy = ClientBindingLookupFactory.DEFAULT_STRATEGY;
        }

        assert strategy != null;
        ClientBindingLookupFactory.LOG.debug("Using binding lookup strategy: " + strategy.getClass().getName());
        return strategy;
    }

    private BindingLookupStrategy createStrategy(Class bindingLookupFactory) {
        assert bindingLookupFactory != null;

        BindingLookupStrategy strategy;
        try {
            BindingLookupFactory factory = (BindingLookupFactory) bindingLookupFactory.newInstance();
            strategy = factory.getInstance();
        }
        catch (IllegalAccessException e) {
            throw new BindingConfigurationException("Unable to create binding lookup from factory \"" + bindingLookupFactory + "\".  Cause: " + e, e);
        }
        catch (InstantiationException e) {
            throw new BindingConfigurationException("Unable to create binding lookup from factory \"" + bindingLookupFactory + "\".  Cause: " + e, e);
        }
        return strategy;
    }
}
