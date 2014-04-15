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
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */
public class Cart
    implements java.io.Serializable {

    private List<LineItem> _itemList;

    public Cart() {
        _itemList = new ArrayList<LineItem>();
    }

    public List<LineItem> getLineItems() {
        return _itemList;
    }

    public int getNumberOfItems() {
        return _itemList.size();
    }

    /**
     * Add an item to the cart.  If the item does not exist in the cart,
     * it is added to the cart and the CartItem's quantity is incremented.
     */
    public void addItem(Item item) {
        LineItem lineItem = getLineItem(item.getItemId());
        if(lineItem == null) {
            lineItem = new LineItem();
            lineItem.setItem(item);
            lineItem.setQuantity(0);
            _itemList.add(lineItem);
        }

        lineItem.incrementQuantity();
    }

    public boolean containsItemId(String itemId) {
        return (getLineItem(itemId) != null);
    }

    public Item removeItemById(String itemId) {
        LineItem lineItem = getLineItem(itemId);

        if(lineItem != null)
            _itemList.remove(lineItem);

        return lineItem.getItem();
    }

    public void incrementQuantityByItemId(String itemId) {
        LineItem lineItem = getLineItem(itemId);

        if(lineItem == null)
            throw new IllegalStateException("Can not increment the quanitty of a Cart item when the item is not in the cart");

        lineItem.incrementQuantity();

        getSubTotal();
    }

    public void setQuantityByItemId(String itemId, int quantity) {
        LineItem lineItem = getLineItem(itemId);

        if(lineItem == null)
            throw new IllegalStateException("Can not set the item's quantity in a Cart that does not contain the item.");

        lineItem.setQuantity(quantity);
    }

    public BigDecimal getSubTotal() {
        BigDecimal subTotal = new BigDecimal("0");

        Iterator items = _itemList.iterator();
        while(items.hasNext()) {
            LineItem lineItem = (LineItem)items.next();
            Item item = lineItem.getItem();
            BigDecimal listPrice = item.getListPrice();
            BigDecimal quantity = new BigDecimal(String.valueOf(lineItem.getQuantity()));
            subTotal = subTotal.add(listPrice.multiply(quantity));
        }

        return subTotal;
    }

    /**
     * Internal method to lookup a CartItem based on an itemId.
     */
    private LineItem getLineItem(String itemId) {
        for(int i = 0; i < _itemList.size(); i++) {
            if(_itemList.get(i).getItem().getItemId().equals(itemId))
                return _itemList.get(i);
        }
        return null;
    }
}
