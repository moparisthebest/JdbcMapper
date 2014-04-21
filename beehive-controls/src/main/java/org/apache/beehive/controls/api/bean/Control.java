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
package org.apache.beehive.controls.api.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Control annotation type is used to annotate a field within a control
 * client source file that is a control reference.  It is the declarative
 * mechanism for instantiating controls in Java clients.  Java Controls
 * runtime implementations will automatically initialize such annotated field
 * references to an appropriate Java Control Bean of the requested type,
 * and perform event listener hookup etc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Control
{
    /**
     * Optional member used to specify the control interface class.
     * Typically only necessary to resolve ambiguities when multiple
     * control interfaces with same name but different packages are present.
     */
    Class<?> interfaceHint() default Object.class;
}
