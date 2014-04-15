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

package org.apache.beehive.controls.test.junit.packaging;

import junit.framework.TestCase;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.controls.packaging.BoundPropertyControlBean;

/**
 * A TestCase that tests controls PropertyInfo
 * <p/>
 * PropertyInfo annotation on control interface is saved into controlBeanInfo class
 * when the control is packaged.
 * <p/>
 * The tests in this class get property info from controlBeanInfo class after the
 * control is packaged into jar, in order to verify this part of control packaging
 * process.
 */
public class PropertyInfoTest extends TestCase {

    @Control
    private BoundPropertyControlBean _bpcBean;

    /**
     * Test getting FeatureInfo from ControlBeanInfo class
     */
    public void testGetPropertyInfo() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(Class.forName(
                "org.apache.beehive.controls.test.controls.packaging.BoundPropertyControlBean"));
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

        // Find and inspect property descriptor declared on BoundPropertyControlBean.
        PropertyDescriptor descriptor = findProperty(descriptors, "Brand");
        assertNotNull(descriptor);
        assertTrue(descriptor.isBound());
        assertFalse(descriptor.isConstrained());

        descriptor = findProperty(descriptors, "Material");
        assertNotNull(descriptor);
        assertFalse(descriptor.isBound());
        assertTrue(descriptor.isConstrained());

        descriptor = findProperty(descriptors, "Quality");
        assertNotNull(descriptor);
        assertTrue(descriptor.isBound());
        assertTrue(descriptor.isConstrained());

        descriptor = findProperty(descriptors, "age");
        assertNotNull(descriptor);
        assertTrue(descriptor.isBound());
        assertFalse(descriptor.isConstrained());

        descriptor = findProperty(descriptors, "height");
        assertNotNull(descriptor);
        assertFalse(descriptor.isBound());
        assertTrue(descriptor.isConstrained());
    }

    private PropertyDescriptor findProperty(PropertyDescriptor[] array, String propertyname) {

        PropertyDescriptor result = null;
        for (PropertyDescriptor anArray : array) {
            result = anArray;
            if (result.getName().equals(propertyname))
                break;
            else
                result = null;
        }
        return result;
    }
}
