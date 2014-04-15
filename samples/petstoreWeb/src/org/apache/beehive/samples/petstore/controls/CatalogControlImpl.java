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

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.samples.petstore.model.Item;
import org.apache.beehive.samples.petstore.model.Category;
import org.apache.beehive.samples.petstore.model.Product;
import org.apache.beehive.samples.petstore.controls.data.CatalogDao;
import org.apache.beehive.samples.petstore.controls.exceptions.InvalidIdentifierException;

@ControlImplementation( isTransient=true )
public class CatalogControlImpl implements CatalogControl
{
	@Control()
	private CatalogDao _catalogDao;

    public Category[] getCategoryList()
    {
		return _catalogDao.getCategoryList();
    }

    public Category getCategory(String key) throws InvalidIdentifierException
    {
		Category cat = new Category();
		cat = _catalogDao.getCategory(key);
		if (cat == null)
			throw new InvalidIdentifierException("Category: " + key + " not found!");
		return cat;
    }

    public Item getItem(String key) throws InvalidIdentifierException
    {
		Item item = new Item();
		item = _catalogDao.getItem(key);
		if (item == null)
			throw new InvalidIdentifierException("Item: " + key + " not found!");
		return item;
    }
    
    public Item[] getItemListByProduct(String key)
    {
		return _catalogDao.getItemListByProduct(key);
    }

    public void updateItemQuantity(Item item, int quantity) throws InvalidIdentifierException
    {
        if(item == null)
            throw new InvalidIdentifierException("cannot update Item inventory with null or empty itemId");

		item.setQty(item.getQty() - quantity);

		_catalogDao.updateItemQuantity(item.getItemId(), item.getQty());
    }

    public Product getProduct(String key) throws InvalidIdentifierException
    {
		Product prod = new Product();
		prod = _catalogDao.getProduct(key);
		if (prod == null)
			throw new InvalidIdentifierException("Product: " + key + " not found!");
		return prod;
    }

    public Product[] getProductListByCategory(String key)
    {
		return _catalogDao.getProductListByCategory(key);
    }

    public Product[] searchProductList(String searchTerm)
    {
		return _catalogDao.searchProductList(searchTerm);
    }
	
	public void initDB(String initString)
	{
		_catalogDao.initDB(initString);
	}
}
