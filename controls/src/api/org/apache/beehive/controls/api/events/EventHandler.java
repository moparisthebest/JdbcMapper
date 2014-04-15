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
package org.apache.beehive.controls.api.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The EventHandler annotation type is used to mark a method that provides the event handler
 * implementation for a Control event.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventHandler
{
    /**
     * The field name of the Java control event source.  This must be an @Control field declared 
     * on the class defining the event handler method (or on a superclass if the field is not
     * declared to be private).
     */
    String field();

    /**
     * The EventSet interface that declares the event.   This must be a valid EventSet interface 
     * associated with the control type of the <code>field</code> member.
     */
    Class eventSet();

    /**
     * The name of the handled event.  This must be the name of a method declared on the EventSet
     * interface referenced by the <code>eventSet</code> member.  The annotated method must have
     * an event signature that <b>exactly</b> matches one of the event methods with this name.
     */
    String eventName();
}
