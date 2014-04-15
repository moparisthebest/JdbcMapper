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
package org.apache.beehive.controls.system.ejb.sample.client;

import org.apache.beehive.controls.system.ejb.sample.control.HelloEJBControlBean;
import junit.framework.Test;
import junit.framework.TestSuite;

public class EJBControlTestCase extends SingleControlTestCase
{

	public EJBControlTestCase(String name) throws Exception {
	        super(name, HelloEJBControlBean.class.getName());
	}

    public static void main (String[] args) {
		junit.textui.TestRunner.run (suite());
	}

	public static Test suite() {
		return new TestSuite(EJBControlTestCase.class);
	}
	
	public void testHello()
	{
	    try 
	    {
	        HelloEJBControlBean helloEJBBean = (HelloEJBControlBean)this.getControl();
	        String str = helloEJBBean.hello();
	        System.out.println(str);
	    }
	    catch (Exception e)
	    {
	        assertTrue("Exception occurred: " + e, false);
	    }
	    
	}

}
