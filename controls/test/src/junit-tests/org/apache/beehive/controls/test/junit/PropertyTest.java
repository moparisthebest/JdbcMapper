/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   $Header:$
*/
package org.apache.beehive.controls.test.junit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.apache.beehive.controls.api.bean.Control;

import org.apache.beehive.controls.test.controls.propertiessimple.Property;
import org.apache.beehive.controls.test.controls.propertiessimple.PropertyBean;
import org.apache.beehive.controls.test.controls.propertiesoptional.OptionalPropertySetBean;
import org.apache.beehive.controls.test.controls.interfacegetter.InterfaceGetterSetterBean;
import org.apache.beehive.controls.test.controls.interfacegetter.CtrlExtensionBean;

/**
 *
 */
public class PropertyTest
    extends ControlTestCase {

    @Control
    @Property.Text(value = "Test value override")
    private PropertyBean _propertyControl;

    @Control
    private OptionalPropertySetBean _optionalControl;

    @Control
    private InterfaceGetterSetterBean _getterSetterControl;

    @Control
    private CtrlExtensionBean _ctrlExtensionControl;

    private boolean _eventHandlerCalled = false;

    public void testDeclarativePropertyInstantiation() {
        assertEquals("Test value override", _propertyControl.getValue());
    }

    public void testPropertyChange() {
        Property property = (Property)instantiateControl(PropertyBean.class.getName());
        PropertyBean propertyBean = (PropertyBean)property;


        propertyBean.addPropertyChangeListener("value",
                                               new PropertyChangeListener() {
                                                   public void propertyChange(PropertyChangeEvent event) {
                                                       _eventHandlerCalled = true;
                                                   }
                                               }
                                               );

        propertyBean.setValue("does this work?");
        assertEquals("does this work?", propertyBean.getValue());
        assertTrue(_eventHandlerCalled); //, "Appears that the event handler wasn't called!");
    }

    /**
     * Verify that members of a PropertySet marked optional do not have property descriptors for
     * their getter methods.
     *
     * @throws Exception
     */
    public void testOptionalPropertySet() throws Exception {
        BeanInfo bi = Introspector.getBeanInfo(_optionalControl.getClass());
        PropertyDescriptor[] pds = bi.getPropertyDescriptors();

        // There should not be a PropertyDescriptor for a property set which has optional=true and hasSetters=false
        // (no getter or setter for property), so there should only be 2 PropertyDescriptors for this control
        assertEquals(2, pds.length);

        assertEquals("value", pds[1].getName());
        assertNull(pds[1].getReadMethod());
        assertNotNull(pds[1].getWriteMethod());
    }

    /**
     * Verify that the generated BeanInfo class contains property descriptors for getters/setters declared in
     * a control interface.
     *
     * @throws Exception
     */
    public void testGetterSetterPropertyGeneration() throws Exception {
        BeanInfo bi = Introspector.getBeanInfo(_getterSetterControl.getClass());
        PropertyDescriptor[] pds = bi.getPropertyDescriptors();

        assertEquals(3, pds.length);

        assertEquals("count", pds[1].getName());
        assertNotNull(pds[1].getReadMethod());
        assertNull(pds[1].getWriteMethod());

        assertEquals("text", pds[2].getName());
        assertNotNull(pds[2].getReadMethod());
        assertNotNull(pds[2].getWriteMethod());
    }

    /**
     * Verify that the generated BeanInfo class contains property descriptors for getters/setters declared in
     * a control interface for an extensible control.
     *
     * @throws Exception
     */
    public void testGetterSetterExtPropertyGeneration() throws Exception {
        BeanInfo bi = Introspector.getBeanInfo(_ctrlExtensionControl.getClass());
        PropertyDescriptor[] pds = bi.getPropertyDescriptors();

        assertEquals(4, pds.length);

        assertEquals("count", pds[1].getName());
        assertNotNull(pds[1].getReadMethod());
        assertNotNull(pds[1].getWriteMethod());

        assertEquals("text", pds[2].getName());
        assertNotNull(pds[2].getReadMethod());
        assertNotNull(pds[2].getWriteMethod());

        assertEquals("textExt", pds[3].getName());
        assertNotNull(pds[3].getReadMethod());
        assertNull(pds[3].getWriteMethod());
    }
}
