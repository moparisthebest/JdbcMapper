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

package org.apache.beehive.netui.test.controls.instantiate;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A control interface with three single-member propertySets and one method
 */
@ControlInterface
public interface SingleProperty {
    static final String GREET_DEFAULT = "Hello";

    /**
     * A single member property with default value
     */
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Greeting {
        public String GreetWord() default GREET_DEFAULT;
    }

    /**
     * A single member property without default value
     */
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Identity {
        public String name();
    }

    /**
     * A single member property of primitive types
     */
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Identifier {
        public int age() default 20;
    }


    public String sayHello();
}
