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
package org.apache.beehive.samples.petstore.controls.data;

import java.sql.SQLException;
import org.apache.beehive.samples.petstore.model.Order;
import org.apache.beehive.samples.petstore.model.OrderItem;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

/** 
 * This control contains access to the pets database
 */ 
@org.apache.beehive.controls.api.bean.ControlExtension
@JdbcControl.ConnectionDriver(databaseDriverClass="org.apache.derby.jdbc.EmbeddedDriver",
		databaseURL="jdbc:derby:" + DBProperties.dbLocation + "/petStoreDB;create=true")  
public interface DerbyOrderDBControl 
    extends JdbcControl {
    
	// addOrder
    @SQL(statement="insert into orders (userId, totalPrice, creditCard, " +
    		"exprDate, cardType, status, shippingAddress, billingAddress) values (" +
    		"{order.userId}, {order.totalPrice}, {order.creditCard}, " +
    		"{order.exprDate}, {order.cardType}, 'OK', " +
    		"{order.shippingAddress}, {order.billingAddress})")
    public void addOrder(Order order) throws SQLException;
	
	// getLastOrderIdForUser
	@SQL(statement="select max(orderId) from Orders where userId = {userId}")
	public int getLastOrderIdForUser(String userId) throws SQLException;

	// addOrderItem
    @SQL(statement="insert into orderitems values ({orderId}, {itemId}, {quantity})")
    public void addOrderItem(int orderId, String itemId, int quantity) throws SQLException;
	
	// getOrder
    @SQL(statement="select orderId, userId, orderDate, totalPrice, creditCard," +
    		"exprDate, cardType, status, shippingAddress, billingAddress from Orders where " +
    		"orderId = {orderId} and userId = {userId}")
    public Order getOrder(int orderId, String userId) throws SQLException;
	
	// getOrderByUserId
    @SQL(statement="select orderId, userId, orderDate, totalPrice, creditCard," +
    		"exprDate, cardType, status, shippingAddress, billingAddress " +
    		"from Orders where userId = {userId}")
    public Order[] getOrderByUserId(String userId) throws SQLException;

	// getOrderItems
    @SQL(statement="select o.itemId, productId, listPrice, unitCost, " +
    		"supplier, status, attr1, inventoryQuantity as Qty, o.quantity " +
    		"from OrderItems o, Items i where o.orderId = {orderId} and o.itemId = i.itemId")
    public OrderItem[] getOrderItems(int orderId) throws SQLException;
}