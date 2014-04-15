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
package org.apache.beehive.netui.test.script;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import org.apache.beehive.netui.test.beans.IProduct;

/**
 * Unit tests for XScript expression parsing.
 */
public abstract class AccessTest
    extends AbstractExpressionTest {

    public void testAccess()
        throws Exception {
        String productName = " Mountain Bike Frame";

        getPageContext().setAttribute("iproduct", IProduct.Factory.getInstance(productName, 2500.00));

        Object result = evaluateExpression("{pageScope.iproduct.name}", getPageContext());
        assertEquals(productName, result);
    }

    public void testMapAccess()
        throws Exception {
        Map map = new HashMap();
        map.put("product1", IProduct.Factory.getInstance("Widget Crankset", 400.00));
        map.put("product2", IProduct.Factory.getInstance("Widget Bottom Bracket", 60.00));

        Iterator iterator = map.entrySet().iterator();
        while(iterator.hasNext()) {
            Object item = iterator.next();
            System.out.println("item type: " + item.getClass().getName());

            java.beans.PropertyDescriptor[] pds = java.beans.Introspector.getBeanInfo(item.getClass()).getPropertyDescriptors();

            getPageContext().setAttribute("currentItem", item);

            Object key = evaluateExpression("{pageScope.currentItem.key}", getPageContext());
            Object value = evaluateExpression("{pageScope.currentItem.value}", getPageContext());
            System.out.println("key: " + key + " value: " + value);
        }
    }

    public AccessTest(String name) {
        super(name);
    }

}
