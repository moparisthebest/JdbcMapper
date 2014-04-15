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

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.controls.packaging.FeatureInfoControlBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;

/**
 * A TestCase that tests controls FeatureInfo in controls packaging
 * <p/>
 * FeatureInfo annotation on control interface is saved into controlBeanInfo class
 * when the control is packaged.
 * <p/>
 * The tests in this class get feature info from controlBeanInfo class after the
 * control is packaged into jar, in order to verify this part of control packaging
 * process.
 */
public class FeatureInfoTest extends ControlTestCase {

    @Control
    private FeatureInfoControlBean _ficBean;

    /**
     * Test getting FeatureInfo from ControlBeanInfo class
     */
    public void testGetClassLevelFeatureInfo() throws Exception {

        BeanInfo beanInfo = Introspector.getBeanInfo(Class.forName(
                "org.apache.beehive.controls.test.controls.packaging.FeatureInfoControlBean"));

        BeanDescriptor descriptor = beanInfo.getBeanDescriptor();
        assertEquals("FeatureInfoControlBean", descriptor.getName());
        assertEquals("A Control to test packaging", descriptor.getDisplayName());
        assertEquals("This control is to test control packaging", descriptor.getShortDescription());
        assertTrue(descriptor.isExpert());
        assertTrue(descriptor.isHidden());
        assertTrue(descriptor.isPreferred());
    }
}
