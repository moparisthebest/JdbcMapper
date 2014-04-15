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
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class Navigation extends TestCase {

   public void testBasicNavigation() throws Exception 
   {
	  /* 
	   * Used to test that a user can click through to all the pages
	   * Later tests go to individual links
	   */

      // Start the conversation
	  WebResponse resp = Util.navToFrontPage();

	  // Go to the store
	  resp = resp.getLinkWithID("enter").click();

	  // Click on the Fish Link
	  resp = resp.getLinkWith("Fish").click();

	  // Click on the Koi Link
	  resp = resp.getLinkWith("FI-FW-01").click();
	  
	  // Click on the First Spotted Koi Link
	  resp = resp.getLinkWith("EST-4").click();
	  
	  // Make sure it has the right text
	  assertTrue(resp.getText().contains("Fresh Water fish from Japan"));
   }
	
   public void testFrontPage() throws Exception 
   {
      // Start the conversation
	  WebResponse resp = Util.navToFrontPage();
	  
	  // Make sure the front page looks correct
	  assertEquals(resp.getTitle().trim(), Util.getBundle("view", "indexTitle") + " - " + Util.getBundle("view", "welcomeLabel")); // "Beehive Petstore Demo"
	  assertNotNull(resp.getLinkWith(Util.getBundle("view", "adminInit"))); // "Initialize the DB (recreates all tables and data)"
	  assertNotNull(resp.getLinkWith(Util.getBundle("view", "adminStop"))); // "Stop Derby DB (stops the DB to allow redeploy)"
   }

   public void testStore() throws Exception 
   {
      // Start the conversation
	  WebResponse resp = Util.navToStore();
	  
	  // Make sure main catalog page looks correct
	  assertTrue( resp.getTitle().contains(Util.getBundle("view", "homeTitle")) );
	  assertNotNull(resp.getLinkWith("Fish"));
	  assertNotNull(resp.getLinkWith("Dogs"));
	  assertNotNull(resp.getLinkWith("Reptiles"));
	  assertNotNull(resp.getLinkWith("Cats"));
	  assertNotNull(resp.getLinkWith("Birds"));
   }

   public void testFishCatagory() throws Exception 
   {
      // Start the conversation
	  WebResponse resp = Util.navToCategory("FISH");

	  // Examine the page
	  assertTrue( resp.getTitle().contains("- Fish") );
	  assertNotNull(resp.getLinkWith(Util.getBundle("view", "mainMenuLabel"))); 
	  
	  // Check the first two rows of the table
	  String[][] table = resp.getTableStartingWith(Util.getBundle("view", "productIdLabel")).asText(); // "Product ID"
	  assertEquals( Util.getBundle("view", "productIdLabel"), table[0][0] ); //"Product ID"
	  assertEquals( Util.getBundle("view", "nameLabel"), table[0][1] ); // "Name"
	  assertEquals( Util.getBundle("view", "descriptionLabel"), table[0][2] ); // "Description"
	  assertEquals( "FI-FW-01",  table[1][0].trim() );
	  assertEquals( "Koi",  table[1][1] );
	  assertEquals( "Fresh Water fish from Japan",  table[1][2] );
   }

   public void testKoiProducts() throws Exception 
   {
      // Start the conversation
	  WebResponse resp = Util.navToProduct("FI-FW-01");

	  // Examine the page
	  assertTrue( resp.getTitle().contains("- Koi") );
	  assertNotNull(resp.getLinkWith("FISH")); 
	  
	  // Check the first two rows of the table
	  String[][] table = resp.getTableStartingWith(Util.getBundle("view", "itemIdLabel")).asText(); // "Item ID"
	  assertEquals( Util.getBundle("view", "itemIdLabel"), table[0][0] ); // "Item ID"
	  assertEquals( Util.getBundle("view", "productIdLabel"), table[0][1] ); // "Product ID"
	  assertEquals( Util.getBundle("view", "descriptionLabel"), table[0][2] ); // "Description"
	  assertEquals( Util.getBundle("view", "listPriceLabel"), table[0][3] ); // "List Price"
	  assertEquals( "EST-4",  table[1][0].trim() );
	  assertEquals( "FI-FW-01",  table[1][1] );
	  assertTrue( table[1][2].contains( "Spotted" ) );
	  assertEquals( "$18.50",  table[1][3].trim() );
	  
	  // Check the text of the page
	  assertTrue(resp.getText().contains("(Fresh Water fish from Japan)"));
   }

   public void testKoiItem() throws Exception 
   {
      // Start the conversation
	  WebResponse resp = Util.navToItem("EST-4");
	  
	  // Examine the page
	  assertTrue( resp.getTitle().contains("Spotted Koi") );
	  assertTrue( resp.getText().contains("Fresh Water fish from Japan") );
	  assertNotNull(resp.getLinkWith("Koi")); 

	  // Check the first two rows of the table
	  WebTable itemTable = resp.getTableStartingWith("EST-4");
	  itemTable.purgeEmptyCells();
	  String[][] table = itemTable.asText();
	  assertEquals( "EST-4",  table[0][0].trim() );
	  assertTrue( table[1][0].contains("in Stock") );
	  assertTrue( table[1][0].contains("$18.50") );
   }

}