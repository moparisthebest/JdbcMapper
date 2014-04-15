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

import java.util.ResourceBundle;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

public class Util {

	private static String _baseURL = "http://localhost:8080/petstoreWeb/";
	
	public static WebResponse navToFrontPage() throws Exception {
		  return navTo("");	}

	public static WebResponse navToStore() throws Exception {
		  return navTo("shop/begin.do"); }

	public static WebResponse navToCart() throws Exception {
	  return navTo("rootSharedFlow.globalViewCart.do"); }

	public static WebResponse navToCategory(String sCategory) throws Exception {
		  return navTo("shop/viewCategory.do?catId=" + sCategory); }

	public static WebResponse navToProduct(String sProduct) throws Exception {
		  return navTo("shop/viewProduct.do?productId=" + sProduct); }

	public static WebResponse navToItem(String sItem) throws Exception {
		  return navTo("shop/viewItem.do?itemId=" + sItem); }
	
	public static WebResponse doSignIn(String userName, String password) throws Exception
	{
		// Turn off javaScript
		HttpUnitOptions.setScriptingEnabled(false);
		
		// Start the conversation
		WebResponse resp = Util.navToStore();
		
		// Click the Sign In link
		resp = resp.getLinkWith("Sign In").click();
		
		// Enter the username/password in the form
		WebForm form = resp.getForms()[1];
		form.setParameter( "j_username", userName ); 
		form.setParameter( "j_password", password );
		
		// Submit
		return form.submit();
	}

	public static WebResponse navToAddresses() throws Exception 
	{
		// Sign in as beehive
		WebResponse resp = Util.doSignIn("beehive", "beehive");
		
		// Make sure we are signed in
		resp = resp.getLinkWith("My Account").click();
		
		// Go to the address page
		return resp.getLinkWith(Util.getBundle("account", "listAddressesLabel")).click(); // "Add/Edit Addresses"
	}
	
	private static WebResponse navTo(String sURL) throws Exception 
	{
	      // Start the conversation
		  WebConversation wc = new WebConversation();
	      return wc.getResponse(_baseURL + sURL);
	}
	
	public static String getBundle(String bundleType, String bundle)
	{
		// Get localized label
		ResourceBundle resBundle = ResourceBundle.getBundle("org.apache.beehive.samples.petstore.resources." + bundleType);
		return resBundle.getString(bundle);
	}

}
