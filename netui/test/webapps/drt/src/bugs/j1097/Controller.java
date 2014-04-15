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
package bugs.j1097;

import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.controls.api.bean.Control;

import bugs.j1097.controls.PageFlowControl;

/**
 * PageFlowController to test NetUI exception handling when
 * ControlContainerContext endContext() throws
 */
@Jpf.Controller(forwards={@Jpf.Forward(name="index", path="index.jsp")})
public class Controller
    extends PageFlowController {

    @Control
    private PageFlowControl _pfControl;

    @Jpf.Action()
    public Forward begin() {
        return new Forward("index");
    }

    @Jpf.Action(
        catches = {
            @Jpf.Catch(
                type = java.lang.Exception.class,
                message = "java.lang.Exception",
                messageKey = "ExceptionKey",
                method = "exceptionHandler")
        })
    public Forward testThrowOnRelease() {
        _pfControl.setThrowException(true);
        return new Forward("index");
    }

    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(name = "errorPage", path = "error.jsp")
        })
    protected Forward exceptionHandler(Exception ex, String actionName,
                                       String message, Object form) {
        StringBuilder displayMessage = new StringBuilder("An exception occurred in the action " + actionName);
        displayMessage.append("\n\ttype: " + ex.getClass().getName());
        displayMessage.append("\n\tmessage: " + ex.getMessage());
        return new Forward("errorPage", "errorMessage", displayMessage);
    }
}
