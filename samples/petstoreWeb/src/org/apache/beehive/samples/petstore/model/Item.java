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

import java.math.BigDecimal;

/**
 *
 */
public class Item
    implements java.io.Serializable {

    private String _itemId;
    private String _productId;
    private String _status;
    private String _attr1;

    private BigDecimal _listPrice;
    private BigDecimal _unitCost;

    private int _supplier;
    private int _inventoryQuantity;
	private String _productName;
	
    public String getItemId() {
        return _itemId;
    }

    public void setItemId(String itemId) {
        _itemId = itemId.trim();
    }

    public void setQty(int qty) {
        _inventoryQuantity = qty;
    }

    public int getQty() {
        return _inventoryQuantity;
    }

    public String getProductId() {
        return _productId;
    }

    public void setProductId(String productId) {
        _productId = productId;
    }

    public void setSupplier(int supplier) {
        _supplier = supplier;
    }

    public int getSupplier() {
        return _supplier;
    }

    public BigDecimal getListPrice() {
        return _listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        _listPrice = listPrice;
    }

    public BigDecimal getUnitCost() {
        return _unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        _unitCost = unitCost;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public String getAttr1() {
        return _attr1;
    }

    public void setAttr1(String attr1) {
        _attr1 = attr1;
    }

    public String toString() {
        return "(" + getItemId().trim() + "-" + getProductId().trim() + ")";
    }
	
    public String getProductName() {
        return _productName;
    }

    public void setProductName(String productName) {
        _productName = productName;
    }

}
