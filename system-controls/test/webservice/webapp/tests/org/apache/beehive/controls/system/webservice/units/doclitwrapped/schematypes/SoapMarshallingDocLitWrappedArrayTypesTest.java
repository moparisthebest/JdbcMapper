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
package org.apache.beehive.controls.system.webservice.units.doclitwrapped.schematypes;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Arrays;
import javax.xml.namespace.QName;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * Marshalling tests for array types for doc/lit/wrapped services.
 */
public class SoapMarshallingDocLitWrappedArrayTypesTest
    extends ControlTestCase {

    @Control
    public schematypestest.SoapMarshallingDocLitWrappedArrayTypesService client;

    /**
     * Echo boolean array.
     * @throws Exception
     */
    public void testEchobooleanArray() throws Exception {
        boolean[] array = new boolean[] {true, false, true};
        assertTrue(Arrays.equals(array, client.echoboolean(array)));
    }

    /**
     * Echo Boolean array.
     * @throws Exception
     */
    public void testEchoBooleanArray() throws Exception {
        boolean[] array = new boolean[] {true, false, true};
        assertTrue(Arrays.equals(array, client.echoBoolean(array)));
    }

    /**
     * Echo byte array.
     * @throws Exception
     */
    public void testEchobyteArray() throws Exception {
        byte[] array = new byte[] {Byte.MAX_VALUE, 0, Byte.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echobyte(array)));
    }

    /**
     * Echo Byte array.
     * @throws Exception
     */
    public void testEchoByteArray() throws Exception {
        byte[] array = new byte[] {Byte.MAX_VALUE, 0, Byte.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echoByte(array)));
    }

    /**
     * Echo short array.
     * @throws Exception
     */
    public void testEchoshortArray() throws Exception {
        short[] array = new short[] {Short.MAX_VALUE, 0, Short.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echoshort(array)));
    }

    /**
     * Echo Short array.
     * @throws Exception
     */
    public void testEchoShortArray() throws Exception {
        short[] array = new short[] {Short.MAX_VALUE, 0, Short.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echoShort(array)));
    }

    /**
     * Echo int array.
     * @throws Exception
     */
    public void testEchointArray() throws Exception {
        int[] array = new int[] {Integer.MAX_VALUE, 0, Integer.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echoint(array)));
    }

    /**
     * Echo int array.
     * @throws Exception
     */
    public void testEchoIntegerArray() throws Exception {
        int[] array = new int[] {Integer.MAX_VALUE, 0, Integer.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echoInteger(array)));
    }

    /**
     * Echo long array.
     * @throws Exception
     */
    public void testEcholongArray() throws Exception {
        long[] array = new long[] {Long.MAX_VALUE, 0, Long.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echolong(array)));
    }

    /**
     * Echo Long array.
     * @throws Exception
     */
    public void testEchoLongArray() throws Exception {
        long[] array = new long[] {Long.MAX_VALUE, 0, Long.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echoLong(array)));
    }

    /**
     * Echo BigInteger array.
     * @throws Exception
     */
    public void testEchoBigIntegerArray() throws Exception {
        BigInteger[] array = new BigInteger[] {BigInteger.TEN, BigInteger.ONE, BigInteger.TEN};
        assertTrue(Arrays.equals(array, client.echoBigInteger(array)));
    }

    /**
     * Echo float array.
     * @throws Exception
     */
    public void testEchofloatArray() throws Exception {
        float[] array = new float[] {Float.MAX_VALUE, 0, Float.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echofloat(array)));
    }

    /**
     * Echo Float array.
     * @throws Exception
     */
    public void testEchoFloatArray() throws Exception {
        float[] array = new float[] {Float.MAX_VALUE, 0, Float.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echoFloat(array)));
    }

    /**
     * Echo double array.
     * @throws Exception
     */
    public void testEchodoubleArray() throws Exception {
        double[] array = new double[] {Double.MAX_VALUE, 0, Double.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echodouble(array)));
    }

    /**
     * Echo Double array.
     * @throws Exception
     */
    public void testEchoDoubleArray() throws Exception {
        double[] array = new double[] {Double.MAX_VALUE, 0, Double.MAX_VALUE};
        assertTrue(Arrays.equals(array, client.echoDouble(array)));
    }

    /**
     * Echo BigDecimal array.
     * @throws Exception
     */
    public void testEchoBigDecimalArray() throws Exception {
        BigDecimal[] array = new BigDecimal[] {BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN};
        assertTrue(Arrays.equals(array, client.echoBigDecimal(array)));
    }

    /**
     * Echo String array.
     * @throws Exception
     */
    public void testEchoStringArray() throws Exception {
        String[] array = new String[] {"Hello", ",", "World!"};
        assertTrue(Arrays.equals(array, client.echoString(array)));
    }

// @todo: fails -- appears to be axis issue - needs more investigation
//    public void testEchoDateArray() throws Exception {
//        Calendar cal = new GregorianCalendar();
//        cal.setTimeInMillis(123456789);
//        Calendar[] array = new Calendar[] {cal, cal};
//        Calendar[] result = client.echoDate(array);
//        assertEquals(0, cal.compareTo(result[0]));
//        assertEquals(0, cal.compareTo(result[1]));
//    }

    /**
     * Echo Calendar array.
     * @throws Exception
     */
    public void testEchoCalendarArray() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(123456789);
        Calendar[] array = new Calendar[] {cal, cal};
        Calendar[] result = client.echoCalendar(array);
        assertEquals(0, cal.compareTo(result[0]));
        assertEquals(0, cal.compareTo(result[1]));
    }

    /**
     * Echo QName array.
     * @throws Exception
     */
    public void testEchoQNameArray() throws Exception {
        QName qname = new QName("http://foo.bar", "foofoo");
        QName array[] = new QName[] { qname, qname };
        assertTrue(Arrays.equals(array, client.echoQName(array)));
    }

    public static Test suite() {
        return new TestSuite(SoapMarshallingDocLitWrappedArrayTypesTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}