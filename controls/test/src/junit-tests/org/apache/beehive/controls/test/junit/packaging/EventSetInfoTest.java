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

import org.apache.beehive.controls.test.junit.ControlTestCase;
import org.apache.beehive.controls.test.controls.packaging.HelloBean;
import org.apache.beehive.controls.api.bean.Control;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;

/**
 * A TestCase that tests controls EventInfo
 * <p/>
 * EventInfo annotation on control interface is saved into controlBeanInfo class
 * when the control is packaged.
 */
public class EventSetInfoTest extends ControlTestCase {

    @Control
    private HelloBean _hBean;
    
    /**
     * Test getting FeatureInfo from ControlBeanInfo class
     */
    public void testGetEventSetInfo() throws Exception {

        BeanInfo beanInfo = Introspector.getBeanInfo(Class.forName(
                "org.apache.beehive.controls.test.controls.packaging.HelloBean"));
        EventSetDescriptor[] descriptors = beanInfo.getEventSetDescriptors();

        // Find and inspect EventSet descriptor declared on HelloBean.
        EventSetDescriptor descriptor = findEventSet(descriptors, "eventSet0");
        assertNotNull(descriptor);
        assertTrue(descriptor.isInDefaultEventSet());
        assertFalse(descriptor.isUnicast());

        descriptor = findEventSet(descriptors, "eventSet1");
        assertNotNull(descriptor);
        assertTrue(descriptor.isInDefaultEventSet());
        assertFalse(descriptor.isUnicast());

        descriptor = findEventSet(descriptors, "eventSet2");
        assertNotNull(descriptor);
        assertTrue(descriptor.isInDefaultEventSet());
        assertTrue(descriptor.isUnicast());
    }

    private EventSetDescriptor findEventSet(EventSetDescriptor[] array, String eventname) {
        EventSetDescriptor result = null;
        for (EventSetDescriptor anArray : array) {
            result = anArray;
            if (result.getName().equals(eventname)) {
                break;
            }
            else {
                result = null;
            }
        }
        return result;
    }
}
