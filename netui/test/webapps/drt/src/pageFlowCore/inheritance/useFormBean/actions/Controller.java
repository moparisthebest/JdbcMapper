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
package pageFlowCore.inheritance.useFormBean.actions;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    protected FormOne _formOne = new FormOne();
    public static class FormOne implements Serializable {
        private String name = "Form One";
        public String getName() {
            return name;
        }
        public void setName(String n) {
            name = n;
        }
    }

    @Jpf.Action(
        useFormBean = "_formOne",
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward actionOne(FormOne form) {
        return new Forward("success");
    }
}
