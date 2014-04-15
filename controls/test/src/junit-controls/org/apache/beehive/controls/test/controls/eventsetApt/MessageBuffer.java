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
package org.apache.beehive.controls.test.controls.eventsetApt;

import org.apache.beehive.controls.spi.svc.InterceptorAnnotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;


/**
 * Requests that a method or event is delivered asynchronously.
 * This annotation must be placed in a control interface that
 * is annotation already with @ControlInterface, @ControlExtension or @EventSet.
 * This annotation should be placed on methods or events that should be asynchronous.
 *
 * <br>NOTE: Message buffering is ONLY currently supported on controls being used within
 * a web service.
 */
@InterceptorAnnotation (service= MessageBufferService.class)
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageBuffer
{
    public boolean enable() default true;
    public int retryCount() default 0;
    public String retryDelay() default "0s";
}
