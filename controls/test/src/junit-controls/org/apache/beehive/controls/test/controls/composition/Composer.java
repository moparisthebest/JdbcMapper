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

package org.apache.beehive.controls.test.controls.composition;

import org.apache.beehive.controls.api.bean.ControlInterface;

import java.lang.annotation.Annotation;

/**
 * A simple control that tests control composition
 */
@ControlInterface
public interface Composer
{
    //
    // Returns a propertySet value for a simple nested control
    //
    public Annotation getControlPropertySet(Class propertySet);

    //
    // Returns a propertySet value for an extension nested control
    //
    public Annotation getExtensionControlPropertySet(Class propertySet);

    //
    // Invokes nested controls
    //
    public void invokeNestedControls();

    //
    // returns recorded events
    //
    public String[] getEventLog();
}
