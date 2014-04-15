/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE-2.0
   
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  
   $Header:$
*/
package org.apache.beehive.controls.test.junit;

import java.util.List;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.test.controls.lifecycle.ControlLifecycle;

/**
 *
 */
public class ComplexLifecycleTest
    extends ControlTestCase {

    private boolean _beginContext;
    private boolean _endContext;

    @Control
    private ControlLifecycle _lifecycleControl;

    public void setUp() {
        /* intentionally, this does not call super */
    }

    public void tearDown() {
        /* intentionally, this does not call super */
    }

    public void testLifecycle() {
        /* startup */
        getControlContainerContextManager().beginContext();
        assertTrue(_beginContext);

        initializeControls();

        /* test */
        assertNotNull(_lifecycleControl);
        assertNotNull(_lifecycleControl.getTheControlBeanContext());
        assertNotNull(_lifecycleControl.getTheResourceContext());

        //System.out.println(_lifecycleControl.echo("foo"));
        List events = _lifecycleControl.getLifecycleEvents();
        assertEquals(2, events.size());
        assertEquals("onCreate", events.get(0));
        assertEquals("onAcquire", events.get(1));

        _lifecycleControl.clearLifecycleEvents();

        _lifecycleControl.echo("bar");
        events = _lifecycleControl.getLifecycleEvents();
        assertEquals(0, events.size());

        /* teardown */
        getControlContainerContextManager().endContext();
        assertTrue(_endContext);
    }

    protected ControlContainerContext initializeControlContainerContext() {
        return new LifecycleTestControlContainerContext();
    }

    class LifecycleTestControlContainerContext
        extends org.apache.beehive.controls.runtime.bean.ControlContainerContext {

        public void beginContext() {
            super.beginContext();
            _beginContext = true;
        }

        public void endContext() {
            super.endContext();
            _endContext = true;
        }
    }
}
