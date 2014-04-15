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
 */
package org.apache.beehive.controls.system.webservice.tests.wscgen;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.beehive.controls.system.webservice.utils.GenericHolder;

import javax.xml.rpc.holders.IntHolder;
import java.lang.reflect.Method;

/**
 * Test wsc generation from a doc/lit/wrapped web service wsdl.
 */
public class WscGenFromDocLitWrapWsdlTest extends TestCase {

    private final Class genWsc = doclitwrap.web.complex.DocumentLiteralWrappedSampleService.class;

    /**
     * Test all of the method signatures.
     * @throws Exception
     */
    public void testMethodSignatures() throws Exception {
        testMethodSignature("changeAddressInBody", "int", GenericHolder.class);
        testMethodSignature("changeAddressInHeader", "int", GenericHolder.class);
        testMethodSignature("createAddressInBody", "void", IntHolder.class, GenericHolder.class);
        testMethodSignature("createAddressInHeader", "void", GenericHolder.class, IntHolder.class);
        testMethodSignature("returnAddressFromBody", "doclitwrap.web.complex.Address", doclitwrap.web.complex.Address.class);
        testMethodSignature("returnAddressFromHeader", "doclitwrap.web.complex.Address", doclitwrap.web.complex.Address.class);
        testMethodSignature("throwAddressException", "int", int.class);

    }

    private void testMethodSignature(String methodName, String returnClassName, Class... params) throws Exception {
        Method m = null;
        try {
            m = genWsc.getMethod(methodName, params);
        } catch (NoSuchMethodException e) {
            fail("Could not find method '" + methodName + "(" + params + ")");
        }
        assertEquals(returnClassName, m.getReturnType().getName());
    }

    public static Test suite() {
        return new TestSuite(WscGenFromDocLitWrapWsdlTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}



