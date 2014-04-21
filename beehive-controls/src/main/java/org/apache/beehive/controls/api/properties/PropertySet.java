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
package org.apache.beehive.controls.api.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The PropertySet annotation type is used to mark an interface that defines a set of
 * properties that are associated with a Java Control.  By convention, property sets
 * are declared as an inner annotation types on the Java Control public interface.
 * <p>
 * Each member of the annotation type targeted by the <code>PropertySet</code> annotation
 * will define a new property for the control.
 * <p>
 * Here is a simple example:
 * <code><pre>
 * public interface MyControl extends org.apache.beehive.controls.api.Control
 * {
 *     <sp>@PropertySet
 *     public @interface MyProperties
 *     {
 *         public String aStringProperty();
 *         public int anIntProperty();
 *         ...
       }
 * }
 * </pre></code>
 * <p>
 * A Java Control can have multiple property sets associated with it.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface PropertySet
{
    /**
     * The prefix member defines a prefix that will be used in all property setter/getter
     * methods for properties in the <code>PropertySet</code>.  It is necessary to specify
     * a prefixes when a control interface has multiple property sets that contain
     * properties with the same name.
     * <p>
     * The following code shows the basic conventions for setter/getter methods on a Java
     * Control Bean:
     * <code><pre>
     *     public void set&lt;prefix&gt;&lt;propertyName&gt;(&lt;propertyType&gt; value);
     *     public &lt;propertyType&gt; get&lt;prefix&gt;&lt;propertyName&gt;();
     * </pre>/code>
     * where <code>prefix</code> is the prefix member value, <code>propertyName</code> is
     * the name of the declared property member, and <code>propertyType</code> is the
     * type associated with the declared property member.
     */
    String prefix() default "";

    /**
     * The externalConfig member defines whether properties in the set will be settable
     * via external configuration.
     */
    boolean externalConfig() default true;

    /**
     * The optional member specifies that this property set may optionally be associated
     * with the control.  Because there is no way to represent an 'unset' property value,
     * optional properties will not expose a getter method to clients;  a control
     * implementation class can determine whether a property is/is not set, because the
     * PropertySet query APIs on ControlBeanContext will return null if unset.  For
     * properties that are not optional, a PropertySet instance with all default values
     * will be returned if unset.
     *
     * @see org.apache.beehive.controls.api.context.ControlBeanContext#getControlPropertySet
     * @see org.apache.beehive.controls.api.context.ControlBeanContext#getMethodPropertySet
     */ 
    boolean optional() default false;

    /**
     * The hasSetters member defines whether properties in the set will have programmatic
     * setter methods.
     */
    boolean hasSetters() default true;
}