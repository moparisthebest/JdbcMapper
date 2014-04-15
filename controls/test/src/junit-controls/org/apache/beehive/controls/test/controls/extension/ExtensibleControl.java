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
package org.apache.beehive.controls.test.controls.extension;


import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A control interface with one method and one propertySet
 */
@ControlInterface
public interface ExtensibleControl {
    static final String CURRENT_POSITION = "In_ExtensibleControl_Interface";
    static final String CURRENT_LAYER = "On_ExtensibleControl_Layer";

    /* A property to be inherited by all the sub controls*/
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface Origin {
        String Birthplace() default "ExtensibleControl";
    }

    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    public @interface WhereAbout {
        String Position() default CURRENT_POSITION;

        String Layer() default CURRENT_LAYER;
    }

    @EventSet
    public interface SuperClassEvent {
        public void method1();
    }

    public String hello();

    public String getLayerByContext();
}
