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
package org.apache.beehive.controls.api.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Base properties that are present intrinsically on all controls.
 */
@PropertySet
@Target( {ElementType.TYPE, ElementType.FIELD} )
@Retention( RetentionPolicy.RUNTIME )
public @interface BaseProperties
{
    /**
     * Fully qualified classname of the implementation class for the control.  If null,
     * the default algorithm for * determining the implementation class will be used --
     * basically, adding "Impl" to the control interface name.
     */
    String controlImplementation() default "";
}
