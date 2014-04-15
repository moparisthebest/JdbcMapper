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

package org.apache.beehive.controls.test.controls.checker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.properties.PropertySet;

@ControlInterface( checkerClass=HelloChecker.class )
public interface HelloChecked
{
    //
    // A simple enumerated type used to customize the greeting by gender
    //
    public enum GenderType
    {
        NEUTRAL, MALE, FEMALE
    }

    public @interface Gender
    {
        GenderType value();
    }

    /**
     * Declare a simple PropertySet, that allows the salutation used by the custom
     * control to be customized.
     */
    @PropertySet
    @Target( {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD} )
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Greeting
    {
        String salutation() default "Hello";
        Gender gender() default @Gender(GenderType.NEUTRAL);
    }

    java.lang.String hello(java.lang.String name);

    java.lang.String lastVisitor();

    int visitorCount();
}
