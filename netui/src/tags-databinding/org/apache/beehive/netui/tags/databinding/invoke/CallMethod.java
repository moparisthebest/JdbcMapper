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

import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.internal.cache.MethodCache;

import javax.servlet.jsp.JspException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <p/>
 * An abstract base class for tags that are capable of reflectively
 * invoking methods.  Specializations of this tag provide method
 * implementations that locate the object on which to invoke the
 * method and that handle any return value from the invoked
 * method.
 * </p>
 * <p/>
 * The <code>CallMethod</code> tag can have child tags of type
 * {@link MethodParameter}; these tags must be in the same
 * order as the parameter list in the method signature of the
 * method that will be invoked.  To invoke an overloaded method, the
 * {@link MethodParameter#setType(String)} property must be set to the String
 * name of the type to pass to the method.  If the type attribute values
 * on nested {@link MethodParameter} tags do not match any method signature,
 * an error will be reported in the page.
 * </p>
 *
 * @jsptagref.tagdescription Calls methods on any Java classes.
 * <p>The <code>controlId</code> attribute is used to specify the cclass to be called.
 * The value returned is stored in the
 * <code>{pageContext...}</code> data binding context object under the
 * attribute specified by the <code>resultId</code>
 * attribute.
 * <p/>
 * <p>For example, if you call a Java class with the following &lt;netui-data:callMethod> tag...
 * <p/>
 * <pre>
 *     &lt;netui-data:callMethod object="${pageFlow}" method="hello" resultId="helloMessage"/>
 * </pre>
 * <p/>
 * <p>...the result of the call is stored in the <code>pageScope</code> data binding context under the
 * attribute <code>helloMessage</code>.
 * <p/>
 * <p>The result can be retrieved with the data binding expression
 * <code>${pageScope.helloMessage}</code>
 * <p/>
 * <pre>   &lt;netui:span value="<b>${pageScope.helloMessage}</b>"/></pre>
 * <p/>
 * In a scriptlet, the result can be retrieved by calling the <code>getAttribute()</code>
 * method on the
 * {@link javax.servlet.jsp.PageContext javax.servlet.jsp.PageContext} object:
 * <p/>
 * <pre>    &lt;%= pageContext.getAttribute("helloMessage") %></pre>
 * <p/>
 * @netui:tag name="callMethod" description="Use this tag to call a method on an object."
 * @see MethodParameter
 * @see CallPageFlow
 * @see javax.servlet.jsp.PageContext
 */
public class CallMethod
    extends AbstractCallMethod {

    private static final String DEFAULT_OBJECT_NAME = Bundle.getString("Tags_CallMethod_defaultObjectName");
    private static final MethodCache _cache = new MethodCache();

    private Object _object = null;

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "CallMethod";
    }

    /**
     * Set the object on which to invoke a method.
     *
     * @param object the object on which to invoke a method
     * @jsptagref.attributedescription A string or data binding expression that names the class on which to call a method.
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_object</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setObject(Object object) {
        _object = object;
    }

    /**
     * Reset all of the fields of this tag.
     */
    protected void localRelease() {
        super.localRelease();
        _object = null;
    }

    protected Object resolveObject()
        throws ObjectNotFoundException, JspException {
        return _object;
    }

    protected final Method findMethod(Object target, String methodName, boolean verifyTypes) {
        List params = getParameterNodes();

        if(verifyTypes) {
            String[] argTypes = new String[params.size()];
            for(int i = 0; i < argTypes.length; i++) {
                String typeName = ((ParamNode)params.get(i)).typeName;
                typeName = typeNameToClassName(typeName);
                argTypes[i] = typeName;
            }

            return _cache.getMethod(target.getClass(), methodName, argTypes);
        }
        else return _cache.getMethod(target.getClass(), methodName, params.size());
    }

    protected String getObjectName() {
        return DEFAULT_OBJECT_NAME;
    }

    private static String typeNameToClassName(String typeName) {
        if(typeName.endsWith("[]")) {
            int dims = countOccurrences(typeName, "[]");
            typeName = typeName.substring(0, typeName.indexOf("["));
            if(typeName.equals("boolean"))
                typeName = "Z";
            else if(typeName.equals("byte"))
            typeName = "B";
            else if(typeName.equals("char"))
            typeName = "C";
            else if(typeName.equals("double"))
                typeName = "D";
            else if(typeName.equals("float"))
                typeName = "F";
            else if(typeName.equals("int"))
                typeName = "I";
            else if(typeName.equals("long"))
                typeName = "J";
            else if(typeName.equals("short"))
                typeName = "S";
            else typeName = "L" + typeName + ";";

            for(int i = 0; i < dims; i++)
                typeName = "[" + typeName;
        }

        return typeName;
    }

    private static int countOccurrences(String typeName, String token) {
        int occurrences = 0;
        int index = -1;
        while((index = typeName.indexOf(token, index+token.length())) >= 0)
            occurrences++;
        return occurrences;
    }
}
