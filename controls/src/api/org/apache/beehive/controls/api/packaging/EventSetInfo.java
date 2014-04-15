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
package org.apache.beehive.controls.api.packaging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The EventSetInfo annotation type defines a JSR-175 syntax for annotating a Control 
 * property declaration to provide java.beans.EventSetDescriptor information.  Generic
 * feature information is defined using the <code>FeatureInfo</code> annotation type
 * <p>
 * The elements of EventStInfo correspond 1-to-1 with the information exposed by the
 * <code>java.beans.EventSetDescriptor</code> class.
 * 
 * @see java.beans.EventSetDescriptor
 */
@Target({ElementType.TYPE})   // appears on EventSet interface declaration
public @interface EventSetInfo
{
    public boolean isUnicast() default false;           // single listener model
    public boolean isDefault() default true;            // is the default event set
}
