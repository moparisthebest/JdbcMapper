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
package org.apache.beehive.controls.system.jaxrpc;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.TypeMapping;

import org.apache.axis.constants.Use;
import org.apache.axis.encoding.XMLType;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOpParameter;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOperation;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlComplexTypes;
import org.apache.beehive.controls.system.webservice.jaxrpc.GenericCall;
import org.apache.beehive.webservice.utils.databinding.AxisBindingLookupFactory;
import org.apache.beehive.webservice.utils.databinding.BindingLookupStrategy;

/**
 * The AXIS 1.x implementation of the call abstraction layer of the service control.
 */
public final class AxisCall
    extends GenericCall {

    private final BindingLookupStrategy _bindingLookupStrategy;

    /**
     * Create a new ScAxisCall instance.
     *
     * @param service Service to create the call with.
     * @param complexTypes Named complex types information from the wsdl file.
     * @throws ServiceException On error.
     */
    public AxisCall(Service service, WsdlComplexTypes complexTypes)
        throws ServiceException {
        TypeMapping typeMapping = service.getTypeMappingRegistry().getDefaultTypeMapping();
        _bindingLookupStrategy = (new AxisBindingLookupFactory()).getInstance();

        _call = service.createCall();
        _typeRegistrar = new AxisTypeRegistrar(typeMapping, _bindingLookupStrategy, complexTypes);
    }

    /* -----------------------------   Protected Methods ------------------------- */

    /**
     * Add a fault to the Call.
     *
     * @param faultName     QName of the fault.
     * @param xmlType       QName of the xmltype of the fault.
     * @param isComplexType Is the xmlType a complex type?
     * @param style         SOAPBinding style.
     * @param use           SOAPBinding use.
     */
    protected void addFault(QName faultName,
                            QName xmlType,
                            boolean isComplexType,
                            WsdlOperation.SOAPBindingStyle style,
                            WsdlOperation.SOAPBindingUse use) {

        Class javaType = _bindingLookupStrategy.qname2class(xmlType);
        QName registeredTypeQName = registerType(javaType, xmlType, style, use);
        ((org.apache.axis.client.Call) _call).addFault(faultName, javaType, registeredTypeQName, isComplexType);
    }

    /**
     * Add a parameter to the Call.
     *
     * @param paramName  QName of the parameter to add.
     * @param paramClass Class of the parameter to add.
     * @param xmlType    XMLType of the parameter to add.
     * @param mode       Mode of the parameter.
     * @param isHeader   True if this parameter should be placed in the header of the SOAP message.
     */
    protected void addParameter(QName paramName, Class paramClass, QName xmlType,
                                WsdlOpParameter.ParameterMode mode, boolean isHeader) {

        ParameterMode jaxrpcMode;
        if (mode == WsdlOpParameter.ParameterMode.IN) {
            jaxrpcMode = ParameterMode.IN;
        }
        else if (mode == WsdlOpParameter.ParameterMode.INOUT) {
            jaxrpcMode = ParameterMode.INOUT;
        }
        else {
            jaxrpcMode = ParameterMode.OUT;
        }

        org.apache.axis.client.Call axisCall = (org.apache.axis.client.Call)_call;
        if (isHeader) {
            axisCall.addParameterAsHeader(paramName, xmlType, paramClass, jaxrpcMode, jaxrpcMode);
        }
        else {
            axisCall.addParameter(paramName, xmlType, paramClass, jaxrpcMode);
        }
    }

    /**
     * Set the operational style of the Call.
     *
     * @param style          JAX-RPC style.
     * @param parameterStyle JAX-RPC parameter style.
     * @param use            JAX-RPC use.
     */
    protected void setOperationStyle(WsdlOperation.SOAPBindingStyle style,
                                     WsdlOperation.SOAPParameterStyle parameterStyle,
                                     WsdlOperation.SOAPBindingUse use) {
        super.setOperationStyle(style, parameterStyle, use);

        if (style == WsdlOperation.SOAPBindingStyle.RPC && use == WsdlOperation.SOAPBindingUse.LITERAL) {
            ((org.apache.axis.client.Call) _call).setOperationUse(Use.LITERAL);
        }
    }

    /**
     * Set the return type for the Call.
     *
     * @param returnType    Java return type.
     * @param xmlReturnType Xml return type.
     */
    protected void setReturnType(Class returnType, QName xmlReturnType) {
        if (!_isOneWay && returnType != void.class) {
            super.setReturnType(returnType, xmlReturnType);
        }
        else {
            _call.setReturnType(XMLType.AXIS_VOID);
        }
    }

    /**
     * Set the port name for the call.
     *
     * @param portQName QName .
     */
    protected void setServicePortName(QName portQName) {
        _call.setProperty(org.apache.axis.client.Call.WSDL_PORT_NAME, portQName);
    }
}
