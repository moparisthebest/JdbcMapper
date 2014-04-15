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
package org.apache.beehive.netui.simpletags.core;

import org.apache.beehive.netui.pageflow.PageFlowContext;
import org.apache.beehive.netui.script.ExpressionEvaluationException;
import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.ExpressionEvaluatorFactory;
import org.apache.beehive.netui.script.ExpressionUpdateException;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.jsp.el.VariableResolver;

public class ExpressionHandling
{
    private static final Logger logger = Logger.getInstance(ExpressionHandling.class);

    private ExpressionEvaluator ee = null;      // cache the expression evaluator
    private Behavior _tag;
    private VariableResolver _vr;

    public ExpressionHandling(Behavior tag, VariableResolver vr)
    {
        _tag = tag;
        _vr = vr;
    }

    /**
     * An internal method that is used for evaluating the <code>dataSource</code>
     * attribute.  The <code>dataSource</code> attribute is handled specially
     * becuase it must be of a particular format in order to be usable by
     * NetUI tags.  This requirement exists in order to facilitate
     * round-tripping the <code>dataSource</code> attribute as the
     * <code>name</code> attribute of HTML tags.  Upon a POST, the <code>name</code>
     * attribute is used as an l-value for an update expression in order
     * to push any POST-ed data back into the bean from whence it came.
     */
    /**
     * Ensure that the passed in data source is a valid expression.
     * @param dataSource
     * @param attrName
     * @param errorId
     * @return String
     */
    public String ensureValidExpression(String dataSource, String attrName, String errorId)
    {
        try {
            boolean isExpr = isExpression(dataSource);

            // @perf: if the isExpr call fails, this is an error condition, and the containsExpression
            // call cost is irrelevant
            if (!isExpr && containsExpression(dataSource)) {
                String s = Bundle.getString(errorId, new Object[]{dataSource});
                _tag.registerTagError(s, null);
                return null;
            }

            if (!isExpr) {
                String s = Bundle.getString(errorId, new Object[]{dataSource});
                _tag.registerTagError(s, null);
                return null;
            }
        }
        catch (Exception e) {
            // pass throw JspExceptions
            //if (e instanceof JspException)
            //    throw (JspException) e;

            String s = Bundle.getString(errorId, new Object[]{dataSource});
            _tag.registerTagError(s, e);
            return null;
        }
        return dataSource;
    }

    /**
     * @param expression
     * @param attrName
     * @return Object
     */
    public Object evaluateExpression(String expression, String attrName)
    {
        return evaluateExpressionInternal(expression, attrName);
    }

    /**
     * This method will update the object identified by the <code>expr</code> parameter with
     * the value.  If the
     * @param expr
     * @param value
     * @throws org.apache.beehive.netui.script.ExpressionUpdateException
     *
     */
    public void updateExpression(String expr, Object value)
            throws ExpressionUpdateException
    {
        if (isExpression(expr)) {

            PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
            VariableResolver vr = ImplicitObjectUtil.getUpdateVariableResolver(pfCtxt.getRequest(), pfCtxt.getResponse(), false);
            ExpressionEvaluatorFactory.getInstance().update(expr, value, vr, false);
        }
        else {
            String s = Bundle.getString("Tags_BindingUpdateExpressionError", new Object[]{expr});
            _tag.registerTagError(s, null);
        }
    }

    /**
     * Return a boolean indicating if the string contains an expression or not.
     * @param expression a <code>String</code> that may or may not contain an expresion.
     * @return <code>true</code> if the string contains an expression.
     */
    private boolean containsExpression(String expression)
    {
        // this shouldn't happen because we have checked in isExpression that the expression isn't null
        assert (expression != null) : "The parameter expression must not be null.";
        return getExpressionEvaluator().containsExpression(expression);
    }

    /**
     */
    private boolean isExpression(String expression)
    {
        if (expression == null)
            return false;
        return getExpressionEvaluator().isExpression(expression);
    }

    /**
     * This is the real implementation of evaluateExpression.
     * @param expression
     * @param attrName
     * @return The object that is the result of the expression evalution
     */
    private Object evaluateExpressionInternal(String expression, String attrName)
    {
        if (logger.isDebugEnabled()) logger.debug("evaluate expression=\"" + expression + "\"");

        Object result = null;

        try {
            result = getExpressionEvaluator().evaluateStrict(expression, _vr);
        }
        catch (ExpressionEvaluationException ee) {
            // if there is an expression evaluation error set the error and
            // return null;

            if (logger.isWarnEnabled())
                logger.warn(Bundle.getString("Tags_ExpressionEvaluationFailure", expression));

            // create the expression info an add it to the error tracking
            EvalErrorInfo info = new EvalErrorInfo();
            info.evalExcp = ee;
            info.expression = expression;
            info.attr = attrName;
            info.tagType = _tag.getTagName();

            // report the error
            _tag.registerTagError(info);
            return null;
        }
        catch (Exception e) {
            String s = Bundle.getString("Tags_ExpressionEvaluationException", new Object[]{expression, e.toString()});
            _tag.registerTagError(s, e);
            return null;
        }

        if (logger.isDebugEnabled()) logger.debug("resulting object: " + result);
        return result;
    }

    /**
     * Return a cached instance of an <code>ExpressionEvaluator</code>.  This will be cached by the
     * tag and release during <code>localRelease</code>.
     * @return the <code>ExpressionEvalutor</code> for tis tag.
     */
    private final ExpressionEvaluator getExpressionEvaluator()
    {
        if (ee == null)
            ee = ExpressionEvaluatorFactory.getInstance();

        assert(ee != null);
        return ee;
    }
}
