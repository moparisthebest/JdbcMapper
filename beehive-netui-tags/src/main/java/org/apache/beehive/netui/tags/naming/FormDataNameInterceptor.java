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
package org.apache.beehive.netui.tags.naming;

import org.apache.beehive.netui.script.ExpressionEvaluationException;
import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.ExpressionEvaluatorFactory;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.jsp.tagext.Tag;

/**
 * A {@link INameInterceptor} that qualifies all non-expression attributes
 * that are expected to be expressions into valid
 * expressions.  This conversion is for Struts compatability;
 * In Struts, the "property" property is used to specify which
 * property on the action form should be rendered in a tag's
 * HTML.  These attributes are converted into expressions
 * by qualifying them into the <code>actionForm</code> binding
 * context.
 * <br/>
 * <br/>
 * For example, the <code>dataSource</code> attribute on a text box
 * tag would be qualified into the <code>actionForm</code> context
 * if the attribute was not an expression.
 */
public class FormDataNameInterceptor
        implements INameInterceptor
{
    private static final Logger logger = Logger.getInstance(FormDataNameInterceptor.class);

    /**
     * Qualify the name of a NetUI JSP tag into the "actionForm" data binding context.
     * This feature is used to convert non-expression tag names, as those used in Struts,
     * into actionForm expressions that NetUI consumes.
     * @param name the name to qualify into the actionForm binding context.  If this is "foo", the returned value
     *             is {actionForm.foo}
     * @return the qualified name or <code>null</code> if an error occurred
     */
    public String rewriteName(String name, Tag currentTag)
            throws ExpressionEvaluationException
    {
        ExpressionEvaluator eval = ExpressionEvaluatorFactory.getInstance();

        try {
            if (!eval.isExpression(name))
                return eval.qualify("actionForm", name);
            return name;
        }
        catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("Could not qualify name \"" + name + "\" into the actionForm binding context.", e);

            // return the Struts name.  This should cause regular Struts databinding to execute so this property will
            // be updated anyway.
            return name;
        }
    }
}
