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
package org.apache.beehive.netui.test.tools;

import java.lang.reflect.Array;

import junit.framework.AssertionFailedError;

/**
 * <p>
 * Helpers for performing asserts between types that JUnit doesn't support.
 * </p>
 * <p>
 * Many of these methods compare String[] to some Java type and convert the String into
 * the compare-to type such as a Boolean, boolean, or Float.
 * </p>
 * <p>
 * Note, for the double / floating comparisons, these may fail on some VMs because of precision
 * comparison problems.
 * </p>
 */
public final class AssertHelper {

    public static void assertEquals(double expected, double actual) {
        if(expected != actual)
            throw new AssertionFailedError("Double assert failed; actual = " + actual + " expected = " + expected);
    }

    public static void assertEquals(float expected, float actual) {
        if(expected != actual)
            throw new AssertionFailedError("Float assert failed; actual = " + actual + " expected = " + expected);
    }

    public static void assertEquals(String[] exp, String[] act) {
        checkArrays(exp, act);
        compareArrays(exp, act);
    }

    public static void assertEquals(String[] exp, boolean[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Boolean.valueOf(exp[i]).booleanValue() != act[i])
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, int[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Integer.parseInt(exp[i]) != act[i])
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, char[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(exp[i].charAt(0) != act[i])
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, byte[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Byte.valueOf(exp[i]).byteValue() != act[i])
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, double[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Double.parseDouble(exp[i]) != act[i])
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, float[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Float.parseFloat(exp[i]) != act[i])
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, long[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Long.parseLong(exp[i]) != act[i])
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, short[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Short.parseShort(exp[i]) != act[i])
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, Boolean[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(((exp[i] == null || exp[i].equals("")) && act[i] == null))
                return;
            else if((exp[i] == null || exp[i].equals("")) && act[i] != null)
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
            else if(Boolean.valueOf(exp[i]).booleanValue() != act[i].booleanValue())
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, Byte[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(((exp[i] == null || exp[i].equals("")) && act[i] == null))
                return;
            else if((exp[i] == null || exp[i].equals("")) && act[i] != null)
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
            else if(Byte.valueOf(exp[i]).byteValue() != act[i].byteValue())
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, Integer[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Integer.parseInt(exp[i]) != act[i].intValue())
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, Character[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(exp[i].charAt(0) != act[i].charValue())
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, Double[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Double.parseDouble(exp[i]) != act[i].doubleValue())
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, Float[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Float.parseFloat(exp[i]) != act[i].floatValue())
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, Long[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Long.parseLong(exp[i]) != act[i].longValue())
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    public static void assertEquals(String[] exp, Short[] act) {
        checkArrays(exp, act);

        for(int i = 0; i < exp.length; i++) {
            if(Long.parseLong(exp[i]) != act[i].shortValue())
                throw new AssertionFailedError("The values expected[" + i + "]=" + exp[i] + " does not equal actual[" + i + "]=" + act[i]);
        }
    }

    private static void checkArrays(Object exp, Object act) {
        if(exp == null && act == null)
            return;
        else if((exp != null && act == null))
            throw new AssertionFailedError("The expected array is non-null and the actual array is null.");
        else if(exp == null && act != null)
            throw new AssertionFailedError("The actual array is non-null and the expected array is null.");

        if(!exp.getClass().isArray())
            throw new AssertionFailedError("The expected type should be an array but is \"" + exp.getClass().getName());
        if(!act.getClass().isArray())
            throw new AssertionFailedError("The actual type should be an array but is \"" + act.getClass().getName());

        if(Array.getLength(exp) != Array.getLength(act))
            throw new AssertionFailedError("The actual and expected value arrays are not of equal length");
    }

    private static void compareArrays(Object[] exp, Object[] act) {
        for(int i = 0; i < exp.length; i++) {
            if(!exp[i].equals(act[i]))
                throw new AssertionFailedError("The values expected[" + i + "]=\"" + exp[i] + "\" does not equal actual[" + i + "]=\"" + act[i] + "\"");
        }
    }
}
