/**
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 $Header:$
 */
package org.apache.beehive.samples.petstore.forms;

import java.util.List;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.beehive.samples.petstore.model.Cart;
import org.apache.beehive.samples.petstore.model.LineItem;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMessages;

/**
 *
 */
public class CartForm
    implements Serializable, Validatable {

    private Cart _cart;

    public void setCart(Cart cart) {
        _cart = cart;
    }

    public Cart getCart() {
        return _cart;
    }

    public void validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest, ActionMessages actionMessages) {
        List<LineItem> items = getCart().getLineItems();
        if(items != null && items.size() > 0) {
            for(int i = 0; i < items.size(); i++) {
                actionMessages.add("invalidQuantity" + i, new ActionError("order.error.invalidQuantity"));
            }
        }
    }
}
