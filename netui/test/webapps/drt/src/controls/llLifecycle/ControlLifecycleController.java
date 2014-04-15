/**
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
package controls.llLifecycle;

import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import org.apache.beehive.netui.test.controls.pfcontainer.PageFlowContextControl;

/**
 * Test Control life cycle within a long lived page flow.
 */
@Jpf.Controller(
    forwards = {@Jpf.Forward(name="success", path="index.jsp")}
)
public class ControlLifecycleController
    extends PageFlowController {

    @Control
    private PageFlowContextControl _control;

    public String getPropertyMsg() {
        return checkControl(".getPropertyMsg()");
    }

    @Jpf.Action()
    public Forward begin() {
        String message = checkControl(".begin()");
        return new Forward("success", "controlResults", message);
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "samePage",
                navigateTo=Jpf.NavigateTo.currentPage)
        })
    public Forward navigateToCurrentPage() {
        String message = checkControl(".navigateToCurrentPage()");
        return new Forward("samePage", "controlResults", message);
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "longlived",
                path = "longlived/begin.do")
        })
    public Forward longlived() {
        String message = checkControl(".longlived(): go to long lived page flow");
        return new Forward("longlived", "controlResults", message);
    }

    @Jpf.Action()
    public Forward removeLonglived() {
        String message = checkControl(".removeLonglived(): call call PageFlowUtils.removeLongLivedPageFlow()");

        // BEEHIVE-1103 - the onDestroy() life cylce method of the long
        // lived page flow does not get called when we run this routine.
        PageFlowUtils.removeLongLivedPageFlow(getModulePath() + "/longlived", getRequest(), getServletContext());
        return new Forward("success", "controlResults", message);
    }

    protected void onCreate() {
        checkControl(".onCreate()");
    }

    protected void beforeAction() {
        checkControl(".beforeAction()");
    }

    protected void afterAction() {
        checkControl(".afterAction()");
    }

    protected void onDestroy(HttpSession session) {
        //causes exception, see BEEHIVE-1094
        //checkControl(".onDestroy()");
        //try...
        System.out.println(getClass().getName() + ".onDestroy() - w/o ctrl testing");
    }

    private String checkControl(String label) {
        StringBuilder ctrlMsg = new StringBuilder(128);
        ctrlMsg.append(getClass().getName()).append(label);
        ctrlMsg.append(" | control test ");
        if (_control != null) {
            ctrlMsg.append(_control.checkPageFlow(this) ? "OK" : "FAILED");
        }
        else {
            ctrlMsg.append("not run");
        }
        System.out.println(ctrlMsg.toString());
        return ctrlMsg.toString();
    }
}
