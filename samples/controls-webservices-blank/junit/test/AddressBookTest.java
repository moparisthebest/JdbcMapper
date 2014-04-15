/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package test;

import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextServiceProvider;
import java.beans.beancontext.BeanContextServices;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Iterator;

import mypackage.EnhancedAddressBook;
import junit.framework.TestCase;
import org.apache.beehive.enhancedaddressbook.Address;
import org.apache.beehive.enhancedaddressbook.Phone;
import org.apache.beehive.enhancedaddressbook.StateType;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.context.ControlBeanContext;

public class AddressBookTest extends TestCase {
	@Control
	public EnhancedAddressBook client;

	public void testGetAddressFromName() throws Exception {
		Address address = Address.Factory.newInstance();
		address.setStreetName("Open Source Way");
		address.setCity("Seattle");
		address.setZip(98119);
		Phone phone = Phone.Factory.newInstance();
		phone.setAreaCode(206);
		phone.setNumber("111-2222");
		address.setPhoneNumber(phone);
		StateType state = StateType.Factory.newInstance();
		state.setState("WA");
		address.setState(state);
		client.addEntry("apache", address);

		// retrieve the address
		Address response = client.getAddressFromName("apache");
		assertTrue(response.getStreetName().equals("Open Source Way"));
		// more assertions here..

	}

	
	
	//   Initialization of the Controls.  
	JunitTestBeanContext beanContext = new JunitTestBeanContext();
	public void setUp() throws Exception {

		try {
			beanContext.beginContext();
			initializeControls(this);
			// set the bean context's base object for loading resources.
			beanContext.setBaseObjectToLoadFrom(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tearDown() {
		beanContext.endContext();
	}
	
	private void initializeControls(Object obj) throws Exception {
		Class cls = obj.getClass();
		// search for fields with @Control annotations
		for (Field field : cls.getFields()) {
			if (null != field.getAnnotation(Control.class)) {

				// attempt to load using client initializer.
				ControlContainerContext ccc = ControlThreadContext.getContext();
				if (null == ccc) {
					throw new Exception("no control container context found");
				}
				Class clientInitializer = cls.getClassLoader().loadClass(
						cls.getName() + "ClientInitializer");
				Method init = clientInitializer.getMethod("initialize",
						ControlBeanContext.class, cls);
				init.invoke(null, ccc, obj);
				break;
			}
		}
	}
}

class JunitTestBeanContext extends
		org.apache.beehive.controls.runtime.bean.ControlContainerContext {

	private static final long serialVersionUID = 1L;
	Object obj;  // used to find the resources 

	public JunitTestBeanContext() {
		super();
	}
	public void setBaseObjectToLoadFrom(Object obj) {
		this.obj=obj;
	}
	public InputStream getResourceAsStream(String name, BeanContextChild bcc)
			throws IllegalArgumentException {

			InputStream is = obj.getClass().getResourceAsStream(name);
			return is;

	}
	public void initialize() {
		super.initialize();
		addService(JUnitServiceProvider.class, new JUnitServiceProvider());
	}
}

class JUnitServiceProvider implements BeanContextServiceProvider {

	public InputStream getResourceAsStream(String name)
			throws IllegalArgumentException {
		return null;
	}


	public Object getService(BeanContextServices arg0, Object arg1, Class arg2,
			Object arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	public void releaseService(BeanContextServices arg0, Object arg1,
			Object arg2) {

	}

	public Iterator getCurrentServiceSelectors(BeanContextServices arg0,
			Class arg1) {
		return null;
	}

}
