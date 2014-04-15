/*
 * XmlBeanDeserializer.java
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
 * Original author: Jonathan Colwell
 */
package org.apache.beehive.webservice.utils.encoding;

import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.DeserializerImpl;
import org.apache.axis.message.MessageElement;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import java.util.HashMap;

/**
 * Custom deserializer for XMLBeans.  AXIS specific, used by WSM module and the service control.
 */
public class XmlBeanDeserializer extends DeserializerImpl
{
    private Class mJavaType;
    private QName mXmlType;

    /**
     * Construct a new XmlBeanDeserializer.
     * @param javaType Class to map xml to.
     * @param xmlType XML type to deserialize.
     */
    public XmlBeanDeserializer(Class javaType, QName xmlType)
    {
        mJavaType = javaType;
        mXmlType = xmlType;
    }

    /**
     *  Overrides AXIS DeserializerImpl method.
     *
     * @param namespace
     * @param localName
     * @param prefix
     * @param attributes
     * @param context
     * @throws SAXException
     */
    public void onStartElement(String namespace,
                               String localName,
                               String prefix,
                               Attributes attributes,
                               DeserializationContext context)
            throws SAXException
    {
        try {

            MessageElement me = context.getCurElement();

            SchemaType st = (SchemaType) mJavaType.getField("type").get(null);
            XmlOptions opts = new XmlOptions().setLoadReplaceDocumentElement(null);
            opts.setDocumentType(st);

            // axis always returns element form qualified responses, this causes
            // problems if the WSDL did not specify elementFormDefault="qualified"
            // if this mis-match is detected, fix it with the following block of code
            if (!isElementFormQualified(st)) {
                HashMap<String, String> substituteNamespaces = new HashMap<String, String>();
                substituteNamespaces.put(mXmlType.getNamespaceURI(), "");
                opts.setLoadSubstituteNamespaces(substituteNamespaces);
            }

            XmlObject xObj = XmlObject.Factory.parse(me, opts);
            assert xObj.validate() : "XmlBeanDeserializer: Could not map to xml bean: " + me.toString();

            setValue(xObj);

        } catch (Exception xe) {
            throw new SAXException(xe);
        }
    }

    /**
     * Check if the elementFormDefault was set to 'qualified' for the schematype.  If elementFormDefault
     * has been set to qualified, each SchemaProperty of the SchemaType will have a namespace URI included
     * as part of its name.
     * @param schemaType SchemaType to check.
     * @return true if elementFormDefault was set to qualified.
     */
    private boolean isElementFormQualified(SchemaType schemaType)
    {
        SchemaProperty[] props = schemaType.getElementProperties();
        if (props != null && props.length > 0) {
            String propNamespace = props[0].getName().getNamespaceURI();
            if (propNamespace != null && !"".equals(propNamespace)) {
                return true;
            }
        }
        return false;
    }
}
