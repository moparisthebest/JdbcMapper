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

/**
 * The ControlInterface annotation type is used to annotate a control public interface.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ControlInterface
{
    /**
     * Placeholder string used in defaultBinding attr.  Tools and runtime should replace
     * instances of INTERFACE_NAME found in values of defaultBinding with the fully
     * qualified name of the interface annotated with @ControlInterface.
     */
    static final String INTERFACE_NAME = "<InterfaceName>";

    /**
     * Specify the fully qualified name of the control implementation for this control interface.
     * If no value is specified the implementation will be the name of the interface with 'Impl' appended.
     * */
    String defaultBinding() default INTERFACE_NAME + "Impl";

    /**
     * @deprecated Replaced by checker() element.
     */
    Class<? extends ControlChecker> checkerClass() default DefaultControlChecker.class;

    /**
     *  Used by control authors wishing to enforce rich semantic validation on extension and field
     *  instance declarations of their controls. By supplying a ControlChecker implementation
     *  (a "checker") and associating it with your control's public interface, when an
     *  extension of your control is processed at build-time, the checker will be invoked and
     *  can do rich validation of the extension type and field instances via introspection and
     *  analysis of the control extension's type structure, signatures and annotations.
     * @see org.apache.beehive.controls.api.bean.ControlChecker
     */
    Class<? extends ControlChecker> checker() default DefaultControlChecker.class;
}
