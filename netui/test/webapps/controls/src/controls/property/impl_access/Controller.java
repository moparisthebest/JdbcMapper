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

package controls.property.impl_access;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.test.controls.property.PropertyControl;
import org.apache.beehive.netui.test.controls.property.PropertyControlBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.beans.Beans;

/**
 * Tests getting control property via control context on control implementation class.
 */
@Jpf.Controller(forwards = {@Jpf.Forward(name = "index", path = "../index.jsp")})
public class Controller extends PageFlowController {

    /**
     * A control that declares some propertySets in its control interface.
     * It has a method that allow accessing these property values via control context.
     */
    @Control
    private PropertyControlBean _myControl;

    @Jpf.Action()
    protected Forward begin() {
        return new Forward("index");
    }

    @Jpf.Action()
    protected Forward implAccess() {

        String attribute1 = _myControl.getAttribute1ByContext();
        String attribute3 = _myControl.getAttribute3ByContext();
        if (!attribute1.equals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE1)
                || !attribute3.equals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE3)) {
            return new Forward("index", "message", "implAccess.do: ERROR: Attribute1:" + attribute1 + ". Attribute3:" + attribute3);
        }
        return new Forward("index", "message", "implAccess.do: PASSED");
    }

    @Jpf.Action()
    protected Forward implAccessP() {
        try {
            PropertyControlBean thebean = (PropertyControlBean) Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.property.PropertyControlBean");
            String attribute1 = thebean.getAttribute1ByContext();
            String attribute3 = thebean.getAttribute3ByContext();
            if (!attribute1.equals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE1)
                    || !attribute3.equals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE3)) {
                return new Forward("index", "message", "implAccessP.do: ERROR: Attribute1:" + attribute1 + ". Attribute3:" + attribute3);
            }
        }
        catch (Exception e) {
            return new Forward("index", "message", "implAccessP.do: ERROR: " + e.getMessage());
        }
        return new Forward("index", "message", "implAccessP.do: PASSED");
    }
}
