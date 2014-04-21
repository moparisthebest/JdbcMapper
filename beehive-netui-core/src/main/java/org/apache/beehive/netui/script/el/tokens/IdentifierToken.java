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
package org.apache.beehive.netui.script.el.tokens;

import java.util.List;
import java.util.Map;

import org.apache.beehive.netui.util.logging.Logger;

/**
 * General representation of an identifier in an expression.  For example, in the expression
 * <code>actionForm.foo.bar</code>, both <code>foo</code> and <code>bar</code> are tokens of this
 * type.
 */
public class IdentifierToken
    extends ExpressionToken {

    private static final Logger LOGGER = Logger.getInstance(IdentifierToken.class);
    private static final boolean TRACE_ENABLED = LOGGER.isTraceEnabled();

    private String _identifier = null;

    public IdentifierToken(String identifier) {
        _identifier = identifier;
    }

    /**
     * Read the object represetned by this token from the given object.
     * @param object the object from which to read
     * @return the read object
     */
    public Object read(Object object) {
        if(object == null) {
            String msg = "Can not evaluate the identifier \"" + _identifier + "\" on a null object.";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }

        if(TRACE_ENABLED)
            LOGGER.trace("Read property " + _identifier + " on object of type " + object.getClass().getName());

        if(object instanceof Map)
            return mapLookup((Map)object, _identifier);
        else if(object instanceof List) {
            int i = parseIndex(_identifier);
            return listLookup((List)object, i);
        }
        else if(object.getClass().isArray()) {
            int i = parseIndex(_identifier);
            return arrayLookup(object, i);
        }
        else return beanLookup(object, _identifier);
    }

    /**
     * Update the value represented by this token on the given object <code>object</code> with
     * the value <code>value</code>.
     * @param object the object to update
     * @param value the new value
     */
    public void write(Object object, Object value) {
        if(object == null) {
            String msg = "Can not update the identifier \"" + _identifier + "\" on a null value object.";
            LOGGER.error(msg);
            throw new RuntimeException(msg);
        }

        if(TRACE_ENABLED)
            LOGGER.trace("Update property named \"" + _identifier + "\" on object of type: \"" + object.getClass().getName() + "\"");

        if(object instanceof Map)
            mapUpdate((Map)object, _identifier, value);
        else if(object instanceof List) {
            int i = parseIndex(_identifier);
            listUpdate((List)object, i, value);
        }
        else if(object.getClass().isArray()) {
            int i = parseIndex(_identifier);
            arrayUpdate(object, i, value);
        }
        else beanUpdate(object, _identifier, value);
    }

    /**
     * Get the String representation of this token.  Note, this assumes that the token was
     * preceeded with a ".".
     * @return the String representing this token
     */
    public String getTokenString() {
        return "." + _identifier;
    }

    public String toString() {
        return _identifier;
    }
}
