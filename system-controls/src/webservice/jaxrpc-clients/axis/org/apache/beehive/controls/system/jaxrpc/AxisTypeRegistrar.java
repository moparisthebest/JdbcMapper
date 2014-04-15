/*
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
 */
package org.apache.beehive.controls.system.jaxrpc;

import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.utils.BeanPropertyDescriptor;
import org.apache.beehive.controls.system.webservice.utils.TypeRegistrar;
import org.apache.beehive.controls.system.webservice.wsdl.ComplexType;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlComplexTypes;
import org.apache.beehive.controls.system.webservice.wsdl.WsdlOperation;
import org.apache.beehive.webservice.utils.databinding.BindingLookupStrategy;
import org.apache.beehive.webservice.utils.encoding.XmlBeanSerializerFactory;
import org.apache.beehive.webservice.utils.encoding.XmlBeanDeserializerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.TypeMapping;
import javax.xml.rpc.encoding.XMLType;
import java.util.Collection;
import java.util.Map;
import java.util.List;

/**
 * Axis client implementation of type registrar.
 */
public class AxisTypeRegistrar
        extends TypeRegistrar {

    private static Log LOGGER = LogFactory.getLog(AxisTypeRegistrar.class);

    /**
     * Create a new AxisTypeRegistrar.
     *
     * @param tm             A JAX-RPC type mapping instance.
     * @param lookupStrategy Lookup strategy for mapping java -> xml types.
     * @param complexTypes   Named complextype information from the wsdl.
     */
    public AxisTypeRegistrar(TypeMapping tm, BindingLookupStrategy lookupStrategy, WsdlComplexTypes complexTypes) {
        super(tm, lookupStrategy, complexTypes);
    }

    /**
     * Register the pair with the specified serializer.
     *
     * @param cls        Class to register.
     * @param xmlType    Xml type of the class.
     * @param serializer Serializer to register with.
     */
    protected void registerAs(Class cls, QName xmlType, Object serializer) {

        if (serializer instanceof SimpleSerializerFactory) {
            _typeMapping.register(cls, xmlType,
                                  new SimpleSerializerFactory(cls, xmlType),
                                  new SimpleDeserializerFactory(cls, xmlType));
        }
        else {
            assert false : "Unexpected serializer type: " + serializer.getClass().getName();
            throw new RuntimeException("Unexpected serializer type: " + serializer.getClass().getName());
        }
    }

    /**
     * Register a type with default serialization, in the case of AXIS this would be the BeanSerializer.
     *
     * @param cls     Class to register.
     * @param xmlType Xml name of the class.
     * @param style   Style of the service.
     * @param use     Use of the service.
     */
    protected void registerAsBean(Class cls, QName xmlType,
                                  WsdlOperation.SOAPBindingStyle style,
                                  WsdlOperation.SOAPBindingUse use)
    {

        LOGGER.debug("Assigned Bean Serialization to  class: " + cls.getCanonicalName() + " qname:" + xmlType);

        _typeMapping.register(cls, xmlType,
                              new BeanSerializerFactory(cls, xmlType),
                              new BeanDeserializerFactory(cls, xmlType));

        // register any types contained within the bean
        Map classProperties = BeanDeserializerFactory.getProperties(cls, TypeDesc.getTypeDescForClass(cls));
        Collection beanPropertyDescriptors = classProperties.values();

        ComplexType ct = _wsdlComplexTypes.getComplexType(xmlType.getLocalPart());
        for (Object o : beanPropertyDescriptors) {
            Class type = ((BeanPropertyDescriptor) o).getType();
            if (!(type.isPrimitive() || type.getName().startsWith("java.") || type.getName().startsWith("javax."))) {

                assert ct != null : "Complex type: " + xmlType.getLocalPart() + " not found in wsdl!";
                registerType(type, ct.getElementType(((BeanPropertyDescriptor) o).getName()), style, use);
            }
        }
    }

    /**
     * Register a type as an XmlBean.
     *
     * @param cls     Class to register.
     * @param xmlType Xml name of the class.
     */
    protected void registerAsXMLBean(Class cls, QName xmlType) {

        // todo: the XmlBeanSerializerFactory is available in Axis 1.2.1
        LOGGER.debug("Assigned XMLBeans Serialization to  class: " + cls.getCanonicalName() + " qname:" + xmlType);
        _typeMapping.register(cls, xmlType,
                              new XmlBeanSerializerFactory(cls, xmlType),
                              new XmlBeanDeserializerFactory(cls, xmlType));
    }

    /**
     * Register a type as an array.
     *
     * @param cls           Class to register.
     * @param xmlType       Xml name of the class.
     * @param componentType Xml type of the array component class.
     * @param style         Soap style.
     * @param use           Soap use.
     * @return The QName of the registered array.
     */
    protected QName registerAsArray(Class cls, QName xmlType, QName componentType,
                                    WsdlOperation.SOAPBindingStyle style, WsdlOperation.SOAPBindingUse use) {

        if (WsdlOperation.SOAPBindingStyle.RPC == style && WsdlOperation.SOAPBindingUse.ENCODED == use) {
            _typeMapping.register(cls, xmlType,
                                  new ArraySerializerFactory(componentType),
                                  new ArrayDeserializerFactory());
            return xmlType;
        }

        ComplexType ct = _wsdlComplexTypes.getComplexType(xmlType.getLocalPart());
        if (ct != null && ct.getElementNames().size() == 1) {
            List<String> names = ct.getElementNames();
            QName componentQName = ct.getElementType(names.get(0));
            QName componentItemName;
            if (WsdlOperation.SOAPBindingStyle.RPC == style) {
                componentItemName = new QName("", names.get(0));
            }
            else {
                componentItemName = new QName(xmlType.getNamespaceURI(), names.get(0));
            }
            registerType(cls.getComponentType(), componentQName, style, use);

            xmlType = (WsdlOperation.SOAPBindingStyle.RPC == style) ? XMLType.SOAP_ARRAY : xmlType;
            _typeMapping.register(cls, xmlType,
                                  new ArraySerializerFactory(componentQName, componentItemName),
                                  new ArrayDeserializerFactory());
            return xmlType;
        }
        else {
            return registerType(cls.getComponentType(), componentType, style, use);
        }
    }
}
