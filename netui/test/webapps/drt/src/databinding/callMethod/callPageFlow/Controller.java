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
package databinding.callMethod.callPageFlow;


import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Jpf.Controller()
public class Controller
    extends PageFlowController {

    private String _defaultText = "Some Default Text";
    private Cart _cart = null;

    public String nullTest(Object object) {
        if(object == null)
            return "SUCCESS";
        else return "FAILURE";
    }

    public String getDefaultText() {
        return _defaultText;
    }

    public String echo(String echo) {
        return echo;
    }

    public Cart getCart() {
        return _cart;
    }

    public Double sumCartItems(List items) {
        if(items == null) return new Double(0);

        double sum = 0;
        for(int i = 0; i < items.size(); i++)
        {
            LineItem item = (LineItem)items.get(i);
            sum += item.getQuantity() * item.getPrice();
        }

        return new Double(sum);
    }
    
    @Jpf.Action(forwards = {@Jpf.Forward(name = "SumTest",path = "sumTest.jsp")})
    public Forward SumTest() {
        if(_cart == null)
            _cart = initCart();

        return new Forward("SumTest");
    }

    @Jpf.Action(forwards = {@Jpf.Forward(name = "SimpleTest",path = "simpleTest.jsp")})
    public Forward SimpleTest() {
        return new Forward("SimpleTest");
    }

    /**
     * @jpf:action 
     * @jpf:forward name="index" path="index.jsp"
     */
    @Jpf.Action(forwards = {@Jpf.Forward(name = "index", path = "index.jsp")})
    public Forward begin() {
        _cart = initCart();
        return new Forward("index");
    }

    private Cart initCart() {
        Cart c = new Cart();
        c.addItem(new LineItem("Product A", 3, 9.95, 1, true));
        c.addItem(new LineItem("Product C", 2, 19.95, 2, false));
        c.addItem(new LineItem("Product B", 5, 29.95, 3, true));
        c.addItem(new LineItem("Product D", 1, 39.95, 4, true));
        c.addItem(new LineItem("Product E", 3, 59.95, 3, false));
        c.addItem(new LineItem("Product F", 2, 1.95, 2, false));
        c.addItem(new LineItem("Product G", 2, 4.95, 2, true));

        return c;
    }

    public static class Cart
        implements Serializable {

        private List items = null;
        
        public void addItem(LineItem item) {
            if(items == null) items = new ArrayList();

            items.add(item);
        }

        public List getLineItemList() {
            return items;
        }

        public LineItem[] getLineItemArray() {
            if(items == null) return null;
            else return (LineItem[])items.toArray();
        }
    }

    public static class LineItem
        implements Serializable {
        public static final int NOT_SHIPPED = 1;
        public static final int IN_TRANSIT = 2;
        public static final int ARRIVED = 3;
        public static final int UNKNOWN = 4;

        private String name = null;
        private int quantity = 0;
        private double price = 0.0;
        private int shipState = NOT_SHIPPED;
        private boolean inStock = false;

        public LineItem(String name, int quantity, double price, int shipState, boolean inStock) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.shipState = shipState;
            this.inStock = inStock;
        }

        public String getName() {return name;}
        public double getPrice() {return price;}
        public int getQuantity() {return quantity;}
        public int getShipState() {return shipState;}
        public boolean getInStock() {return inStock;}
    }
}
