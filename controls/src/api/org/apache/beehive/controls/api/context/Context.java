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
package org.apache.beehive.controls.api.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Context annotation type is used to annotate a field within a control implementation
 * class that refers to a contextual service.  The Java Controls runtime will automatically
 * initialize the field value to an appropriate provider of the requested service, or will
 * throw a construction or deserialization error if no such provider is available.
 *
 * The following is a simple example:
 *
 * <code><pre>
 * <sp>@ControlImplementation
 * public class MyControlImpl
 * {
 *     <sp>@Context
 *     ControlContext myContext;
 * }
 * </pre></code>
 * This example declares a field named <code>myContext</code> that will automatically be
 * initialized by the Java Controls runtime to refer to a provider of the 
 * <code>ControlContext</code> contextual service.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Context
{
}
