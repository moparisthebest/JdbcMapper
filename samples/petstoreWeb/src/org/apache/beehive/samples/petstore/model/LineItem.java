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
public class LineItem
    implements java.io.Serializable {

    private int _lineNum;
    private int _quantity;
    private int _orderId = -1;

    private Item _item;

    public LineItem() {
        super();
    }

    public LineItem(int orderId, int lineNum, Item item) {
        this();
        _orderId = orderId;
        _lineNum = lineNum;
        _item = item;
        _quantity = 0;
    }

    public int getOrderId() {
        return _orderId;
    }

    public String getItemId() {
        return _item.getItemId();
    }

    public boolean isInStock() {
        return _item.getQty() > 0;
    }

    public Item getItem() {
        return _item;
    }

    public void setItem(Item item) {
        _item = item;
        calculateTotal();
    }

    public int getLineNum() {
        return _lineNum;
    }

    public void setLineNum(int lineNum) {
        _lineNum = lineNum;
    }

    public int getQuantity() {
        return _quantity;
    }

    public void setQuantity(int quantity) {
        _quantity = quantity;
        calculateTotal();
    }

    public BigDecimal getUnitPrice() {
        return _item.getListPrice();
    }

    public BigDecimal getTotal() {
        return calculateTotal();
    }

    public void incrementQuantity() {
        _quantity++;
    }

    private BigDecimal calculateTotal() {
        BigDecimal total = null;
        if(_item != null && _item.getListPrice() != null)
            total = _item.getListPrice().multiply(new BigDecimal(_quantity));

        return total;
    }

}
