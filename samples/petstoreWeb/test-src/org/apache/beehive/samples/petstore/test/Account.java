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

public class Account extends TestCase {

   public void testBasicAccountForm() throws Exception 
   {
	 /*
	  * Make sure my account looks right
	  */
	  
	  // Sign in as beehive
	  WebResponse resp = Util.doSignIn("beehive", "beehive");
	  
	  // Make sure we are signed in
	  resp = resp.getLinkWith("My Account").click();
	  
	  // Make sure things look ok
	  String[][] table = resp.getTableStartingWith(Util.getBundle("account", "userIdLabel") + ":").asText(); // User ID
	  assertEquals( "beehive",  table[0][1] );
   }

   public void testAccountEditing() throws Exception 
   {
	 /*
	  * Update Account Values, then log out and log back in again to see if they stuck. Finally, reset them to default values.
	  */
	  
	  // Sign in as beehive
	  WebResponse resp = Util.doSignIn("beehive", "beehive");
	  
	  // Make sure we are signed in
	  resp = resp.getLinkWith("My Account").click();

	  // Enter the new data in the form
	  WebForm form = resp.getForms()[1];
	  form.setParameter( "{actionForm.firstName}", "Bob" ); 
	  form.setParameter( "{actionForm.lastName}", "Smith" );
	  form.setParameter( "{actionForm.email}", "bobsmith@beehive.com" );
	  form.setParameter( "wlw-select_key:{actionForm.langPref}", "Japanese" );
	  form.setParameter( "wlw-select_key:{actionForm.favCategory}", "CATS" );
	  form.setCheckbox("wlw-checkbox_key:{actionForm.myListOpt}", false);
	  form.setCheckbox("wlw-checkbox_key:{actionForm.bannerOpt}", false);
	  
	  // Submit
	  resp = form.submit();
	  
	  // Logout, then login again
	  resp = resp.getLinkWith("Sign Out").click();
	  resp = Util.doSignIn("beehive", "beehive");
	  resp = resp.getLinkWith("My Account").click();
	  
	  // Make sure the values stuck
	  form = resp.getForms()[1];
	  assertEquals(form.getParameterValue("{actionForm.firstName}"), "Bob");
	  assertEquals(form.getParameterValue("{actionForm.lastName}"), "Smith");
	  assertEquals(form.getParameterValue("{actionForm.email}"), "bobsmith@beehive.com");
	  assertEquals(form.getParameterValue("wlw-select_key:{actionForm.langPref}"), "Japanese" );
	  assertEquals(form.getParameterValue("wlw-select_key:{actionForm.favCategory}"), "CATS" );
	  assertNull(form.getParameterValue("wlw-checkbox_key:{actionForm.myListOpt}"));
	  assertNull(form.getParameterValue("wlw-checkbox_key:{actionForm.bannerOpt}"));
	  
	  // Now set them back to the default
	  form.setParameter( "{actionForm.firstName}", "Joe" ); 
	  form.setParameter( "{actionForm.lastName}", "User" );
	  form.setParameter( "{actionForm.email}", "yourname@yourdomain.com" );
	  form.setParameter( "wlw-select_key:{actionForm.langPref}", "English" );
	  form.setParameter( "wlw-select_key:{actionForm.favCategory}", "DOGS" );
	  form.setCheckbox("wlw-checkbox_key:{actionForm.myListOpt}", true);
	  form.setCheckbox("wlw-checkbox_key:{actionForm.bannerOpt}", true);
	  form.submit();
   }

   public void testNewAccount() throws Exception 
   {
	   // Turn off javaScript
	   HttpUnitOptions.setScriptingEnabled(false);
	   
	   // Start the conversation
	   WebResponse resp = Util.navToStore();

	   // Click the Sign In link
		resp = resp.getLinkWith("Sign In").click();

	   // Click the Register Now link
	   resp = resp.getLinkWith(Util.getBundle("view", "buttonRegisterNow")).click();

	   // Enter the new user data
	   WebForm form = resp.getForms()[1];
	   form.setParameter( "{actionForm.userId}", "newuser" );
	   form.setParameter( "{actionForm.password}", "x" );
	   form.setParameter( "{actionForm.repeatedPassword}", "x" );
	   form.setParameter( "{actionForm.firstName}", "New" ); 
	   form.setParameter( "{actionForm.lastName}", "User" );
	   form.setParameter( "{actionForm.email}", "newuser@beehive.com" );
	   form.setParameter( "wlw-select_key:{actionForm.langPref}", "English" );
	   form.setParameter( "wlw-select_key:{actionForm.favCategory}", "DOGS" );
	   form.setCheckbox("wlw-checkbox_key:{actionForm.myListOpt}", true);
	   form.setCheckbox("wlw-checkbox_key:{actionForm.bannerOpt}", true);
	   resp = form.submit();

	  // Logout, then login again
	  resp = resp.getLinkWith("Sign Out").click();
	  resp = Util.doSignIn("newuser", "x");
	  resp = resp.getLinkWith("My Account").click();

	  // Make sure the values stuck
	  form = resp.getForms()[1];
	  assertEquals(form.getParameterValue("{actionForm.firstName}"), "New");
	  assertEquals(form.getParameterValue("{actionForm.lastName}"), "User");
	  assertEquals(form.getParameterValue("{actionForm.email}"), "newuser@beehive.com");
	  assertEquals(form.getParameterValue("wlw-select_key:{actionForm.langPref}"), "English" );
	  assertEquals(form.getParameterValue("wlw-select_key:{actionForm.favCategory}"), "DOGS" );
	  assertNotNull(form.getParameterValue("wlw-checkbox_key:{actionForm.myListOpt}"));
	  assertNotNull(form.getParameterValue("wlw-checkbox_key:{actionForm.bannerOpt}"));
   }
}
