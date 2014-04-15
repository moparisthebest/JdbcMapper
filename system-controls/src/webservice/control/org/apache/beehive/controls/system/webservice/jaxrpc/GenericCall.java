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

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.holders.Holder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.system.webservice.utils.HolderUtils;
import org.apache.beehive.controls.system.webservice.utils.TypeRegistrar;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOpFault;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOpFaultList;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOpParameter;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOpReturnType;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOperation;

/**
 * The abstract base class for the service control's JAX-RPC Call abstraction layer.  Due to some
 * JAX-RPC limitations it becomes necessary for the service control to use some implementation specific
 * API's.  This base class and classes derived from it are used to hide any SOAP stack implementation
 * specific APIs and enable the service control to perform dynamic discovery of its runtime environment.
 */
public abstract class GenericCall
{
    /**
     * Value for wrapped operation style.
     */
    protected static final String WRAPPED_OPSTYLE = "wrapped";

    /**
     * Value for document operation style.
     */
    protected static final String DOCUMENT_OPSTYLE = "document";

    /**
     * Value for rpc operation style.
     */
    protected static final String RPC_OPSTYLE = "rpc";

    /**
     * JAX-RPC call.
     */
    protected Call _call;

    /**
     * Type registrar, set by concrete implementation of this class.
     */
    protected TypeRegistrar _typeRegistrar;

    /**
     * Wsdl operation object model this call was configured with.
     */
    private WsdlOperation _op;

    /**
     * Number of call parameters.
     */
    private int _parameterCount;

    /**
     * Is this a oneway call? See JSR 181 specification.
     */
    protected boolean _isOneWay = false;

    /**
     * Configure this call for the specified wsdl operation.
     *
     * @param op       WsdlOperation used to configure the call.
     * @param method   Webservice control's method.
     * @param portType The port type to configure call with.
     */
    void configure(WsdlOperation op, Method method, QName portType) {

        _op = op;
        setOperationName(_op.getOperationQName());
        setOperationStyle(_op.getSOAPBindingStyle(), _op.getSOAPParameterStyle(), _op.getSOAPBindingUse());
        setSOAPAction(_op.getSOAPAction());
        setCallParameters(method);
        setCallReturnType(method.getReturnType());
        setFaults();
        setServicePortName(portType);
    }

    /**
     * Invoke the call.  Basically the same as calling the invoke() method on a standard JAX-RPC call object.
     * As an added bonus this invoke method does not require that you remove any OUT mode parameters from the
     * args array.
     *
     * @param args Values for the call's parameters.
     * @return The result from the call.
     * @throws java.rmi.RemoteException On error.
     */
    Object invoke(Object[] args) throws RemoteException {
        Object result;
        try {
        result = _call.invoke(processArgs(args));
        } catch (RemoteException re) {
            throw re;
        }
        setHolderValues(args);
        return result;
    }

    /**
     * Set user defined properties from the service control.  Each of these property values can be set
     * by the user and may change on a per-call basis.
     *
     * @param wsdlPort        The WSDL port. Equivalent to the setPortTypeName() JAX-RPC API.
     * @param serviceEndpoint The service endpoint. Equivalent to the setTargetEndpointAddress() JAX-RPC API.
     * @param username        Username for the call. Equivalent to the JAX-RPC Call.USERNAME_PROPERTY.
     * @param password        Password for the call. Equivalent to the JAX-RPC Call.PASSWORD_PROPERTY.
     */
    void setUserDefinedProperties(QName wsdlPort, String serviceEndpoint, String username, String password) {
        _call.setPortTypeName(wsdlPort);
        _call.setTargetEndpointAddress(serviceEndpoint);

        if (username != null) {
            _call.setProperty(Call.USERNAME_PROPERTY, username);
        }
        else {
            _call.removeProperty(Call.USERNAME_PROPERTY);
        }

        if (password != null) {
            _call.setProperty(Call.PASSWORD_PROPERTY, password);
        }
        else {
            _call.removeProperty(Call.PASSWORD_PROPERTY);
        }
    }

    /* -----------------------------  Abstract Methods ------------------------- */

    /**
     * Add a parameter to the Call (abstract).
     *
     * @param paramName  QName of the parameter.
     * @param paramClass Class of the parameter.
     * @param xmlType    XMLType of the parameter.
     * @param mode       JAX-RPC parameter mode.
     * @param isHeader   true if this parameter should be placed in the header of the message.
     */
    abstract protected void addParameter(QName paramName, Class paramClass, QName xmlType,
                                         WsdlOpParameter.ParameterMode mode, boolean isHeader);

    /**
     * Add a fault to the Call (abstract).
     *
     * @param faultName     QName of the fault.
     * @param xmlType       QName of the fault's xml type.
     * @param isComplexType Is this fault a complex data type?
     * @param style         SOAPBindingStyle.
     * @param use           SOAPBindingUse.
     */
    abstract protected void addFault(QName faultName, QName xmlType, boolean isComplexType,
                                     WsdlOperation.SOAPBindingStyle style, WsdlOperation.SOAPBindingUse use);

    /**
     * Set the service port name for the call (abstract).  May be necessary to support JAX-RPC handlers.
     *
     * @param portQName QName of the service port.
     */
    abstract protected void setServicePortName(QName portQName);

    /* -----------------------------   Protected Methods ------------------------- */

    /**
     * Register a type.
     *
     * @param typeClass
     * @param xmlName
     * @param style
     * @param use
     * @return QName
     */
    protected QName registerType(Class typeClass,
                                 QName xmlName,
                                 WsdlOperation.SOAPBindingStyle style,
                                 WsdlOperation.SOAPBindingUse use) {
        if (_typeRegistrar == null) {
            throw new RuntimeException("TypeRegistrar was not initialized correctly.");
        }
        return _typeRegistrar.registerType(typeClass, xmlName, style, use);
    }

    /**
     * Mark this call as a oneway call (no return value).  See JSR181 specification for details.
     *
     * @param isOneWay true if one way.
     */
    protected void setOneWay(boolean isOneWay) {
        _isOneWay = isOneWay;
    }

    /**
     * Set the operation name for the call.
     *
     * @param opName The operation QName.
     */
    protected void setOperationName(QName opName) {
        _call.setOperationName(opName);
    }

    /**
     * Set the operation style for this call.
     *
     * @param style          JAX-RPC style.
     * @param parameterStyle JAX-RPC parameter style.
     * @param use            JAX-RPC use.
     */
    protected void setOperationStyle(WsdlOperation.SOAPBindingStyle style,
                                     WsdlOperation.SOAPParameterStyle parameterStyle,
                                     WsdlOperation.SOAPBindingUse use) {

        if (style == WsdlOperation.SOAPBindingStyle.DOCUMENT) {
            if (parameterStyle == WsdlOperation.SOAPParameterStyle.WRAPPED) {
                _call.setProperty(Call.OPERATION_STYLE_PROPERTY, WRAPPED_OPSTYLE);
            }
            else {
                _call.setProperty(Call.OPERATION_STYLE_PROPERTY, DOCUMENT_OPSTYLE);
            }

        }
        else if (style == WsdlOperation.SOAPBindingStyle.RPC) {
            _call.setProperty(Call.OPERATION_STYLE_PROPERTY, RPC_OPSTYLE);

        }
        else {
            throw new ControlException("Invalid Binding style: " + style);
        }
    }

    /**
     * Set the return type of the Call.
     *
     * @param returnType    Return type class.
     * @param xmlReturnType Return Xml type.
     */
    protected void setReturnType(Class returnType, QName xmlReturnType) {
        _call.setReturnType(xmlReturnType, returnType);
    }

    /**
     * Process the argument list for the call before the call's invoke() method is called.
     * Processing consists of removing any OUT mode parameters and pulling values from Holders
     * and placing them in the new argument list.
     *
     * @param args Argument list for a Call.
     * @return A new argument list with any OUT mode parameters removed and Holder values insterted.
     */
    protected Object[] processArgs(Object[] args) {
        ArrayList<Object> pList = new ArrayList<Object>();
        List<WsdlOpParameter> parameters = _op.getParameters();

        for (int i = 0; i < _parameterCount; i++) {
            WsdlOpParameter.ParameterMode mode = parameters.get(i).getMode();
            if (mode.equals(WsdlOpParameter.ParameterMode.IN)) {
                pList.add(args[i]);

            }
            else if (mode.equals(WsdlOpParameter.ParameterMode.INOUT)) {
                if (args[i] instanceof Holder) {
                    try {
                        Holder holder = (Holder) args[i];
                        Field valueField = holder.getClass().getField("value");
                        pList.add(valueField.get(holder));
                    }
                    catch (Exception e) {
                        throw new ControlException("failed in getting holder value for call argument");
                    }
                }
                else {
                    pList.add(args[i]);
                }
            }
        }
        return pList.toArray();
    }

    /* -----------------------------   Private Methods ------------------------- */

    /**
     * After a Call has been invoked it is necessary to map values for OUT and INOUT mode parameters back
     * into their Holders from the arg list to the Call.
     *
     * @param args The argument list which the Call was invoked with.
     */
    private void setHolderValues(Object[] args) {
        Map outParams = _call.getOutputParams();
        if (outParams.isEmpty()) {
            return;
        }

        List<WsdlOpParameter> parameters = _op.getParameters();
        for (int i = 0; i < _parameterCount; i++) {

            WsdlOpParameter param = parameters.get(i);
            if (!param.getMode().equals(WsdlOpParameter.ParameterMode.IN)
                    && args[i] instanceof Holder) {

                QName paramQName = param.getName();
                if (!param.isNameElementFormQualified()) {
                    paramQName = new QName(paramQName.getLocalPart());
                }

                try {
                    Holder holder = (Holder) args[i];
                    if (outParams.containsKey(paramQName)) {
                        HolderUtils.stuffHolderValue(holder, outParams.get(paramQName));
                    }
                }
                catch (Exception e) {
                    throw new ControlException("Failed in getting holder value for: " + paramQName);
                }
            }
        }
    }

    /**
     * Set the parameter list for this call.
     *
     * @param method service control method.
     */
    private void setCallParameters(Method method) {

        WsdlOpParameter[] operationParameters = _op.getParameters().toArray(new WsdlOpParameter[0]);
        Class[] methodParameterTypes = method.getParameterTypes();
        Type[] genericMethodParamTypes = method.getGenericParameterTypes();
        _parameterCount = operationParameters.length;

        if (operationParameters.length != methodParameterTypes.length) {
            throw new ControlException("Object model and method definition don't match!");
        }

        for (int i = 0; i < methodParameterTypes.length; i++) {
            setCallParameter(operationParameters[i], methodParameterTypes[i], genericMethodParamTypes[i]);
        }
    }

    /**
     * Set a call pararmeter on the call.
     *
     * @param opParam     Parameter.
     * @param javaType    Java type of parameter.
     * @param genericType Generic type of the java class.
     */
    private void setCallParameter(WsdlOpParameter opParam, Class javaType, Type genericType) {

        WsdlOpParameter.ParameterMode mode = opParam.getMode();
        if (mode != WsdlOpParameter.ParameterMode.IN && Holder.class.isAssignableFrom(javaType)) {
            javaType = HolderUtils.getHoldersValueClass(genericType);
        }

        QName registeredTypeQName = registerType(javaType, opParam.getXmlType(),
                                                 _op.getSOAPBindingStyle(), _op.getSOAPBindingUse());

        QName paramName = opParam.getName();
        if (!opParam.isNameElementFormQualified()) {
            paramName = new QName(paramName.getLocalPart());
        }
        addParameter(paramName, javaType, registeredTypeQName, mode, opParam.isHeader());
    }

    /**
     * Set the return type for the call.
     *
     * @param returnType Return type class.
     */
    private void setCallReturnType(Class returnType) {

        WsdlOpReturnType rtype = _op.getReturnType();
        QName resultTypeName = (rtype != null) ? rtype.getXmlType() : null;
        if (!_op.isOneway() && returnType != void.class) {
            resultTypeName = registerType(returnType, resultTypeName, _op.getSOAPBindingStyle(), _op.getSOAPBindingUse());
        }
        setOneWay(_op.isOneway());
        setReturnType(returnType, resultTypeName);
    }

    /**
     * Set the faults for the call.
     */
    private void setFaults() {

        WsdlOpFaultList faultList = _op.getFaults();
        for (WsdlOpFault fault : faultList) {
            QName faultName = fault.getName();
            if (!fault.isFaultNameElementFormQualified()) {
                faultName = new QName(faultName.getLocalPart());
            }

            addFault(faultName, fault.getXmlType(), fault.isComplexType(),
                     _op.getSOAPBindingStyle(), _op.getSOAPBindingUse());
        }
    }

    /**
     * Set the SOAPAction uri for the call.
     * @param actionURIString
     */
    private void setSOAPAction(String actionURIString) {
       if (actionURIString != null && !actionURIString.equals("")) {
           _call.setProperty(Call.SOAPACTION_USE_PROPERTY, true);
           _call.setProperty(Call.SOAPACTION_URI_PROPERTY, actionURIString);
       }
    }
}
