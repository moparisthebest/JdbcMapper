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
package org.apache.beehive.controls.api.bean;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.Declaration;

/**
 * The ControlChecker interface is implemented by control authors wishing to
 * enforce rich semantic validation on extension and field instance declarations of
 * their controls.  By supplying a ControlChecker implementation (a "checker")
 * and associating it with your control's public interface, when an extension (.jcx) 
 * of your control is processed at build-time, the checker will be invoked and
 * can do rich validation of the jcx type and field instances via introspection and
 * analysis of the jcx's type structure, signatures and annotations.
 * <p>
 * Checkers are instantiated by, and required to implement, a no-arg constructor.
 * They are provided with type information and context via the Sun mirror API.
 */
public interface ControlChecker
{
    /**
     * Invoked by the control build-time infrastructure to process a declaration of
     * a control extension (ie, an interface annotated with @ControlExtension), or
     * a field instance of a control type.
     */
    public void check(Declaration decl, AnnotationProcessorEnvironment env);
}
