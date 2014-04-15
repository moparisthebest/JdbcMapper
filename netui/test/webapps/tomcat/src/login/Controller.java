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
package login;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.*;
import javax.security.auth.login.LoginException;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="mustBeLoggedIn", path="success.jsp", loginRequired=true)
    },
    catches={
        @Jpf.Catch(type=Exception.class, path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="loggedIn.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward doLogin( LoginForm form )
        throws LoginException
    {
        login( form.getUsername(), form.getPassword() );
        return new Forward( "success" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.jsp")
        }
    )
    public Forward doLogout()
        throws LoginException
    {
        logout( false );
        return new Forward( "success" );
    }

    @Jpf.FormBean()
    public static class LoginForm
    {
        private String _username;
        private String _password;

        public void setUsername( String username )
        {
            _username = username;
        }

        @Jpf.ValidatableProperty(
            displayName="The username",
            validateRequired=@Jpf.ValidateRequired()
        )
        public String getUsername()
        {
            return _username;
        }

        public void setPassword( String password )
        {
            _password = password;
        }

        @Jpf.ValidatableProperty(
            displayName="The password",
            validateRequired=@Jpf.ValidateRequired()
        )
        public String getPassword()
        {
            return _password;
        }
    }
}
