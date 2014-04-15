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

import org.apache.beehive.samples.petstore.model.Cart;
import org.apache.beehive.samples.petstore.model.Order;

public class OrderForm
    extends AbstractPetstoreForm {

    private Order _order;
	private Cart _cart;

    public void setOrder(Order order) {
        _order = order;
    }

    public Order getOrder() {
        return _order;
    }
	
	public Cart getCart() {
		return _cart;
	}

	public void setCart(Cart cart) {
		_cart = cart;
	}

}