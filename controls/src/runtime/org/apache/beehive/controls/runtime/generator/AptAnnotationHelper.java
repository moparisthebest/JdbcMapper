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
import java.util.Map;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;

/**
 * The AptAnnotationHelper class is a helper class that aids in the reading of annotation
 * values using APT metadata
 */
public class AptAnnotationHelper
{
    /**
     * Initialize a new helper instance based upon a specific annotation declaration.
     * @param  annot The annotation value declaration
     */
    public AptAnnotationHelper(AnnotationMirror annot)
    {
        //
        // Build maps from the element name to its declaration and values
        //
        Map <AnnotationTypeElementDeclaration,AnnotationValue> elemValues = 
            annot.getElementValues();

        for (AnnotationTypeElementDeclaration ated : elemValues.keySet())
        {
            _elementMap.put(ated.getSimpleName(), ated);
            _valueMap.put(ated.getSimpleName(), elemValues.get(ated));
        }
    };

    /**
     * Returns the AnnotationTypeElementDeclaration for a particular element
     */
    public AnnotationTypeElementDeclaration getElementDeclaration(String elemName)
    {
        if (_elementMap.containsKey(elemName))
            return _elementMap.get(elemName);
        return null;
    }

    /**
     * Returns the value of a particular element as a String
     */
    public String getStringValue(String elemName)
    {
        if (_valueMap.containsKey(elemName))
            return _valueMap.get(elemName).toString();
        return null;
    }

    /**
     * Returns the value of a particular element as an Object
     */
    public Object getObjectValue(String elemName)
    {
        if (_valueMap.containsKey(elemName))
            return _valueMap.get(elemName).getValue();
        return null;
    }

    private HashMap<String,AnnotationTypeElementDeclaration> _elementMap = 
                new HashMap<String,AnnotationTypeElementDeclaration>();
    private HashMap<String,AnnotationValue> _valueMap = 
                new HashMap<String,AnnotationValue>();
}
