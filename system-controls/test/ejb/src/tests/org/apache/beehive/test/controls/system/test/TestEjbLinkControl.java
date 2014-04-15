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

import org.apache.beehive.test.controls.system.controls.EjbLinkControl;
import org.apache.beehive.test.controls.system.ejb.session.SimpleSessionHome;
import org.apache.beehive.test.controls.system.test.util.CactusControlTestCase;
import org.apache.beehive.controls.api.bean.Control;

/**
 * Test an Ejb Link.
 */
public class TestEjbLinkControl extends CactusControlTestCase
{
    @Control
    private EjbLinkControl _ejbControl;

    public void testEjbControlHello() throws Exception {
        String result = _ejbControl.sayHello();
        assertEquals("Hello!", result);
    }

    public void testEjbControlEcho() throws Exception {
        String result = _ejbControl.echo("Hi there");
        assertEquals("Hi there", result);

    }

    public void testGetHome() throws Exception {
        SimpleSessionHome home = (SimpleSessionHome)_ejbControl.getEJBHomeInstance();
        assertNotNull(home);
    }

    public void testHasBeanInstance() throws Exception {
        _ejbControl.sayHello();
        assertTrue(_ejbControl.hasEJBBeanInstance());
    }

    public void testGetBeanInstance() throws Exception {
        _ejbControl.sayHello();
        Object bean = _ejbControl.getEJBBeanInstance();
        assertNotNull(bean);
    }

    public void testGetEjbException() throws Exception {
        Throwable t = _ejbControl.getEJBException();
        assertNull(t);
    }
}
