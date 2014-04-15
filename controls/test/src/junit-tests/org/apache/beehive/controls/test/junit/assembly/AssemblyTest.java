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

package org.apache.beehive.controls.test.junit.assembly;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlReferences;
import org.apache.beehive.controls.test.controls.assembly.AssemblyTestControl;
import org.apache.beehive.controls.test.controls.assembly.AssemblyTestControl2;
import org.apache.beehive.controls.test.junit.ControlTestCase;

@ControlReferences({AssemblyTestControl2.class})
public class AssemblyTest extends ControlTestCase {
    @Control
    private AssemblyTestControl atc;

    /**
     * Test that a Class generated at assembly time is available.
     */
    public void testAssemblyDeclaritive() throws Exception {
        try {
            Class c = Class.forName("org.apache.beehive.controls.test.assembly" +
                    ".generated.AssemblyTestGenerated");
            assertNotNull(c.newInstance());
        }
        catch (ClassNotFoundException cnfe) {
            fail(cnfe.toString());
        }
    }

    /**
     * Test that a control type specified via @ControlReference participates in assembly.
     */
    public void testAssemblyViaReferences() throws Exception {
        try {
            Class c = Class.forName("org.apache.beehive.controls.test.assembly" +
                    ".generated.AssemblyTest2Generated");
            assertNotNull(c.newInstance());
        }
        catch (ClassNotFoundException cnfe) {
            fail(cnfe.toString());
        }
    }
}
