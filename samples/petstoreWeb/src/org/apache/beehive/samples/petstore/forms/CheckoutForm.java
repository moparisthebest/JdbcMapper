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
package org.apache.beehive.samples.petstore.forms;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.beehive.samples.petstore.model.Order;

import javax.servlet.http.HttpServletRequest;

public class CheckoutForm
    extends OrderForm {

    private boolean _isCheckOut;

    public boolean isCheckOut() {
        return _isCheckOut;
    }

    public void setCheckOut(boolean checkOut) {
        _isCheckOut = checkOut;
    }

    // todo: format validation for e-mail, zip, credit card, date
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        Order order = getOrder();
        ActionErrors errors = new ActionErrors();

        addErrorIfStringEmpty(errors, "creditCard", "order.error.creditCard.required", order.getCreditCard());
        addErrorIfStringEmpty(errors, "expirationDate", "order.error.expirationDate.required", order.getExprDate());
        addErrorIfStringEmpty(errors, "cardType", "order.error.cardType.required", order.getCardType());

        if (order.getBillingAddress() == -1)
			errors.add("billingAddress", new ActionError("order.error.billingAddress.required"));

        if (order.getShippingAddress() == -1)
			errors.add("shippingAddress", new ActionError("order.error.shippingAddress.required"));

        return errors;
    }
}
