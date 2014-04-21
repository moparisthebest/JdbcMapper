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
package org.apache.beehive.netui.tags.databinding.script;

import javax.servlet.jsp.JspException;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * <p>
 * This tag evaluates an expression and places the result in the
 * {@link javax.servlet.jsp.PageContext} object, where the data is available to the JSP EL and JSP scriptlet.
 * This tag can be used to extract data from forms, Controller files, and any data binding context and
 * make it available to scriptlets.
 * </p>
 * <p>
 * In the following example, the getData tag gets the value of a property in the page flow and
 * makes it available to the JSP via the JSP EL implicit object <code>${pageScope}</code>.
 * <pre>
 *     &lt;netui-data:getData resultId="myData" value="${pageFlow.myData}"/>
 * </pre>
 * <p>
 * The following scriptlet extracts the data from the <code>PageContext</code> object and writes it to
 * the rendered HTML: <br/>
 * <pre>
 *     ${pageScope.myData}
 * </pre>
 * </p>
 * In this first example, the &lt;netui-data:getData> tag loads data into the {@link javax.servlet.jsp.PageContext}'s
 * attribute map.  It can then be accessed using the {@link javax.servlet.jsp.PageContext#getAttribute(String)} method.
 * <pre>
 *     &lt;netui:form action="lastNameAction" focus="lastname"&gt;
 *         ...
 *         &lt;netui-data:getData resultId="first" value="${actionForm.firstname}"/&gt;
 *         ...
 *         &lt;%
 *             String firstName = (String)pageContext.getAttribute("first");
 *             System.out.println("First Name = " + firstName);
 *             ...
 *         %&gt;
 *         ...
 *     &lt;/netui:form&gt;</pre>
 * <p/>
 * <p>
 * This example shows how to use &lt;netui-data:getData&gt; and the <code>PageContext</code> inside of other
 * containers, in this case a &lt;netui-data:repeater> tag. The &lt;netui-data:getData> below extracts each
 * element as the &lt;netui-data:repeater> iterates over the data set and writes it to the Java console:
 * <br/>
 * <pre>    &lt;netui-data:repeater dataSource="pageFlow.strArr"&gt;
 *         ...
 *         &lt;netui-data:repeaterItem&gt;
 *             &lt;netui:span value="${container.item}" /&gt;
 *             &lt;netui-data:getData resultId="item" value="${container.item}"/&gt;
 *             &lt;%
 *                 String currentItem = (String) pageContext.getAttribute("item");
 *                 System.out.println(currentItem);
 *                 ...
 *             %&gt;
 *          &lt;/netui-data:repeaterItem&gt;
 *          ...
 *      &lt;/netui-data:repeater&gt;
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * This tag evaluates an expression and places the result in the
 * {@link javax.servlet.jsp.PageContext} object, where the data is available to the JSP EL and JSP scriptlet.
 * This tag can be used to extract data from forms, Controller files, and any data binding context and
 * make it available to scriptlets.
 * </p>
 * <p>
 * In the following example, the getData tag gets the value of a property in the page flow and
 * makes it available to the JSP via the JSP EL implicit object <code>${pageScope}</code>.
 * <pre>
 *     &lt;netui-data:getData resultId="myData" value="${pageFlow.myData}"/>
 * </pre>
 * <p>
 * The following scriptlet extracts the data from the <code>PageContext</code> object and writes it to
 * the rendered HTML: <br/>
 * <pre>
 *     ${pageScope.myData}
 * </pre>
 *
 * @example
 * In this first example, the &lt;netui-data:getData> tag loads data into the {@link javax.servlet.jsp.PageContext}'s
 * attribute map.  It can then be accessed using the {@link javax.servlet.jsp.PageContext#getAttribute(String)} method.
 * <pre>
 *     &lt;netui:form action="lastNameAction" focus="lastname"&gt;
 *         ...
 *         &lt;netui-data:getData resultId="first" value="${actionForm.firstname}"/&gt;
 *         ...
 *         &lt;%
 *             String firstName = (String)pageContext.getAttribute("first");
 *             System.out.println("First Name = " + firstName);
 *             ...
 *         %&gt;
 *         ...
 *     &lt;/netui:form&gt;</pre>
 * <p/>
 * <p>
 * This example shows how to use &lt;netui-data:getData&gt; and the <code>PageContext</code> inside of other
 * containers, in this case a &lt;netui-data:repeater> tag. The &lt;netui-data:getData> below extracts each
 * element as the &lt;netui-data:repeater> iterates over the data set and writes it to the Java console:
 * <br/>
 * <pre>    &lt;netui-data:repeater dataSource="pageFlow.strArr"&gt;
 *         ...
 *         &lt;netui-data:repeaterItem&gt;
 *             &lt;netui:span value="${container.item}" /&gt;
 *             &lt;netui-data:getData resultId="item" value="${container.item}"/&gt;
 *             &lt;%
 *                 String currentItem = (String) pageContext.getAttribute("item");
 *                 System.out.println(currentItem);
 *                 ...
 *             %&gt;
 *          &lt;/netui-data:repeaterItem&gt;
 *          ...
 *      &lt;/netui-data:repeater&gt;
 * </pre>
 * </p>
 * @netui:tag name="getData"
 *            description="Evaluates an expression and places the result in the JSP's PageContext. Can be used to extract objects from forms, page flows, and other objects that can be databound.  You can then write a scriplet to access the data by using the getAttribute method of javax.servlet.jsp.PageContext."
 * @deprecated The JSP expression language should be used instead. 
 */
public class GetData
        extends AbstractClassicTag {

    private static final Logger LOGGER = Logger.getInstance(GetData.class);

    private Object _value = null;
    private String _resultId = null;

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "GetData";
    }

    /**
     * The data binding expression to evaluate. The result will be stored in the {@link javax.servlet.jsp.PageContext}
     * object as specified in the <code>resultId</code> attribute.
     *
     * @param value the expression to evaluate
     * @jsptagref.attributedescription
     * The data binding expression to evaluate. The result will be stored in the {@link javax.servlet.jsp.PageContext}
     * object as specified in the <code>resultId</code> attribute.
     * @jsptagref.attributesyntaxvalue <i>expression_value</i>
     * @netui:attribute required="true" rtexprvalue="true"
     */
    public void setValue(Object value) {
        _value = value;
    }

    /**
     * Set the String attribute name under which the result of evaluating an expression will be stored in
     * the {@link javax.servlet.jsp.PageContext}..
     *
     * @param resultId the String key
     * @jsptagref.attributedescription
     * Specifies the property of the PageContext object where the data will be stored.
     * @jsptagref.attributesyntaxvalue <i>string_resultId</i>
     * @netui:attribute required="true"
     */
    public void setResultId(String resultId) {
        _resultId = resultId;
    }

    /**
     * Start the tag evaluation.  This tag ignores its body content.
     *
     * @return {@link #SKIP_BODY}
     */
    public int doStartTag() {
        return SKIP_BODY;
    }

    /**
     * Evaluate the expression at the <code>value</code> attribute and store the result in the
     * {@link javax.servlet.jsp.PageContext} under the attribute key specified in {@link #setResultId(String)}.
     * If an existing key in the PageContext's attribute map exists, a warning will be written to the log file.
     * If errors occur during expression evaluation, they will be reported in the JSP page.  If the value
     * returned by the expression is null, an attribute named <code>resultId</code> will be removed from
     * the PageContext's attribute map.
     *
     * @return {@link #EVAL_PAGE}
     */
    public int doEndTag()
            throws JspException {
        if(_value != null) {
            if(LOGGER.isInfoEnabled() && pageContext.getAttribute(_resultId) != null)
                LOGGER.info("Overwriting a value in PageContext attribute map with key \"" + _resultId +
                        "\" and object of type \"" + _value.getClass().getName());

            pageContext.setAttribute(_resultId, _value);
        }
        else {
            if(LOGGER.isInfoEnabled())
                LOGGER.info("Removing a value from the PageContext attribute map with key \"" + _resultId +
                        "\".  The object returned by the expression is null.");

            pageContext.removeAttribute(_resultId);
        }

        localRelease();
        return EVAL_PAGE;
    }

    /**
     * Reset all of the fields of this tag.
     */
    protected void localRelease() {
        super.localRelease();
        _resultId = null;
        _value = null;
    }
}
