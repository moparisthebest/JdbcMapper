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
package org.apache.beehive.netui.tags.rendering;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

/**
 * This is an implementation of {@link AbstractRenderAppender} which appends
 * Strings to a {@link InternalStringBuilder}
 */
public class StringBuilderRenderAppender
    extends AbstractRenderAppender
{
    private InternalStringBuilder _sb;

    public StringBuilderRenderAppender()
    {
    }

    public StringBuilderRenderAppender(InternalStringBuilder sb)
    {
        _sb = sb;
    }

    public void setInternalStringBuilder(InternalStringBuilder sb)
    {
        _sb = sb;
    }

    public void append(String s)
    {
        assert _sb != null : "Render appender doesn't have a valid string builder";
        _sb.append(s);
    }

    public void append(char c)
    {
        assert _sb != null : "Render appender doesn't have a valid string builder";
        _sb.append(c);
    }

    public String toString()
    {
        assert _sb != null : "Render appender doesn't have a valid string builder";
        return _sb.toString();
    }
}
