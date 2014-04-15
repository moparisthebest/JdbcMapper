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

package org.apache.beehive.netui.test.controls.extension;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A sub control extending ExtensibleControl.
 * This control declares one new method and one new propertySet.
 * It also resets the value of the propertySet inherited from ExtensibleControl.
 */
//@ControlExtension ::JIRA-118 and JIRA-197
@ControlInterface
@ExtensibleControl.WhereAbout(Layer = "On_SubControl_Interface_Layer")
public interface SubControl extends ExtensibleControl {

    static final String A_MESSAGE = "New Property Declared by Sub Control";

    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NewProperty {
        String Message() default A_MESSAGE;
    }

    @EventSet
    public interface SubClassEvent {
        public void method1();
    }

    public String hello2();

    public String accessInheritedProperty();

    public String getAnnotatedInheritedPropertyByContext();

    public String getExtendedPropertyByContext();

    public int invokeInheritedEventFromSubControl();

    public int invokeExtendedEventFromSubControl();
}
