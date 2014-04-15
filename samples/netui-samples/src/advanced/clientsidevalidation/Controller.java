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
package advanced.clientsidevalidation;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * This is the page flow controller for the Client-side Validation sample.
 */
@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="org.apache.beehive.samples.netui.resources.clientsidevalidation.messages")
    }
)
public class Controller
    extends PageFlowController {

    @Jpf.Action(
        forwards = {
        @Jpf.Forward(name = "success", path = "success.jsp")
            },
        validationErrorForward = @Jpf.Forward(name = "failure", path = "index.jsp")
    )
    public Forward submit(MyForm form) {
        return new Forward("success");
    }

    /**
     * The form bean, with validation annotations on its properties.
     */
    @Jpf.FormBean()
    public static class MyForm implements java.io.Serializable {
        private String _fullName;
        private String _email;

        @Jpf.ValidatableProperty(
            displayNameKey = "displayname.fullName",
            validateRequired = @Jpf.ValidateRequired(messageKey = "errors.required"),
            validateMinLength = @Jpf.ValidateMinLength(chars = 3, messageKey = "errors.minlength"),
            validateMask = @Jpf.ValidateMask(regex = "^[A-Za-z ]*$", messageKey = "errors.letters")
        )
        public String getFullName() {
            return _fullName;
        }

        public void setFullName(String fullName) {
            _fullName = fullName;
        }

        @Jpf.ValidatableProperty(
            displayNameKey = "displayname.email",
            validateRequired = @Jpf.ValidateRequired(messageKey = "errors.required"),
            validateEmail = @Jpf.ValidateEmail(messageKey = "errors.email")
        )
        public String getEmail() {
            return _email;
        }

        public void setEmail(String email) {
            _email = email;
        }
    }
}
