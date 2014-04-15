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
 */
package org.apache.beehive.controls.system.webservice.units.rpclit.schematypes;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * Marshalling tests for rpc/lit services.
 */
public class SoapMarshallingRpcLitTest
    extends ControlTestCase {

    @Control
    public schematypestest.SoapMarshallingRpcLitService client;

    /**
     * Echo boolean.
     * @throws Exception
     */
    public void testEchoboolean() throws Exception {
        assertFalse(client.echoboolean(false));
        assertTrue(client.echoboolean(true));
    }

    /**
     * Echo Boolean.
     * @throws Exception
     */
    public void testEchoBoolean() throws Exception {
        assertFalse(client.echoBoolean(new Boolean(false)));
        assertTrue(client.echoBoolean(new Boolean(true)));
    }

    /**
     * Echo byte.
     * @throws Exception
     */
    public void testEchobyte() throws Exception {
        assertEquals(1, client.echobyte((byte)1));
    }

    /**
     * Echo Byte.
     * @throws Exception
     */
    public void testEchoByte() throws Exception {
        assertEquals(Byte.MAX_VALUE, client.echoByte(Byte.MAX_VALUE));
    }

    /**
     * Echo short.
     * @throws Exception
     */
    public void testEchoshort() throws Exception {
        assertEquals(Short.MAX_VALUE, client.echoshort(Short.MAX_VALUE));
    }

    /**
     * Echo Short.
     * @throws Exception
     */
    public void testEchoShort() throws Exception {
        assertEquals(Short.MAX_VALUE, client.echoShort(Short.MAX_VALUE));
    }

    /**
     * Echo int.
     * @throws Exception
     */
    public void testEchoint() throws Exception {
        assertEquals(Integer.MAX_VALUE, client.echoint(Integer.MAX_VALUE));
    }

    /**
     * Echo Integer.
     * @throws Exception
     */
    public void testEchoInteger() throws Exception {
        assertEquals(Integer.MAX_VALUE, client.echoInteger(Integer.MAX_VALUE));
    }

    /**
     * Echo long.
     * @throws Exception
     */
    public void testEcholong() throws Exception {
        assertEquals(Long.MAX_VALUE, client.echolong(Long.MAX_VALUE));
    }

    /**
     * Echo Long.
     * @throws Exception
     */
    public void testEchoLong() throws Exception {
        assertEquals(Long.MAX_VALUE, client.echoLong(Long.MAX_VALUE));
    }

    /**
     * Echo BigInteger.
     * @throws Exception
     */
    public void testEchoBigInteger() throws Exception {
        assertEquals(BigInteger.TEN, client.echoBigInteger(BigInteger.TEN));
    }

    /**
     * Echo float.
     * @throws Exception
     */
    public void testEchofloat() throws Exception {
        assertEquals(Float.MAX_VALUE, client.echofloat(Float.MAX_VALUE));
    }

    /**
     * Echo Float.
     * @throws Exception
     */
    public void testEchoFloat() throws Exception {
        assertEquals(Float.MAX_VALUE, client.echoFloat(Float.MAX_VALUE));
    }

    /**
     * Echo double.
     * @throws Exception
     */
    public void testEchodouble() throws Exception {
        assertEquals(Double.MAX_VALUE, client.echodouble(Double.MAX_VALUE));
    }

    /**
     * Echo Double.
     * @throws Exception
     */
    public void testEchoDouble() throws Exception {
        assertEquals(Double.MAX_VALUE, client.echoDouble(Double.MAX_VALUE));
    }

    /**
     * Echo BigDecimal.
     * @throws Exception
     */
    public void testEchoBigDecimal() throws Exception {
        assertEquals(BigDecimal.TEN, client.echoBigDecimal(BigDecimal.TEN));
    }

    /**
     * Echo String.
     * @throws Exception
     */
    public void testEchoString() throws Exception {
        assertEquals("Hello World!", client.echoString("Hello World!"));
    }

    /**
     * Echo Date.
     * @throws Exception
     */
    public void testEchoDate() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(123456789);
        Calendar result = client.echoDate(cal);
        assertEquals(0, cal.compareTo(result));
    }

    /**
     * Echo Calendar.
     * @throws Exception
     */
    public void testEchoCalendar() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(123456789);
        Calendar result = client.echoCalendar(cal);
        assertEquals(0, cal.compareTo(result));
    }

    /**
     * Echo QName.
     * @throws Exception
     */
    public void testEchoQName() throws Exception {
        QName qname = new QName("http://foo.bar", "foofoo");
        assertEquals(qname.toString(), client.echoQName(qname).toString());
    }

    public static Test suite() { return new TestSuite(SoapMarshallingRpcLitTest.class); }

    public static void main(String[] args) { junit.textui.TestRunner.run(suite()); }
}