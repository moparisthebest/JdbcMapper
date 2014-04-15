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

package org.apache.beehive.controls.test.junit;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.controls.extension.ExtensibleControl;
import org.apache.beehive.controls.test.controls.extension.SubControl;
import org.apache.beehive.controls.test.controls.extension.SubControlBean;

/**
 * A TeseCase that tests control inheritance by instantiating a _subcontrol and invoking the methods
 * and getting/setting the properties.
 * All tests on controls instantiated declaratively are deactivated until this feature is supported.
 */
public class ControlExtensionTest extends ControlTestCase {

    /**
     * A control extending ExtensibleControl in the same package
     */
    @Control
    private SubControlBean _subcontrol;

    /**
     * Tests getting/setting a property declared on super control interface
     * that is annotated with @Inherited
     */
    public void testAnnotatedInheritedProperty() throws Exception {
        String theBirthplace = _subcontrol.getBirthplace();
        assertNotNull(theBirthplace);
        assertEquals("ExtensibleControl", theBirthplace);

        _subcontrol.setBirthplace("JUNIT");
        assertEquals("JUNIT", _subcontrol.getAnnotatedInheritedPropertyByContext());
    }


    /**
     * Tests invoking a method inherited by the _subcontrol
     */
    public void testInheritedMethod() throws Exception {
        assertEquals("Hello from super control", _subcontrol.hello());
    }

    /**
     * Tests invoking a method inherited by the _subcontrol.
     * The _subcontrol is instantiated programmically
     */
    public void testInheritedMethod2() throws Exception {

        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(Thread.currentThread().getContextClassLoader(),
                                                                           "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        assertEquals("Hello from super control", sub.hello());
    }

    /**
     * Tests invoking a method declared on the _subcontrol
     */
    public void testExtendedMethod() throws Exception {
        assertEquals("Hello from subcontrol", _subcontrol.hello2());
    }

    /**
     * Tests invoking a method declared on the _subcontrol
     * The _subcontrol is instantiated programmically
     */
    public void testExtendedMethod2() throws Exception {
        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(Thread.currentThread().getContextClassLoader(),
                                                                           "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        assertEquals("Hello from subcontrol", sub.hello2());
    }

    /**
     * Tests getting propertySet inherited from ExtensibleControl from a _subcontrol instance.
     * The property is retrieved via control context.
     * The _subcontrol is instantiated declaratively
     */
    public void testGettingInheritedPropertyByContext() throws Exception {
        assertEquals("In_ExtensibleControl_Interface", _subcontrol.accessInheritedProperty());
    }

    /**
     * Tests getting the propertySet inherited from ExtensibleControl from a _subcontrol instance
     * The property is retrieved via control context.
     * The _subcontrol is instantiated programmatically
     */
    public void testGettingInheritedPropertyByContext2() throws Exception {
        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(Thread.currentThread().getContextClassLoader(),
                                                                           "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        assertEquals("In_ExtensibleControl_Interface", sub.accessInheritedProperty());
    }

    /**
     * Tests getting an inherited property from a _subcontrol instance.
     * The property is retrieved via getter.
     * The _subcontrol is instantiated declaratively
     */
    public void testGettingInheritedPropertyByGetter() throws Exception {
        assertEquals(ExtensibleControl.CURRENT_POSITION, _subcontrol.getPosition());
    }

    /**
     * Tests getting an inherited property from a _subcontrol instance
     * The property is retrieved via getter.
     * The _subcontrol is instantiated programmatically
     */
    public void testGettingInheritedPropertyByGetter2() throws Exception {
        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        assertEquals(ExtensibleControl.CURRENT_POSITION, sub.getPosition());
    }

    /**
     * Tests setting an inherited property from a _subcontrol instance.
     * The property is retrieved via getter.
     * The _subcontrol is instantiated declaratively
     */
    public void testSettingInheritedPropertyBySetter() throws Exception {

        _subcontrol.setPosition("A_NEW_POSITION");
        assertEquals("A_NEW_POSITION", _subcontrol.accessInheritedProperty());
    }

    /**
     * Tests setting an inherited property from a _subcontrol instance
     * The property is retrieved via getter.
     * The _subcontrol is instantiated programmatically
     */
    public void testSettingInheritedPropertyBySetter2() throws Exception {

        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.extension.SubControlBean");

        sub.setPosition("A_NEW_POSITION");
        assertEquals("A_NEW_POSITION", sub.accessInheritedProperty());
    }

    /**
     * Tests getting the reconfigured and inherited property.(A property declared on super control,
     * but reconfigured on sub control)
     * The reset property is retrieve by context.
     * The _subcontrol is instantiated declaratively
     */
    public void testGetReconfiguredPropertyByContext() throws Exception {
        assertEquals("On_SubControl_Interface_Layer", _subcontrol.getLayerByContext());
    }

    /**
     * Tests getting the reconfigured and inherited property.(A property declared on super control,
     * but reconfigured on sub control)
     * The reset property is retrieve by context.
     * The _subcontrol is instantiated programmatically
     */
    public void testGetReconfiguredPropertyByContext2() throws Exception {
        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(Thread.currentThread().getContextClassLoader(),
                                                                           "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        assertEquals("On_SubControl_Interface_Layer", sub.getLayerByContext());
    }

    /**
     * Tests getting a reconfigured inherited property.(The property is declared on super control
     * but reconfigured on sub control interface).
     * The _subcontrol is instantiated declaratively
     */
    public void testGetReconfiguredPropertyByGetter() throws Exception {
        assertEquals("On_SubControl_Interface_Layer", _subcontrol.getLayer());
    }

    /**
     * Tests getting a reconfigured inherited property.(The property is declared on super control
     * but reconfigured on sub control interface).
     * The _subcontrol is instantiated programmatically
     */
    public void testGetReconfiguredPropertyByGetter2() throws Exception {
        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        assertEquals("On_SubControl_Interface_Layer", sub.getLayer());
    }

    /**
     * Tests setting the reconfigured and inherited property using bean setter
     * The _subcontrol is instantiated declaratively
     */
    public void testSetReconfiguredPropertyBySetter() throws Exception {
        _subcontrol.setLayer("NEW_VALUE_FOR_LAYER");
        assertEquals("NEW_VALUE_FOR_LAYER", _subcontrol.getLayerByContext());
    }

    /**
     * Tests setting the reconfigured and inherited property using bean setter
     * The _subcontrol is instantiated programmatically
     */
    public void testSetReconfiguredPropertyBySetter2() throws Exception {
        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        sub.setLayer("NEW_VALUE_FOR_LAYER");
        assertEquals("NEW_VALUE_FOR_LAYER", sub.getLayerByContext());
    }

    /* More tests to be added on getting property value reconfigured on _subcontrol
    * by declaration.
    * The above tests testGetReconfiguredPropertyByContext(2) gets the value using an
    * inherited method.
    * Once testExtendedMethod passes, we need to add one or two tests to
    * get the reconfigured value using an extended method on _subcontrol
    *
    * Like: testGetReconfigurePropertyBySubImpl
    * While the above two methods are really: testGetReconfiguredPropertyBySuperImpl
    */

    /**
     * Tests getting the property declared on SubControl interface.
     * The value is retrieved by control context.
     * The _subcontrol is instantiated declaratively.
     */
    public void testGetExtendedPropertyByContext() throws Exception {
        assertEquals("New Property Declared by Sub Control", _subcontrol.getExtendedPropertyByContext());
    }

    /**
     * Tests getting the property declared on SubControl interface.
     * The property is retrieved by control context.
     * The _subcontrol is instantiated programmatically.
     */
    public void testGetExtendedPropertyByContext2() throws Exception {

        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        assertEquals("New Property Declared by Sub Control", sub.getExtendedPropertyByContext());
    }

    /**
     * Tests getting the property declared SubControl interface.
     * The value is retrieved by getter on _subcontrol bean
     * The _subcontrol is instantiated declaratively
     */
    public void testGetExtendedPropertyByGetter() throws Exception {
        assertEquals(SubControl.A_MESSAGE, _subcontrol.getMessage());
    }

    /**
     * Tests getting the property declared on SubControl interface
     * The value is retrieved by getter on the _subcontrol bean.
     * The _subcontrol is instantiated programmatically
     */
    public void testGetExtendedPropertyByGetter2() throws Exception {
        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        assertEquals(SubControl.A_MESSAGE, sub.getMessage());
    }

    /**
     * Tests setting the property declared SubControl interface.
     * The value is changed by setter on _subcontrol bean
     * The _subcontrol is instantiated declaratively
     */
    public void testSetExtendedPropertyBySetter() throws Exception {
        _subcontrol.setMessage("NEW_VALUE_FOR_EXTENDED_PROPERTY");
        assertEquals("NEW_VALUE_FOR_EXTENDED_PROPERTY", _subcontrol.getExtendedPropertyByContext());
    }

    /**
     * Tests setting the property declared on SubControl interface
     * The value is changed by setter on the _subcontrol bean.
     * The _subcontrol is instantiated programmatically
     */
    public void testSetExtendedPropertyBySetter2() throws Exception {
        SubControlBean sub = (SubControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.extension.SubControlBean");
        sub.setMessage("NEW_VALUE_FOR_EXTENDED_PROPERTY");
        assertEquals("NEW_VALUE_FOR_EXTENDED_PROPERTY", sub.getExtendedPropertyByContext());
    }
}
