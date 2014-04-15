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

public class SignIn extends TestCase {

   public void testBasicSignIn() throws Exception 
   {
	 /*
	  * Test the basic sign in mechanism by signing in as beehive
	  */
	  
	  // Sign in as beehive
	  WebResponse resp = Util.doSignIn("beehive", "beehive");
	  
	  // Make sure we are signed in
	  assertNotNull(resp.getLinkWith("Sign Out"));
   }

   public void testSignOut() throws Exception 
   {
	  /*
	   * Sign in, then sign out
	   */

	  // Sign in as beehive
	  WebResponse resp = Util.doSignIn("beehive", "beehive");
	   
	  // Sign Out
	  resp = resp.getLinkWith("Sign Out").click();
	  
	  // Make sure we are signed out
	  assertNotNull(resp.getLinkWith("Sign In"));		  
   }

   public void testInvalidSignIn() throws Exception 
   {
	 /*
	  * Test the error page by signing in as non_user
	  */
	  
	  // Sign in as beehive
	  WebResponse resp = Util.doSignIn("non_user", "non_user");
	  
	  // Make sure we have the error page
	  assertTrue(resp.getText().contains(Util.getBundle("view", "loginErrorMessage"))); // "Sorry, your user name and password were not recognized."
   }

}
