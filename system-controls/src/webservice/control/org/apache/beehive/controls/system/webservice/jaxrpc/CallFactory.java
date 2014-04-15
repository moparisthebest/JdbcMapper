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
import org.apache.beehive.controls.system.webservice.wsdl.WsdlComplexTypes;

import javax.xml.rpc.Service;
import java.lang.reflect.Constructor;


/**
 * Factory for creating the JAX-RPC call abstraction used by the service control.  This factory
 * determines the JAX-RPC implementations available at runtime by attemmpting to load various
 * implementations using the class loader.  If found an implementation is added to the _callMappings
 * Map along with its service control Call abstraction.
 */
public final class CallFactory {

    /**
     * Return an new GenericCall based on the implementation of the Service instance.
     *
     * @param service Service to create call for.
     * @param provider Provider name of the JAX-RPC client.
     * @param complexTypes Named complex type information from the wsdl.
     * @return An GenericCall instance.
     */
    static GenericCall getCall(Service service,
                               ServiceControl.ServiceFactoryProviderType provider,
                               WsdlComplexTypes complexTypes)
    {

        switch (provider) {
            case APACHE_AXIS:
            case DEFAULT:
                return getProviderCallImpl("org.apache.beehive.controls.system.jaxrpc.AxisCall", service, complexTypes);
            default:
                throw new UnsupportedOperationException("JAX-RPC service implementation for: "
                        + service.getClass().getName() + "has not been registered with the CallFactory!");
        }
    }

    static private GenericCall getProviderCallImpl(String callClass, Service service, WsdlComplexTypes complexTypes) {

        try {
            Class axisCallClass = Class.forName(callClass);
            Constructor c = axisCallClass.getConstructor(Service.class, WsdlComplexTypes.class);
            return (GenericCall) c.newInstance(service, complexTypes);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not create JAX-RPC call for service control", e);
        }
    }
}
