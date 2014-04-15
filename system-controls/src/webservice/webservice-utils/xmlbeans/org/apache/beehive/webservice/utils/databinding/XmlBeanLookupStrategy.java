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

import java.lang.reflect.Method;
import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * XMLBean based implementation fo the {@link BindingLookupStrategy} which provides a mechanims for translating
 * a Java class name to a {@link javax.xml.namespace.QName} and a {@link javax.xml.namespace.QName} to a Java class.
 */
public class XmlBeanLookupStrategy
    implements BindingLookupStrategy {

    private static Log LOGGER = LogFactory.getLog(XmlBeanLookupStrategy.class);

    public QName class2qname(Class cls) {
        if(XmlObject.class.isAssignableFrom(cls))
            return XmlBeans.typeForClass(cls).getName();
        else return null;
    }

    public QName class2qname(Class cls, String defaultnamespace) {
        return class2qname(cls);
    }

    public Class qname2class(QName qname) {
        LOGGER.debug("Get XMLBeans class for Qname: " + qname);

        SchemaTypeLoader stl = XmlBeans.getContextTypeLoader();
        SchemaType st = stl.findType(qname);
        if(st == null) {
            SchemaField sf = stl.findElement(qname);
            if(sf != null)
                st = sf.getType();
        }

        if(st != null) {
            Class xmlClass = st.getJavaClass();

            if(st.isBuiltinType()) {
                Method[] declared = xmlClass.getDeclaredMethods();
                Class natural = scanDeclaredMethodsForViableReturnType(declared);
                if(natural != null)
                    return natural;
                else {
                    if(xmlClass.isInterface()) {
                        for(Class cl : xmlClass.getInterfaces()) {
                            natural = scanDeclaredMethodsForViableReturnType(cl.getDeclaredMethods());
                            if(natural != null) {
                                return natural;
                            }
                        }
                    }
                    else {
                        declared = xmlClass.getSuperclass().getDeclaredMethods();
                        natural = scanDeclaredMethodsForViableReturnType(declared);
                        if(natural != null) {
                            return natural;
                        }
                    }
                }
            }
            return xmlClass;
        }
        else {
            // NOTE jcolwell@bea.com 2004-Nov-30 --
            // keep in mind that a real TMU based on a viable SOAP stack should
            // be used and this pure XmlBean implementation is just a fallback.
            return null;
        }
    }

    private Class scanDeclaredMethodsForViableReturnType(Method[] declared) {
        /*
        todo:  does this make sense?  IT looks as if the type of the class is determined based
               on the method return types.
        */
        for(Method meth : declared) {
            Class returnType = meth.getReturnType();
            if(!returnType.equals(Void.TYPE)) {
                /*
                built-in XmlBeans types may be of the following natural types:
                primitives, byte arrays, Strings, Calendars, BigIntegers and BigDecimals
                */
                if(returnType.isArray()
                    || returnType.isPrimitive()
                    || returnType.equals(String.class)
                    || returnType.equals(QName.class)
                    || returnType.equals(java.util.Calendar.class)
                    || returnType.equals(java.math.BigDecimal.class)
                    || returnType.equals(java.math.BigInteger.class)) {

                    return returnType;
                }
            }
        }
        return null;
    }
}
