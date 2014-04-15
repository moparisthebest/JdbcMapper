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

package org.apache.beehive.netui.test.controls.composition;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.events.EventSet;

/**
 * A control interface to test control composition.
 */
@ControlInterface
public interface OuterControl {

    @EventSet(unicast = true)
    public interface OuterEvents {
        int report(String message);
    }

    public void fireOuterEvents(String message);

    public InnerControlBean getDeclaredNestedControl();

    public InnerControlBean getDeclaredNestedControl2();

    public InnerControlBean instantiateNestedControlProgrammatically();

    public InnerControlBean instantiateNestedControlWithProperty();

    /*
      * Test outer control receiving event from nested control using
      * EventHandler.
      */
    public String testActivityWakeup();

    public String testActivityReadMessage();

    public String testActivityReport();

    public String testActionShopping();

    public String testActionDostuff();

    /*
      * Tests outer control receiving event from nested control using
      * event listener. The nested control is instantiated programmatically
      */
    public String testEventListener();

    /*
      * Tests outer control receiving event from nested control using
      * event listener. The nested control is instantiated decalratively
      */
    public String testEventListenerByDeclare();

    /*
      * Tests outer control receiving event from nested control using
      * inner class listener. The nested control is instantiated programmatically
      */
    public String testInnerClassListener();

    /*
      * Tests outer control receiving event from nested control using
      * inner class listener. The nested control is instantiated decalratively
      */
    public String testInnerClassListenerByDeclare();
}
