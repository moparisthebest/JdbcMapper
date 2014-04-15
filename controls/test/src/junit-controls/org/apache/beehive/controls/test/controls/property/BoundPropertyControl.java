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

package org.apache.beehive.controls.test.controls.property;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.bean.ExternalPropertySets;
import org.apache.beehive.controls.api.packaging.PropertyInfo;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * A control interface with bound and/or constrain propertySet
 */
@ControlInterface
@ExternalPropertySets({BoundExtPropertySet.class})
public interface BoundPropertyControl {
    static final String BRAND_DEFAULT = "DEFAULT_BRAND";
    static final String MATERIAL_DEFAULT = "DEFAULT_MATERIAL";
    static final String QUALITY_DEFAULT = "DEFAULT_QUALITY";

    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Wheel {
        @PropertyInfo(bound = true)
        public String Brand() default BRAND_DEFAULT;
    }

    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Door {
        @PropertyInfo(constrained = true)
        public String Material() default MATERIAL_DEFAULT;
    }

    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Window {
        @PropertyInfo(bound = true, constrained = true)
        public String Quality() default QUALITY_DEFAULT;
    }

    public String sayHello();
}
