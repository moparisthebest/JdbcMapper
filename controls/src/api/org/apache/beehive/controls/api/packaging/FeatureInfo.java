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
package org.apache.beehive.controls.api.packaging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The FeatureInfo annotation type defines a JSR-175 syntax for annotating a Control to
 * provide BeanInfo FeatureDescriptor information for the bean, its properties, methods,
 * or events.
 * <p>
 * The elements of FeatureInfo correspond 1-to-1 with the information exposed by the
 * <code>java.beans.FeatureDescriptor</code> class.
 * 
 * @see java.beans.FeatureDescriptor
 */
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface FeatureInfo
{
    public String displayName() default "";           // default: use reflection name
    public String name() default "";                  // default: use reflection name
    public String shortDescription() default "";
    public boolean isExpert() default false;
    public boolean isHidden() default false;
    public boolean isPreferred() default false;
    public FeatureAttribute [] attributes() default {};
}
