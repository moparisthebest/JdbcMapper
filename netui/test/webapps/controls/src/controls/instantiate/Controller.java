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

package controls.instantiate;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.Controls;
import org.apache.beehive.controls.api.properties.BeanPropertyMap;
import org.apache.beehive.netui.test.controls.instantiate.SingleProperty;
import org.apache.beehive.netui.test.controls.instantiate.SinglePropertyBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * Instantiate a custom control declaratively with property
 */
@Jpf.Controller(forwards = {@Jpf.Forward(name = "index", path = "index.jsp")})
public class Controller extends PageFlowController {

    private static String EXPECTED_GREETING = "Good evening!";

    @Control
    @SingleProperty.Greeting(GreetWord = "Good evening!")
    public SinglePropertyBean myPropertyBean;

    @Jpf.Action
    protected Forward begin() {
        return new Forward("index");
    }

    @Jpf.Action
    protected Forward instantiate() {
        if (myPropertyBean == null || !myPropertyBean.sayHello().equals(EXPECTED_GREETING)) {
            return new Forward("index", "message", "instantiate: ERROR: control was null or invalid message.");
        }
        return new Forward("index", "message", "instantiate: PASSED");
    }

    @Jpf.Action
    protected Forward instantiateP() {
        try {
            BeanPropertyMap greetAttr = new BeanPropertyMap(SingleProperty.Greeting.class);
            greetAttr.setProperty(SinglePropertyBean.GreetWordKey, "Good afternoon!");
            SinglePropertyBean spbean = (SinglePropertyBean) Controls.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.instantiate.SinglePropertyBean", greetAttr);

            if (!"Good afternoon!".equals(spbean.sayHello())) {
                return new Forward("index", "message", "instantiateP: ERROR: invalid message: " + spbean.sayHello());
            }
        }
        catch (Exception e) {
            return new Forward("index", "message", "instantiateP: ERROR: " + e.getMessage());
        }
        return new Forward("index", "message", "instantiateP: PASSED");
    }

}
