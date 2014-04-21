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
package org.apache.beehive.controls.runtime.generator;

import java.io.IOException;
import java.io.Writer;

/**
 * The IndentingWriter class is a simple implementation of an indenting code writer
 */
public class IndentingWriter extends Writer 
{
    /** current depth:
     * <PRE>
     * // depth = 0;
     * {
     *   // depth now is 2
     *   {
     *     // depth now is 4
     *   }
     *   // depth now is 2
     * }
     * // depth now is 0
     * </PRE>
     */
    protected int depth = 0;

    public IndentingWriter(Writer delegate) 
    {
	    this(delegate, 
             Integer.getInteger("org.apache.beehive.controls.runtime.generator.indentLevel", 4).intValue());
    }

    public IndentingWriter(Writer delegate, int indentLevel) 
    {
	    _out = delegate;
	    this._indentLevel = indentLevel;
    }

    public void write(char cbuf[], int off, int len) throws IOException 
    {
	    if (off < 0 || off + len > cbuf.length)
	        throw new ArrayIndexOutOfBoundsException();

	    for (int i = off; i < off + len; i++) 
        {
	        char c = cbuf[i];
	        if (c == '}') 
            {
                decrDepth();
	        } 
            else if ((c == ' ' || c == '\t') && _needIndent) 
            {
		        continue;
	        }

	        if (_needIndent)
		        indent();
	        _out.write(c);

	        if (c == '\n')
            {
		        _needIndent = true;
            }
	        else if (c == '{') 
            {
                incrDepth();
            }
	    }
    }

    public void flush() throws IOException 
    {
	    _out.flush();
    }

    public void close() throws IOException 
    {
	    _out.close();
    }

    private void indent() throws IOException 
    {
	    for (int i = 0; i < depth; i++) 
        {
	        _out.write(' ');
	    }
	    _needIndent = false;
    }

    private void incrDepth() 
    {
	    depth += _indentLevel;
    }

    private void decrDepth() 
    {
	    depth -= _indentLevel;
	    if (depth < 0)
	        depth = 0;
    }

    protected Writer _out;
    protected int _indentLevel;
    private boolean _needIndent = false;
}
