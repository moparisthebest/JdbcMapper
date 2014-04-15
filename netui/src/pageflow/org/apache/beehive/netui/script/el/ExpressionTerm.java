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
package org.apache.beehive.netui.script.el;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.beehive.netui.script.Expression;
import org.apache.beehive.netui.script.el.tokens.ArrayIndexToken;
import org.apache.beehive.netui.script.el.tokens.ContextToken;
import org.apache.beehive.netui.script.el.tokens.ExpressionToken;
import org.apache.beehive.netui.script.el.tokens.IdentifierToken;
import org.apache.beehive.netui.script.el.tokens.MapKeyToken;
import org.apache.beehive.netui.script.el.util.WrappedObject;
import org.apache.beehive.netui.script.el.util.ParseUtils;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * This class represents an executable expression that is evaluated against an object and can read or 
 * write properties, arrays, lists, and maps on that object.
 */
public class ExpressionTerm
    extends Expression
    implements Term {

    private static Logger LOGGER = Logger.getInstance(ExpressionTerm.class);
    private static boolean TRACE_ENABLED = LOGGER.isTraceEnabled();

    private String _expressionString = null;
    private boolean _sealed = false;
    private ContextToken _implicitObjectToken = null;
    private ExpressionToken[] _tokenArray = new ExpressionToken[0];
    private List _noModTokens = null;

    public ExpressionTerm() {
        super();
    }

    public void seal() {
        _sealed = true;

        assert _tokenArray[0] instanceof ContextToken;
        _implicitObjectToken = (ContextToken)_tokenArray[0];

        InternalStringBuilder buf = new InternalStringBuilder();
        for(int i = 0; i < _tokenArray.length; i++)
            buf.append(_tokenArray[i].getTokenString());

        _expressionString = buf.toString();
    }

    public Object read(NetUIVariableResolver vr) {
        assert _tokenArray != null;
        return _evaluate(_tokenArray.length, vr);
    }

    public String getContext() {
        assert _implicitObjectToken != null;
        return _implicitObjectToken.getName();
    }

    public List getTokens() {
        if(_noModTokens == null) {
            _noModTokens = new LinkedList();
            for(int i = 0; i < _tokenArray.length; i++)
                _noModTokens.add(_tokenArray[i]);
            _noModTokens = Collections.unmodifiableList(_noModTokens);
        }

        return _noModTokens;
    }

    public int getTokenCount() {
        assert _tokenArray != null;
        return _tokenArray.length;
    }

    public ExpressionToken getToken(int index) {
        assert _tokenArray != null;
        assert index >= 0;
        assert index < _tokenArray.length;
        return _tokenArray[index];
    }

    public String getExpressionString() {
        return _expressionString;
    }

    public String toString() {
        return _expressionString;
    }

    public String getExpression(int start) {
        if(start >= _tokenArray.length)
            throw new IllegalStateException("The index \"" +
                start +
                "\" is an invalid reference into an expression with \"" +
                _tokenArray.length +
                "\" _tokens.");

        boolean needDot = true;
        InternalStringBuilder buf = new InternalStringBuilder();
        buf.append("{");
        for(int i = start; i < _tokenArray.length; i++) {
            ExpressionToken tok = _tokenArray[i];
            if(tok instanceof ArrayIndexToken) {
                buf.append(tok.getTokenString());
                needDot = false;
            }
            else if(tok instanceof IdentifierToken) {
                if(needDot && i != start)
                    buf.append(".");
                buf.append(tok.toString());
                needDot = true;
            }
            else if(tok instanceof MapKeyToken) {
                buf.append(tok.getTokenString());
                needDot = false;
            }
        }
        buf.append("}");
        return buf.toString();
    }

    public void addToken(ExpressionToken token) {
        if(_sealed)
            throw new IllegalStateException("Can't add token to already sealed expression");

        assert _tokenArray != null;
        ExpressionToken[] newToken = new ExpressionToken[_tokenArray.length + 1];
        System.arraycopy(_tokenArray, 0, newToken, 0, _tokenArray.length);
        newToken[_tokenArray.length] = token;
        _tokenArray = newToken;
    }

    public void update(Object newValue, NetUIVariableResolver vr) {

        // Execute the expression up to the last token.  This will return the object that should be updated
        Object branch = _evaluate(_tokenArray.length - 1, vr);

        // Get the token to update
        ExpressionToken token = _tokenArray[_tokenArray.length - 1];

        if(TRACE_ENABLED)
            LOGGER.trace("Update leaf token: " + token + " on object: " + branch);

        // update the object
        token.write(branch, newValue);
    }

    public String changeContext(String previousImplicitObject, String newImplicitObject, Object lookupKey) {
        String thisExpr = getExpressionString();

        if(TRACE_ENABLED)
            LOGGER.trace("previous implicit object: " + previousImplicitObject + " new implicit object: " + newImplicitObject + " expression: " + thisExpr);

        // needs to be checked for atomicity
        ParsedExpression pe = ParseUtils.parse(newImplicitObject);

        if(!pe.isExpression()) {
            String msg = "The expression can not be qualified into new _context because the new _context is not atomic.";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }

        // this isn't a failure; it just means that there isn't anything else to replace
        if(!thisExpr.startsWith(previousImplicitObject))
            return "{" + thisExpr + "}";

        if(lookupKey instanceof Integer && ((Integer)lookupKey).intValue() > 32767) {
            String msg = "Can not create an indexed expression with an array index greater than " +
                "the Java array limit array length \"" +
                thisExpr + "\"";
            LOGGER.warn(msg);
            throw new RuntimeException(msg);
        }

        newImplicitObject = pe.getExpressionString() + "[" + lookupKey + "]";

        if(TRACE_ENABLED)
            LOGGER.trace("expression: " + thisExpr + " implicit object: " + newImplicitObject);

        thisExpr = thisExpr.replaceFirst(previousImplicitObject, newImplicitObject);

        InternalStringBuilder buf = new InternalStringBuilder();
        buf.append("{").append(thisExpr).append("}");
        return buf.toString();
    }

    public String qualify(String contextName) {
        InternalStringBuilder buf = new InternalStringBuilder();
        buf.append("{").append(contextName).append(".").append(getExpressionString()).append("}");
        return buf.toString();
    }

    private Object _evaluate(int index, NetUIVariableResolver vr) {
        assert _tokenArray != null;

        Object result = vr.resolveVariable(getContext());

        /*
        This is a special case used to unwrap something that has been wrapped in
        a Map facade but that delegates to some underlying implementation.  For
        example, an HttpSession might be wrapped inside of a Map facade in order
        to expose it as a Map for reads / updates in the expression engine.
         */
        if(_tokenArray.length == 1 && result instanceof WrappedObject) {
            result = ((WrappedObject)result).unwrap();
        }
        /*
        Otherwise, read each additional term in the expression with the result of the previous term.
         */
        else {
            for(int i = 1; i < index; i++)
                result = _tokenArray[i].read(result);
        }

        return result;
    }
}
