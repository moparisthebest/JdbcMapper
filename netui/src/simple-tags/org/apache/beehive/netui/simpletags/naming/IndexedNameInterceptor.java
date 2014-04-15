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
package org.apache.beehive.netui.simpletags.naming;

// java imports

import org.apache.beehive.netui.script.Expression;
import org.apache.beehive.netui.script.ExpressionEvaluationException;
import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.ExpressionEvaluatorFactory;
import org.apache.beehive.netui.script.common.IDataAccessProvider;
import org.apache.beehive.netui.simpletags.core.Behavior;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.core.services.BehaviorStack;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.util.logging.Logger;

import java.util.List;

// external imports

/**
 * A {@link INameInterceptor} that is used to rewrite names which
 * reference the <code>container</code> databinding context.  This
 * INameInterceptor is for use by tags that render form-updatable HTML
 * elements.  If the dataSource attribute of the tag references a
 * <code>container</code> binding context, the name must be qualified
 * into a real path down a bean / property hierarchy in order to
 * correctly update that value on a POST.  This INameInterceptor
 * rewrites that name using the given name and the hierarchy of
 * {@link org.apache.beehive.netui.script.common.IDataAccessProvider} tags in a JSP page.
 */
public class IndexedNameInterceptor
        implements INameInterceptor
{
    private static final Logger _logger = Logger.getInstance(IndexedNameInterceptor.class);

    /**
     * Rewrite an expression into a fully-qualified reference to a specific JavaBean property
     * on an object.
     * @param name       the expression to rewrite
     * @param currentTag the current JSP tag that can be used as the leaf for walking up
     *                   to find parent tags that provide information used to
     *                   rewrite the expression.
     */
    public final String rewriteName(String name, Behavior currentTag)
            throws ExpressionEvaluationException
    {
        if (_logger.isDebugEnabled()) _logger.debug("rewrite expression \"" + name + "\"");

        IDataAccessProvider dap = getCurrentProvider(currentTag);
        // if the DAP is null, there is no rewriting to do
        if (dap == null)
            return name;

        // given a hierarchy of "container.container.container.item.someProp", the correct parent needs
        // to be found so that expression rewriting can happen correctly.
        //
        // ensure that this expression contains container.item
        Expression parsed = getExpressionEvaluator().parseExpression(name);
        assert parsed != null;

        int containerCount = 0;
        List tokens = parsed.getTokens();
        for (int i = 0; i < tokens.size(); i++) {
            String tok = tokens.get(i).toString();
            if (i == 0) {
                if (!tok.equals("container"))
                    break;
                else
                    continue;
            }
            // this skips the "current" IDataAccessProvider
            else if (tok.equals("container"))
                containerCount++;
            else if (tok.equals("item"))
                break;
        }

        if (_logger.isDebugEnabled()) _logger.debug("container parent count: " + containerCount);

        // now walk up the DataAccessProvier hierarchy until the top-most parent is found
        // the top-most parent is the first one that does not reference "container.item" but
        // is bound directly to a specific object such as "actionForm" or "pageFlow".  This 
        // handles the case where a set of nested IDataAccessProvider tags are "skipped" by
        // an expression like "container.container.container.item.foo".  In order to find
        // the correct root to start rewriting the names, one needs to walk up three 
        // DAPs in order to find the correct root from which to start. 
        //
        // In general, containerCount is zero here for the "container.item.foo" case.
        for (int i = 0; i < containerCount; i++) {
            dap = dap.getProviderParent();
        }

        // now, the top-most DAP parent is known
        assert dap != null;
        
        // strip off the "container.item" from the expression that is being rewritten
        // this should be two tokens into the expression.
        if (containerCount > 0) {
            name = parsed.getExpression(containerCount);
        }

        // now, change the binding context of the parent DAP hierarchy to create a 
        // String that looks like "actionForm.customers[42].order[12].lineItem[2].name"
        // note, this is done without using the expression that was passed-in and 
        // is derived entirely from the IDataAccessProvider parent hierarchy.
        String parentNames = rewriteNameInternal(dap);

        if (_logger.isDebugEnabled()) _logger.debug("name hierarchy: " + parentNames + " name: " + name);

        // with a newly re-written expression prefix, substitute this fully-qualified binding
        // string into the given expression for "container.item".
        String newName = changeContext(name, "container.item", parentNames, dap.getCurrentIndex());

        if (_logger.isDebugEnabled()) _logger.debug("rewrittenName: " + newName);

        return newName;
    }

    /**
     * A default method to find the "current" IDataAccessProvider.  This method is
     * left as non-final so that the implementation here can be tested
     * outside of a servlet container.
     */
    protected IDataAccessProvider getCurrentProvider(Behavior tag)
    {
        TagContext tagCtxt = ContextUtils.getTagContext();
        BehaviorStack stack = tagCtxt.getBehaviorStack();
        return (IDataAccessProvider) stack.findAncestorWithClass(tag, IDataAccessProvider.class);
    }

    /**
     * Rewrite a parent IDataAccessProvider's dataSource to be fully qualified.
     *
     * "container.container.container.container.item.foo" -> "DS1.DS2.DS3.DS4.foo"
     */
    private final String rewriteNameInternal(IDataAccessProvider dap)
            throws ExpressionEvaluationException
    {
        if (_logger.isDebugEnabled())
            _logger.debug("assign index to name: " + dap.getDataSource());

        Expression parsedDataSource = getExpressionEvaluator().parseExpression(dap.getDataSource());
        assert parsedDataSource != null;

        boolean isContainerBound = (parsedDataSource.getTokens().get(0)).toString().equals("container");

        // rewrite the name of the current IDataAccessProvider.
        String parentName = null;
        // if the current DAP has a parent IDataAccessProvider, rewrite the name of the parent
        if (dap.getProviderParent() != null)
            parentName = rewriteNameInternal(dap.getProviderParent());
        // if the current DAP has no parent, or it does not reference the "container." binding context,
        // we've found the "root" IDataAccessProvider
        else if (dap.getProviderParent() == null || (dap.getProviderParent() != null && !isContainerBound)) {
            return dap.getDataSource();
        }

        // now, we've found the root and can start rewriting the expressions throughout 
        // the rest of the DAP hierarchy
        if (_logger.isDebugEnabled()) {
            _logger.debug("changeContext: DAP.dataSource=" + dap.getDataSource() + " oldContext=container newContext=" +
                    parentName + " currentIndex=" + dap.getProviderParent().getCurrentIndex() +
                    " parentName is container: " + isContainerBound);
        }

        String retVal = null;
        String ds = dap.getDataSource();

        // If the current DAP's dataSource is "container.item", the binding context needs to change to that
        // of the parent.  This case should only occur for the last token -- the "name" passed into
        // the method.  Oterwise, just replace the "container" to that of the parent.  Both are 
        // qualified with the DAP's current index so that "actionForm.customers" becomes 
        // "actionForm.customers[12]".  

        boolean isContainerItemBound = false;
        if (isContainerBound && (parsedDataSource.getTokens().get(1)).toString().equals("item"))
            isContainerItemBound = true;

        if (isContainerItemBound)
            retVal = changeContext(ds, "container.item", parentName, dap.getProviderParent().getCurrentIndex());
        else
            retVal = changeContext(ds, "container", parentName, dap.getProviderParent().getCurrentIndex());

        if (_logger.isDebugEnabled()) _logger.debug("fully-qualified binding expression: \"" + retVal + "\"");

        return retVal;
    }

    protected ExpressionEvaluator getExpressionEvaluator()
    {
        return ExpressionEvaluatorFactory.getInstance();
    }

    private final String changeContext(String dataSource, String oldContext, String newContext, int index)
            throws ExpressionEvaluationException
    {
        try {
            return getExpressionEvaluator().changeContext(dataSource, oldContext, newContext, index);
        }
        catch (ExpressionEvaluationException ee) {
            if (_logger.isErrorEnabled())
                _logger.error("An error occurred changing the binding context of the expression \"" +
                        dataSource + "\".  Cause: " + ee, ee);

            throw ee;
        }
    }
}
