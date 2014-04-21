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
package org.apache.beehive.controls.api.versioning;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by the control author to specify the version (major.minor) of the control interface.
 * Allowed on interfaces annotated with @ControlInterface.  This version number
 * is the basis for control versioning, and versioning constraints against it are enforced both at
 * compile time and runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Version
{
    /**
     * Major version number, typically used to track significant functionality changes.
     */
    int major(); 
    /**
     * Minor version number, typically used to track small internal changes/fixes.  Version
     * constraints default to ignoring the minor version number in their comparisons, but may
     * be configured to specify a particular minor version.
     */
    int minor() default 0;
}
