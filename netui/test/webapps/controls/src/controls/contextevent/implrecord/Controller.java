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

package controls.contextevent.implrecord;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.test.controls.contextevent.RecorderBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowControlContainer;
import org.apache.beehive.netui.pageflow.PageFlowControlContainerFactory;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * Test linstening to control life cycle events by EventHandler on control implementation class.
 * The control is instantiated declaratively
 */
@Jpf.Controller(forwards = {@Jpf.Forward(name = "index", path = "../index.jsp")})
public  class Controller extends PageFlowController {

    @Control
    public RecorderBean _recorder;

    @Jpf.Action()
    protected Forward begin() {
        return new Forward("index");
    }

    @Jpf.Action()
    protected Forward testContextEvents() {

        String record = _recorder.getRecord();
        if (!record.equals("initonCreateonAcquire")) {
            return new Forward("index", "message", "The events recorded by control impl:" + record);
        }
        return new Forward("index", "message", "PASSED");
    }

    @Jpf.Action()
    protected Forward testContextEventsP() {

        try {
            PageFlowControlContainer pfcc = PageFlowControlContainerFactory.getControlContainer(getRequest(), getServletContext());
            pfcc.createAndBeginControlBeanContext(this, getRequest(), getResponse(), getServletContext());

            RecorderBean recorder = (RecorderBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.contextevent.RecorderBean");

            String record = recorder.getRecord();
            if (!record.equals("initonCreateonAcquire")) {
                return new Forward("index", "message", "The events recorded by control impl:" + record);
            }
        }
        catch (Exception e) {
            // fail
        }
        return new Forward("index", "message", "PASSED");
    }
}
