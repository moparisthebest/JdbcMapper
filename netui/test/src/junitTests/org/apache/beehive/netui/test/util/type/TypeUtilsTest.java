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
package org.apache.beehive.netui.test.util.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.type.TypeUtils;
import org.apache.beehive.netui.test.util.config.TestConfigUtil;

/**
 *
 */
public class TypeUtilsTest
    extends TestCase {

    private static final Logger _logger = Logger.getInstance(TypeUtilsTest.class);

    // String value, target type, should convert, conversion validator
    private Object[][] _tests =
        {
            {"42", int.class, Boolean.TRUE, new Integer(42)},
            {"42.42", float.class, Boolean.TRUE, new Float(42.42)},

            {"42", Integer.class, Boolean.TRUE, new Integer(42)},
            {"42.42", Float.class, Boolean.TRUE, new Float(42.42)},
            {"11/12/2002", java.util.Date.class, Boolean.TRUE, new java.util.Date("11/12/2002")},
            {"2002-11-12", java.sql.Date.class, Boolean.TRUE, java.sql.Date.valueOf("2002-11-12")},
            {"2002-11-12 00:00:00.00", java.sql.Timestamp.class, Boolean.TRUE, java.sql.Timestamp.valueOf("2002-11-12 00:00:00.00")},
            {"18:20:00", java.sql.Time.class, Boolean.TRUE, java.sql.Time.valueOf("18:20:00")},

            {"", Integer.class, Boolean.TRUE, null},
            {"", Byte.class, Boolean.TRUE, null},
            {"", Double.class, Boolean.TRUE, null},
            {"", Float.class, Boolean.TRUE, null},
            {"", Long.class, Boolean.TRUE, null},
            {"", Short.class, Boolean.TRUE, null},
            {"", Character.class, Boolean.TRUE, null},
            {"", String.class, Boolean.TRUE, ""},
            {"", BigDecimal.class, Boolean.TRUE, null},
            {"", BigInteger.class, Boolean.TRUE, null},
            {null, Integer.class, Boolean.TRUE, null},
            {null, Byte.class, Boolean.TRUE, null},
            {null, Double.class, Boolean.TRUE, null},
            {null, Float.class, Boolean.TRUE, null},
            {null, Long.class, Boolean.TRUE, null},
            {null, Short.class, Boolean.TRUE, null},
            {null, Character.class, Boolean.TRUE, null},
            {null, String.class, Boolean.TRUE, null},
            {null, BigDecimal.class, Boolean.TRUE, null},
            {null, BigInteger.class, Boolean.TRUE, null},

            {"1234", int.class, Boolean.TRUE, new Integer(1234)},
            {"-42", byte.class, Boolean.TRUE, new Byte((byte)-42)},
            {"3.1415", double.class, Boolean.TRUE, new Double(3.1415)},
            {"42.42", float.class, Boolean.TRUE, new Float(42.42)},
            {"123456789", long.class, Boolean.TRUE, new Long(123456789)},
            {"12", short.class, Boolean.TRUE, new Short((short)12)},
            {"A", char.class, Boolean.TRUE, new Character('A')},
            {"12345678987654321", BigDecimal.class, Boolean.TRUE, new BigDecimal("12345678987654321")},
            {"12345678987654321", BigInteger.class, Boolean.TRUE, new BigInteger("12345678987654321")},
            {"Now is the time for all good men to come to the aid of their country", String.class, Boolean.TRUE,
             new String("Now is the time for all good men to come to the aid of their country")},

            {"", int.class, Boolean.TRUE, new Integer(0)},
            {"", byte.class, Boolean.TRUE, new Byte((byte)0)},
            {"", double.class, Boolean.TRUE, new Double(0)},
            {"", float.class, Boolean.TRUE, new Float(0)},
            {"", long.class, Boolean.TRUE, new Long(0)},
            {"", short.class, Boolean.TRUE, new Short((short)0)},
            {"", char.class, Boolean.TRUE, new Character('\u0000')},
            {null, int.class, Boolean.TRUE, new Integer(0)},
            {null, byte.class, Boolean.TRUE, new Byte((byte)0)},
            {null, double.class, Boolean.TRUE, new Double(0)},
            {null, float.class, Boolean.TRUE, new Float(0)},
            {null, long.class, Boolean.TRUE, new Long(0)},
            {null, short.class, Boolean.TRUE, new Short((short)0)},
            {null, char.class, Boolean.TRUE, new Character('\u0000')},

            // error cases
            {"2002/11/12", java.util.Date.class, Boolean.FALSE, null},
            {"2002-11-12 00:00:00.00", java.sql.Date.class, Boolean.FALSE, null},
            {"11:12:11.00", java.sql.Time.class, Boolean.FALSE, null},
            {"asdf", java.sql.Timestamp.class, Boolean.FALSE, null},

            {"abc", int.class, Boolean.FALSE, null},
            {"abc", byte.class, Boolean.FALSE, null},
            {"abc", double.class, Boolean.FALSE, null},
            {"abc", float.class, Boolean.FALSE, null},
            {"abc", long.class, Boolean.FALSE, null},
            {"abc", short.class, Boolean.FALSE, null},
            {"abc", int.class, Boolean.FALSE, null},
            {"abc", byte.class, Boolean.FALSE, null},
            {"abc", double.class, Boolean.FALSE, null},
            {"abc", float.class, Boolean.FALSE, null},
            {"abc", long.class, Boolean.FALSE, null},
            {"abc", short.class, Boolean.FALSE, null},
        };

    public void testConverter()
        throws Exception {
        int i = 0;
        try {
            for(i = 0; i < _tests.length; i++) {
                String value = (String)_tests[i][0];
                Class type = (Class)_tests[i][1];
                boolean valid = ((Boolean)_tests[i][2]).booleanValue();

                if(_logger.isDebugEnabled())
                    _logger.debug("tests[" + i + "]: convert \"" + value + "\" to type \"" + type + "\"");

                Object result = null;
                try {
                    result = TypeUtils.convertToObject(value, type, Locale.US);

                    if(!valid)
                        throw new TestFailedException("The test case [" + i + "] failed because an illegal value \"" +
                            value + "\" was accepted in converting to type \"" + type + "\"");
                } catch(Exception e) {
                    if(!valid)
                        continue;
                    else if(valid)
                        throw new TestFailedException("The test case [" + i + "] failed because an illegal value \"" +
                            value + "\" was accepted in converting to type \"" + type + "\"");
                }

                Object target = _tests[i][3];
                boolean correct = false;
                if(target instanceof TypeConverterValidator)
                    correct = ((TypeConverterValidator)target).validate(result);
                else if(target == null && result == null)
                    correct = true;
                else if((target == null && result != null) || (target != null && result == null))
                    correct = false;
                else
                    correct = target.equals(result);

                if(_logger.isDebugEnabled()) {
                    _logger.debug("target.toString(): " + target);
                    _logger.debug("result.toString(): " + result);
                }

                if(!correct)
                    throw new TestFailedException("The test case [" + i + "] failed because the converted value \"" +
                        result + "\" did not match the expected value \"" + target + "\" in converting to type \"" + type.getName() + "\"");
            }
        } catch(Exception e) {
            throw new TestFailedException("The test case [" + i + "] failed because an exception was thrown: " + e, e);
        }
    }

    interface TypeConverterValidator {

        boolean validate(Object value);
    }

    class TestFailedException
        extends RuntimeException {

        TestFailedException(String message) {
            super(message);
        }

        TestFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public TypeUtilsTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TypeUtilsTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
        TestConfigUtil.testInit();
    }

    protected void tearDown() {
    }
}
