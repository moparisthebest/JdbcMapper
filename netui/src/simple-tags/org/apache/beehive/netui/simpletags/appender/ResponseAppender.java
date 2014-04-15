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
package org.apache.beehive.netui.simpletags.appender;

import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This Appender will append to a <code>ServletResponse</code>.
 */
public class ResponseAppender extends Appender
{
    private static final Logger logger = Logger.getInstance(ResponseAppender.class);

    private PrintWriter _writer;

    /**
     * Create a new <code>Appender</code> that will write to the <code>ServletResponse</code>.
     * @param response The <code>ServletResponse</code> that will be written to.
     */
    public ResponseAppender(ServletResponse response)
    {
        try {
            _writer = response.getWriter();
        }
        catch (IOException e) {
            logger.error(Bundle.getString("Tags_WriteException"), e);
        }
    }

    /**
     * Append a <code>String</code> to the output.
     * @param s The <code>String</code> value to be output.
     */
    public void append(String s)
    {
        _writer.print(s);
    }

    /**
    * Append a character to the output.
    * @param c The character value to be output.
    */
    public void append(char c)
    {
        _writer.print(c);
    }

}
