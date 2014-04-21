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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * <p>
 * Calls methods on the current Page Flow.  If no Controller file is found, an
 * {@link ObjectNotFoundException} is thrown and the tag execution fails.
 * Any return value is stored in the <code>${pageScope...}</code> data binding context object under the
 * attribute specified by the <code>resultId</code> attribute.
 * </p>
 * <p>
 * For example, given a <code>hello</code> method and the following &lt;netui-data:callPageFlow> tag:
 * <pre>
 * &lt;netui-data:callPageFlow method="hello" resultId="helloMessage"/>
 * </pre>
 * <p>
 * the result of the call is stored in the <code>${pageScope}</code> JSP EL implicit object under
 * the attribute <code>helloMessage</code>.  It will be accessible via <code>${pageScope.helloMessage}</code> and
 * can be used as:
 * <pre>
 *     &lt;netui:span value="<b>${pageScope.helloMessage}</b>"/>
 * </pre>
 * </p>
 * <p>
 * In JSP scriptlet, the result can be retrieved by calling the <code>getAttribute()</code> method on the
 * {@link javax.servlet.jsp.PageContext javax.servlet.jsp.PageContext} object:
 * <pre>
 *     &lt;%= pageContext.getAttribute("helloMessage") %>
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription
 * Calls methods on the current Page Flow.  If no Controller file is found, an
 * {@link ObjectNotFoundException} is thrown and the tag execution fails.
 * Any return value is stored in the <code>${pageScope...}</code> data binding context object under the
 * attribute specified by the <code>resultId</code> attribute.
 * <p/>
 * <p>
 * For example, given a <code>hello</code> method and the following &lt;netui-data:callPageFlow> tag:
 * <pre>
 * &lt;netui-data:callPageFlow method="hello" resultId="helloMessage"/>
 * </pre>
 * <p>
 * the result of the call is stored in the <code>${pageScope}</code> JSP EL implicit object under
 * the attribute <code>helloMessage</code>.  It will be accessible via <code>${pageScope.helloMessage}</code> and
 * can be used as:
 * <pre>
 *     &lt;netui:span value="<b>${pageScope.helloMessage}</b>"/>
 * </pre>
 * </p>
 * <p>
 * In JSP scriptlet, the result can be retrieved by calling the <code>getAttribute()</code> method on the
 * {@link javax.servlet.jsp.PageContext javax.servlet.jsp.PageContext} object:
 * <pre>
 *     &lt;%= pageContext.getAttribute("helloMessage") %>
 * </pre>
 * </p>
 * @example
 * In the following sample, the &lt;netui-data:callPageFlow> tag calls the
 * sumCartItems method on the Controller file.  The {@link org.apache.beehive.netui.tags.html.Span} tag
 * accesses the result through the ${pageScope} data binding context.
 * <pre>
 *      &lt;netui-data:callPageFlow method="sumCartItems" resultId="cartSum">
 *          &lt;netui-data:methodParameter value="${pageFlow.cart.lineItemList}"/>
 *      &lt;/netui-data:callPageFlow>
 *      ...
 *      &lt;netui:span value="${pageScope.cartSum}"/>
 * </pre>
 * @netui:tag name="callPageFlow" deprecated="true"
 *            description="Use this tag to call a method on the current page flow controller."
 */
public class CallPageFlow
    extends CallMethod {

    private static final Logger LOGGER = Logger.getInstance(CallPageFlow.class);
    private static final String DEFAULT_OBJECT_NAME = Bundle.getString("Tags_CallPageFlow_defaultObjectName");

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "CallPageFlow";
    }

    /**
     * Get the name of the object that is the target of the invocation.
     *
     * @return a name for the object on which the method will be invoked.
     */
    protected String getObjectName() {
        return DEFAULT_OBJECT_NAME;
    }

    /**
     * Get the PageFlow for the using JSP's directory.  This is an implementation of the
     * {@link CallMethod#resolveObject()} method that finds the current PageFlow
     * using the {@link PageFlowUtils#getCurrentPageFlow} method.
     *
     * @return the current PageFlow.  If there is no current PageFlow, the {@link ObjectNotFoundException} will
     *         be thrown.
     * @throws ObjectNotFoundException when an exception occurs ensuring that a Page Flow is created.
     */
    protected Object resolveObject()
        throws ObjectNotFoundException {
        try {
            /* todo: because of a javac bug (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5090006), we
                    can't call PageFlowUtils.getCurrentPageFlow here; thus, the call to getCurrentActionResolver.
             */
            Object jpf = PageFlowUtils.getCurrentPageFlow((HttpServletRequest)pageContext.getRequest(), pageContext.getServletContext());

            if(LOGGER.isDebugEnabled())
                LOGGER.debug("Found a pageflow of type: " + (jpf != null ? jpf.getClass().getName() : "null"));

            return jpf;
        }
        catch(Exception e) {
            throw new ObjectNotFoundException(Bundle.getErrorString("Tags_CallPageFlow_noPageFlow"), e);
        }
    }
}
