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
 */
package org.apache.beehive.controls.system.webservice.jaxrpc;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.system.webservice.ServiceControl;
import org.apache.beehive.controls.system.webservice.wsdl.ControlWsdlLocator;
import org.apache.beehive.controls.system.webservice.wsdl.Wsdl;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.handler.HandlerInfo;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Web service control implementation class.
 */
@ControlImplementation
public class ServiceControlImpl
        implements ServiceControl, Extensible, java.io.Serializable {

    private static final Log LOGGER = LogFactory.getLog(ServiceControlImpl.class);

    // contexts provided by the beehive controls runtime
    @org.apache.beehive.controls.api.context.Context
    protected ControlBeanContext _context;
    @org.apache.beehive.controls.api.context.Context
    protected ResourceContext _resourceContext;

    private QName _portType;
    private Wsdl _wsdl;
    private URL _serviceEndpoint;
//    private QName _servicePortQName;
    private QName _serviceName;
    private String _username;
    private String _password;
    private String _serviceFactoryClassName;
    private List<HandlerInfo> _handlerChain;

    private transient Service _service;
    private transient HashMap<String, GenericCall> _callCache;

    /**
     * Constructor.
     */
    public ServiceControlImpl() {
    }

    /**
     * Invoked by the controls runtime when a new instance of this class is aquired by the runtime.
     * Determine the JAX-RPC client type, and initialize the control.
     */
    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onAcquire")
    public void onAquire() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: onAquire()");
        }

        if (_service == null) {
            ServiceControl.ServiceFactoryProvider sfp =
                    _context.getControlPropertySet(ServiceControl.ServiceFactoryProvider.class);
            _serviceFactoryClassName = sfp.value().getServiceFactoryClassName();

            initialize();
        }
    }

    /**
     * Invoked by the controls runtime when an instance of this class is released by the runtime.
     * Release the service and clear the call cache.
     */
    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: onRelease()");
        }

        _service = null;
        _callCache = null;
    }

    /**
     * Called by the Controls runtime to handle calls to methods of an extensible control.
     *
     * @param method The extended operation that was called.
     * @param args   Parameters of the operation.
     * @return The value that should be returned by the operation.
     * @throws Throwable any exception declared on the extended operation may be
     *                   thrown.  If a checked exception is thrown from the implementation that is not declared
     *                   on the original interface, it will be wrapped in a ControlException.
     */
    public Object invoke(Method method, Object[] args) throws Throwable {

        LOGGER.debug("invoke method: " + method.getName());
        ServiceControl.OperationName opName = _context.getMethodPropertySet(method, ServiceControl.OperationName.class);
        WsdlOperation op = _wsdl.getOperation(opName.value());

        GenericCall call;
        if (getCallCache().containsKey(opName.value())) {
            call = getCallCache().get(opName.value());
        }
        else {
            call = buildCallObject(method, op);
            getCallCache().put(opName.value(), call);
        }

        call.setUserDefinedProperties(/*getServicePort()*/null,
                                      getEndPoint().toExternalForm(),
                                      getUsername(),
                                      getPassword());
        return call.invoke(args);
    }

    /**
     * Get the service endpoint currently in use by this control.
     *
     * @return Returns the endpoint URL.
     */
    public URL getEndPoint() {
        if (_serviceEndpoint == null) {
            configureEndPoint();
        }
        return _serviceEndpoint;
    }

    /**
     * Set the service endpoint to be used by this control.
     *
     * @param endPoint The endPoint to set.
     */
    public void setEndPoint(URL endPoint) {
        _serviceEndpoint = endPoint;
    }

    /**
     * Set a client side handler chain. Set to null to clear all previously set handlers.
     *
     * @param handlerChain A List of HandlerInfo objects.
     */
    public void setHandlers(List<HandlerInfo> handlerChain) {
        _handlerChain = handlerChain;
        getService(_serviceName).getHandlerRegistry().setHandlerChain(_portType, handlerChain);
    }

    /**
     * Get the password used for authentication with the web service.
     * This value may only be set by the setPassword() method.
     *
     * @return Returns the password.
     */
    public String getPassword() {
        return _password;
    }

    /**
     * Set the password used for authentication with the web service.
     *
     * @param password The password to set, null value to clear.
     */
    public void setPassword(String password) {
        _password = password;
    }

    /**
     * Get the username for service authentication.
     * Value may only be set by the setUsername() method.
     *
     * @return Returns the username.
     */
    public String getUsername() {
        return _username;
    }

    /**
     * Set the username for service authentication.
     *
     * @param username The username to set, null value to clear.
     */
    public void setUsername(String username) {
        _username = username;
    }

//    /**
//     * Returns the name of the port that will be used by the web service control.
//     *
//     * @return Returns the QName of the service port.
//     */
//    public QName getServicePort() {
//        return _servicePortQName;
//    }
//
//    /**
//     * Specifies the name for the port within the service which the web service control should use.
//     *
//     * @param servicePortName The wsdlPort to set.
//     */
//    public void setServicePort(QName servicePortName) {
//        _wsdl.setPort(servicePortName);
//        _servicePortQName = servicePortName;
//    }
//
//    /**
//     *
//     */
//    public void reset() {
//        _handlerChain = null;
//        _serviceEndpoint = null;
//        _servicePortQName = null;
//        _username = null;
//        _password = null;
//    }

    /* --------------------------------------------------------------------------------------- */
    /*                        PRIVATE METHODS                                                  */
    /* --------------------------------------------------------------------------------------- */

    /**
     * Build a call object for the specified web method.
     *
     * @param method WSC method.
     * @param op     The WSDL operation which corresponds to the method.
     * @return Call A JAX-RPC Call instance.
     */
    private GenericCall buildCallObject(Method method, WsdlOperation op) {

        ServiceControl.ServiceFactoryProviderType sfpt =
                _context.getControlPropertySet(ServiceControl.ServiceFactoryProvider.class).value();

        GenericCall call = CallFactory.getCall(getService(_serviceName), sfpt, _wsdl.getComplexTypes());
        call.configure(op, method, _portType);
        return call;
    }

    /**
     * Initialize the service control. Invoked during onAquire() event.
     */
    private void initialize() {

        ServiceControl.WSDL wsdl = _context.getControlPropertySet(ServiceControl.WSDL.class);
        _serviceName = new QName(wsdl.serviceTns(), wsdl.service());
        ControlWsdlLocator locator = new ControlWsdlLocator(wsdl.path(), _context);
        _wsdl = new Wsdl(locator, _serviceName, wsdl.portName());
        _portType = _wsdl.getPortTypeQName();
    }

    /**
     * Configure the endpoint for the web service.  If an endpoint has been explicitly set by the user,
     * use that endpoint.  If an endpoint has not been set get the endpoint from the ServiceControl.Location
     * annotation.
     */
    private void configureEndPoint() {

        if (_serviceEndpoint == null) {
            try {
                setEndPoint(new URL(_wsdl.getServiceEndpoint()));
            }
            catch (MalformedURLException e) {
                throw new ControlException(e.getMessage(), e);
            }
        }
    }

    /**
     * The callCache is transient so may need to create a new one
     * if a serialization has occured.
     *
     * @return HashMap for call caching.
     */
    private HashMap<String, GenericCall> getCallCache() {
        if (_callCache == null) {
            _callCache = new HashMap<String, GenericCall>();
        }
        return _callCache;
    }

    /**
     * Get the service, service is transient so may need to create a new one
     * if a serialization has occurs.
     *
     * @param serviceName QName of service.
     * @return Service
     */
    private Service getService(QName serviceName) {

        if (_service == null) {
            try {
                Class factory = Class.forName(_serviceFactoryClassName);
                ServiceFactory sf = (ServiceFactory) factory.newInstance();
                _service = sf.createService(serviceName);
            }
            catch (ClassNotFoundException e) {
                throw new ControlException("Can not find ServiceFactory class: " + _serviceFactoryClassName);
            }
            catch (IllegalAccessException e) {
                throw new ControlException("Can not create instance of ServiceFactory class: " + _serviceFactoryClassName);
            }
            catch (InstantiationException e) {
                throw new ControlException("Can not create instance of ServiceFactory class: " + _serviceFactoryClassName);
            }
            catch (ServiceException e) {
                throw new ControlException(e.getMessage(), e);
            }
            _service.getHandlerRegistry().setHandlerChain(_portType, _handlerChain);
        }
        return _service;
    }
}
