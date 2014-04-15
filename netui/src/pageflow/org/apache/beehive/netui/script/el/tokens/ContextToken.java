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

/**
 * Token that represents the implicit object -- aka the binding context.  This is the
 * outer most data item referenced by an expression.
 */
public class ContextToken
    extends ExpressionToken {

    private String _context = null;

    public ContextToken(String context) {
        _context = context;
    }

    public Object read(Object value) {
        return null;
    }

    public void write(Object value, Object newValue) {
    }

    public String getName() {
        return _context;
    }

    public String getTokenString() {
        return _context;
    }

    public String toString() {
        return _context;
    }
}
