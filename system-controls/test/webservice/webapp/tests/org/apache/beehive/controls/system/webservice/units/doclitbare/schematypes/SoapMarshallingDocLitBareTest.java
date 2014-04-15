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
package org.apache.beehive.controls.system.webservice.units.doclitbare.schematypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.namespace.QName;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * Marshalling tests for doc/lit/bare services.
 */
public class SoapMarshallingDocLitBareTest
    extends ControlTestCase {

    @Control
    public schematypestest.SoapMarshallingDocLitBareService client;

    /**
     * Echo a boolean value.
     * @throws Exception
     */
    public void testEchoboolean() throws Exception {
        assertFalse(client.echoboolean(false));
        assertTrue(client.echoboolean(true));
    }

    /**
     * Echo a Boolean value.
     * @throws Exception
     */
    public void testEchoBoolean() throws Exception {
        assertFalse(client.echoBoolean(new Boolean(false)));
        assertTrue(client.echoBoolean(new Boolean(true)));
    }

    /**
     * Echo a byte value.
     * @throws Exception
     */
    public void testEchobyte() throws Exception {
        assertEquals(1, client.echobyte((byte) 1));
    }

    /**
     * Echo a Byte value.
     * @throws Exception
     */
    public void testEchoByte() throws Exception {
        assertEquals(Byte.MAX_VALUE, client.echoByte(Byte.MAX_VALUE));
    }

    /**
     * Echo a short value.
     * @throws Exception
     */
    public void testEchoshort() throws Exception {
        assertEquals(Short.MAX_VALUE, client.echoshort(Short.MAX_VALUE));
    }

    /**
     * Echo a Short value.
     * @throws Exception
     */
    public void testEchoShort() throws Exception {
        assertEquals(Short.MAX_VALUE, client.echoShort(Short.MAX_VALUE));
    }

    /**
     * Echo an int value.
     * @throws Exception
     */
    public void testEchoint() throws Exception {
        assertEquals(Integer.MAX_VALUE, client.echoint(Integer.MAX_VALUE));
    }

    /**
     * Echo an Integer value.
     * @throws Exception
     */
    public void testEchoInteger() throws Exception {
        assertEquals(Integer.MAX_VALUE, client.echoInteger(Integer.MAX_VALUE));
    }

    /**
     * Echo a long value.
     * @throws Exception
     */
    public void testEcholong() throws Exception {
        assertEquals(Long.MAX_VALUE, client.echolong(Long.MAX_VALUE));
    }

    /**
     * Echo a Long value.
     * @throws Exception
     */
    public void testEchoLong() throws Exception {
        assertEquals(Long.MAX_VALUE, client.echoLong(Long.MAX_VALUE));
    }

    /**
     * Echo a BigInteger value.
     * @throws Exception
     */
    public void testEchoBigInteger() throws Exception {
        assertEquals(BigInteger.TEN, client.echoBigInteger(BigInteger.TEN));
    }

    /**
     * Echo a float value.
     * @throws Exception
     */
    public void testEchofloat() throws Exception {
        assertEquals(Float.MAX_VALUE, client.echofloat(Float.MAX_VALUE));
    }

    /**
     * Echo a Float value.
     * @throws Exception
     */
    public void testEchoFloat() throws Exception {
        assertEquals(Float.MAX_VALUE, client.echoFloat(Float.MAX_VALUE));
    }

    /**
     * Echo a double value.
     * @throws Exception
     */
    public void testEchodouble() throws Exception {
        assertEquals(Double.MAX_VALUE, client.echodouble(Double.MAX_VALUE));
    }

    /**
     * Echo a Double value.
     * @throws Exception
     */
    public void testEchoDouble() throws Exception {
        assertEquals(Double.MAX_VALUE, client.echoDouble(Double.MAX_VALUE));
    }

    /**
     * Echo a BigDecimal value.
     * @throws Exception
     */
    public void testEchoBigDecimal() throws Exception {
        assertEquals(BigDecimal.TEN, client.echoBigDecimal(BigDecimal.TEN));
    }

    /**
     * Echo a String value.
     * @throws Exception
     */
    public void testEchoString() throws Exception {
        assertEquals("Hello World!", client.echoString("Hello World!"));
    }

    /**
     * Echo a Date value.
     * @throws Exception
     */
    public void testEchoDate() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(123456789);
        Calendar result = client.echoDate(cal);
        assertEquals(0, cal.compareTo(result));
    }

    /**
     * Echo a Calendar value.
     * @throws Exception
     */
    public void testEchoCalendar() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(123456789);
        Calendar result = client.echoCalendar(cal);
        assertEquals(0, cal.compareTo(result));
    }

    /**
     * Echo a QName value.
     * @throws Exception
     */
    public void testEchoQName() throws Exception {
        QName qname = new QName("http://foo.bar", "foofoo");
        assertEquals(qname.toString(), client.echoQName(qname).toString());
    }

    public static Test suite() {
        return new TestSuite(SoapMarshallingDocLitBareTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}