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
package org.apache.beehive.controls.runtime.bean;

import java.beans.PersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;

import org.apache.beehive.controls.api.properties.AnnotatedElementMap;

/**
 * The AnnotatedElementMapPersistenceDelegate is an XMLEncoder PersistenceDelegate for
 * the <code>org.apache.beehive.controls.api.properties.AnnotatedElementMap</code>
 * class.
 */
public class AnnotatedElementMapPersistenceDelegate extends PersistenceDelegate
{
    protected Expression instantiate(Object oldInstance, Encoder out)
    {
        //
        // Modify the default constructor to pass in the AnnotatedElement wrapped by the map
        //
        AnnotatedElementMap aem = (AnnotatedElementMap)oldInstance;
        return new Expression(aem, aem.getClass(), "new",
                              new Object [] { aem.getAnnotatedElement() });
    }
}
