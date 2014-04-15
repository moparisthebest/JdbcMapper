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
package pageFlowCore.inheritance.override.actions.super1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionForm;

import pageFlowCore.inheritance.override.base.Base;

@Jpf.Controller(
    inheritLocalPaths=true,
    readOnly = true,
    loginRequired = true,
    rolesAllowed = {"role1", "role2"},
    simpleActions={
        @Jpf.SimpleAction(
            name="begin",
            path="index.jsp",
            loginRequired = false)
    }
)
public class Super1 extends Base
{
    public static class FormOne extends ActionForm {
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String n) {
            name = n;
        }
    }

    @Jpf.Action(
        loginRequired = false,
        forwards = {
            @Jpf.Forward(
                name = "noFormSuccess",
                path = "index.jsp")
        })
    public Forward actionOne() {
        return new Forward("noFormSuccess");
    }

    @Jpf.Action(
        readOnly = false,
        loginRequired = false,
        forwards = {
            @Jpf.Forward(
                name = "formSuccess",
                path = "index.jsp")
        })
    public Forward actionTwo(FormOne form) {
        return new Forward("formSuccess");
    }

    @Jpf.Action(
        readOnly = true,
        forwards = {
            @Jpf.Forward(
                name = "Success",
                path = "index.jsp")
        })
    public Forward actionThree() {
        return new Forward("Success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "loginSuccess1",
                path = "index.jsp")
        })
    public Forward loginOne() {
        return new Forward("loginSuccess1");
    }

    @Jpf.Action(
        readOnly = true,
        loginRequired = true,
        rolesAllowed = {"roleA"},
        forwards = {
            @Jpf.Forward(
                name = "loginSuccess2",
                path = "index.jsp")
        })
    public Forward loginTwo() {
        return new Forward("loginSuccess2");
    }

    @Jpf.Action(
        loginRequired = true,
        forwards = {
            @Jpf.Forward(
                name = "loginSuccess3",
                path = "index.jsp")
        })
    public Forward loginThree() {
        return new Forward("loginSuccess3");
    }
}
