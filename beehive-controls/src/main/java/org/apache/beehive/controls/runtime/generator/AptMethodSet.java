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

import java.util.HashMap;
import java.util.Collection;

/**
 * The AptMethodSet method represents a collection of AptMethod objects.  It contains special
 * support for method overloading, to ensure that overloaded method objects contained within
 * the set will each have a unique index value.
 *
 * @see org.apache.beehive.controls.runtime.generator.AptMethod#setIndex
 */ 
public class AptMethodSet<T extends AptMethod>
{
    /**
     * Adds a new method to the list.  Also detects overloaded methods and ensures that they
     * will receive a unique index value.
     */
    public void add(T method)
    {
        // check for overridden method
        if (isOverrideMethod(method)) {
            return;
        }

        // Add to the list of managed methods
        _methods.put(method.getName() + method.getArgTypes(), method);

        // Ensure that all added methods have a unique index
        Object nameValue = _nameMap.get(method.getName());
        if (nameValue == null)
        {
            // first method with this name, considered unique (for now)
            _nameMap.put(method.getName(), method);
        }
        else
        {
            int nextIndex;
            if (nameValue instanceof AptMethod)
            {
                // 2nd method with this name, add index to original and start indexing
                ((AptMethod)nameValue).setIndex(0);
                nextIndex = 1;
            }
            else
            {
                // 3rd (or later) method with this name, continue indexing
                nextIndex = ((Integer)nameValue).intValue();
            }

            method.setIndex(nextIndex++);
            _nameMap.put(method.getName(), nextIndex);
        }
    }

    /**
     * Get the collection of methods in this set.
     */
    public Collection<T> getMethods()
    {
        return _methods.values();
    }

    /**
     * Get the number of methods in this set.
     */
    public int size()
    {
        return _methods.size();
    }

    /**
     * Determine of the method overrides a previously added method.
     * @param method Method to check.
     * @return true if method is an override to an existing method and does not need to be added
     * to this method set.
     */
    private boolean isOverrideMethod(T method)
    {
        return _methods.containsKey(method.getName() + method.getArgTypes());
    }
        
    private HashMap _nameMap = new HashMap();   // method name -> a single (unique) AptMethod or next index
    private HashMap<String, T> _methods = new HashMap<String, T>(); // method name + arg types -> AptMethod
}
