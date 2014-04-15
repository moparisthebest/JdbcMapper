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
package org.apache.beehive.samples.petstore.model;

import java.math.*;
import java.util.*;

public class Order
    implements java.io.Serializable {

    private int _orderId = -1;

    private Date _orderDate;
    private Date _time;
    private BigDecimal _totalPrice;
    private String _creditCard;
    private String _exprDate;
    private String _cardType;
    private String _status;
	private String _userId;
    private int _shippingAddress = -1;
    private int _billingAddress = -1;

    public void initOrder(String userId, Cart cart) {
        _userId = userId;
        _totalPrice = cart.getSubTotal();
    }

    public int getOrderId() {
        return _orderId;
    }

    public void setOrderId(int orderId) {
        _orderId = orderId;
    }

    public Date getOrderDate() {
        return _orderDate;
    }

    public void setOrderDate(Date orderDate) {
        _orderDate = orderDate;
    }

    public Date getTime() {
        return _time;
    }

    public void setTime(Date time) {
        _time = time;
    }

    public BigDecimal getTotalPrice() {
        return _totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        _totalPrice = totalPrice;
    }

    public String getCreditCard() {
        return _creditCard;
    }

    public void setCreditCard(String creditCard) {
        _creditCard = creditCard;
    }

    public String getExprDate() {
        return _exprDate;
    }

    public void setExprDate(String exprDate) {
        _exprDate = exprDate;
    }

    public String getCardType() {
        return _cardType;
    }

    public void setCardType(String cardType) {
        _cardType = cardType;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public int getShippingAddress() {
        return _shippingAddress;
    }

    public void setShippingAddress(int shippingAddress) {
        _shippingAddress = shippingAddress;
    }

    public int getBillingAddress() {
        return _billingAddress;
    }

    public void setBillingAddress(int billingAddress) {
        _billingAddress = billingAddress;
    }

	public void setUserId(String userId) {
		_userId = userId;
	}
	
	public String getUserId() {
		return _userId;
	}
}
