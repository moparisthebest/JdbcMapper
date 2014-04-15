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
package auth;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;

import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchAccountException;
import org.apache.beehive.samples.petstore.forms.ReturnToForm;
import org.apache.beehive.samples.petstore.forms.SecurityCheckForm;

@Jpf.Controller(
    nested = true,
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="rootSharedFlow", type=webappRoot.SharedFlow.class)
    }
)
public class Controller
    extends PageFlowController
{
    @Jpf.SharedFlowField(name="rootSharedFlow")
    private webappRoot.SharedFlow _sharedFlow = null;

    private ReturnToForm _initForm;

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "login.jsp")
        }
    )
    protected Forward begin(ReturnToForm initForm) {
        _initForm = initForm;
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "login.jsp")
        }
    )
    protected Forward viewLogin() {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "failure", path = "loginError.jsp"),
            @Jpf.Forward(
                name = "success",
                returnAction = "rootSharedFlow.loginDone",
                outputFormBean = "_initForm"
            )
        }
    )
    protected Forward securityCheck(SecurityCheckForm form)
        throws NoSuchAccountException, InvalidIdentifierException
    {
        try
        {
            _sharedFlow.handleLogin(form.getj_username());
        }
        catch (NoSuchAccountException e)
        {
        }

        if(!_sharedFlow.isUserLoggedIn())
            return new Forward("failure");
        else return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "/Controller.jpf")
        }
    )
    protected Forward logout() {
        _sharedFlow.handleLogout();
        return new Forward("success");
    }
}
