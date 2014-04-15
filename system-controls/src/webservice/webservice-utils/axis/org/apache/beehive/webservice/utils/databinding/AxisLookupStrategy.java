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
package org.apache.beehive.webservice.utils.databinding;

import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.TypeMappingRegistry;

import org.apache.axis.Constants;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.TypeMappingRegistryImpl;
import org.apache.axis.encoding.TypeMapping;
import org.apache.axis.utils.ClassUtils;
import org.apache.axis.wsdl.fromJava.Namespaces;
import org.apache.axis.wsdl.fromJava.Types;
import org.apache.axis.wsdl.toJava.Utils;

/**
 * Strategy for mapping QName objects to Class objects and vice versa.  This implementation
 * is specific to Axis 1.x.
 */
public class AxisLookupStrategy
    implements BindingLookupStrategy {

    private static final char[] PACKAGE_SEPARATORS = {'.', ':'};

    public QName class2qname(Class cls) {
        if(cls.isArray())
            cls = cls.getComponentType();

        TypeDesc td = TypeDesc.getTypeDescForClass(cls);

        if(td != null)
            return td.getXmlType();
        else {
            String namespace = Namespaces.makeNamespace(cls.getName());
            if(namespace == null || namespace.endsWith("DefaultNamespace"))
                namespace = "http://no.namespace.specified";
            return class2qname(cls, namespace);
        }
    }

    public QName class2qname(Class cls, String namespace) {
        QName qname = AxisTypeMappingMetaData.getBuiltInTypeQname(cls);

        if(qname == null)
            qname = new QName(namespace, Types.getLocalNameFromFullName(cls.getName()));
        return qname;
    }

    public Class qname2class(QName qType) {
        if(qType == null)
            return null;

        /*
         If the type has the rpc encoded namespace, lookup the class it maps
         to in the type mapping table for the rpc enc namespace.  NOTE: Axis
         appends '[]' to the end of the name if it is an array type, strip the
         '[]' characters before doing the lookup.
        */
        if (Constants.URI_SOAP11_ENC.equals(qType.getNamespaceURI())) {
            TypeMappingRegistry tmr = new TypeMappingRegistryImpl(true);
            TypeMapping tm = (TypeMapping)tmr.getTypeMapping(Constants.URI_SOAP11_ENC);
            qType = stripArrayDelimiters(qType);
            return tm.getClassForQName(qType);
        }

        /*
         If the type has one of the standard schema type namespaces, just look it up
         in the type mapping table.  NOTE: The type name may have '[]' appended to it
         if it is an array, strip the '[]' before looking up.
        */
        if (Constants.URI_1999_SCHEMA_XSD.equals(qType.getNamespaceURI())
            || Constants.URI_2000_SCHEMA_XSD.equals(qType.getNamespaceURI())
            || Constants.URI_2001_SCHEMA_XSD.equals(qType.getNamespaceURI())) {
            qType = stripArrayDelimiters(qType);
            TypeMappingRegistry tmr = new TypeMappingRegistryImpl(true);
            TypeMapping tm = (TypeMapping)tmr.getTypeMapping(qType.getNamespaceURI());
            return tm.getClassForQName(qType);
        }

        /* special case for apache xml soap types */
        if (Constants.NS_URI_XMLSOAP.equals(qType.getNamespaceURI())) {
            if ("Image".equals(qType.getLocalPart()) || "Image[]".equals(qType.getLocalPart())) {
                return java.awt.Image.class;
            }
            else if ("DataHandler".equals(qType.getLocalPart()) || "DataHandler[]".equals(qType.getLocalPart())) {
                try {
                    return Class.forName("javax.activation.DataHandler");
                }
                catch (ClassNotFoundException e) {
                    // fall through
                }
            }
            else if ("Multipart".equals(qType.getLocalPart()) || "Multipart[]".equals(qType.getLocalPart())) {
                try {
                    return Class.forName("javax.mail.internet.MimeMultipart");
                }
                catch (ClassNotFoundException e) {
                    // fall through
                }
            }
            else if ("Source".equals(qType.getLocalPart()) || "Source[]".equals(qType.getLocalPart())) {
                    return javax.xml.transform.Source.class;
            }
        }

        String packageName = getPackageNameFromQName(qType);
        String className;
        if(packageName != null && packageName.length() > 0)
            className = packageName + "." + Utils.xmlNameToJavaClass(qType.getLocalPart());
        else className = Utils.xmlNameToJavaClass(qType.getLocalPart());

        Class javaType = null;
        try {
            javaType = ClassUtils.forName(className);
        }
        catch(ClassNotFoundException e) {
            System.out.println("Unable to find the class \"" + className + "\".  No Axis generated classes were found for qname \"" + qType + "\"");
        }

        if(javaType != null)
            System.out.println("Found an Axis generated type for qname \"" + qType + "\" class \"" + javaType.getCanonicalName() + "\"");

        return javaType;
    }

    private String getPackageNameFromQName(QName qType) {
        /* todo: caching */
        String packageName = Utils.makePackageName(qType.getNamespaceURI());
        return normalizePackageName(packageName, '.');
    }

    /**
     * If the type QName has been suffixed with '[]' strip the brackets and return the new QName.
     * @param type QName to check.
     * @return A new QName.
     */
    private QName stripArrayDelimiters(QName type) {
        if (type.getLocalPart().endsWith("[]")) {
            String namespaceUri = type.getNamespaceURI();
            String localPart = type.getLocalPart().substring(0, type.getLocalPart().length()-2);

            return new QName(namespaceUri, localPart);
        }
        else return type;
    }

    private static String normalizePackageName(String pkg, char separator) {
        for(int i = 0; i < PACKAGE_SEPARATORS.length; i++)
            pkg = pkg.replace(PACKAGE_SEPARATORS[i], separator);
        return pkg;
    }
}
