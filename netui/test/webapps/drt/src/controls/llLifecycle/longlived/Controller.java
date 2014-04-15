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
package controls.llLifecycle.longlived;

import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import org.apache.beehive.netui.test.controls.pfcontainer.PageFlowContextControl;

/**
 * Test Control life cycle within a long lived page flow.
 */
@Jpf.Controller(
    longLived=true
)
public class Controller extends PageFlowController
{
    @Control()
    private PageFlowContextControl _control;

    public String getPropertyMsg() {
        return checkControl(".getPropertyMsg()");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward begin() {
        String message = checkControl(".begin()");
        return new Forward("success", "llControlResults", message);
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "leave",
                path = "../begin.do")
        })
    public Forward leave() {
        String message = checkControl(".leave(): leave the long lived page flow");
        return new Forward("leave", "llControlResults", message);
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "leave",
                path = "../begin.do")
        })
    public Forward leaveAndRemove() {
        // Call remove() and go to another pageflow
        // so we can see onDestroy() work. Note that
        // onDestroy will blow away the CBC for the
        // PageFlowController so the endContext() after
        // this action will blow if this onRelease()
        // tries to call the control!
        String message = checkControl(".leaveAndRemove(): leave the long lived page flow, call remove()");
        remove();
        return new Forward("leave", "llControlResults", message);
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "samePage",
                navigateTo=Jpf.NavigateTo.currentPage)
        })
    public Forward doNothing() {
        String message = checkControl(".doNothing()");
        return new Forward("samePage", "llControlResults", message);
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

    public void valueBound(javax.servlet.http.HttpSessionBindingEvent event) {
        System.out.println(getClass().getName() + ".valueBound()");
        super.valueBound(event);
    }

    public void valueUnbound(javax.servlet.http.HttpSessionBindingEvent event) {
        System.out.println(getClass().getName() + ".valueUnbound()");
        super.valueUnbound(event);
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
