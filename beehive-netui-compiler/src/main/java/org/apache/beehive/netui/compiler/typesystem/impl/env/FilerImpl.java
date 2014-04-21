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
package org.apache.beehive.netui.compiler.typesystem.impl.env;

import org.apache.beehive.netui.compiler.typesystem.impl.DelegatingImpl;
import org.apache.beehive.netui.compiler.typesystem.env.Filer;

import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class FilerImpl
    extends DelegatingImpl
    implements Filer
{

    protected FilerImpl( com.sun.mirror.apt.Filer delegate )
    {
        super( delegate );
    }

    public static Filer get( com.sun.mirror.apt.Filer delegate )
    {
        return new FilerImpl(delegate);
    }

    public PrintWriter createTextFile(File file)
            throws IOException
    {
        return getDelegate().createTextFile(com.sun.mirror.apt.Filer.Location.CLASS_TREE, "", file, "UTF-8");
    }

    protected com.sun.mirror.apt.Filer getDelegate()
    {
        return ( com.sun.mirror.apt.Filer ) super.getDelegate();
    }
}
