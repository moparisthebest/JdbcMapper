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
package org.apache.beehive.netui.pageflow;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Extension of org.apache.struts.validator.DynaValidatorForm that implements Map.  This allows it to be
 * used with NetUI tags.
 */ 
public class DynaFormData
    extends DynaValidatorForm
    implements Map
{
    public void clear()
    {
        dynaValues.clear();
    }

    public boolean containsKey( Object key )
    {
        return dynaValues.containsKey( key );
    }

    public boolean containsValue( Object value )
    {
        return dynaValues.containsValue( value );
    }

    public Set entrySet()
    {
        return dynaValues.entrySet();
    }

    public Object get( Object name )
    {
        return super.get( name.toString() );
    }

    public boolean isEmpty()
    {
        return dynaValues.isEmpty();
    }

    public Set keySet()
    {
        return dynaValues.keySet();
    }

    public Object put( Object key, Object value )
    {
        String keyStr = key.toString();
        set( keyStr, value );
        return get( key );
    }

    public void putAll( Map map )
    {
        for ( Iterator i = map.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = ( Map.Entry ) i.next();
            set( entry.getKey().toString(), entry.getValue() );
        }
    }

    public Object remove( Object key )
    {
        return dynaValues.remove( key );
    }

    public int size()
    {
        return dynaValues.size();
    }

    public Collection values()
    {
        return dynaValues.values();
    }
}

