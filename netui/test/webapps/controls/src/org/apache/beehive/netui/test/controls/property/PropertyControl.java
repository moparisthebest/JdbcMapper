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

package org.apache.beehive.netui.test.controls.property;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A control interface with multiple propertySets declared
 */
@ControlInterface
public interface PropertyControl {
    static final String DEFAULT_ATTRIBUTE_VALUE1 = "Hello";
    static final String DEFAULT_ATTRIBUTE_VALUE3 = "Hello3";

    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PropertyOne {
        public String attribute1() default DEFAULT_ATTRIBUTE_VALUE1;

        public String attribute2();
    }

    @PropertySet(prefix = "PropertyTwo")
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PropertyTwo {
        public String attribute3() default DEFAULT_ATTRIBUTE_VALUE3;

        public String attribute4();
    }


    @PropertySet(optional = true)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PropertyThree {
        public String attribute5();

        public String attribute6();
    }

    @PropertySet(hasSetters = false)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PropertyFout {
        public String attribute7();

        public String attribute8();
    }


    public String getAttribute1ByContext();

    public String getAttribute3ByContext();
}
