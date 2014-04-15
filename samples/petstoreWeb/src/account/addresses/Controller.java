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
package account.addresses;

import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.samples.petstore.controls.AddressControl;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchAddressException;
import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;
import org.apache.beehive.samples.petstore.forms.AddressForm;
import org.apache.beehive.samples.petstore.forms.CreateAddressForm;
import org.apache.beehive.samples.petstore.model.Address;

@Jpf.Controller(
    sharedFlowRefs = {
        @Jpf.SharedFlowRef(name = "rootSharedFlow", type = webappRoot.SharedFlow.class)
    },
    messageBundles = {@Jpf.MessageBundle(bundlePath = "org.apache.beehive.samples.petstore.resources.account")}
)
public class Controller
    extends PageFlowController {

    @Control()
    private AddressControl _addressControl;

    @Jpf.SharedFlowField(name = "rootSharedFlow")
    private webappRoot.SharedFlow _sharedFlow = null;

    private CreateAddressForm _createForm = null;

	@Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "listAddresses.jsp",
            actionOutputs = {
                @Jpf.ActionOutput(name = "addresses",
                                  type = Address[].class,
                                  required = true)
                })
        }
    )
    protected Forward begin() {
        _sharedFlow.ensureLogin();

        Forward forward = new Forward("success");
        forward.addActionOutput("addresses", _addressControl.getUserAddresses(_sharedFlow.getAccount().getUserId()));
        return forward;
    }
	
	@Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "createUpdate.jsp")
        }
    )
    protected Forward createUpdateAddress()
        throws InvalidIdentifierException {
        _sharedFlow.ensureLogin();
		
		// If an addressId is passed in, this is an edit of an existing address
		String addressId = getRequest().getParameter("addressId");
		Address address;

        /* create new address */
        if (addressId == null) {
			address = new Address();
			address.setUserId(_sharedFlow.getAccount().getUserId());
		}
        /* existing address */
        else address = _addressControl.getAddress(Integer.parseInt(addressId));

		_createForm = new CreateAddressForm(address);
        return new Forward("success", _createForm);
    }

	@Jpf.Action(
	     useFormBean = "_createForm",
	     forwards = {
	         @Jpf.Forward(name = "success", action = "begin")
	     }, validationErrorForward = @Jpf.Forward(name = "failure", navigateTo = Jpf.NavigateTo.currentPage)
	)
    protected Forward updateAddress(CreateAddressForm form)
        throws InvalidIdentifierException, NoSuchAddressException {
        _sharedFlow.ensureLogin();

        Address address = AddressForm.getAddress(form);
		
		if (address.getAddressId() == -1)
			_addressControl.insertAddress(address);
		else _addressControl.updateAddress(address);

		return new Forward("success");
    }	

	@Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", action = "begin")
        }
    )
    protected Forward deleteAddress()
        throws InvalidIdentifierException, NoSuchAddressException {
		_sharedFlow.ensureLogin();
	
		String addressId = getRequest().getParameter("addressId");
		if (addressId == null)
			throw new InvalidIdentifierException("AddressId not passed in");
			
        // Will only delete the address if the user owns it
		_addressControl.deleteAddress(Integer.parseInt(addressId), _sharedFlow.getAccount().getUserId());

        return new Forward("success");
    }

}