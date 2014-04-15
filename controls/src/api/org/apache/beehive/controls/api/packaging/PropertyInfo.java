package org.apache.beehive.controls.api.packaging;

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import java.beans.PropertyEditor;

/**
 * The PropertyInfo annotation type defines a JSR-175 syntax for annotating a Control 
 * property declaration to provide java.beans.PropertyDescriptor information.  Generic
 * feature information is defined using the <code>FeatureInfo</code> annotation type
 * <p>
 * The elements of PropertyInfo correspond 1-to-1 with the information exposed by the
 * <code>java.beans.PropertyDescriptor</code> class.
 * 
 * @see java.beans.PropertyDescriptor
 */
@Target({ElementType.METHOD})   // appears on PropertySet method declaration (i.e. properties)
public @interface PropertyInfo
{
    /** 
     * The NoEditor class can be used as the value of the editorClass attribute to 
     *  indicate that the property has no editor
     */
    static public class NoEditor {};

    public boolean bound() default false;                       // Sends PropertyChange events
    public boolean constrained() default false;                 // Sends VetoableChange events
    public Class editorClass() default NoEditor.class;          // default == no editor
}
