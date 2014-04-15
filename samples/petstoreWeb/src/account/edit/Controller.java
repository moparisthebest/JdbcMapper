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
package account.edit;

import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.beehive.netui.pageflow.Forward;

import org.apache.beehive.samples.petstore.model.*;
import org.apache.beehive.samples.petstore.controls.*;
import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchAccountException;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchUserException;
import org.apache.beehive.samples.petstore.forms.AccountForm;
import org.apache.beehive.samples.petstore.forms.UpdateAccountForm;
import org.apache.beehive.samples.petstore.controller.AccountController;
import org.apache.beehive.controls.api.bean.Control;

@Jpf.Controller(
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="rootSharedFlow", type=webappRoot.SharedFlow.class)
    },
    messageBundles = {
        @Jpf.MessageBundle(bundlePath = "org.apache.beehive.samples.petstore.resources.account")
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="edit.jsp"),
		@Jpf.SimpleAction(name="viewOrder", path = "/checkout/viewOrder.do")
    }
)
public class Controller
    extends AccountController {

    @Control()
    private OrderControl _orderControl;

    @Control()
    private AccountControl _accountControl;

    @Jpf.SharedFlowField(name="rootSharedFlow")
    private webappRoot.SharedFlow _sharedFlow = null;

    private UpdateAccountForm _updateForm;
    private Order[] _orders;

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "update", path = "edit.jsp",
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
    protected Forward viewUpdateAccount() {
        _sharedFlow.ensureLogin();

        _updateForm = new UpdateAccountForm(_sharedFlow.getAccount());

        Forward forward = new Forward("update");
        forward.addOutputForm(_updateForm);
        forward.addActionOutput("languages", getLanguages());
        forward.addActionOutput("categoryNames", _sharedFlow.getCategoryNames());
        return forward;
    }

    @Jpf.Action(
        useFormBean = "_updateForm",
        forwards = {
            @Jpf.Forward(
                redirect = true,
                name = "success",
                path = "/shop/Controller.jpf"
           )
        },
        validationErrorForward = @Jpf.Forward(name = "failure", navigateTo = Jpf.NavigateTo.currentPage)
   )
    protected Forward updateAccount(UpdateAccountForm form)
        throws InvalidIdentifierException, NoSuchAccountException, NoSuchUserException
    {
        _sharedFlow.ensureLogin();

        Account account = AccountForm.getAccount(form);

        _accountControl.updateAccount(account);

        _sharedFlow.updateAccount(account);

        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "listOrders.jsp",
                         actionOutputs = {
                             @Jpf.ActionOutput(name = "orders",
                                               type = org.apache.beehive.samples.petstore.model.Order[].class,
                                               required = false)
                         })
        }
    )
    protected Forward listOrders() {
        _sharedFlow.ensureLogin();

        _orders = _orderControl.getOrdersByUserId(_sharedFlow.getAccount().getUserId());

        Forward forward = new Forward("success");
        forward.addActionOutput("orders", _orders);
        return forward;
    }

    /**
     *
     */
}