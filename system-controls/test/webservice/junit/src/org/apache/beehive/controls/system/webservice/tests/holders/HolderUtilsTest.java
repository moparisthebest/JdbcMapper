/*
 * 
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
package org.apache.beehive.controls.system.webservice.tests.holders;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.beehive.controls.system.webservice.utils.HolderUtils;
import org.apache.beehive.controls.system.webservice.utils.GenericHolder;

import javax.xml.rpc.holders.IntHolder;
import javax.xml.rpc.holders.BigDecimalHolder;
import javax.xml.rpc.holders.IntegerWrapperHolder;
import javax.xml.rpc.holders.CalendarHolder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.math.BigDecimal;

public class HolderUtilsTest extends TestCase {

    public void testStuffIntHolder() throws Exception {
        IntHolder ih = new IntHolder();
        HolderUtils.stuffHolderValue(ih, null);
        assertEquals(0, ih.value);

        HolderUtils.stuffHolderValue(ih, 8);
        assertEquals(8, ih.value);

        HolderUtils.stuffHolderValue(ih, 9);
        assertEquals(9, ih.value);
    }

    public void testStuffIntegerWrapperHolder() throws Exception {
        IntegerWrapperHolder ih = new IntegerWrapperHolder();
        HolderUtils.stuffHolderValue(ih, null);
        assertNull(ih.value);

        HolderUtils.stuffHolderValue(ih, 5);
        assertEquals(5, (int)ih.value);

        HolderUtils.stuffHolderValue(ih, 4);
        assertEquals(4, (int)ih.value);
    }

    public void testStuffBigDecimalHolder() throws Exception {
        BigDecimalHolder bdh = new BigDecimalHolder();
        HolderUtils.stuffHolderValue(bdh, null);
        assertNull(bdh.value);

        HolderUtils.stuffHolderValue(bdh, BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, bdh.value);
    }

    public void testStuffCalendarHolder() throws Exception {
        CalendarHolder ch = new CalendarHolder();
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(123456789);
        HolderUtils.stuffHolderValue(ch, c);
        assertEquals(0, c.compareTo(ch.value));
    }

    public void testGenericHolderComplexValue() throws Exception {
        GenericHolder<MyClass> myHolder = new GenericHolder<MyClass>(new MyClass());
        MyClass theValue = new MyClass("AAA");
        HolderUtils.stuffHolderValue(myHolder, theValue);
        assertEquals("AAA", myHolder.value.name);
    }

    public void testGenericHolderNullValue() throws Exception {
        GenericHolder<MyClass> myHolder = new GenericHolder<MyClass>(new MyClass());
        HolderUtils.stuffHolderValue(myHolder, null);
        assertNull(myHolder.value);
    }

    public void testMyClassArrayHolderArrayStuffObject() throws Exception {

        // stuff object to holder of static array
        MyClassArrayHolder myStaticArrayHolder = new MyClassArrayHolder();
        MyClass theValue = new MyClass("BBB");
        HolderUtils.stuffHolderValue(myStaticArrayHolder, theValue);
        MyClass[] myStaticArrayValueResult = myStaticArrayHolder.value;
        assertEquals("BBB", myStaticArrayValueResult[0].name);
    }

    public void testGenericHolderStuffObject() throws Exception {
        // holder of array stuff object
        GenericHolder<MyClass[]> myArrayHolder = new GenericHolder<MyClass[]>(new MyClass[0]);
        MyClass theValue = new MyClass("CCC");
        HolderUtils.stuffHolderValue(myArrayHolder, theValue);
        MyClass[] myArrayValueResult = myArrayHolder.value;
        assertEquals("CCC", myArrayValueResult[0].name);
    }

    public void testGenericHolderArrayStuffArray() throws Exception {
        // holder of array stuff array list of objects.
        GenericHolder<MyClass[]> myArrayHolder = new GenericHolder<MyClass[]>(new MyClass[0]);
        ArrayList<MyClass> myClassList = new ArrayList<MyClass>();
        myClassList.add(new MyClass("aaaa"));
        myClassList.add(new MyClass("bbbb"));
        HolderUtils.stuffHolderValue(myArrayHolder, myClassList);
        MyClass[] myArrayValueResult = myArrayHolder.value;
        assertEquals("aaaa", myArrayValueResult[0].name);
        assertEquals("bbbb", myArrayValueResult[1].name);

    }

    public void testMyClassListHolderStuffList() throws Exception {
        MyClassListHolder myHolder = new MyClassListHolder();
        ArrayList<MyClass> myClassList = new ArrayList<MyClass>();
        myClassList.add(new MyClass("cccc"));
        myClassList.add(new MyClass("dddd"));
        HolderUtils.stuffHolderValue(myHolder, myClassList);
        assertEquals("cccc", ((MyClass)myHolder.value.get(0)).name);
        assertEquals("dddd", ((MyClass)myHolder.value.get(1)).name);
    }

    public static Test suite() {
        return new TestSuite(org.apache.beehive.controls.system.webservice.tests.holders.HolderUtilsTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}



