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
package org.apache.beehive.netui.compiler.typesystem.impl.declaration;

import org.apache.beehive.netui.compiler.typesystem.declaration.Modifier;

public class ModifierImpl
        extends Modifier
{
    protected ModifierImpl( com.sun.mirror.declaration.Modifier delegate )
    {
        super();
        
        switch ( delegate )
        {
            case ABSTRACT:
                setVal( INT_ABSTRACT );
                return;
            case PRIVATE:
                setVal( INT_PRIVATE );
                return;
            case PROTECTED:
                setVal( INT_PROTECTED );
                return;
            case PUBLIC:
                setVal( INT_PUBLIC );
                return;
            case STATIC:
                setVal( INT_STATIC );
                return;
            case TRANSIENT:
                setVal( INT_TRANSIENT );
                return;
            case FINAL:
                setVal( INT_FINAL );
                return;
            case SYNCHRONIZED:
                setVal( INT_SYNCHRONIZED );
                return;
            case NATIVE:
                setVal( INT_NATIVE );
                return;
        }
        
        assert false : "no Modifier impl for " + delegate.toString();
    }
    
    public static Modifier get( com.sun.mirror.declaration.Modifier delegate )
    {
        return delegate != null ? new ModifierImpl( delegate ) : null;
    }
}
