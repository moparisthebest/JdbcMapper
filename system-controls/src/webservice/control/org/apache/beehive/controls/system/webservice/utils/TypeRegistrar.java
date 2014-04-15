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
package org.apache.beehive.controls.system.webservice.utils;

import org.apache.beehive.controls.system.webservice.wsdl.WsdlComplexTypes;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOperation;
import org.apache.beehive.webservice.utils.databinding.BindingLookupStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.TypeMapping;

/**
 * Abstract base class for registering types with underlying JAX-RPC client implementation.
 */
public abstract class TypeRegistrar {

    private static final Log LOGGER = LogFactory.getLog(TypeRegistrar.class);
    private static Class XMLOBJECT_CLASS;

    // check to see if xmlbeans is present
    static {
        try {
            XMLOBJECT_CLASS = Class.forName("org.apache.xmlbeans.XmlObject");
        }
        catch (ClassNotFoundException e) {
            // just means xmlbeans is not present at runtime.
            XMLOBJECT_CLASS = null;
        }
    }

    protected final WsdlComplexTypes _wsdlComplexTypes;
    protected final TypeMapping _typeMapping;
    private final BindingLookupStrategy _lookupStrategy;

    /**
     * Create a new TypeRegistrar.
     *
     * @param typeMapping    Service's type mapping interface.
     * @param lookupStrategy Used to perform class -> xmltype and xmltype -> class lookups.
     * @param complexTypes   Provides access to named complex types from the wsdl, useful when mapping beans.
     */
    protected TypeRegistrar(TypeMapping typeMapping,
                            BindingLookupStrategy lookupStrategy,
                            WsdlComplexTypes complexTypes) {
        _typeMapping = typeMapping;
        _lookupStrategy = lookupStrategy;
        _wsdlComplexTypes = complexTypes;
    }

    /**
     * Register a type.
     *
     * @param cls     Class to register.
     * @param xmlType XmlType of the class.
     * @param style   Style of the web service.
     * @param use     Use of the web service.
     * @return QName of the registered type.
     */
    public QName registerType(Class cls, QName xmlType, WsdlOperation.SOAPBindingStyle style, WsdlOperation.SOAPBindingUse use) {

        LOGGER.debug("Register class: " + cls.getCanonicalName() + " qName: " + xmlType);
        if (xmlType == null) {
            throw new RuntimeException("Invalid registeration request: xmlType is null!");
        }

        if (_typeMapping.isRegistered(cls, xmlType)) {
            return xmlType;
        }

        // in AXIS it is possible that the typemapping inteface can find a serializer for
        // this pair even though the pair has not be specifically registered
        if (!cls.isArray() && _typeMapping.getSerializer(cls, xmlType) != null) {
            registerAs(cls, xmlType, _typeMapping.getSerializer(cls, xmlType));
            return xmlType;
        }

        if (cls.isArray()) {
            return registerAsArray(cls, xmlType, _lookupStrategy.class2qname(cls.getComponentType()), style, use);
        }

        if (XMLOBJECT_CLASS != null && XMLOBJECT_CLASS.isAssignableFrom(cls)) {
            registerAsXMLBean(cls, xmlType);
            return xmlType;
        }

        registerAsBean(cls, xmlType, style, use);
        return xmlType;
    }

    /**
     * Register a class with a specific serializer.
     *
     * @param cls        Class to register.
     * @param xmlType    Xml type of the class.
     * @param serializer Serializer to register class with.
     */
    protected abstract void registerAs(Class cls, QName xmlType, Object serializer);

    /**
     * Register a class using the default serialization mechanism of the JAX-RPC client.
     *
     * @param cls     Class to register.
     * @param xmlType Xml type of the class.
     * @param style   Service style.
     * @param use     Service use.
     */
    protected abstract void registerAsBean(Class cls, QName xmlType,
                                           WsdlOperation.SOAPBindingStyle style,
                                           WsdlOperation.SOAPBindingUse use);

    /**
     * Register a class for XmlBean serialization.
     *
     * @param cls     Class to register.
     * @param xmlType Xml type of the class.
     */
    protected abstract void registerAsXMLBean(Class cls, QName xmlType);

    /**
     * Register a class as an array.
     *
     * @param cls           Class to register.
     * @param xmlType       Xml type of the class.
     * @param componentType Xml type of the array component class.
     * @param style         SOAP style.
     * @param use           SOAP use.
     * @return QName  QName for the array.
     */
    protected abstract QName registerAsArray(Class cls, QName xmlType, QName componentType,
                                             WsdlOperation.SOAPBindingStyle style, WsdlOperation.SOAPBindingUse use);
}