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

package controls.property.client_access;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.test.controls.property.PropertyControl;
import org.apache.beehive.netui.test.controls.property.PropertyControlBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.beans.Beans;

/**
 * Tests client getting control property via getter/setter on control bean
 * The control bean is instantiated declaratively
 */
@Jpf.Controller(forwards = {@Jpf.Forward(name = "index", path = "../index.jsp")})
public class Controller extends PageFlowController {

    @Control
    private PropertyControlBean _myControl;

    @Jpf.Action
    protected Forward begin() {
        return new Forward("index");
    }

    @Jpf.Action
    protected Forward clientAccess() {
        String attribute1 = _myControl.getAttribute1();
        String attribute3 = _myControl.getPropertyTwoAttribute3();

        if (!attribute1.equals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE1)
                || !attribute3.equals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE3)) {
            return new Forward("index", "message", "clientAccess.do: ERROR: Attribute1:" + attribute1 + ". Attribute3" + attribute3);
        }
        return new Forward("index", "message", "clientAccess.do: PASSED");
    }

    @Jpf.Action
    protected Forward clientAccessP() {
        try {
            PropertyControlBean thebean = (PropertyControlBean) Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.property.PropertyControlBean");

            String attribute1 = thebean.getAttribute1();
            String attribute3 = thebean.getPropertyTwoAttribute3();

            if (!attribute1.equals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE1)
                    || !attribute3.equals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE3)) {
                return new Forward("index", "message", "clientAccessP.do: ERROR: Attribute1:" + attribute1 + ". Attribute3" + attribute3);
            }
        }
        catch (Exception e) {
            return new Forward("index", "message", "clientAccessP.do: ERROR: " + e.getMessage());
        }
        return new Forward("index", "message", "clientAccessP.do: PASSED");
    }
}
