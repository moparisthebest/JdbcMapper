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
package org.apache.beehive.netui.tags.databinding.invoke;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * A tag that is used to add an argument to a method that will be called
 * on some object. This tag can be nested within tags that extend the AbstractCallMethod
 * class. Those tags are:
 * <ul>
 * <li>{@link CallMethod}</li>
 * <li>{@link CallPageFlow}</li>
 * </ul>
 * </p>
 * <p>
 * The {@link MethodParameter} tags are used to parameterize the method that the {@link AbstractCallMethod} class
 * will call; each <code>methodParameter</code> tag represents a single parameter. These tags are evaluated
 * in order and the parameters they describe are passed in order.
 * </p>
 * <p>
 * Overloaded methods on an object can be invoked by setting the <code>type</code> attribute on each
 * <code>methodParameter</code> tag that is embedded in a method invocation tag.  The type name must
 * exactly match the primitive type name or the fully qualified class name of the argument.  The
 * <code>methodParameter</code> tags must also be in the order that they will be passed to this method.
 * The value of the type attribute must be an exact match of the type if it were printed after having been
 * accessed through Java reflection.
 * </p>
 * <p>
 * In order to pass <code>null</code> as an argument to a method, the null attribute must be set on this tag.
 * Either the null attribute or the value attribute must be set on this tag.
 * </p>
 * <p>
 * This example shows how to pass parameters to the method call <code>foo(int integer, String string)</code>.
 * <pre>
 *    &lt;netui-data:methodParamter value="42"/&gt;
 *    &lt;netui-data:methodParamter null="true"/&gt;
 * </pre>
 * This will correspond to the method call:<br/>
 * <pre>
 *     foo(42, null);
 * </pre>
 * The following sample shows how to pass parameters to the method call <code>foo(int integer, String string)</code>
 * where the class has both of the methods <code>foo(int integer, String string)</code> and
 * <code>foo(Integer integer, String string)</code>.
 * <pre>
 *     &lt;netui-data:methodParamter type="int" value="42"/&gt;
 *     &lt;netui-data:methodParamter type="java.lang.String" null="true"/&gt;
 * </pre>
 * This will correspond to the method call:<br/>
 * <pre>
 *     foo(42, null);
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * A tag that is used to add an argument to a method that will be called
 * on some object. This tag can be nested within tags that extend the AbstractCallMethod
 * class. Those tags are:
 * <ul>
 * <li>{@link CallMethod}</li>
 * <li>{@link CallPageFlow}</li>
 * </ul>
 * </p>
 * <p>
 * The {@link MethodParameter} tags are used to parameterize the method that the {@link AbstractCallMethod} class
 * will call; each <code>methodParameter</code> tag represents a single parameter. These tags are evaluated
 * in order and the parameters they describe are passed in order.
 * </p>
 * <p>
 * Overloaded methods on an object can be invoked by setting the <code>type</code> attribute on each
 * <code>methodParameter</code> tag that is embedded in a method invocation tag.  The type name must
 * exactly match the primitive type name or the fully qualified class name of the argument.  The
 * <code>methodParameter</code> tags must also be in the order that they will be passed to this method.
 * The value of the type attribute must be an exact match of the type if it were printed after having been
 * accessed through Java reflection.
 * </p>
 * <p>
 * In order to pass <code>null</code> as an argument to a method, the null attribute must be set on this tag.
 * Either the null attribute or the value attribute must be set on this tag.
 * </p>
 *
 * @example
 * <p>
 * This example shows how to pass parameters to the method call <code>foo(int integer, String string)</code>.
 * <pre>
 *    &lt;netui-data:methodParamter value="42"/&gt;
 *    &lt;netui-data:methodParamter null="true"/&gt;
 * </pre>
 * This will correspond to the method call:<br/>
 * <pre>
 *     foo(42, null);
 * </pre>
 * The following sample shows how to pass parameters to the method call <code>foo(int integer, String string)</code>
 * where the class has both of the methods <code>foo(int integer, String string)</code> and
 * <code>foo(Integer integer, String string)</code>.
 * <pre>
 *     &lt;netui-data:methodParamter type="int" value="42"/&gt;
 *     &lt;netui-data:methodParamter type="java.lang.String" null="true"/&gt;
 * </pre>
 * This will correspond to the method call:<br/>
 * <pre>
 *     foo(42, null);
 * </pre>
 * </p>
 *
 * @netui:tag name="methodParameter"
 *            description="Use this tag to add an argument to a method that will be called on some object."
 */
public class MethodParameter
        extends AbstractClassicTag {

    /**
     * An identifier denoting that the value of this method parameter
     * should be treated as 'null'.
     */
    public static final Integer NULL_ARG = new Integer(-1);

    private boolean _isNull = false;
    private Object _value = null;
    private String _type = null;

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "MethodParamter";
    }

    /**
     * <p/>
     * Set a String matching the type of this parameter on the method to invoke.
     * </p>
     * <p/>
     * This name should match the primitive type name or fully qualified class
     * name of the parameters on the signature of the method to which this
     * parameter will be passed.
     * </p>
     * <p/>
     * For example:
     * <table>
     * <tr><td>Method Signature</td><td>Argument Name</td><td>Type value</td></tr>
     * <tr><td>addToPrice(int price)</td><td>price</td><td><code>int</code></td></tr>
     * <tr><td>addToPrice(Integer price)</td><td>price</td><td><code>java.lang.Integer</code></td></tr>
     * </table>
     *
     * @param type the type name
     * @jsptagref.attributedescription <p>
     * Set a String matching the type of this parameter on the method to invoke.
     * </p>
     * <p/>
     * This name should match the primitive type name or fully qualified class
     * name of the parameters on the signature of the method to which this
     * parameter will be passed.
     * </p>
     * <p/>
     * For example:
     * <table border='1'>
     * <tr><td><b>Method Signature</b></td><td><b>Argument Name</b></td><td><b>Type value</b></td></tr>
     * <tr><td>addToPrice(int price)</td><td>price</td><td><code>int</code></td></tr>
     * <tr><td>addToPrice(Integer price)</td><td>price</td><td><code>java.lang.Integer</code></td></tr>
     * </table>
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="false"
     */
    public void setType(String type) {
        _type = type;
    }

    /**
     * Sets the value of the method parameter that will be passed
     * to the method call.  This String can be an expression.
     * If the value is not an expression that references
     * an Ojbect, the {@link AbstractCallMethod#doEndTag()} will attempt to convert
     * the String to type that matches the position of the MethodParameter
     * tag in the list of MethodParameter tags nested inside of an {@link AbstractCallMethod}
     * tag.
     *
     * @param value a String value which may be an expression
     * @jsptagref.attributedescription The value of the method parameter that will be passed to the method call.
     * @jsptagref.attributesyntaxvalue <i>expression_value</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setValue(Object value) {
        _value = value;
    }

    /**
     * Sets a boolean that describes that the parameter that should be passed
     * to the method is null.
     *
     * @param isNull a value that describes whether or not this tag should pass null; if
     *               <code>true</code> null will be passed; otherwise the value from the value attribute
     *               will be passed.
     * @jsptagref.attributedescription Boolean. Determines if the parameter passed to the method is null.
     * @jsptagref.attributesyntaxvalue <i>boolean_passNullValue</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setNull(boolean isNull) {
        _isNull = isNull;
    }

    /**
     * Start this tag's lifecycle.  Verify that this tag is nested within
     * a {@link AbstractCallMethod} tag and that one of the "null" and "value"
     * attributes are set.
     *
     * @return {@link #SKIP_BODY}
     * @throws JspException if an error occurs getting the parameter
     */
    public int doStartTag()
            throws JspException {
        Tag parent = getParent();
        if(parent == null || !(parent instanceof AbstractCallMethod)) {
            String msg = Bundle.getErrorString("Tags_MethodParameter_invalidParent");
            registerTagError(msg, null);
            reportErrors();
            return SKIP_BODY;
        }

        if(!_isNull && _value == null) {
            String msg = Bundle.getErrorString("Tags_MethodParameter_undefinedValue");
            registerTagError(msg, null);
            reportErrors();
            return SKIP_BODY;
        }

        return SKIP_BODY;
    }

    /**
     * Prepare the value to pass up to the {@link AbstractCallMethod} type
     * parent.
     *
     * @return {@link #EVAL_PAGE} to continue evaluating the page
     */
    public int doEndTag()
            throws JspException {

        if(hasErrors())
            reportErrors();
        else {
            AbstractCallMethod cm = (AbstractCallMethod)getParent();
            cm.addParameter(_type, _isNull ? null : _value);
        }

        localRelease();
        return EVAL_PAGE;
    }

    /**
     * Reset all of the fields of this tag.
     */
    protected void localRelease() {
        super.localRelease();
        _isNull = false;
        _value = null;
        _type = null;
    }
}
