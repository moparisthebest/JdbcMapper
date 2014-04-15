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
package org.apache.beehive.netui.test.script;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.AssertionFailedError;

import org.apache.beehive.netui.test.beans.SimpleTypeActionForm;
import org.apache.beehive.netui.test.beans.ComplexTypeActionForm;
import org.apache.beehive.netui.test.tools.AssertHelper;
import org.apache.beehive.netui.script.IllegalExpressionException;
import org.apache.beehive.netui.util.logging.Logger;

/**
 *
 */
public abstract class ExpressionUpdateTest
    extends AbstractExpressionTest {

    private static final Logger _logger = Logger.getInstance(ExpressionUpdateTest.class);

    private static final String[] EMPTY_ARRAY = {"", "", ""};

    /* ==========================================================================
     *
     * Tests that verify that an update expression is created, executed, and 
     * that the model reflects these changes correctly.
     *
     * ==========================================================================
     */

    public void testB43011()
        throws Exception {
        String[] newValue = {"123", "456"};

        evaluateUpdateExpression("{actionForm.stringProperty}", newValue, getPageContext().getRequest(), getPageContext().getResponse(), getActionForm());
        assertEquals(newValue[1], ((SimpleTypeActionForm)getActionForm()).getStringProperty());
    }

    public void testNonexistentPropertySet()
        throws Exception {
        boolean exception = false;
        try {
            // boolean array
            String[] boolStrArray = {"true", "false", "false", "true"};
            evaluateUpdateExpression("{actionForm.neverMoreProperty}", boolStrArray, getPageContext().getRequest(),
                getPageContext().getResponse(), getActionForm());
        } catch(Exception e) {
            exception = true;
        }
        assertTrue(exception);
    }

    public void testComplexUpdateExpressionEvaluation()
        throws Exception {
        Object[][] data = complexUpdateData;

        executeComplexTest("complex update expressions", data);
    }

    /**
     * Test updates of String -> primitive types
     */
    public void testPrimitiveUpdate()
        throws Exception {
        Object[][] data = primitiveUpdateData;
        executeSimpleTest("primitive update", data);
    }

    public void testPrimitiveWrapperUpdate()
        throws Exception {
        Object[][] data = primitiveWrapperUpdateData;
        executeSimpleTest("primitive wrapper update", data);
    }

    /**
     * Test updates of String[] -> primitive[]
     * Test updates of String[] -> primitiveWrapper[]
     */
    public void testArrayUpdate()
        throws Exception {
        Object[][] data = arrayUpdateData;

        executeSimpleTest("array update test", data);
    }

    /**
     * Test for setting complex types into a WebScriptableObject.
     */
    public void testSettingComplexTypes()
        throws Exception {
        useForm(COMPLEX_FORM);

        ServletRequest request = getPageContext().getRequest();
        ServletResponse response = getPageContext().getResponse();
        Object[][] data = settingComplexTypeData;
        for(int i = 0; i < data.length; i++) {
            String expr = (String)data[i][0];
            Object act = (Object)data[i][1];

            evaluateUpdateExpression(expr, act, request, response, getActionForm(), false);
            Object foo = evaluateExpression(expr, getPageContext());
            assertTrue(act.equals(foo));

            _logger.debug("returned value: " + evaluateExpression(expr, getPageContext()));
        }

        // primitive int test
        String expr = "{actionForm.intProperty}";
        evaluateUpdateExpression(expr, "42", request, response, getActionForm());
        assertTrue(42 == ((Integer)evaluateExpression(expr, getPageContext())).intValue());

        // primitive int[] test
        expr = "{actionForm.intArrayProperty}";
        evaluateUpdateExpression(expr, "42", request, response, getActionForm());
        assertTrue(42 == ((int[])evaluateExpression(expr, getPageContext()))[0]);

        // primitive boolean[] test
        expr = "{actionForm.boolArrayProperty}";
        evaluateUpdateExpression(expr, new String[]{"true", "true", "false", "true", "true"}, request, response, getActionForm());
        assertTrue(((boolean[])evaluateExpression(expr, getPageContext()))[0]);
        assertTrue(((boolean[])evaluateExpression(expr, getPageContext()))[1]);
        assertTrue(!((boolean[])evaluateExpression(expr, getPageContext()))[2]);
        assertTrue(((boolean[])evaluateExpression(expr, getPageContext()))[3]);
        assertTrue(((boolean[])evaluateExpression(expr, getPageContext()))[4]);
    }

    public void testWritableContexts()
        throws Exception {
        Object[][] data = writableContextData;

        for(int i = 0; i < data.length; i++) {
            String expr = (String)data[i][0];
            try {
                evaluateUpdateExpression(expr, null, getRequest(), getResponse(), getActionForm(), true);
            } catch(Exception e) {
                e.printStackTrace();

                if(e.getCause() instanceof IllegalExpressionException)
                    continue;
            }

            throw new ExpressionTestException("The expression \"" + expr + "\" did not throw an exception as expected.", null);
        }
    }

    public void testDirectListUpdate()
        throws Exception {
        useForm(COMPLEX_FORM);

        executeComplexTest("list update", directListUpdate);
    }

    /**
     * Test indexed property updates
     */
    public void testIndexedUpdate()
        throws Exception {
        // array initializations
        ((SimpleTypeActionForm)getActionForm()).setIntWrapperArrayProperty
            (new Integer[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5)});

        executeSimpleTest("indexed update", indexedUpdateData);
    }

    /* ==========================================================================
     *
     * Update test data
     *
     * ==========================================================================
     */
    private Object[][] writableContextData =
        {
            {"{applicationScope.foo}", Boolean.FALSE},
            {"{container.foo}", Boolean.FALSE},
            {"{pageScope.foo}", Boolean.FALSE},
            {"{requestScope.foo}", Boolean.FALSE},
            {"{sessionScope.foo}", Boolean.FALSE},
            {"{param.foo}", Boolean.FALSE},
        };

    private Object[][] settingComplexTypeData =
        {
            {"{requestScope.myNewBean}", new SimpleTypeActionForm()},
            {"{sessionScope.anotherBean}", new ComplexTypeActionForm()},
            {"{actionForm.bigDecimal}", new BigDecimal("1234567890.98273835093245")},
            {"{actionForm.date}", new Date()},
            {"{sessionScope.complexBean}", new ComplexTypeActionForm()}, // this must precede the next four tests
            {"{sessionScope.complexBean.bigDecimal}", new BigDecimal("1234567890.98273835093245")},
            {"{sessionScope.complexBean.date}", new Date()},
            {"{actionForm.array[2].intWrapperProperty}", new Integer(42)},
            {"{actionForm.array[2].intWrapperArrayProperty}", new Integer[]{new Integer(42)}},
            // {"{request.complexBean.array[2].intWrapperArrayProperty}", new Integer(42)} // should this work?
        };

    private Object[][] complexUpdateData =
        {
            // expr, value, update expr, expected object result, comparator
            {"{actionForm.map.bean.shortProperty}",
             "4",
             new Short((short)4),
             new UpdateComparatorAdaptor() {
                 public void compareComplex(Object expected, ComplexTypeActionForm complexBean) {
                     assertEquals(((Short)expected).shortValue(),
                         ((SimpleTypeActionForm)complexBean.getMap().get("bean")).getShortProperty());

                 }
             }
            },
            {"{actionForm.array[0].shortProperty}",
             "7",
             new Short((short)7),
             new UpdateComparatorAdaptor() {
                 public void compareComplex(Object expected, ComplexTypeActionForm complexBean) {
                     assertEquals(((Short)expected).shortValue(),
                         ((SimpleTypeActionForm)complexBean.getArray()[0]).getShortProperty());

                 }
             }
            }
        };

    // expression, string update value, Object expected value, comparator
    private Object[][] primitiveUpdateData =
        {
//         // this test will fail and is used to make sure the harness is running correctly
//         {"{actionForm.intProperty}", "1234321", new Integer(1234), new UpdateComparatorAdaptor()
//             {
//                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
//                     assertEquals(actualBean.getIntProperty(), 
//                                  ((Integer)expected).intValue());
//                 }
//             }
//         },
            {"{actionForm.boolProperty}", "true", Boolean.TRUE, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getBoolProperty(),
                        ((Boolean)expected).booleanValue());
                }
            }},
            {"{actionForm.boolProperty}", "", Boolean.FALSE, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getBoolProperty(),
                        ((Boolean)expected).booleanValue());
                }
            }
            },
            {"{actionForm.byteProperty}", "8", new Byte("8"), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getByteProperty(),
                        ((Byte)expected).byteValue());
                }
            }
            },
            {"{actionForm.byteProperty}", "", new Byte("0"), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getByteProperty(),
                        ((Byte)expected).byteValue());
                }
            }
            },
            {"{actionForm.charProperty}", "Z", new Character('Z'), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getCharProperty(), ((Character)expected).charValue());
                }
            },
            },
            {"{actionForm.charProperty}", "", new Character('\u0000'), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getCharProperty(), ((Character)expected).charValue());
                }
            },
            },
            {"{actionForm.doubleProperty}", "3.1415", new Double(3.1415), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    AssertHelper.assertEquals(actualBean.getDoubleProperty(),
                        ((Double)expected).doubleValue());
                }
            }
            },
            {"{actionForm.doubleProperty}", "", new Double(0.0), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    AssertHelper.assertEquals(actualBean.getDoubleProperty(),
                        ((Double)expected).doubleValue());
                }
            }
            },
            {"{actionForm.floatProperty}", "3.1415f", new Float(3.1415), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    AssertHelper.assertEquals(actualBean.getFloatProperty(),
                        ((Float)expected).floatValue());
                }
            }
            },
            {"{actionForm.floatProperty}", "", new Float(0.0), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    AssertHelper.assertEquals(actualBean.getFloatProperty(),
                        ((Float)expected).floatValue());
                }
            }
            },
            {"{actionForm.intProperty}", "1234321", new Integer(1234321), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getIntProperty(),
                        ((Integer)expected).intValue());
                }
            }
            },
            {"{actionForm.intProperty}", "1234321 ", new Integer(1234321), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getIntProperty(),
                        ((Integer)expected).intValue());
                }
            }
            },
            {"{actionForm.intProperty}", " 1234321 ", new Integer(1234321), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getIntProperty(),
                        ((Integer)expected).intValue());
                }
            }
            },
            {"{actionForm.intProperty}", "    1234321     ", new Integer(1234321), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getIntProperty(),
                        ((Integer)expected).intValue());
                }
            }
            },
            // @todo: this fails
//         {"{actionForm.intProperty}", "     ", new Integer(0), new UpdateComparatorAdaptor()
//             {
//                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
//                     assertEquals(actualBean.getIntProperty(), ((Integer)expected).intValue());
//                 }
//             }
//         },
//         {"{actionForm.intProperty}", new Character('\t'), new Integer(0), new UpdateComparatorAdaptor()
//             {
//                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
//                     assertEquals(actualBean.getIntProperty(), ((Integer)expected).intValue());
//                 }
//             }
//         },
//         {"{actionForm.intProperty}", new Character('\n'), new Integer(0), new UpdateComparatorAdaptor()
//             {
//                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
//                     assertEquals(actualBean.getIntProperty(), 
//                                  ((Integer)expected).intValue());
//                 }
//             }
//         },
//         {"{actionForm.intProperty}", new Character('\r'), new Integer(0), new UpdateComparatorAdaptor()
//             {
//                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
//                     assertEquals(actualBean.getIntProperty(), 
//                                  ((Integer)expected).intValue());
//                 }
//             }
//         },
            {"{actionForm.intProperty}", "", new Integer(0), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getIntProperty(),
                        ((Integer)expected).intValue());
                }
            }
            },
            {"{actionForm.intProperty}", null, new Integer(0), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getIntProperty(), ((Integer)expected).intValue());
                }
            }
            },
            // this test fails on jRockit because of precision differences in how the VMs execute
//         {"{actionForm.longProperty}", "12345678987654321L", new Long(12345678987654321L), new UpdateComparatorAdaptor()
//             {
//                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean)
//                 {
//                     assertEquals(actualBean.getLongProperty(), 
//                                  ((Long)expected).longValue());
//                 }
//             }
//         },
            {"{actionForm.longProperty}", "", new Long(0L), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getLongProperty(),
                        ((Long)expected).longValue());
                }
            }
            },
            {"{actionForm.shortProperty}", "32766", new Short((short)32766), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getShortProperty(),
                        ((Short)expected).shortValue());
                }
            }
            },
            {"{actionForm.shortProperty}", "", new Short((short)0), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getShortProperty(),
                        ((Short)expected).shortValue());
                }
            }
            }
        };

    private Object[][] primitiveWrapperUpdateData =
        {
            {"{actionForm.stringProperty}", "aNewStringProperty", "aNewStringProperty", new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getStringProperty(), expected);
                }
            }
            },
            {"{actionForm.stringProperty}", "", "", new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getStringProperty(), expected);
                }
            }
            },
            {"{actionForm.stringProperty}", null, null, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertNull(actualBean.getStringProperty());
                }
            }
            },
            {"{actionForm.booleanWrapperProperty}", "true", Boolean.TRUE, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getBooleanWrapperProperty().booleanValue(),
                        ((Boolean)expected).booleanValue());
                }
            }
            },
            {"{actionForm.booleanWrapperProperty}", "false", Boolean.FALSE, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getBooleanWrapperProperty().booleanValue(),
                        ((Boolean)expected).booleanValue());
                }
            }
            },
            {"{actionForm.booleanWrapperProperty}", "", null, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    if(expected.equals("") && actualBean.getBooleanWrapperProperty() == null)
                        return;
                    else
                        throw new AssertionFailedError("Expected null but found " + actualBean.getBooleanWrapperProperty());
                }
            }
            },
            {"{actionForm.byteWrapperProperty}", "8", new Byte("8"), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getByteWrapperProperty().byteValue(),
                        ((Byte)expected).byteValue());
                }
            }
            },
            {"{actionForm.byteWrapperProperty}", "", null, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    if(expected.equals("") && actualBean.getByteWrapperProperty() == null)
                        return;
                    else
                        throw new AssertionFailedError("Expected null but found " + actualBean.getByteWrapperProperty());
                }
            }
            },
            {"{actionForm.charWrapperProperty}", "Z", new Character('Z'), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getCharWrapperProperty().charValue(),
                        ((Character)expected).charValue());
                }
            }
            },
            {"{actionForm.charWrapperProperty}", "", null, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    if(expected.equals("") && actualBean.getCharWrapperProperty() == null)
                        return;
                    else
                        throw new AssertionFailedError("Expected null but found " + actualBean.getCharWrapperProperty());
                }
            }
            },
            {"{actionForm.doubleWrapperProperty}", "3.1415", new Double(3.1415), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    AssertHelper.assertEquals(actualBean.getDoubleWrapperProperty().doubleValue(),
                        ((Double)expected).doubleValue());
                }
            }
            },
            {"{actionForm.doubleWrapperProperty}", "", null, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    if(expected.equals("") && actualBean.getDoubleWrapperProperty() == null)
                        return;
                    else
                        throw new AssertionFailedError("Expected null but found " + actualBean.getDoubleWrapperProperty());
                }
            }
            },
            {"{actionForm.floatWrapperProperty}", "3.1415f", new Float(3.1415), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    AssertHelper.assertEquals(actualBean.getFloatWrapperProperty().floatValue(),
                        ((Float)expected).floatValue());
                }
            }
            },
            {"{actionForm.floatWrapperProperty}", "", null, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    if(expected.equals("") && actualBean.getFloatWrapperProperty() == null)
                        return;
                    else
                        throw new AssertionFailedError("Expected null but found " + actualBean.getFloatWrapperProperty());
                }
            }
            },
            {"{actionForm.intWrapperProperty}", "1234321", new Integer(1234321), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getIntWrapperProperty().intValue(),
                        ((Integer)expected).intValue());
                }
            }
            },
            {"{actionForm.intWrapperProperty}", "", null, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    if(expected.equals("") && actualBean.getIntWrapperProperty() == null)
                        return;
                    else
                        throw new AssertionFailedError("Expected null but found " + actualBean.getIntWrapperProperty());
                }
            }
            },
            // this test fails on jRockit because of precision differences in how the VMs execute
//        {"{actionForm.longWrapperProperty}", "12345678987654321L", new Long(12345678987654321L), new UpdateComparatorAdaptor()
//              {
//                  public void compareSimple(Object expected, SimpleTypeActionForm actualBean)
//                 {
//                     assertEquals(actualBean.getLongWrapperProperty().longValue(), 
//                                  ((Long)expected).longValue());
//                 }
//             }
//         },
            {"{actionForm.longWrapperProperty}", "", null, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    if(expected.equals("") && actualBean.getLongWrapperProperty() == null)
                        return;
                    else
                        throw new AssertionFailedError("Expected null but found " + actualBean.getLongWrapperProperty());
                }
            }
            },
            {"{actionForm.shortWrapperProperty}", "32766", new Short((short)32766), new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    assertEquals(actualBean.getShortWrapperProperty().shortValue(),
                        ((Short)expected).shortValue());
                }
            }
            },
            {"{actionForm.shortWrapperProperty}", "", null, new UpdateComparatorAdaptor() {
                public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                    if(expected.equals("") && actualBean.getShortWrapperProperty() == null)
                        return;
                    else
                        throw new AssertionFailedError("Expected null but found " + actualBean.getShortWrapperProperty());
                }
            }
            }
        };

    private Object[][] arrayUpdateData =
        {
            {"{actionForm.stringArrayProperty}",
             new String[]{"One", "Two", "Three", "Four", "Five"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getStringArrayProperty());
                 }
             }
            },
            {"{actionForm.stringArrayProperty}",
             new String[0],
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getStringArrayProperty());
                 }
             }
            },
            {"{actionForm.stringArrayProperty}",
             null,
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     assertNull(actualBean.getStringArrayProperty());
                 }
             }
            },
            {"{actionForm.stringArrayProperty}",
             new String[]{""},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, ((SimpleTypeActionForm)getActionForm()).getStringArrayProperty());
                 }
             }
            },
            {"{actionForm.stringArrayProperty}",
             "singleString",
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     assertEquals("singleString", ((SimpleTypeActionForm)getActionForm()).getStringArrayProperty()[0]);
                 }
             }
            },
            // expression, update values, compare values,
            {"{actionForm.boolArrayProperty}",
             new String[]{"true", "false", "false", "true"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getBoolArrayProperty());
                 }
             }
            },
            {"{actionForm.boolArrayProperty}",
             EMPTY_ARRAY,
             new String[]{"false", "false", "false"},
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getBoolArrayProperty());
                 }
             }
            },
            {"{actionForm.byteArrayProperty}",
             new String[]{"-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getByteArrayProperty());
                 }
             }
            },
            {"{actionForm.byteArrayProperty}",
             EMPTY_ARRAY,
             new String[]{"0", "0", "0"},
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getByteArrayProperty());
                 }
             }
            },
            {"{actionForm.charArrayProperty}",
             new String[]{"a", "b", "c", "d"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getCharArrayProperty());
                 }
             }
            },
            {"{actionForm.doubleArrayProperty}",
             new String[]{"3.1415", "2.718281828"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getDoubleArrayProperty());
                 }
             }
            },
            {"{actionForm.floatArrayProperty}",
             new String[]{"3.1415", "2.718281828"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getFloatArrayProperty());
                 }
             }
            },
            {"{actionForm.floatArrayProperty}",
             // the f's in the below make a big difference in JavaScript; they're not supported.  :)
             new String[]{"3.1415f", "2.718281828f"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getFloatArrayProperty());
                 }
             }
            },
            {"{actionForm.intArrayProperty}",
             new String[]{"123", "456", "789"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getIntArrayProperty());
                 }
             }
            },
            {"{actionForm.intArrayProperty}",
             null,
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     assertNull(actualBean.getIntArrayProperty());
                 }
             }
            },
            {"{actionForm.longArrayProperty}",
             new String[]{"-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getLongArrayProperty());
                 }
             }
            },
            {"{actionForm.shortArrayProperty}",
             new String[]{"-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getShortArrayProperty());
                 }
             }
            },

            //
            // Primitive Wrapper Type Array updates
            //

            // expression, update values, compare values,
            {"{actionForm.boolWrapperArrayProperty}",
             new String[]{"true", "false", "false", "true"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getBoolWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.boolWrapperArrayProperty}",
             EMPTY_ARRAY,
             new String[]{null, null, null},
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getBoolWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.byteWrapperArrayProperty}",
             new String[]{"-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getByteWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.byteWrapperArrayProperty}",
             EMPTY_ARRAY,
             new String[]{null, null, null},
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getByteWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.charWrapperArrayProperty}",
             new String[]{"a", "b", "c", "d"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getCharWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.doubleWrapperArrayProperty}",
             new String[]{"3.1415", "2.718281828"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getDoubleWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.floatWrapperArrayProperty}",
             new String[]{"3.1415", "2.718281828"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getFloatWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.floatWrapperArrayProperty}",
             // the f's in the below make a big difference in JavaScript; they're not supported.  :)
             new String[]{"3.1415f", "2.718281828f"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getFloatWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.intWrapperArrayProperty}",
             new String[]{"123", "456", "789"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getIntWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.longWrapperArrayProperty}",
             new String[]{"-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getLongWrapperArrayProperty());
                 }
             }
            },
            {"{actionForm.shortWrapperArrayProperty}",
             new String[]{"-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     AssertHelper.assertEquals((String[])expected, actualBean.getShortWrapperArrayProperty());
                 }
             }
            }
        };

    Object[][] indexedUpdateData =
        {
            {"{actionForm.intWrapperArrayProperty[4]}",
             new Integer(123),
             null,
             new UpdateComparatorAdaptor() {
                 public void compareSimple(Object expected, SimpleTypeActionForm actualBean) {
                     if(123 == actualBean.getIntWrapperArrayProperty()[4].intValue())
                         return;
                     else
                         throw new RuntimeException
                             ("Error testing indexed updates; received value \"" + actualBean.getIntWrapperArrayProperty()[4] + "\"; expected value 123", null);
                 }
             }
            },
        };

    Object[][] directListUpdate =
        {
            {"{actionForm.list}",
             new String[]{"foo", "bar", "baz", "blee"},
             null,
             new UpdateComparatorAdaptor() {
                 public void compareComplex(Object expected, ComplexTypeActionForm actualBean) {
                     List list = actualBean.getList();
//                  System.out.println("list: " + list);
//                  for(int i = 0; i < list.size(); i++)
//                  {
//                      System.out.println("list[" + i + "]: " + list.get(i));
//                  }

                     assertEquals("foo", list.get(list.size() - 4));
                     assertEquals("bar", list.get(list.size() - 3));
                     assertEquals("baz", list.get(list.size() - 2));
                     assertEquals("blee", list.get(list.size() - 1));
                 }
             }
            }
        };

    /* ===============================================================
     *
     * End Test Cases
     *
     * ===============================================================
     */
    private void executeSimpleTest(String testName, Object[][] data)
        throws ExpressionTestException {
        for(int i = 0; i < data.length; i++) {
            String expr = (String)data[i][0];
            Object updateData = data[i][1];
            Object updatedValue = (data[i][2] == null ? updateData : data[i][2]);
            UpdateComparator comparator = (UpdateComparator)data[i][3];

            // @TODO: plug-in all evaluation contexts here
            ServletRequest request = getPageContext().getRequest();
            ServletResponse response = getPageContext().getResponse();

            try {
                evaluateUpdateExpression(expr, updateData, request, response, getActionForm());

                SimpleTypeActionForm simpleBean = (SimpleTypeActionForm)getActionForm();

                comparator.compareSimple(updatedValue, simpleBean);
            } catch(Throwable e) {
                throw new ExpressionTestException("An error occurred executing " + testName + " [" + i + "] with expression '" + expr + "'", e);
            }
        }
    }

    private void executeComplexTest(String testName, Object[][] data)
        throws Exception {
        useForm(COMPLEX_FORM);

        for(int i = 0; i < data.length; i++) {
            String expr = (String)data[i][0];
            Object value = data[i][1];
            Object expected = data[i][2];
            UpdateComparator comparator = (UpdateComparator)data[i][3];

            try {
                evaluateUpdateExpression(expr, value, getRequest(), getResponse(), getActionForm());

                ComplexTypeActionForm complexBean = (ComplexTypeActionForm)getRequest().getAttribute("complexBean");

                comparator.compareComplex(expected, complexBean);
            } catch(Throwable t) {
                throw new ExpressionTestException("An error occurred executing " + testName + " [" + i + "] with expression '" + expr + "'", t);
            }
        }
    }

    public ExpressionUpdateTest(String name) {
        super(name);
    }

    /**
     * A protected interface that allows the result of an expression update to be compared
     * against an expected value.  This facilitates a cleaner and more extensible test
     * framework as there doesn't have to be code for each type of test.  A comparison
     * can be made simply by implementing this interface.
     */
    protected interface UpdateComparator {

        public void compareSimple(Object expected, SimpleTypeActionForm simpleBean);

        public void compareComplex(Object expected, ComplexTypeActionForm complexBean);
    }

    protected static class UpdateComparatorAdaptor
        implements UpdateComparator {

        public void compareSimple(Object expected, SimpleTypeActionForm simpleBean) {
        }

        public void compareComplex(Object expected, ComplexTypeActionForm complexBean) {
        }
    }
}
