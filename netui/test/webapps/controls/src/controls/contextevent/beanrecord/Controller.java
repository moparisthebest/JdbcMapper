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

package controls.contextevent.beanrecord;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.netui.test.controls.contextevent.RecorderBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.beans.PropertyChangeEvent;

/**
 * Test linstening to control life cycle events by adding listeners to control bean class.
 * The control is instantiated declaratively
 */
@Jpf.Controller(
        forwards = {
        @Jpf.Forward(name = "index", path = "../index.jsp")
                })
public class Controller extends PageFlowController {

    @Control
    public RecorderBean _recorder;

    private boolean _onCreateReceived = false;
    private boolean _onAcquireReceived = false;
    private boolean _onReleaseReceived = false;

    @Jpf.Action()
    protected Forward begin() {

        ControlBeanContext beanContext = _recorder.getControlBeanContext();
        beanContext.addLifeCycleListener(new ControlBeanContext.LifeCycle() {
            public void onCreate() {
                _onCreateReceived = true;
            }

            public void onPropertyChange(PropertyChangeEvent pce) {
            }

            public void onVetoableChange(PropertyChangeEvent pce) {
            }
        });

        try {
            ResourceContext resourceContext = beanContext.getService(ResourceContext.class, null);
            resourceContext.addResourceEventsListener(new ResourceContext.ResourceEvents() {
                public void onAcquire() {
                    _onAcquireReceived = true;
                }

                public void onRelease() {
                    _onReleaseReceived = true;
                }
            });

            String s = _recorder.getRecord();
        }
        catch (Exception e) {
            return new Forward("index", "message", e.getMessage());
        }

        try {
            Thread.sleep(500);
            if (!_onAcquireReceived) {
                return new Forward("index", "message", "The onAcquire ResourceContext event was not received.");
            }
        }
        catch (Exception e) {
            return new Forward("index", "message", e.getMessage());
        }
        return new Forward("index", "message", "PASSED");
    }
}
