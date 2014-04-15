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
package org.apache.beehive.controls.system.webservice.units.rpclit.schematypes;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import javax.xml.namespace.QName;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Arrays;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Marshalling tests for array types, for rpc/lit services.
 */
public class SoapMarshallingRpcLitArrayTypesTest
        extends ControlTestCase {

    @Control
    public schematypestest.SoapMarshallingRpcLitArrayTypesService client;

    /**
     * Echo boolean array.
     *
     * @throws Exception
     */
    public void testEchobooleanArray() throws Exception {
        boolean[] ar = new boolean[]{true, false};
        boolean[] result = client.echoboolean(ar);
        assertEquals(2, result.length);
        assertEquals(true, result[0]);
        assertEquals(false, result[1]);
    }

    /**
     * Echo Boolean array.
     *
     * @throws Exception
     */
    public void testEchoBooleanArray() throws Exception {
        boolean[] ar = new boolean[]{true, false, true};

        boolean[] result = client.echoBoolean(ar);
        assertEquals(3, result.length);
        assertEquals(true, result[0]);
        assertEquals(false, result[1]);
        assertEquals(true, result[2]);
    }

    /**
     * Echo byte array.
     *
     * @throws Exception
     */
    public void testEchobyteArray() throws Exception {
        byte[] array = new byte[]{Byte.MAX_VALUE, 0, Byte.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echobyte(array)));
    }

    // todo: broken appears to be wsm issue
    /*
    public void testEchoByteArray() throws Exception {
        byte[] ar = new byte[]{Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.MAX_VALUE};
        byte[] result = client.echoByte(ar);
        assertEquals(3, result.length);
        assertEquals(Byte.MAX_VALUE, result[0]);
        assertEquals(Byte.MIN_VALUE, result[1]);
        assertEquals(Byte.MAX_VALUE, result[2]);
    }
    /*

    /**
     * Echo short array.
     *
     * @throws Exception
     */
    public void testEchoshortArray() throws Exception {
        short[] ar = new short[]{Short.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE};
        short[] result = client.echoshort(ar);
        assertEquals(3, result.length);
        assertEquals(Short.MAX_VALUE, result[0]);
        assertEquals(Short.MIN_VALUE, result[1]);
        assertEquals(Short.MAX_VALUE, result[2]);
    }

    /**
     * Echo Short array.
     *
     * @throws Exception
     */
    public void testEchoShortArray() throws Exception {
        short[] ar = new short[]{Short.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE};
        short[] result = client.echoShort(ar);
        assertEquals(3, result.length);
        assertEquals(Short.MAX_VALUE, result[0]);
        assertEquals(Short.MIN_VALUE, result[1]);
        assertEquals(Short.MAX_VALUE, result[2]);
    }

    /**
     * Echo int array.
     *
     * @throws Exception
     */
    public void testEchointArray() throws Exception {
        int[] ar = new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};
        int[] result = client.echoint(ar);
        assertEquals(3, result.length);
        assertEquals(Integer.MAX_VALUE, result[0]);
        assertEquals(Integer.MIN_VALUE, result[1]);
        assertEquals(Integer.MAX_VALUE, result[2]);
    }

    /**
     * Echo Integer array.
     *
     * @throws Exception
     */
    public void testEchoIntegerArray() throws Exception {
        int[] ar = new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};
        int[] result = client.echoInteger(ar);
        assertEquals(3, result.length);
        assertEquals(Integer.MAX_VALUE, result[0]);
        assertEquals(Integer.MIN_VALUE, result[1]);
        assertEquals(Integer.MAX_VALUE, result[2]);
    }

    /**
     * Echo long array.
     *
     * @throws Exception
     */
    public void testEcholongArray() throws Exception {
        long[] ar = new long[]{Long.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE};
        long[] result = client.echolong(ar);
        assertEquals(3, result.length);
        assertEquals(Long.MAX_VALUE, result[0]);
        assertEquals(Long.MIN_VALUE, result[1]);
        assertEquals(Long.MAX_VALUE, result[2]);
    }

    /**
     * Echo Long array.
     *
     * @throws Exception
     */
    public void testEchoLongArray() throws Exception {
        long[] ar = new long[]{Long.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE};
        long[] result = client.echoLong(ar);
        assertEquals(3, result.length);
        assertEquals(Long.MAX_VALUE, result[0]);
        assertEquals(Long.MIN_VALUE, result[1]);
        assertEquals(Long.MAX_VALUE, result[2]);
    }

    /**
     * Echo BigInteger array.
     *
     * @throws Exception
     */
    public void testEchoBigIntegerArray() throws Exception {
        BigInteger[] ar = new BigInteger[]{BigInteger.TEN, BigInteger.ONE, BigInteger.TEN};
        BigInteger[] result = client.echoBigInteger(ar);
        assertEquals(3, result.length);
        assertEquals(BigInteger.TEN, result[0]);
        assertEquals(BigInteger.ONE, result[1]);
        assertEquals(BigInteger.TEN, result[2]);
    }

    /**
     * Echo float array.
     *
     * @throws Exception
     */
    public void testEchofloatArray() throws Exception {
        float[] ar = new float[]{Float.MAX_VALUE, Float.MIN_VALUE, Float.MAX_VALUE};
        float[] result = client.echofloat(ar);
        assertEquals(3, result.length);
        assertEquals(Float.MAX_VALUE, result[0]);
        assertEquals(Float.MIN_VALUE, result[1]);
        assertEquals(Float.MAX_VALUE, result[2]);
    }

    /**
     * Echo Float array.
     *
     * @throws Exception
     */
    public void testEchoFloatArray() throws Exception {
        float[] ar = new float[]{Float.MAX_VALUE, Float.MIN_VALUE, Float.MAX_VALUE};
        float[] result = client.echoFloat(ar);
        assertEquals(3, result.length);
        assertEquals(Float.MAX_VALUE, result[0]);
        assertEquals(Float.MIN_VALUE, result[1]);
        assertEquals(Float.MAX_VALUE, result[2]);
    }

    /**
     * Echo double array.
     *
     * @throws Exception
     */
    public void testEchodoubleArray() throws Exception {
        double[] ar = new double[]{Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE};
        double[] result = client.echodouble(ar);
        assertEquals(3, result.length);
        assertEquals(Double.MAX_VALUE, result[0]);
        assertEquals(Double.MIN_VALUE, result[1]);
        assertEquals(Double.MAX_VALUE, result[2]);
    }

    /**
     * Echo Double array.
     *
     * @throws Exception
     */
    public void testEchoDoubleArray() throws Exception {
        double[] ar = new double[]{Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE};
        double[] result = client.echoDouble(ar);
        assertEquals(3, result.length);
        assertEquals(Double.MAX_VALUE, result[0]);
        assertEquals(Double.MIN_VALUE, result[1]);
        assertEquals(Double.MAX_VALUE, result[2]);
    }

    /**
     * Echo BigDecimal array.
     *
     * @throws Exception
     */
    public void testEchoBigDecimalArray() throws Exception {
        BigDecimal[] ar = new BigDecimal[]{BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN};
        BigDecimal[] result = client.echoBigDecimal(ar);
        assertEquals(3, result.length);
        assertEquals(BigDecimal.TEN, result[0]);
        assertEquals(BigDecimal.ONE, result[1]);
        assertEquals(BigDecimal.TEN, result[2]);
    }

    /**
     * Echo String array.
     *
     * @throws Exception
     */
    public void testEchoStringArray() throws Exception {
        String[] ar = new String[]{"Hello", ",", "World!"};
        String[] result = client.echoString(ar);
        assertEquals(3, result.length);
        assertEquals("Hello", result[0]);
        assertEquals(",", result[1]);
        assertEquals("World!", result[2]);
    }

    // todo: broken needs further investigation
//    public void testEchoDateArray() throws Exception {
//        Calendar cal = new GregorianCalendar();
//        cal.setTimeInMillis(123456789);
//
//        Calendar carray[] = new Calendar[]{cal, cal};
//        Calendar[] result = client.echoDate(carray);
//        assertEquals(2, result.length);
//        assertEquals(0, cal.compareTo(result[0]));
//        assertEquals(0, cal.compareTo(result[1]));
//    }

    /**
     * Echo Calendar array.
     *
     * @throws Exception
     */
    public void testEchoCalendarArray() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(123456789);
        Calendar[] ar = new Calendar[]{cal, cal};
        Calendar[] result = client.echoCalendar(ar);
        assertEquals(2, result.length);
        assertEquals(0, cal.compareTo(result[0]));
        assertEquals(0, cal.compareTo(result[1]));
    }

    /**
     * Echo QName array.
     *
     * @throws Exception
     */
    public void testEchoQNameArray() throws Exception {

        QName qarray[] = new QName[2];
        QName qname1 = new QName("urn://foo.bar", "Hello");
        QName qname2 = new QName("urn://bar.foo", "Goodbye");
        qarray[0] = qname1;
        qarray[1] = qname2;

        QName[] result = client.echoQName(qarray);
        assertEquals(2, result.length);
        assertTrue(qname1.toString().equals(result[0].toString()));
        assertTrue(qname2.toString().equals(result[1].toString()));
    }

    public static Test suite() {
        return new TestSuite(SoapMarshallingRpcLitArrayTypesTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}