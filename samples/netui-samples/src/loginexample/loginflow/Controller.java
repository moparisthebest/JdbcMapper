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
package loginexample.loginflow;

import javax.security.auth.login.LoginException;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;

@Jpf.Controller(
    nested=true,
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="cancel", returnAction="loginCancel")
    },
    catches={
        @Jpf.Catch(type=LoginException.class, path="index.jsp")
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="org.apache.beehive.samples.netui.resources.loginexample.messages")
    }
)
public class Controller extends PageFlowController {

    @Jpf.Action(
        forwards = {
        @Jpf.Forward(name = "success", returnAction = "loginSuccess")
            },
        validationErrorForward = @Jpf.Forward(name = "failure", path = "index.jsp")
    )
    public Forward login(LoginForm form)
        throws LoginException {
        // This ultimately calls login on org.apache.beehive.samples.netui.login.ExampleLoginHandler.
        login(form.getUsername(), form.getPassword());
        return new Forward("success");
    }

    @Jpf.FormBean()
    public static class LoginForm implements java.io.Serializable {
        private String _username;
        private String _password;

        @Jpf.ValidatableProperty(
            // We could have also used the 'displayName' attribute -- a hardcoded string or a 
            // JSP 2.0-style expression.
            displayNameKey = "displaynames.username",
            validateRequired = @Jpf.ValidateRequired(),
            validateMinLength = @Jpf.ValidateMinLength(chars = 4)
        )
        public String getUsername() {
            return _username;
        }

        public void setUsername(String username) {
            _username = username;
        }

        @Jpf.ValidatableProperty(
            displayNameKey = "displaynames.password",
            validateRequired = @Jpf.ValidateRequired(),
            validateMinLength = @Jpf.ValidateMinLength(chars = 4)
        )
        public String getPassword() {
            return _password;
        }

        public void setPassword(String password) {
            _password = password;
        }
    }
}
