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
package account.create;

import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.samples.petstore.controller.AccountController;
import org.apache.beehive.samples.petstore.controls.AccountControl;
import org.apache.beehive.samples.petstore.controls.CatalogControl;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchAccountException;
import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;
import org.apache.beehive.samples.petstore.controls.exceptions.AccountAlreadyExistsException;
import org.apache.beehive.samples.petstore.controls.exceptions.UserAlreadyExistsException;
import org.apache.beehive.samples.petstore.forms.AccountForm;
import org.apache.beehive.samples.petstore.forms.CreateAccountForm;
import org.apache.beehive.samples.petstore.model.Account;

@Jpf.Controller(
    sharedFlowRefs = {
    @Jpf.SharedFlowRef(name = "rootSharedFlow", type = webappRoot.SharedFlow.class)
    },
    messageBundles = {@Jpf.MessageBundle(bundlePath = "org.apache.beehive.samples.petstore.resources.account")}
)
public class Controller
    extends AccountController {

    @Control()
    private CatalogControl _catalogControl;

    @Control()
    private AccountControl _accountControl;

    @Jpf.SharedFlowField(name = "rootSharedFlow")
    private webappRoot.SharedFlow _sharedFlow = null;

    private boolean _isUsernameTaken;
    
    private CreateAccountForm _createForm = null;

    public boolean isUsernameTaken() {
        return _isUsernameTaken;
    }

    @Jpf.Action(
        forwards = {
        @Jpf.Forward(name = "viewCreateAccount", path = "viewCreateAccount.do")
            }
    )
    public Forward begin() {
        return new Forward("viewCreateAccount");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "create.jsp",
            actionOutputs = {
                @Jpf.ActionOutput(name = "languages",
                                  type = java.lang.String[].class,
                                  required = true),
                @Jpf.ActionOutput(name = "categoryNames",
                                  type = java.lang.String[].class,
                                  required = true)
                })
        }
    )
    public Forward viewCreateAccount() {
        Account account = new Account();
        _createForm = new CreateAccountForm(account);

        Forward forward = new Forward("success", _createForm);
        forward.addActionOutput("categoryNames", _sharedFlow.getCategoryNames());
        forward.addActionOutput("languages", getLanguages());
        return forward;
    }

    @Jpf.Action(
        useFormBean = "_createForm",
        forwards = {
            @Jpf.Forward(redirect = true,
                         name = "shop",
                         path = "/shop/Controller.jpf")
            },
            validationErrorForward = @Jpf.Forward(name = "failure", navigateTo = Jpf.NavigateTo.currentPage)
    )
    public Forward createAccount(CreateAccountForm form)
        throws AccountAlreadyExistsException, InvalidIdentifierException, UserAlreadyExistsException, NoSuchAccountException {

        // ensure unique user name
        _isUsernameTaken = false;

        if(_accountControl.checkAccountExists(form.getUserId())) {
            _isUsernameTaken = true;
            return new Forward("failure");
        }

        Account userAccount = AccountForm.getAccount(form);

        _accountControl.insertAccount(userAccount);
        _sharedFlow.updateAccount(userAccount);

        return new Forward("shop");
    }
}