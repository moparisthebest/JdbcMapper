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
import org.apache.beehive.samples.petstore.model.Category;
import org.apache.beehive.samples.petstore.model.Item;
import org.apache.beehive.samples.petstore.model.Product;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

/** 
 * This control contains access to the pets database
 */ 

@org.apache.beehive.controls.api.bean.ControlExtension
@JdbcControl.ConnectionDriver(databaseDriverClass="org.apache.derby.jdbc.EmbeddedDriver",
		databaseURL="jdbc:derby:" + DBProperties.dbLocation + "/petStoreDB;create=true")  

public interface DerbyCatalogDBControl extends JdbcControl {
    
	// getCategory
    @SQL(statement="select catId, name, description, image from Categories where catId = {catId}")
    public Category getCategory(String catId) throws SQLException;

	// getCategoryList
    @SQL(statement="select catId, name, description, image from Categories")
    public Category[] getCategoryList() throws SQLException;

	// getProductListByCategory
    @SQL(statement="select productId, catId as category, name, description, image from Products where catId = {catId}")
    public Product[] getProductListByCategory(String catId) throws SQLException;

	// getProduct
    @SQL(statement="select productId, catId as category, name, description, image from Products where productId = {productId}")
    public Product getProduct(String productId) throws SQLException;

	// getItemListByProduct
    @SQL(statement="select i.itemId, i.productId, i.status, i.attr1, i.listPrice, i.unitCost, i.supplier, i.inventoryQuantity as Qty, p.name as productName from Items i, Products p where i.productId = p.productId and i.productId = {productId}")
    public Item[] getItemListByProduct(String productId) throws SQLException;

	// getItem
	@SQL(statement="select i.itemId, i.productId, i.status, i.attr1, i.listPrice, i.unitCost, i.supplier, i.inventoryQuantity as Qty, p.name as productName from Items i, Products p where i.productId = p.productId and i.itemId = {itemId}")
    public Item getItem(String itemId) throws SQLException;

	// updateItemQuantity
    @SQL(statement="update Items set inventoryQuantity = inventoryQuantity - {quantity} where itemId = {itemId}")
    public void updateItemQuantity(String itemId, int quantity) throws SQLException;

	// searchProductList
    @SQL(statement="select p.productId, p.catId as category, p.name, p.description, p.image from Products p, Categories c where p.catId = c.catId and (p.name like {searchTerm} or p.description like {searchTerm} or c.name like {searchTerm})")
    public Product[] searchProductList(String searchTerm) throws SQLException;
		
	// initDB
	@SQL(statement="{sql: initString}")
	public void initDB(String initString) throws SQLException;
}