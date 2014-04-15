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
package org.apache.beehive.controls.test.junit;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.controls.interfaceHint.c.BarControl;

/**
 * Test that the proper control is being associated with the control reference.
 * There are two controls with the same name but in different java packages.
 */
public class InterfaceHintTest
    extends ControlTestCase {

    @Control
    private BarControl _barControl;

    public void testFooType() {
        assertNotNull(_barControl);
        assertTrue(_barControl.getFooBean() instanceof org.apache.beehive.controls.test.controls.interfaceHint.b.FooControlBean);
    }
}
