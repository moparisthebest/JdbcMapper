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

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class Checkout extends TestCase {

   public void testCheckOut() throws Exception 
   {
	  /*
	   * Add an item to the cart, then proceed through checkout
	   */

 	  // Turn off javaScript
	  HttpUnitOptions.setScriptingEnabled(false);
	  
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
	  
  	  // proceed to checkout
	  resp = resp.getLinkWith(Util.getBundle("view", "buttonProceedToCheckout")).click(); // "Proceed to Checkout"
	  
	  // Make sure it says Checkout Summary
	  assertTrue(resp.getText().contains(Util.getBundle("view", "checkoutSummaryLabel"))); // "Checkout Summary"
	  
	  // Now click continue
	  resp = resp.getLinkWith(Util.getBundle("view", "buttonContinue")).click(); // "Continue >>"
	  
	  // Sign in
	  WebForm form = resp.getForms()[1];
	  form.setParameter( "j_username", "beehive" ); 
	  form.setParameter( "j_password", "beehive" );
	  resp = form.submit();	  
	  
	  // Enter the Checkout Info
	  form = resp.getForms()[1];
	  form.setParameter( "wlw-select_key:{actionForm.order.cardType}", "American Express" );
	  form.setParameter( "{actionForm.order.creditCard}", "1234" ); 
	  form.setParameter( "{actionForm.order.exprDate}", "09-2000" );
	  form.setParameter( "wlw-radio_button_group_key:{actionForm.order.billingAddress}", "1" );
	  form.setParameter( "wlw-radio_button_group_key:{actionForm.order.shippingAddress}", "1" );
	  resp = form.submit();
	  
	  // Make sure it took
	  assertTrue(resp.getText().contains(Util.getBundle("view", "confirmMessage"))); // "Please confirm the information below and then press continue..."
	  itemTable = resp.getTableWithID("orderTable");
	  itemTable.purgeEmptyCells();
	  table = itemTable.asText();
	  assertEquals( "American Express", table[1][1].trim());
	  assertEquals( "1234", table[2][1].trim());
	  assertEquals( "09-2000", table[3][1].trim());
	  
	  // Click Continue
	  resp = resp.getLinkWith(Util.getBundle("view", "buttonContinue")).click(); // "Continue >>"

	  // Make sure it took
	  assertTrue(resp.getText().contains(Util.getBundle("view", "orderSubmittedMessage"))); // "Thank you, your order has been submitted."
   }

   public void testOrders() throws Exception 
   {
	  // Sign in as beehive
	  WebResponse resp = Util.doSignIn("beehive", "beehive");
	  
	  // Go to my account
	  resp = resp.getLinkWith("My Account").click();
	  
	  // go to orders 
	  resp = resp.getLinkWith(Util.getBundle("account", "listOrdersLabel")).click(); // "List Orders"
	  
	  // Make sure the new order appears with the proper total price 
	  WebTable itemTable = resp.getTableStartingWith(Util.getBundle("view", "orderId")); // "Order ID"
	  String[][] table = itemTable.asText();
	  assertEquals( "1", table[1][0].trim());
	  assertEquals( "$18.50",  table[1][2].trim() );	  
   }

   public void testNewItemQuantity() throws Exception 
   {
	  /*
	   * Make sure the new quantity is 9999
	   */

      // Start the conversation
	  WebResponse resp = Util.navToItem("EST-4");
	  
	  // Check the first two rows of the table
	  WebTable itemTable = resp.getTableStartingWith("EST-4");
	  itemTable.purgeEmptyCells();
	  String[][] table = itemTable.asText();
	  assertEquals( "EST-4",  table[0][0].trim() );
	  assertTrue( table[1][0].contains("9999 in Stock") );
   }
}
