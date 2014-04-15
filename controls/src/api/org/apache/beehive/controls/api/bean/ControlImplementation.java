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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.beehive.controls.api.assembly.ControlAssembler;
import org.apache.beehive.controls.api.assembly.DefaultControlAssembler;

/**
 * The ControlImplementation annotation type is used to annotate the implementation class for a
 * Java Control.  It marks the class as a control implementation and (in the future) parameterizes
 * it.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ControlImplementation
{
    /** class name for the Class (which implements
     *  com.bea.control.assembly.ControlAssembler) whose assemble()
     *  method is called at assembly time - if left Void then no
     *  special assembly is needed
     */
    Class assemblyHelperClass() default java.lang.Void.class; // DEPRECATED

    /**
     * Class that implements ControlAssembler, which gets called at assembly time.
     * Default implementation does nothing.
     */
    Class<? extends ControlAssembler> assembler() default DefaultControlAssembler.class;

    /**
     * Specifies whether the control implementation class contains state that should be
     * serialized as part of the containing Control/JavaBean or is fully stateless/transient. 
     */
    boolean isTransient() default false;  // default to assuming stateful
}
