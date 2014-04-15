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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test Suite for HTTPUnit.
 * Tests all main functionality and is localization independent.
 */
public class AllTests extends TestSuite{
    
	public static Test suite() 
	{
        TestSuite suite = new TestSuite();
		suite.addTestSuite(Database.class);
		suite.addTestSuite(Navigation.class);
		suite.addTestSuite(Search.class);
		suite.addTestSuite(SignIn.class);
		suite.addTestSuite(Account.class);
		suite.addTestSuite(Address.class); 
		suite.addTestSuite(Cart.class);
		suite.addTestSuite(Checkout.class); 
        return suite;
    }

     public static void main( String[] args ) {
        junit.textui.TestRunner.run( suite() );
    }
}
