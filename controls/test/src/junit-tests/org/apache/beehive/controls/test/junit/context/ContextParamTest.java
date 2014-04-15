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

package org.apache.beehive.controls.test.junit.context;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.controls.context.ContextParam;
import org.apache.beehive.controls.test.controls.context.ContextParamExt;
import org.apache.beehive.controls.test.controls.context.ContextParamExtBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.lang.reflect.Method;

/**
 * A TestCase that tests controls context services
 */
public class ContextParamTest extends ControlTestCase {

    @Control
    private ContextParamExtBean paramBean;

    /**
     * Tests context access to parameter names
     */
    public void testContextParamNames() throws Exception {
        String [] paramNames;
        Method intfMethod = ContextParam.class.getMethod("paramNameTest", Method.class);
        Method extMethod = ContextParamExt.class.getMethod("valueTest", String.class, Integer.TYPE, Integer.TYPE,
                                                           Integer.TYPE, Integer.TYPE);

        // Try to read parameter names from methods declared on a base ControlInterface
        paramNames = paramBean.paramNameTest(intfMethod);
        assertNotNull(paramNames);
        assertTrue(paramNames.length == intfMethod.getParameterTypes().length);
        assertEquals(paramNames[0], "m");

        // Try to read parameter names from methods declared on a ControlExtension
        paramNames = paramBean.paramNameTest(extMethod);
        assertNotNull(paramNames);
        assertEquals(paramNames.length, extMethod.getParameterTypes().length);
        assertEquals(paramNames[0], "paramName");
        assertEquals(paramNames[1], "firstArg");
        assertEquals(paramNames[2], "anotherArg");
        assertEquals(paramNames[3], "hasNoParam");
        assertEquals(paramNames[4], "lastArg");
    }

    /**
     * Tests context access to parameter annotations
     */
    public void testContextParamAnnotations() throws Exception {
        Method extMethod = ContextParamExt.class.getMethod("valueTest", String.class, Integer.TYPE, Integer.TYPE,
                                                           Integer.TYPE, Integer.TYPE);

        ContextParam.Param [] paramAnnots = paramBean.paramAnnotTest(extMethod);
        assertNotNull(paramAnnots);
        assertEquals(paramAnnots.length, extMethod.getParameterTypes().length);
        assertNotNull(paramAnnots[0]);
        assertEquals(paramAnnots[0].value(), "paramName");
        assertNotNull(paramAnnots[1]);
        assertEquals(paramAnnots[1].value(), "firstArg");
        assertNotNull(paramAnnots[2]);
        assertEquals(paramAnnots[2].value(), "anotherArg");
        assertNull(paramAnnots[3]);
        assertNotNull(paramAnnots[4]);
        assertEquals(paramAnnots[4].value(), "lastArg");
    }

    /**
     * Tests context access to parameter values by name
     */
    public void testContextParamValues() throws Exception {

        assertEquals(0, paramBean.valueTest("firstArg", 0, 1, 2, 3));
        assertEquals(1, paramBean.valueTest("anotherArg", 0, 1, 2, 3));
        assertEquals(2, paramBean.valueTest("hasNoParam", 0, 1, 2, 3));
        assertEquals(3, paramBean.valueTest("lastArg", 0, 1, 2, 3));
    }
}
