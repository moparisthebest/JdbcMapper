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
 */
package org.apache.beehive.samples.petstore.test;

import junit.framework.TestCase;

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class Cart extends TestCase {

   public void testEmptyCart() throws Exception 
   {
	 /*
	  * Test the cart is empty at startup
	  */
	  
      // Start the conversation
	  WebResponse resp = Util.navToCart();

	  // Make sure the cart is empty
	  assertTrue(resp.getText().contains(Util.getBundle("view", "emptyCartMessage"))); // "Your cart is empty."
	  
   }

   public void testAddItem() throws Exception 
   {
	  /*
	   * Add an item to the cart
	   */

	  // Go to the spotted koi page
	  WebResponse resp = Util.navToItem("EST-4");	   

	  // Add the item to the cart
	  resp = resp.getLinkWith(Util.getBundle("view", "buttonAddToCart")).click();

	  // Make sure item is in the cart
	  // Check the first two rows of the table
	  WebTable itemTable = resp.getTableStartingWith(Util.getBundle("view", "itemIdLabel")); // "Item ID"
	  itemTable.purgeEmptyCells();
	  String[][] table = itemTable.asText();
	  assertEquals( "EST-4", table[1][0].trim());
	  assertEquals( "FI-FW-01", table[1][1].trim());
	  assertTrue( table[1][2].contains("Spotted") );
	  assertEquals( "$18.50",  table[1][6].trim() );
   }
   
   public void testRemoveItem() throws Exception 
   {
	 /*
	  * Test the cart is empty after adding then removing an item
	  */
	  
      // Start the conversation
	  WebResponse resp = Util.navToCart();

	  // Make sure the cart is empty
	  assertTrue(resp.getText().contains(Util.getBundle("view", "emptyCartMessage"))); // "Your cart is empty."
	  
	  // Add an item
	  // Go to the spotted koi page
	  resp = Util.navToItem("EST-4");	   

	  // Add the item to the cart
	  resp = resp.getLinkWith(Util.getBundle("view", "buttonAddToCart")).click();

	  // Make sure item is in the cart
	  // Check the first two rows of the table
	  WebTable itemTable = resp.getTableStartingWith(Util.getBundle("view", "itemIdLabel")); // "Item ID"
	  itemTable.purgeEmptyCells();
	  String[][] table = itemTable.asText();
	  assertEquals( "EST-4", table[1][0].trim());
	  
	  // Now remove it
	  resp = resp.getLinkWith(Util.getBundle("view", "buttonRemove")).click(); // "Remove"
	  
	  // Make sure the cart is empty
	  assertTrue(resp.getText().contains(Util.getBundle("view", "emptyCartMessage"))); // "Your cart is empty."
   }
   
   public void testUpdateQuantity() throws Exception 
   {
	 /*
	  * Test the cart quantity is updated correctly
	  */
	  
      // Start the conversation
	  WebResponse resp = Util.navToCart();

	  // Make sure the cart is empty
	  assertTrue(resp.getText().contains("Your cart is empty."));
	  
	  // Add an item
	  // Go to the spotted koi page
	  resp = Util.navToItem("EST-4");	   

	  // Add the item to the cart
	  resp = resp.getLinkWith(Util.getBundle("view", "buttonAddToCart")).click();

	  // Make sure item is in the cart
	  // Check the first two rows of the table
	  WebTable itemTable = resp.getTableStartingWith("Item ID");
	  itemTable.purgeEmptyCells();
	  String[][] table = itemTable.asText();
	  assertEquals( "EST-4", table[1][0].trim());
	  
	  // Set quantity to 3
	  WebForm form = resp.getFormWithID("cart");
	  form.setParameter("{actionForm.cart.lineItems[0].quantity}", "3");
	  resp = form.submit();
	  
	  // Make sure the Total is $55.50
	  itemTable = resp.getTableStartingWith("Item ID");
	  itemTable.purgeEmptyCells();
	  table = itemTable.asText();
	  assertEquals( "$55.50",  table[1][6].trim() );
   }   
   
}
