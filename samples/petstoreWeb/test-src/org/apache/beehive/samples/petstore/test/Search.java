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

public class Search extends TestCase {

	   public void testBasicSearch() throws Exception 
	   {
		 /*
		  * Test the basic search mechanism by searching for 'Cat'
		  */
		   
		  // Start the search
		  WebResponse resp = doSearch("Cat");
		  
		  // Make sure we got to the results page
		  assertTrue(resp.getText().contains(Util.getBundle("search", "searchResultsLabel"))); // "Search Results"
		  
		  // Make sure we got the right results
		  WebTable itemTable = resp.getTableStartingWith(Util.getBundle("view", "productIdLabel"));
		  itemTable.purgeEmptyCells();
		  String[][] table = itemTable.asText();
		  assertEquals( Util.getBundle("view", "productIdLabel"), table[0][0] ); // "Product ID"
		  assertEquals( Util.getBundle("view", "nameLabel"),  table[0][1] ); // "Name"
		  assertEquals( Util.getBundle("view", "descriptionLabel"),  table[0][2] ); // "Description"
		  assertEquals( "FL-DLH-02",  table[1][0].trim() );
		  assertEquals( "Persian",  table[1][1].trim() );
		  assertEquals( "Friendly house cat, doubles as a princess",  table[1][2].trim() );
		  assertEquals( "FL-DSH-01",  table[2][0].trim() );
		  assertEquals( "Manx",  table[2][1].trim() );
		  assertEquals( "Great for reducing mouse populations",  table[2][2].trim() );
	   }

	   public void testNothingFound() throws Exception 
	   {
		  /*
		   * Test when no search items are found by searching for bogus text
		   */

		  // Start the search
		  WebResponse resp = doSearch("Should Find Nothing");
		  
		  // Make sure we got to the results page
		  assertTrue(resp.getText().contains(Util.getBundle("search", "searchResultsLabel"))); // "Search Results"

		  // Make sure we got no results
		  assertTrue(resp.getText().contains(Util.getBundle("search", "noSearchResults"))); // "No products found"
	   }

	   public void testNothingEntered() throws Exception 
	   {
		  /*
		   * For now, searching on an empty search term will find every product in the catalog
		   * This should be 17 products
		   */

		  // Start the search
		  WebResponse resp = doSearch("");
		  
		  // Make sure we got to the results page
		  assertTrue(resp.getText().contains(Util.getBundle("search", "searchResultsLabel"))); // "Search Results"

		  // Make sure we got the right results
		  WebTable itemTable = resp.getTableStartingWith(Util.getBundle("view", "productIdLabel")); //"Product ID"
		  assertEquals(itemTable.getRowCount(), 17);
	   }
	   
	   private WebResponse doSearch(String searchTerm) throws Exception
	   {
			// Start the conversation
			WebResponse resp = Util.navToStore();
			  
			// Get the search form
			WebForm form = resp.getFormWithName("Netui_Form_0");
			  
			// Set the search term
			form.setParameter( "{actionForm.keyword}", searchTerm ); 
	
			// Submit
			return form.submit();
	   }
}
