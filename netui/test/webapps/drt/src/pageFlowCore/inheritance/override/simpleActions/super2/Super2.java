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
package pageFlowCore.inheritance.override.simpleActions.super2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionForm;

import pageFlowCore.inheritance.override.base.Base;

@Jpf.Controller(
    inheritLocalPaths=true,
    simpleActions={
        @Jpf.SimpleAction(
            name="begin",
            path="index.jsp",
            loginRequired = false
        ),
        @Jpf.SimpleAction(
            name="actionOne",
            path="index.jsp",
            loginRequired = false
        ),
        @Jpf.SimpleAction(
            name="actionTwo",
            path="index.jsp",
            useFormBeanType=pageFlowCore.inheritance.override.simpleActions.super2.Super2.FormOne.class,
            readOnly = false,
            loginRequired = false
        ),
        @Jpf.SimpleAction(
            name="actionThree",
            path="index.jsp",
            readOnly = true
        ),
        @Jpf.SimpleAction(name="loginOne", path="index.jsp"),
        @Jpf.SimpleAction(
            name="loginTwo",
            path="index.jsp",
            readOnly = true,
            loginRequired = true,
            rolesAllowed = {"roleA"}                
        ),
        @Jpf.SimpleAction(
            name="loginThree",
            path="index.jsp",
            loginRequired = true
        )
    }
)
public class Super2 extends Base
{
    protected FormOne _form;
    public static class FormOne extends ActionForm {
        private String name = "No Name Yet";
        public String getName() {
            return name;
        }
        public void setName(String n) {
            name = n;
        }
    }
}
