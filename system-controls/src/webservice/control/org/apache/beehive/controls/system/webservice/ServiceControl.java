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
package org.apache.beehive.controls.system.webservice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;

import org.apache.beehive.controls.api.bean.AnnotationMemberTypes;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.properties.PropertySet;

/**
 * <p>
 * Provides simplified access to web services. A web service control provides an interface between an application and a
 * web service, which allows an application to invoke the web service's operations. Using a web service control, you
 * can connect to any web service for which a WSDL file is available.
 * </p>
 * <p>
 * A web service control is generated from a WSDL file.  The target web service's operations are exposed as
 * methods of the web service control.
 * </p>
 */
@ControlInterface(defaultBinding = "org.apache.beehive.controls.system.webservice.jaxrpc.ServiceControlImpl")
public interface ServiceControl {

    /**
     * OperationName is the name of the operation as it appears in the WSDL file. It provides a means of
     * mapping a web service control's method to the WSDL operation.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @PropertySet(prefix = "OperationName", externalConfig = false, optional = false, hasSetters = false)
    public @interface OperationName {
        String value();
    }

    /**
     * The WSDL annotation contains information pertinant to the WSDL used to generate the web service
     * control.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @PropertySet(prefix = "WSDL", externalConfig = false, optional = false, hasSetters = false)
    public @interface WSDL {

        /**
         * The path to the WSDL for use at runtime by the web service control.
         */
        @AnnotationMemberTypes.FilePath String path();

        /**
         *  The portName of the service.
         */
        String portName();

        /**
         * The local name of the service in the WSDL.
         */
        String service();

        /**
         * The target namespace of the service .
         */
        String serviceTns();
    }

    /**
     * Currently the only service provider supported is Apache AXIS.
     * As new service providers are added this enumeration should be updated.
     */
    public enum ServiceFactoryProviderType {
       APACHE_AXIS ("org.apache.axis.client.ServiceFactory"),
       DEFAULT ("org.apache.axis.client.ServiceFactory") ;

        private String _serviceFactoryClassName;

        ServiceFactoryProviderType(String serviceFactoryClassName) {
            _serviceFactoryClassName = serviceFactoryClassName;
        }

        public String getServiceFactoryClassName() {
            return _serviceFactoryClassName;
        }
    }

    /**
     * Specify the JAXRPC ServiceFactory implementation for a webservice control.
     * Currently only Apache AXIS is supported.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @PropertySet(prefix="", externalConfig = false, optional = false, hasSetters = false)
    public @interface ServiceFactoryProvider {
        ServiceFactoryProviderType value() default ServiceFactoryProviderType.DEFAULT;
    }

    /**
     * Manual control over URL of the target service.
     *
     * @param url The new destination for callbacks.
     */
    public void setEndPoint(URL url);

    /**
     * Gets the URL that the Service control instance will use as the base URL.
     *
     * @return The URL that will be used.
     */
    public URL getEndPoint();

//    /**
//     * Specifies the name for the port within the service which the web service control should use.
//     *
//     * @param servicePortName The name of the port to use.
//     */
//    public void setServicePort(QName servicePortName);
//
//    /**
//     * Returns the name of the port that will be used by the web service control.
//     *
//     * @return The name of the port that will be used.
//     */
//    public QName getServicePort();

    /**
     * Sets the username that will be sent with the next outgoing Service control method invocation. Used if the Service
     * control uses HTTP basic authentication.
     *
     * @param username The username to send for authentication.
     */
    public void setUsername(String username);

    /**
     * Retrieves the username string that was set by the most recent call to setUsername.
     *
     * @return The username set by the setUsername method.
     */
    public String getUsername();

    /**
     * Sets the password that will be sent with the next outgoing Service control method invocation. Used if the Service
     * control uses HTTP basic authentication.
     *
     * @param password The password to send for authentication.
     */
    public void setPassword(String password);

    /**
     * Retrieves the password string that was set by the most recent call to the setPassword method.
     *
     * @return The password set by the setPassword method.
     */
    public String getPassword();

    /**
     * Set any JAX-RPC handlers for this client.
     * @param handlers List of HandlerInfo.
     */
    public void setHandlers(List<HandlerInfo> handlers);

//    /**
//     * Clears all parameters that were set by previous calls to the setOutputHeaders, setPassword, or setUsername
//     * methods.
//     */
//    public void reset();
}
