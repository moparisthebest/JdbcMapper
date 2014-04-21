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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * This class represents a complete and parsed expression string.  An expression can be made up
 * of any number of ExpressionTerm and LiteralTerm objects.  If this parsed expression contains
 * a single ExpressionTerm, it is considered "atomic" because it references a single property on
 * a single object.
 */
public class ParsedExpression {

    private static Logger LOGGER = Logger.getInstance(ParsedExpression.class);
    private static final boolean TRACE_ENABLED = LOGGER.isTraceEnabled();
    private static final String EMPTY_STRING = "";

    private String _expressionString;
    private boolean _isExpression = false;
    private boolean _containsExpression = false;

    /**
     * An atomic expression is the common case, so keep a pointer to the first element in the array
     * in order to avoid doing a cast from Term to ExpressionTerm when evaluating an expression.  This
     * trades off space for time, but it's a small amount of space relatively speaking.
     */
    private ExpressionTerm _atomicExpression = null;
    private Term[] _termArray = new Term[0];

    public void seal() {
        InternalStringBuilder buf = new InternalStringBuilder();
        for(int i = 0; i < _termArray.length; i++) {
            Term t = _termArray[i];
            assert t != null;

            t.seal();

            if(t instanceof ExpressionTerm) {
                if(_termArray.length == 1) {
                    _atomicExpression = (ExpressionTerm)t;
                    _isExpression = true;
                }
                _containsExpression = true;
            }
            else if(t instanceof LiteralTerm) {
                String lit = t.getExpressionString();
                if(lit != null && lit.indexOf("{") > -1)
                    _containsExpression = true;
            }

            buf.append(t.getExpressionString());
        }
        _expressionString = buf.toString();
    }

    public boolean isExpression() {
        return _isExpression;
    }

    public boolean containsExpression() {
        return _containsExpression;
    }

    public void addTerm(Term term) {
        assert _termArray != null;
        Term[] newTerms = new Term[_termArray.length + 1];
        System.arraycopy(_termArray, 0, newTerms, 0, _termArray.length);
        newTerms[_termArray.length] = term;
        _termArray = newTerms;
    }

    public int getTokenCount() {
        return _termArray.length;
    }

    public Term getTerm(int i) {
        assert _termArray != null;
        assert i > 0 && i < _termArray.length;

        return _termArray[i];
    }

    public ExpressionTerm getAtomicExpressionTerm() {
        return _atomicExpression;
    }

    public Object evaluate(NetUIVariableResolver vr) {
        if(TRACE_ENABLED)
            LOGGER.trace("evaluate expression: " + _expressionString);

        if(_isExpression) {
            if(TRACE_ENABLED)
                LOGGER.trace("atoimc expression");

            return _atomicExpression.read(vr);
        } else {
            InternalStringBuilder buf = new InternalStringBuilder();

            for(int i = 0; i < _termArray.length; i++) {
                if(TRACE_ENABLED)
                    LOGGER.trace(
                        "term[" + i + "]: " +
                        _termArray[i].getClass().getName() +
                        " expression string: " +
                        _termArray[i].getExpressionString());

                Object result = _termArray[i].read(vr);

                buf.append(result != null ? result.toString() : EMPTY_STRING);
            }

            return buf.toString();
        }
    }

    public void update(Object value, NetUIVariableResolver vr) {
        if(!_isExpression) {
            String msg = "The expression can not be updated because it is not atomic.";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }

        _atomicExpression.update(value, vr);
    }

    public String changeContext(String oldContext, String newContext, Object index) {
        if(!_isExpression) {
            String msg = "The expression can not change context because it is not atomic.";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }

        return _atomicExpression.changeContext(oldContext, newContext, index);
    }

    public String qualify(String implicitObjectName) {
        return "{" + implicitObjectName + "." + getExpressionString() + "}";
    }

    public String getExpressionString() {
        return _expressionString;
    }

    public String toString() {
        return _expressionString;
    }
}
