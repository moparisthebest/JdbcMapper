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

package org.apache.beehive.test.controls.system.test;

import org.apache.beehive.test.controls.system.ejb.entity.SimpleEntityHome;
import org.apache.beehive.test.controls.system.controls.BMPEntityBeanControl;
import org.apache.beehive.test.controls.system.test.util.CactusControlTestCase;
import org.apache.beehive.controls.api.bean.Control;

/**
 * Test a Bean-Managed-Persistence Entity Bean.
 */
public class TestBMPEntityBeanControl extends CactusControlTestCase
{
    @Control
    private BMPEntityBeanControl _ejbControl;

    public void setUp() throws Exception {
        super.setUp();
        _ejbControl.create("entbean1");
    }
    
    public void testEjbControlName() throws Exception {
        _ejbControl.setName("foo");
        assertEquals("foo", _ejbControl.getName());
    }

    public void testEjbControlRef() throws Exception {
        _ejbControl.setRef(66);
        assertEquals(66, _ejbControl.getRef());

    }

    public void testGetHome() throws Exception {
        SimpleEntityHome home = (SimpleEntityHome)_ejbControl.getEJBHomeInstance();
        assertNotNull(home);
    }

    public void testHasBeanInstance() throws Exception {
        _ejbControl.setRef(99);
        assertTrue(_ejbControl.hasEJBBeanInstance());
    }

    public void testGetBeanInstance() throws Exception {
        _ejbControl.setRef(111);
        Object bean = _ejbControl.getEJBBeanInstance();
        assertNotNull(bean);
    }

    public void testGetEjbException() throws Exception {
        Throwable t = _ejbControl.getEJBException();
        assertNull(t);
    }

    public void testGetNextBeanInstance() throws Exception {
        _ejbControl.create("entBean2");
        _ejbControl.create("entBean3");


        _ejbControl.getEJBNextBeanInstance();
        assertEquals("entBean3", _ejbControl.getName());

        Object result = _ejbControl.getEJBNextBeanInstance();
        assertNull(result);
    }
}
