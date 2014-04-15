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
package controls.binding;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.properties.BaseProperties;
import org.apache.beehive.netui.test.controls.binding.BindingTestControl;
import org.apache.beehive.netui.test.controls.binding.DefaultBindingTestControl;
import org.apache.beehive.netui.test.controls.binding.ExternalBindingTestControl;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
        forwards = {@Jpf.Forward(name = "index", path = "index.jsp")}
)
public class ControlBindingController
    extends PageFlowController {

    @Control
    @BaseProperties(controlImplementation = "org.apache.beehive.netui.test.controls.binding.BindingTestControlOverrideImpl")
    public BindingTestControl btc;

    @Control
    public DefaultBindingTestControl dbtc;

    @Control
    public ExternalBindingTestControl ebtc;

    @Jpf.Action
    protected Forward begin() {
        getRequest().setAttribute("BindingTestControlStatus", btc.getStatus());
        getRequest().setAttribute("DefaultBindingTestControlStatus", dbtc.getStatus());
        getRequest().setAttribute("ExternalBindingTestControlStatus", ebtc.getStatus());
        return new Forward("index");
    }
}
