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
import java.util.Iterator;
import org.apache.beehive.samples.petstore.model.Cart;
import org.apache.beehive.samples.petstore.model.Order;
import org.apache.beehive.samples.petstore.model.LineItem;
import org.apache.beehive.samples.petstore.model.Item;
import org.apache.beehive.samples.petstore.model.OrderItem;
import org.apache.beehive.samples.petstore.controls.exceptions.DataStoreException;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.log4j.Logger;

@ControlImplementation( isTransient=true )
public class DerbyOrderDao implements OrderDao
{
	@Control()
	private DerbyCatalogDBControl _catalogControl;

	@Control()
	private DerbyOrderDBControl _dbControl;
 	private static final Logger _logger = Logger.getLogger( DerbyAccountDao.class );

	public int getLastOrderIdForUser(String userId)
	{
		int lastOrder = -1;
		try {
			lastOrder = _dbControl.getLastOrderIdForUser(userId);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return lastOrder;
	}

	public int addOrder(Order order, Cart cart)
    {
		int orderId = -1;
		try {
			java.sql.Connection connection = _dbControl.getConnection();
			connection.setAutoCommit( false );
			
			// Add order to DB
			_dbControl.addOrder(order);
			orderId = _dbControl.getLastOrderIdForUser(order.getUserId());
			if (orderId == -1)
			{
				// somehow the order didn't get recorded
	            _logger.error( "Unexpected DAO exception");
				throw new DataStoreException("unexpected database exception");
			}
			
	        // Add the cart items and update the quantities in the DB
			Item item = null;
	        Iterator i = cart.getLineItems().iterator();
	        while (i.hasNext())
	        {
	            LineItem lineItem = (LineItem) i.next();
	            _catalogControl.updateItemQuantity(lineItem.getItem().getItemId(), lineItem.getQuantity());
				_dbControl.addOrderItem(orderId, lineItem.getItem().getItemId(), lineItem.getQuantity());
	        }
			connection.commit();
			connection.setAutoCommit( true );

		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return orderId;
    }

    public Order getOrder(int key, String userId)
    {
		Order order = new Order();
		try {
			order = _dbControl.getOrder(key, userId);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
        return order;
    }

    public Order[] getOrderByUserId(String userId)
    {
        Order[] orders;
		try {
			orders = _dbControl.getOrderByUserId(userId);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
        return orders;
    }

    public OrderItem[] getOrderItems(int orderId)
    {
        OrderItem[] orderItems;
		try {
			orderItems = _dbControl.getOrderItems(orderId);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
        return orderItems;
    }
}