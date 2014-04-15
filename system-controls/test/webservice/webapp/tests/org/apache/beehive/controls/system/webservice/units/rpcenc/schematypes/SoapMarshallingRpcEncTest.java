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
package org.apache.beehive.controls.system.webservice.units.rpcenc.schematypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Calendar;
import javax.xml.namespace.QName;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * Marshalling tests for Rpc/enc services.
 */
public class SoapMarshallingRpcEncTest
    extends ControlTestCase {
    
    @Control
    public schematypestest.SoapMarshallingRpcEncService client;

    /**
     * Echo boolean.
     * @throws Exception
     */
    public void testEchoboolean() throws Exception {
        boolean b1 = true;
        boolean b2 = false;

        boolean result1 = client.echoboolean(b1);
        assertTrue(result1);
        boolean result2 = client.echoboolean(b2);
        assertFalse(result2);
    }

    /**
     * Echo Boolean.
     * @throws Exception
     */
    public void testEchoBoolean() throws Exception {
        Boolean b1 = true;
        Boolean b2 = false;

        Boolean result1 = client.echoBoolean(b1);
        assertTrue(result1);
        Boolean result2 = client.echoBoolean(b2);
        assertFalse(result2);
    }

    /**
     * Echo byte.
     * @throws Exception
     */
    public void testEchobyte() throws Exception {
        byte b = Byte.MAX_VALUE;
        byte result = client.echobyte(b);
        assertEquals(b, result);
    }

    /**
     * Echo Byte.
     * @throws Exception
     */
    public void testEchoByte() throws Exception {
        Byte b = Byte.MAX_VALUE;
        Byte result = client.echoByte(b);
        assertEquals(b, result);
    }

    /**
     * Echo short.
     * @throws Exception
     */
    public void testEchoshort() throws Exception {
        short s = Short.MAX_VALUE;
        short result = client.echoshort(s);
        assertEquals(s, result);
    }

    /**
     * Echo Short.
     * @throws Exception
     */
    public void testEchoShort() throws Exception {
        Short s = Short.MAX_VALUE;
        Short result = client.echoShort(s);
        assertEquals(s, result);
    }

    /**
     * Echo int.
     * @throws Exception
     */
    public void testEchoint() throws Exception {
        int i = Integer.MAX_VALUE;
        int result = client.echoint(i);
        assertEquals(i, result);
    }

    /**
     * Echo integer.
     * @throws Exception
     */
    public void testEchoInteger() throws Exception {
        Integer i = Integer.MAX_VALUE;
        Integer result = client.echoInteger(i);
        assertEquals(i, result);
    }

    /**
     * Echo long.
     * @throws Exception
     */
    public void testEcholong() throws Exception {
        long l = Long.MIN_VALUE;
        long result = client.echolong(l);
        assertEquals(l, result);
    }

    /**
     * Echo Long.
     * @throws Exception
     */
    public void testEchoLong() throws Exception {
        Long l = new Long(Long.MAX_VALUE);
        Long result = client.echoLong(l);
        assertEquals(l, result);
    }

    /**
     * Echo BigInteger.
     * @throws Exception
     */
    public void testEchoBigInteger() throws Exception {
        BigInteger bi = BigInteger.TEN;
        BigInteger result = client.echoBigInteger(bi);
        assertEquals(bi, result);
    }

    /**
     * Echo float.
     * @throws Exception
     */
    public void testEchofloat() throws Exception {
        float f = Float.MAX_VALUE;
        float result = client.echofloat(f);
        assertEquals(f, result);
    }

    /**
     * Echo Float.
     * @throws Exception
     */
    public void testEchoFloat() throws Exception {
        Float f = Float.MAX_VALUE;
        Float result = client.echoFloat(f);
        assertEquals(f, result);
    }

    /**
     * Echo double.
     * @throws Exception
     */
    public void testEchodouble() throws Exception {
        double d = Double.MAX_VALUE;
        double result = client.echodouble(d);
        assertEquals(d, result);
    }

    /**
     * Echo Double.
     * @throws Exception
     */
    public void testEchoDouble() throws Exception {
        Double d = Double.MAX_VALUE;
        Double result = client.echoDouble(d);
        assertEquals(d, result);
    }

    /**
     * Echo BigDecimal.
     * @throws Exception
     */
    public void testEchoBigDecimal() throws Exception {
        BigDecimal bd = BigDecimal.TEN;
        BigDecimal result = client.echoBigDecimal(bd);
        assertEquals(bd, result);
    }

    /**
     * Echo String.
     * @throws Exception
     */
    public void testEchoString() throws Exception {
        String s = "Hello World!";
        String result = client.echoString(s);
        assertEquals(s, result);
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
        QName result = client.echoQName(qname);
        assertEquals(qname.toString(), result.toString());
    }

    public static Test suite() {
        return new TestSuite(SoapMarshallingRpcEncTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}