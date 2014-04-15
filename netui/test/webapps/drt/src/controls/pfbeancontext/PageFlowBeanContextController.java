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
package controls.pfbeancontext;

import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.test.controls.pfcontainer.PageFlowContextControl;
import org.apache.beehive.controls.api.bean.Control;

/**
 *
 */
@Jpf.Controller(forwards={@Jpf.Forward(name="index", path="index.jsp")})
public class PageFlowBeanContextController
    extends PageFlowController {

    @Control
    private PageFlowContextControl _pfContextControl;

    public boolean isMatch() {
        return check();
    }

    protected void onCreate() {
        check();
    }

    protected void beforeAction() {
        check();
    }

    @Jpf.Action()
    public Forward begin() {
        check();
        return new Forward("index");
    }

    protected void afterAction() {
        check();
    }

    protected void onDestroy(HttpSession httpSession) {
        // This check is disabled because of a bug in JPF + Controls integration where the Page Flow
        // runtime doesn't "beginContext" on a ControlContainerContext during JPF destruction. 
        // check();
    }

    private boolean check() {
        if(!_pfContextControl.checkPageFlow(this))
            throw new IllegalStateException("The Control's PageFlow and this PageFlow are not the same!");

        return true;
    }
}
