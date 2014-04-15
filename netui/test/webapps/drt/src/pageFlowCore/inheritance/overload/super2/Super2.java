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
package pageFlowCore.inheritance.overload.super2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionForm;

import pageFlowCore.inheritance.override.base.Base;

@Jpf.Controller(
    readOnly = true
)
public class Super2 extends Base
{
    public static class FormOne extends ActionForm {
        private String name = "No Name Yet";
        public String getName() {
            return name;
        }
        public void setName(String n) {
            name = n;
        }
    }

    public static class FormTwo extends ActionForm {
        private String name = "No Name Yet";
        public String getName() {
            return name;
        }
        public void setName(String n) {
            name = n;
        }
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "formOneSuccess",
                path = "form1Result.jsp")
        })
    public Forward actionOne(FormOne form) {
        return new Forward("formOneSuccess");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "formTwoSuccess",
                path = "form2Result.jsp")
        })
    public Forward actionOne(FormTwo form) {
        return new Forward("formTwoSuccess");
    }
}
