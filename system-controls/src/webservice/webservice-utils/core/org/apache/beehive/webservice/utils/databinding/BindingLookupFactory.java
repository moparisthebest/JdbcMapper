/**
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
package org.apache.beehive.webservice.utils.databinding;

import org.apache.beehive.webservice.utils.exception.BindingConfigurationException;
import org.apache.commons.discovery.tools.DiscoverClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class BindingLookupFactory {

    private static final Log LOG = LogFactory.getLog(BindingLookupFactory.class);
    private static final BindingLookupStrategy DEFAULT_STRATEGY = new SchemaTypesLookupStrategy();

    public BindingLookupStrategy builtInTypes() {
        return DEFAULT_STRATEGY;
    }

    public BindingLookupStrategy getInstance() {

        DiscoverClass discoverClass = new DiscoverClass();
        Class factoryClass = discoverClass.find(BindingLookupFactory.class);
        BindingLookupStrategy strategy = null;

        if(factoryClass != null)
            strategy = createStrategy(factoryClass);
        else strategy = DEFAULT_STRATEGY;

        assert strategy != null;
        LOG.debug("Using binding lookup strategy: " + strategy.getClass().getName());

        return strategy;
    }

    private BindingLookupStrategy createStrategy(Class bindingLokupFactory) {
        assert bindingLokupFactory != null;

        BindingLookupStrategy strategy = null;
        try {
            BindingLookupFactory factory = (BindingLookupFactory)bindingLokupFactory.newInstance();
            strategy = factory.getInstance();
        }
        catch (IllegalAccessException e) {
            throw new BindingConfigurationException("Unable to create binding lookup from factory \"" + bindingLokupFactory + "\".  Cause: " + e, e);
        }
        catch (InstantiationException e) {
            throw new BindingConfigurationException("Unable to create binding lookup from factory \"" + bindingLokupFactory + "\".  Cause: " + e, e);
        }
        return strategy;
    }
}
