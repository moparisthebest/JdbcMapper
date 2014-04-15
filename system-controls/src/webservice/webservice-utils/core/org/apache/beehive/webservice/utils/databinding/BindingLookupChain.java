/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 */
package org.apache.beehive.webservice.utils.databinding;

import java.util.ArrayList;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the {@link org.apache.beehive.webservice.utils.databinding.BindingLookupStrategy} class that chains
 * together some number of {@link org.apache.beehive.webservice.utils.databinding.BindingLookupStrategy}
 * implementations.  This list should be managed using the add / remove methods.
 */
public final class BindingLookupChain
    implements BindingLookupStrategy {

    private static final Log LOG = LogFactory.getLog(BindingLookupChain.class);

    private ArrayList<BindingLookupStrategy> _lookupServiceList = new ArrayList<BindingLookupStrategy>();

    public BindingLookupChain() {
    }

    public void addBindingLookupService(BindingLookupStrategy bindingLookupStrategy) {
        if(bindingLookupStrategy == null)
            return;

        LOG.debug("Add binding lookup strategy: " + bindingLookupStrategy.getClass());
        _lookupServiceList.add(bindingLookupStrategy);
    }

    public void removeBindingLookupService(BindingLookupStrategy bindingLookupStrategy) {
        if(bindingLookupStrategy == null)
            return;

        LOG.debug("Remove binding lookup strategy: " + bindingLookupStrategy.getClass());

        _lookupServiceList.remove(bindingLookupStrategy);
    }

    public QName class2qname(Class clazz) {
        for(BindingLookupStrategy lookUpStrategy : _lookupServiceList) {
            QName qname = lookUpStrategy.class2qname(clazz);
            if(qname != null)
                return qname;
        }
        return null;
    }

    public QName class2qname(Class clazz, String namespace) {
        for(BindingLookupStrategy lookUpStrategy : _lookupServiceList) {
            QName qname = lookUpStrategy.class2qname(clazz, namespace);
            if(qname != null)
                return qname;
        }
        return null;
    }

    public Class qname2class(QName qname) {
        for(BindingLookupStrategy lookUpStrategy : _lookupServiceList) {
            Class clazz = lookUpStrategy.qname2class(qname);
            if(clazz != null)
                return clazz;
        }
        return Object.class;
    }
}
