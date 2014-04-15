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
package org.apache.beehive.samples.petstore.controls;

import java.util.ArrayList;
import java.util.List;
import org.apache.beehive.samples.petstore.model.Cart;
import org.apache.beehive.samples.petstore.model.Order;
import org.apache.beehive.samples.petstore.model.OrderItem;
import org.apache.beehive.samples.petstore.controls.data.OrderDao;
import org.apache.beehive.samples.petstore.controls.exceptions.NoSuchOrderException;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;

@ControlImplementation( isTransient=true )
public class OrderControlImpl implements OrderControl
{
    @Control()
    private OrderDao _orderDao;
 
    private static final ArrayList<String> CARD_TYPE_LIST;

    static
    {
        CARD_TYPE_LIST = new ArrayList<String>();
        CARD_TYPE_LIST.add("Visa");
        CARD_TYPE_LIST.add("MasterCard");
        CARD_TYPE_LIST.add("American Express");
    }

    public Order createOrder()
    {
        Order order = new Order();
        return order;
	}

    public int commitOrder(Order order, Cart cart)
    {
		return _orderDao.addOrder(order, cart);
    }

    public Order getOrder(String userId, int key) throws NoSuchOrderException
    {
		Order order = new Order();
		order = _orderDao.getOrder(key, userId);
		if((order == null) || (order.getOrderId() == -1))
			throw new NoSuchOrderException("No order could be found with order id: " + key + " for user: " + userId);

		return order;
    }

    public Order[] getOrdersByUserId(String userId)
    {
        return _orderDao.getOrderByUserId(userId);
    }

    public OrderItem[] getOrderItemsByOrderId(int orderId)
    {
        return _orderDao.getOrderItems(orderId);
    }

    public List getCreditCards()
    {
        return CARD_TYPE_LIST;
    }
}