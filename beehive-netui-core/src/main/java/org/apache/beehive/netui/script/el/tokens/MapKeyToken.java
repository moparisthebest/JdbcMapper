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

import java.util.Map;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * Token that represents a Map lookup token.
 */
public class MapKeyToken
    extends ExpressionToken {

    private static final Logger LOGGER = Logger.getInstance(MapKeyToken.class);

    private String _identifier = null;
    private char _quoteChar = '\'';

    public MapKeyToken(String identifier) {
        assert identifier != null;
        assert identifier.length() >= 2;
        
        if(identifier.startsWith("\""))
            _quoteChar = '"';
        // convert the Java string to an EcmaScript string.  Strip the quotes that exist because they're
        // always there for this token.
        _identifier = convertToEcmaScriptString(identifier.substring(1, identifier.length() - 1));
    }

    /**
     * Given a Java String, this value needs to be converted into a JavaScript compliant String.
     * See JavaScript: The Definitive Guide for how to do this
     */
    private String convertToEcmaScriptString(String string) {
        int len = string.length();
        InternalStringBuilder buf = new InternalStringBuilder(len);
        for(int i = 0; i < len; i++) {
            char c = string.charAt(i);
            // skip the \\ and consume the next character either appending it or turning it back
            // into the single character that it should have been in the first place.
            
            // if slash and not at the last character...
            if(c == '\\' && i + 1 < len) {
                i++;

                // skip the slash
                c = string.charAt(i);

                if(c == 'b')
                    c = '\b';
                else if(c == 't')
                    c = '\t';
                else if(c == 'n')
                    c = '\n';
                //else if(c == 'v') c = '\v';
                else if(c == 'f')
                    c = '\f';
                else if(c == 'r') c = '\r';
                // @todo: unicode escaping?
            }

            buf.append(c);
        }

        LOGGER.trace("converted String to JavaScript compliant string: " + buf.toString());

        return buf.toString();
    }

    /**
     * Read the value represented by this token from the given <code>object</code>.
     * @param object the object
     * @return the value of this property on object
     */
    public Object read(Object object) {
        if(object instanceof Map)
            return mapLookup((Map)object, _identifier);
        else return beanLookup(object, _identifier);
    }

    /**
     * Update a the value represented by this token on the given <code>object</code> with the
     * new value.
     * @param object the object
     * @param value the new value of this property on the object
     */
    public void write(Object object, Object value) {
        if(object instanceof Map)
            mapUpdate((Map)object, _identifier, value);
        else beanUpdate(object, _identifier, value);
    }

    public String getTokenString() {
        return _quoteChar + _identifier + _quoteChar;
    }

    public String toString() {
        return _identifier;
    }
}
