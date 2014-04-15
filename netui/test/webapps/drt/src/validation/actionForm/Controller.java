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
package validation.actionForm;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

// This test uses a form bean that extends the Struts ActionForm and overrides
// the validate() method to ensure we support this user code path.
// It is similar to the Struts Validatable interface test,
// miniTests.validation.Controller
@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "resources.application") 
    })
public class Controller extends PageFlowController
{
    public static class Form extends ActionForm implements Serializable
    {
        private String _text;
        public String getText() {
            return _text;
        }
        public void setText(String text) {
            _text = text;
        }

        public ActionErrors validate(ActionMapping mapping,
                                     HttpServletRequest request) {
            ActionErrors errors = new ActionErrors();
            if (_text == null || !_text.equals("pass")) {
                errors.add("text", new ActionError("error.text.validate"));
            }
            return errors;
        }
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "linkOne",
                path = "index.jsp") 
        })
    public Forward testValidate(Form form)
    {
        return new Forward("linkOne");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "index.jsp") 
        })
    public Forward begin()
    {
        return new Forward("begin");
    }
}
