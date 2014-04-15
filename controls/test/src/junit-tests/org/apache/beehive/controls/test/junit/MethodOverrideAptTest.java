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
import org.apache.beehive.controls.test.controls.methodOverride.CustomerDao;

/**
 * Junit tests for making sure that APT can properly generate a control bean when
 * the control's interface overrides methods in a super interface.
 *
 * If this doesn't work compilation will fail, all thats really necessary here is
 * to verify the controls can be initialized.
 */
public class MethodOverrideAptTest
    extends ControlTestCase {

    @Control
    private CustomerDao _customer;

    public void testMethodOverride() {
        assertNotNull(_customer);
    }
}
