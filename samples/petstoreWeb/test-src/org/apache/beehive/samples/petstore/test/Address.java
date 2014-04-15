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

import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class Address extends TestCase {

   public void testBasicAddressForm() throws Exception 
   {
	 /*
	  * Make sure address page looks right
	  */
	  
	  // Sign in and go to addresses
	  WebResponse resp = Util.navToAddresses();
	  
	  // Make sure things look ok
  	  assertTrue( resp.getTitle().contains(Util.getBundle("view", "addresses")) );
   }

   public void testAddAddress() throws Exception 
   {
	 /*
	  * Add new address
	  */
	  
	  // Sign in and go to addresses
	  WebResponse resp = Util.navToAddresses();

	  // Click the add link
	  resp = resp.getLinkWith(Util.getBundle("view", "buttonAddAddress")).click();
	  
	  // Enter the new data in the form
	  WebForm form = resp.getForms()[1];

	  // Make sure this is a new form
	  assertEquals(form.getParameterValue("{actionForm.name}"), "");
	  
	  // Set the new values
	  form.setParameter( "{actionForm.name}", "TestAddress" ); 
	  form.setParameter( "{actionForm.addr1}", "1234 Main Street" );
	  form.setParameter( "{actionForm.addr2}", "Apt 123" );
	  form.setParameter( "{actionForm.city}", "Seattle" );
	  form.setParameter( "{actionForm.state}", "WA" );
	  form.setParameter( "{actionForm.zip}", "98104" );
	  form.setParameter( "{actionForm.country}", "USA" );
	  form.setParameter( "{actionForm.phone}", "206-333-1234" );
	  
	  // Submit
	  resp = form.submit();
	  
	  // Make sure the address took
	  WebTable table = resp.getTableWithID("address_TestAddress");
	  assertNotNull(table);
	  TableCell cell = table.getTableCell(0, 0);
	  assertNotNull(cell);
	  assertTrue(cell.getText().contains("TestAddress"));
	  assertTrue(cell.getText().contains("1234 Main Street"));
	  assertTrue(cell.getText().contains("Apt 123"));
	  assertTrue(cell.getText().contains("Seattle"));
	  assertTrue(cell.getText().contains("WA"));
	  assertTrue(cell.getText().contains("98104"));
	  assertTrue(cell.getText().contains("USA"));
	  assertTrue(cell.getText().contains("206-333-1234"));	  
   }
   
   public void testEditAddress() throws Exception 
   {
	 /*
	  * Edit the address we just added
	  */

	   // Sign in and go to addresses
	  WebResponse resp = Util.navToAddresses();

	  // Get the newly added address
	  WebTable table = resp.getTableWithID("address_TestAddress");
	  assertNotNull(table);
	  TableCell cell = table.getTableCell(0, 0);
	  assertNotNull(cell);
	  
	  // Edit the address by clicking the edit link
	  resp = cell.getLinkWith(Util.getBundle("view", "buttonEdit")).click(); // "Edit"
	  
	  // Enter the new data in the form
	  WebForm form = resp.getForms()[1];

	  // Make sure this is the right form
	  assertEquals(form.getParameterValue("{actionForm.name}"), "TestAddress");
	  
	  // Set the new values
	  form.setParameter( "{actionForm.name}", "EditedTestAddress" ); 
	  form.setParameter( "{actionForm.addr1}", "543 Main Street" );
	  form.setParameter( "{actionForm.addr2}", "Unit 345" );
	  form.setParameter( "{actionForm.city}", "Portland" );
	  form.setParameter( "{actionForm.state}", "OR" );
	  form.setParameter( "{actionForm.zip}", "98799" );
	  form.setParameter( "{actionForm.country}", "USA2" );
	  form.setParameter( "{actionForm.phone}", "208-222-3456" );
	  
	  // Submit
	  resp = form.submit();
	  
	  // Make sure the address took
	  table = resp.getTableWithID("address_EditedTestAddress");
	  assertNotNull(table);
	  cell = table.getTableCell(0, 0);
	  assertNotNull(cell);
	  assertTrue(cell.getText().contains("EditedTestAddress"));
	  assertTrue(cell.getText().contains("543 Main Street"));
	  assertTrue(cell.getText().contains("Unit 345"));
	  assertTrue(cell.getText().contains("Portland"));
	  assertTrue(cell.getText().contains("OR"));
	  assertTrue(cell.getText().contains("98799"));
	  assertTrue(cell.getText().contains("USA2"));
	  assertTrue(cell.getText().contains("208-222-3456"));	  
   }
   
   public void testRemoveAddress() throws Exception 
   {
	  // Sign in and go to addresses
	  WebResponse resp = Util.navToAddresses();

	  // Get the newly added address
	  WebTable table = resp.getTableWithID("address_EditedTestAddress");
	  assertNotNull(table);
	  TableCell cell = table.getTableCell(0, 0);
	  assertNotNull(cell);
	  
	  // Click the remove link
	  resp = cell.getLinkWith(Util.getBundle("view", "buttonRemove")).click();
	  
	  // Make sure it's gone
	  table = resp.getTableWithID("address_EditedTestAddress");
	  assertNull(table);
   }
}
