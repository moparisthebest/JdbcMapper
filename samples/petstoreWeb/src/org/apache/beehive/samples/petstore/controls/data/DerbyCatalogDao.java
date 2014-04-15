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

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.samples.petstore.model.Item;
import org.apache.beehive.samples.petstore.model.Category;
import org.apache.beehive.samples.petstore.model.Product;
import org.apache.beehive.samples.petstore.controls.exceptions.DataStoreException;
import org.apache.log4j.Logger;

import java.sql.SQLException;

@ControlImplementation( isTransient=true )
public class DerbyCatalogDao implements CatalogDao
{
	@Control()
	private DerbyCatalogDBControl _dbControl;
	private static final Logger _logger = Logger.getLogger( DerbyAccountDao.class );
	
    public Category[] getCategoryList()
    {
        // todo: bad form -- should return an immutable array here
		Category[] categories;
		try {
			categories = _dbControl.getCategoryList();
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return categories;
    }

    public Category getCategory(String key)
    {
		Category cat = new Category();
		try {
			cat = _dbControl.getCategory(key);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return cat;
    }

    public Item getItem(String key)
    {
		Item item = new Item();
		try {
			item = _dbControl.getItem(key);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return item;
    }
    
    public Item[] getItemListByProduct(String key)
    {
		Item[] items;
		try {
			items = _dbControl.getItemListByProduct(key);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return items;
    }

    // todo: this doesn't handle the case where something goes "out of stock", ie a negative quantity
    public void updateItemQuantity(String item, int quantity)
    {
		try {
			_dbControl.updateItemQuantity(item, quantity);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
    }

    public Product getProduct(String key)
    {
		Product prod = new Product();
		try {
			prod = _dbControl.getProduct(key);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return prod;
    }

    public Product[] getProductListByCategory(String key)
    {
		Product[] products;
		try {
			products = _dbControl.getProductListByCategory(key);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return products;
    }

    public Product[] searchProductList(String searchTerm)
    {
		Product[] products;
		try {
			products = _dbControl.searchProductList("%" + searchTerm + "%");
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
		return products;
    }
	
	public void initDB(String initString)
	{
		try {
			_dbControl.initDB(initString);
		} catch (SQLException e) {
            _logger.error( "Unexpected DAO exception", e );
			throw new DataStoreException("unexpected database exception");
		}
	}
}
