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
package ui.formposting;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

@Jpf.Controller(
    forwards={
        @Jpf.Forward(name="basicFormSuccess", path="basicForm.jsp"),
        @Jpf.Forward(name="outputFormSuccess", path="outputForm.jsp"),
        @Jpf.Forward(name="validatedFormSuccess", path="validatedForm.jsp")
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="org.apache.beehive.samples.netui.resources.formposting.messages")
    }
)
public class Controller
    extends PageFlowController {

    @Jpf.Action()
    public Forward showBasicForm() {
        return new Forward("basicFormSuccess");
    }

    @Jpf.Action()
    public Forward postBasicForm(NameForm form) {
        return new Forward("basicFormSuccess");
    }

    @Jpf.Action()
    public Forward showOutputForm() {
        NameForm outputForm = new NameForm();
        outputForm.setName("Frank");
        return new Forward("outputFormSuccess", outputForm);
    }

    @Jpf.Action()
    public Forward postOutputForm(NameForm form) {
        return new Forward("outputFormSuccess", form);
    }

    @Jpf.Action()
    public Forward showValidatedForm() {
        return new Forward("validatedFormSuccess");
    }

    @Jpf.Action(
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward postValidatedForm(ValidatedNameForm form) {
        return new Forward("validatedFormSuccess");
    }

    public static class NameForm
        implements Serializable {

        private String _name = null;

        public String getName() {
            return _name;
        }

        public void setName(String name) {
            _name = name;
        }
    }

    public static class ValidatedNameForm
        extends NameForm
        implements Validatable {

        private static final String REQUIRED_NAME = "Frank";

        public void validate(ActionMapping actionMapping,
                             HttpServletRequest httpServletRequest,
                             ActionMessages actionMessages) {
            if(!REQUIRED_NAME.equals(getName())) {
                actionMessages.add("name", new ActionMessage("formposting.invalidname"));
            }
        }
    }
}
