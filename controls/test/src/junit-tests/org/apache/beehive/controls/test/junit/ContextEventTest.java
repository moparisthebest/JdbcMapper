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
import org.apache.beehive.controls.test.controls.contextevent.BeanContextRecorderBean;

import java.beans.Beans;

/**
 * A TestCase that tests control's context event by listening to and recording controls'
 * context event.
 * All tests on controls instantiated declaratively are deactivated until this feature is supported.
 * <p/>
 * There are two sources of context events: ControlBeanContext and ResourceContext.
 * ResourceContext is unavaliable for Java test by default.
 */
public class ContextEventTest extends ControlTestCase {

    /**
     * A control that listens to and records context events in its impl
     */
    @Control
    private BeanContextRecorderBean myRecorder;

    /**
     * Tests control impl listening to context events.
     * The control is instantiated by declaration
     */
    public void testRecordedByImplDeclaration() throws Exception {
        assertNotNull(myRecorder);
        assertEquals("initonCreate", myRecorder.getRecord());
    }

    /**
     * Tests control impl listening to its context events.
     * The control is instantiated programmatically
     */
    public void testRecordedByImplProgram() throws Exception {
        BeanContextRecorderBean recorderbean = (BeanContextRecorderBean) Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.contextevent.BeanContextRecorderBean");

        assertNotNull(recorderbean);
        assertEquals("initonCreate", recorderbean.getRecord());
    }
}
