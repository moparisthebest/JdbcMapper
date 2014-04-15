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
package org.apache.beehive.controls.test.junit.utils;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A utility class for introspecting Control beans
 */
final class ControlIntrospector {

    private static String LINE_SEPARATOR = System.getProperty("line.separator");

    private Class _beanClass;
    private int _indentLevel = 0;
    private PrintWriter _pw;

    public ControlIntrospector(Class beanClass, OutputStream output) {
        _beanClass = beanClass;
        _pw = new PrintWriter(output);
    }

    private void indent() {
        _indentLevel++;
    }

    private void unindent() {
        _indentLevel--;
    }

    private void printf(String format, Object ...args) {
        for (int i = 0; i < _indentLevel; i++)
            _pw.append("    ");
        _pw.printf(format, args);
        _pw.printf(LINE_SEPARATOR);
    }

    private void printElement(String name, String value) {
        printf("<%s>", name);
        indent();
        printf("%s", value);
        unindent();
        printf("</%s>", name);
    }

    /**
     * Introspects the target class and writes the formatted results from introspection to
     * the provided output stream
     */
    public void introspect(int flags)
            throws IntrospectionException {

        BeanInfo beanInfo = Introspector.getBeanInfo(_beanClass, flags);
        if (beanInfo == null)
            throw new IntrospectionException("No BeanInfo for " + _beanClass);

        introspectBeanInfo(beanInfo);

        _pw.flush();
    }

    public void introspect()
            throws IntrospectionException {
        introspect(Introspector.USE_ALL_BEANINFO);
    }

    private void introspectBeanInfo(BeanInfo beanInfo) {
        printf("<bean-info name=\"%s\">", beanInfo.getBeanDescriptor().getBeanClass().getName());
        indent();
        {
            introspectBeanDescriptor(beanInfo.getBeanDescriptor());

            MethodDescriptor [] methodDescs = beanInfo.getMethodDescriptors();
            if (methodDescs != null && methodDescs.length != 0) {
                printf("<method-descriptors>");
                indent();
                introspectMethodDescriptors(methodDescs);
                unindent();
                printf("</method-descriptors>");
            }
            else printf("<method-descriptors/>");

            PropertyDescriptor [] propDescs = beanInfo.getPropertyDescriptors();
            if (propDescs != null && propDescs.length != 0) {
                printf("<property-descriptors default-index=%d>", beanInfo.getDefaultPropertyIndex());
                indent();
                for (int i = 0; i < propDescs.length; i++)
                    introspectPropertyDescriptor(propDescs[i],
                                                 i == beanInfo.getDefaultPropertyIndex());
                unindent();
                printf("</property-descriptors>");
            }
            else printf("<property-descriptors/>");

            EventSetDescriptor [] eventSetDescs = beanInfo.getEventSetDescriptors();
            if (eventSetDescs != null && eventSetDescs.length != 0) {
                printf("<event-set-descriptors default-index=%d>",
                       beanInfo.getDefaultEventIndex());
                indent();
                for (int i = 0; i < eventSetDescs.length; i++)
                    introspectEventSetDescriptor(eventSetDescs[i],
                                                 i == beanInfo.getDefaultEventIndex());
                unindent();
                printf("</event-set-descriptors>");
            }
            else printf("<event-set-descriptors/>");

            BeanInfo [] additionalBeanInfo = beanInfo.getAdditionalBeanInfo();
            if (additionalBeanInfo != null && additionalBeanInfo.length != 0) {
                printf("<additional-bean-info>");
                indent();
                {
                    for (int i = 0; i < additionalBeanInfo.length; i++)
                        introspectBeanInfo(additionalBeanInfo[i]);
                }
                unindent();
                printf("</additional-bean-info>");
            }
            else printf("<additional-bean-info/>");
        }
        unindent();
        printf("</bean-info>");
    }

    private void introspectBeanDescriptor(BeanDescriptor beanDesc) {
        printf("<bean-descriptor name=\"%s\">", beanDesc.getDisplayName());
        indent();
        {
            introspectFeatureDescriptor(beanDesc);
            ;
            printElement("bean-class", beanDesc.getBeanClass().getName());
            Class customizerClass = beanDesc.getCustomizerClass();
            if (customizerClass != null)
                printElement("customizer-class", customizerClass.getName());
        }
        unindent();
        printf("</bean-descriptor>");
    }

    private void introspectFeatureDescriptor(FeatureDescriptor featureDesc) {
        printf("<feature-descriptor name=\"%s\"", featureDesc.getDisplayName());
        indent();
        {
            printf("full-name=\"%s\"", featureDesc.getName());
            printf("is-expert=%b", featureDesc.isExpert());
            printf("is-hidden=%b", featureDesc.isHidden());
            printf("is-preferred=%b>", featureDesc.isPreferred());

            printElement("short-description", featureDesc.getShortDescription());

            Enumeration<String> attrNames = featureDesc.attributeNames();
            if (attrNames != null && attrNames.hasMoreElements()) {
                SortedSet<String> sortedNames = new TreeSet<String>();
                while (attrNames.hasMoreElements())
                    sortedNames.add(attrNames.nextElement());
                printf("<attributes>");
                indent();
                {
                    for (String attrName : sortedNames) {
                        // Convert the attribute value to a String, but drop any object ID
                        // that is going to fail an equivalence comparison
                        String attrValue = featureDesc.getValue(attrName).toString();
                        int atIndex = attrValue.indexOf('@');
                        if (atIndex > 0)
                            attrValue = attrValue.substring(0, atIndex + 1);

                        printf("<attribute name=\"%s\" value=\"%s\">",
                               attrName, attrValue);
                    }
                }
                unindent();
                printf("</attributes>");
            }
        }
        unindent();
        printf("</feature-descriptor>", featureDesc.getDisplayName());
    }

    /**
     * Provides a predictable ordering of method descriptors, based upon the underlying
     * java.lang.reflect.Method attributes.  Uses the following tests:
     * - compare method names.  If equal, then:
     * - compare parameter list lengths.  If equals, then:
     * - compare parameter type names, in order, until they are unequal
     */
    static private class MethodDescriptorComparator
            implements Comparator<MethodDescriptor> {

        public int compare(MethodDescriptor md1, MethodDescriptor md2) {
            Method m1 = md1.getMethod();
            Method m2 = md2.getMethod();
            int retval = m1.getName().compareTo(m2.getName());
            if (retval == 0) {
                Class [] parms1 = m1.getParameterTypes();
                Class [] parms2 = m1.getParameterTypes();
                if (parms1.length < parms2.length)
                    retval = -1;
                else if (parms1.length > parms2.length)
                    retval = 1;
                else {
                    for (int i = 0; i < parms1.length; i++) {
                        retval = parms1[i].getName().compareTo(parms2[i].getName());
                        if (retval != 0)
                            break;
                    }
                }
            }
            return retval;
        }

        public boolean equals(Object o) {
            return o != null && o instanceof MethodDescriptorComparator;
        }
    }

    /**
     * Sorts an input array of MethodDescriptors
     */
    private Set<MethodDescriptor> sortMethodDescriptors(MethodDescriptor [] methodDescs) {
        Set<MethodDescriptor> sortedMethodDescs =
                new TreeSet<MethodDescriptor>(new MethodDescriptorComparator());
        for (int i = 0; i < methodDescs.length; i++)
            sortedMethodDescs.add(methodDescs[i]);
        return sortedMethodDescs;
    }

    private void introspectMethodDescriptors(MethodDescriptor [] methodDescs) {
        Set<MethodDescriptor> sortedMethodDescs = sortMethodDescriptors(methodDescs);
        for (MethodDescriptor methodDesc : sortedMethodDescs)
            introspectMethodDescriptor(methodDesc);
    }

    private void introspectMethodDescriptor(MethodDescriptor methodDesc) {
        printf("<method-descriptor name=\"%s\"", methodDesc.getDisplayName());
        indent();
        {
            printElement("method", methodDesc.getMethod().toGenericString());
            ParameterDescriptor [] paramDescs = methodDesc.getParameterDescriptors();
            if (paramDescs != null && paramDescs.length != 0) {
                printf("<parameter-descriptors>");
                indent();
                for (int i = 0; i < paramDescs.length; i++)
                    introspectParameterDescriptor(paramDescs[i]);
                unindent();
                printf("</parameter-descriptors>");
            }
            else
                printf("<param-descriptors/>");
            introspectFeatureDescriptor(methodDesc);
        }
        unindent();
        printf("</method-descriptor>");
    }

    private void introspectParameterDescriptor(ParameterDescriptor paramDesc) {
        printf("<parameter-descriptor name=\"%s\">", paramDesc.getDisplayName());
        indent();
        introspectFeatureDescriptor(paramDesc);
        unindent();
        printf("</parameter-descriptor>", paramDesc.getDisplayName());
    }

    private void introspectPropertyDescriptor(PropertyDescriptor propDesc, boolean isDefault) {
        printf("<property-descriptor name=\"%s\">", propDesc.getDisplayName());
        indent();
        {
            printf("type=\"%s\"", propDesc.getPropertyType().getName());
            printf("isBound=%b", propDesc.isBound());
            printf("isConstrained=%b", propDesc.isConstrained());
            printf("isDefault=%b>", isDefault);

            Method readMethod = propDesc.getReadMethod();
            if (readMethod != null)
                printElement("read-method", readMethod.toGenericString());

            Method writeMethod = propDesc.getWriteMethod();
            if (writeMethod != null)
                printElement("write-method", writeMethod.toGenericString());

            Class propertyEditorClass = propDesc.getPropertyEditorClass();
            if (propertyEditorClass != null)
                printElement("property-editor-class", propertyEditorClass.getName());
            introspectFeatureDescriptor(propDesc);
        }
        unindent();
        printf("</property-descriptor>", propDesc.getDisplayName());
    }

    private void introspectEventSetDescriptor(EventSetDescriptor eventDesc, boolean isDefault) {
        printf("<event-descriptor name=\"%s\"", eventDesc.getDisplayName());
        indent();
        {
            printf("is-unicast=%b", eventDesc.isUnicast());
            printf("is-in-default=%b", eventDesc.isInDefaultEventSet());
            printf("is-default=%b>", isDefault);
            printElement("listener-type", eventDesc.getListenerType().getName());

            Method meth = eventDesc.getAddListenerMethod();
            if (meth != null)
                printElement("add-listener-method", meth.toGenericString());

            meth = eventDesc.getRemoveListenerMethod();
            if (meth != null)
                printElement("remove-listener-method", meth.toGenericString());

            meth = eventDesc.getGetListenerMethod();
            if (meth != null)
                printElement("get-listener-method", meth.toGenericString());

            MethodDescriptor [] methodDescs = eventDesc.getListenerMethodDescriptors();
            if (methodDescs != null && methodDescs.length != 0) {
                printf("<listener-method-descriptors>");
                indent();
                introspectMethodDescriptors(methodDescs);
                unindent();
                printf("</listener-method-descriptors>");
            }
            else printf("<listener-method-descriptors/>");
            introspectFeatureDescriptor(eventDesc);
        }
        unindent();
    }

    public static void main(String[] args)
            throws Exception {

        if (args.length < 1)
            System.err.println("Usage: java org.apache.beehive.controls.test.controls.util.ControlIntrospector [-path <path>] beanClass");

        String className = null;
        String outputPath = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-outputPath")) {
                if (i + 1 < args.length)
                    outputPath = args[i + 1];
                else throw new RuntimeException("Invalid command line; unable to find value for \"-outputPath\"");
            }
        }

        className = args[args.length - 1];
        System.out.println("Create .beaninfo for class \"" + className + "\"");
        if (outputPath != null)
            System.out.println("Write .beaninfo to file \"" + outputPath + "\"");

        Class beanClass = Class.forName(className);
        PrintStream ps = System.out;
        try {
            if (outputPath != null)
                ps = new PrintStream(new FileOutputStream(new File(outputPath)));

            ControlIntrospector ci = new ControlIntrospector(beanClass, ps);
            ci.introspect();
        }
        finally {
            if (ps != null && ps != System.out)
                ps.close();
        }
    }
}
