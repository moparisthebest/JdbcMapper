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

import org.apache.beehive.netui.util.logging.Logger;

/**
 *
 */
public class ArrayIndexToken
    extends ExpressionToken {

    private static final Logger LOGGER = Logger.getInstance(ArrayIndexToken.class);

    private int _index;

    public ArrayIndexToken(String identifier) {
        _index = Integer.parseInt(identifier);
    }

    /**
     * Evaluate an array index token.  This token takes the form "[1234]" where
     * "1234" is the index into the array.  The token is identified as this because
     * an integer number appears between the square brackets.
     *
     * @param value the object from which to read a value at the index represented by this token
     * @return the value of the item at this array index
     */
    public Object read(Object value) {
        if(value instanceof List)
            return listLookup((List)value, _index);
        else if(value.getClass().isArray())
            return arrayLookup(value, _index);
        else {
            String message = "The index \"" +
            _index +
            "\" can not be used to index a property on an object that is not an Array or a List";
            LOGGER.error(message);
            throw new RuntimeException(message);
        }
    }

    public void write(Object root, Object value) {
        if(root instanceof List)
            listUpdate((List)root, _index, value);
        else if(root.getClass().isArray())
            arrayUpdate(root, _index, value);
        else {
            String message = "The index " +
                _index +
                "\" can not be used to update a property on an object that is not an Array or a List";
            LOGGER.error(message);
            throw new RuntimeException(message);
        }
    }

    public String getTokenString() {
        return "[" + _index + "]";
    }

    public String toString() {
        return "" + _index;
    }
}
