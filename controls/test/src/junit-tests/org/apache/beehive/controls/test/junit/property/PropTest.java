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

package org.apache.beehive.controls.test.junit.property;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.properties.BeanPropertyMap;
import org.apache.beehive.controls.api.properties.PropertyKey;
import org.apache.beehive.controls.api.properties.PropertyMap;
import org.apache.beehive.controls.test.controls.property.ExtPropertySet;
import org.apache.beehive.controls.test.controls.property.NestPropsBean;
import org.apache.beehive.controls.test.controls.property.Props;
import org.apache.beehive.controls.test.controls.property.PropsBean;
import org.apache.beehive.controls.test.controls.property.PropsExtension;
import org.apache.beehive.controls.test.controls.property.PropsExtensionBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.util.Arrays;

public class PropTest extends ControlTestCase {

    @Control
    private PropsBean _propsBean;

    @Control
    private PropsExtensionBean _propsExtBean;

    @Control
    private NestPropsBean _nestPropsBean;

    public void testIntProperty() throws Exception {
        assertEquals(Props.INT_DEFAULT, _propsBean.getSimpleInt());
        _propsBean.setSimpleInt(237);
        assertEquals(237, _propsBean.getSimpleInt());
    }

    public void testStringProperty() throws Exception {
        assertEquals(Props.STRING_DEFAULT, _propsBean.getSimpleString());
        _propsBean.setSimpleString("howdy");
        assertEquals("howdy", _propsBean.getSimpleString());
    }

    public void testClassProperty() throws Exception {
        assertEquals(Props.CLASS_DEFAULT, _propsBean.getSimpleClass());
        Class testSimpleClass = java.util.Vector.class;
        _propsBean.setSimpleClass(testSimpleClass);
        assertEquals(testSimpleClass, _propsBean.getSimpleClass());
    }

    public void testEnumProperty() throws Exception {
        assertEquals(Props.ENUM_DEFAULT, _propsBean.getSimpleEnum());
        _propsBean.setSimpleEnum(Props.SimpleEnum.ChoiceB);
        assertEquals(Props.SimpleEnum.ChoiceB, _propsBean.getSimpleEnum());
    }

    public void testIntArrayProperty() throws Exception {
        int [] arrayInt = _propsBean.getArrayInt();
        assertNotNull(arrayInt);
        assertEquals(Props.ARRAY_INT_DEFAULT.length, arrayInt.length);
        assertTrue(Arrays.equals(Props.ARRAY_INT_DEFAULT, arrayInt));

        int [] testArrayInt = {1, 2, 3, 4, 5};
        _propsBean.setArrayInt(testArrayInt);
        arrayInt = _propsBean.getArrayInt();
        assertNotNull(arrayInt);
        assertEquals(testArrayInt.length, arrayInt.length);
        assertTrue(Arrays.equals(testArrayInt, arrayInt));
    }

    public void testStringArrayProperty() throws Exception {
        String [] arrayString = _propsBean.getArrayString();
        assertNotNull(arrayString);
        assertEquals(Props.ARRAY_STRING_DEFAULT.length, arrayString.length);
        assertTrue(Arrays.equals(Props.ARRAY_STRING_DEFAULT, arrayString));

        String [] testArrayString = {"fee", "fi", "fo", "fum"};
        _propsBean.setArrayString(testArrayString);
        arrayString = _propsBean.getArrayString();
        assertNotNull(arrayString);
        assertEquals(testArrayString.length, arrayString.length);
        assertTrue(Arrays.equals(testArrayString, arrayString));
    }

    public void testClassArrayProperty() throws Exception {
        Class [] arrayClass = _propsBean.getArrayClass();
        assertNotNull(arrayClass);
        assertEquals(Props.ARRAY_CLASS_DEFAULT.length, arrayClass.length);
        assertTrue(Arrays.equals(Props.ARRAY_CLASS_DEFAULT, arrayClass));

        Class [] testArrayClass = {Integer.class, Long.class, Short.class, Float.class,
                Double.class, Character.class, Boolean.class};
        _propsBean.setArrayClass(testArrayClass);
        arrayClass = _propsBean.getArrayClass();
        assertNotNull(arrayClass);
        assertEquals(testArrayClass.length, arrayClass.length);
        assertTrue(Arrays.equals(testArrayClass, arrayClass));
    }

    public void testEnumArrayProperty() throws Exception {
        Props.SimpleEnum [] arrayEnum = _propsBean.getArrayEnum();
        assertNotNull(arrayEnum);
        assertEquals(Props.ARRAY_ENUM_DEFAULT.length, arrayEnum.length);
        assertTrue(Arrays.equals(Props.ARRAY_ENUM_DEFAULT, arrayEnum));

        Props.SimpleEnum [] testArrayEnum =
                {Props.SimpleEnum.ChoiceB, Props.SimpleEnum.ChoiceC, Props.SimpleEnum.ChoiceA};
        _propsBean.setArrayEnum(testArrayEnum);
        arrayEnum = _propsBean.getArrayEnum();
        assertNotNull(arrayEnum);
        assertEquals(testArrayEnum.length, arrayEnum.length);
        assertTrue(Arrays.equals(testArrayEnum, arrayEnum));
    }

    public void testAnnotationProperties() throws Exception {

        Props.SimpleProps simpleAnnot = _propsBean.getSimpleAnnot();
        assertNotNull(simpleAnnot);
        assertEquals(Props.ANNOT_INT_DEFAULT, simpleAnnot.simpleInt());
        assertEquals(Props.ANNOT_STRING_DEFAULT, simpleAnnot.simpleString());
        assertEquals(Props.ANNOT_CLASS_DEFAULT, simpleAnnot.simpleClass());
        assertEquals(Props.ANNOT_ENUM_DEFAULT, simpleAnnot.simpleEnum());


        PropertyMap simpleAnnotMap = new BeanPropertyMap(Props.SimpleProps.class);
        Integer testAnnotInt = 5150;
        simpleAnnotMap.setProperty(new PropertyKey(Props.SimpleProps.class, "simpleInt"), testAnnotInt);
        String testAnnotString = "abracadabra";
        simpleAnnotMap.setProperty(new PropertyKey(Props.SimpleProps.class, "simpleString"), testAnnotString);
        Class testAnnotClass = org.apache.beehive.controls.api.bean.Control.class;
        simpleAnnotMap.setProperty(new PropertyKey(Props.SimpleProps.class, "simpleClass"), testAnnotClass);

        _propsBean.setSimpleAnnot(simpleAnnotMap.getPropertySet(Props.SimpleProps.class));
        simpleAnnot = _propsBean.getSimpleAnnot();
        assertNotNull(simpleAnnot);
        assertEquals((int) testAnnotInt, simpleAnnot.simpleInt());
        assertEquals(testAnnotString, simpleAnnot.simpleString());
        assertEquals(testAnnotClass, simpleAnnot.simpleClass());
    }

    public void testExternalPropertySet() throws Exception {
        int age = _propsBean.getAge();
        assertEquals(ExtPropertySet.AGE_DEFAULT, age);
        _propsBean.setAge(60);
        age = _propsBean.getAge();
        assertEquals(60, age);
    }


    /**
     * Basic test: Reading property values inside implementation via ControlBeanContext API
     */
    public void testImplAccess() throws Exception {
        Props.SimpleProps simpleProps = (Props.SimpleProps) _propsExtBean.getControlPropertySet(Props.SimpleProps.class);
        assertEquals(PropsExtension.SIMPLE_INT_VALUE, simpleProps.simpleInt());

        simpleProps = (Props.SimpleProps) _propsExtBean.getPropertySetOnMethod1(Props.SimpleProps.class);
        assertEquals(PropsExtension.SIMPLE_CLASS_VALUE1, simpleProps.simpleClass());

        simpleProps = (Props.SimpleProps) _propsExtBean.getPropertySetOnMethod2(Props.SimpleProps.class);
        assertEquals(PropsExtension.SIMPLE_CLASS_VALUE2, simpleProps.simpleClass());

        simpleProps = (Props.SimpleProps) _nestPropsBean.getNestedPropertySet(Props.SimpleProps.class);
        assertEquals("A field annotation value", simpleProps.simpleString());

        String [] arrayExpect = {"One", "Two", "Three"};
        Props.ArrayProps arrayProps = (Props.ArrayProps) _nestPropsBean.getExtensionControlPropertySet(Props.ArrayProps.class);
        String [] arrayString = arrayProps.arrayString();
        assertEquals(arrayExpect.length, arrayString.length);
        assertTrue(Arrays.equals(arrayExpect, arrayString));

        simpleProps = (Props.SimpleProps) _nestPropsBean.getExtensionControlPropertySet(Props.SimpleProps.class);
        assertEquals(PropsExtension.SIMPLE_INT_VALUE, simpleProps.simpleInt());
    }
}
