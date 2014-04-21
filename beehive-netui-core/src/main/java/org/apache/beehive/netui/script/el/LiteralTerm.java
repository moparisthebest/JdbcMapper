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

import org.apache.beehive.netui.util.logging.Logger;

/**
 *
 */
public class LiteralTerm
    implements Term {

    private static final Logger LOGGER = Logger.getInstance(LiteralTerm.class);
    private static final boolean TRACE_ENABLED = LOGGER.isTraceEnabled();

    private String _text = null;

    public LiteralTerm(String text) {
        super();

        /* todo:  probably being lazy in not fixing the grammar */
        if(text.equals("\\{"))
            _text = "{";
        else _text = text;

        if(TRACE_ENABLED)
            LOGGER.trace("LiteralTerm: " + text);
    }

    public void seal() {
    }

    public String getExpressionString() {
        return _text;
    }

    public Object read(NetUIVariableResolver vr) {
        return _text;
    }

    public String toString() {
        return "LiteralTerm:\n  " + _text + "\n";
    }
}

