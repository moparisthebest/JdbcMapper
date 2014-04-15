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
package org.apache.beehive.netui.xdoclet;

import org.apache.tools.ant.BuildException;

/**
 * Extension of ant's BuildException that does not print a stack trace. This is used to
 * signal ant that the build has failed from the doclet task, but without the
 * XJavaDocTask superclass printing out a stacktrace, so that pageflow build error messages
 * will be easier to find.
 */
public class NetuiBuildException extends BuildException
{

    public NetuiBuildException()
    {
        super();
    }

    public void printStackTrace()
    {
        // no-op so we won't let XJavaDocTask print out a stack when the build fails
    }
}
