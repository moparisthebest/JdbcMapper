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

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * AnnotationConstraints defines meta-annotations that allow
 * specification of additional constraints that aren't
 * expressible using J2SE 5.0 meta-annotations.
 *
 * Actual enforcement of these semantics is implementation dependent.
 * An <code>apt</code>-based reference implementation is provided by
 * {@link org.apache.beehive.controls.runtime.bean.AnnotationConstraintValidator}.
 *
 * @see org.apache.beehive.controls.runtime.bean.AnnotationConstraintValidator
 */
public interface AnnotationConstraints
{
    /**
     * Defines a number of simple constraints on the way annotation members
     * can be used together.
     *
     * @see MembershipRule
     */
    public enum MembershipRuleValues
    {
        AT_LEAST_ONE,
        AT_MOST_ONE,
        EXACTLY_ONE,
        ALL_IF_ANY
    }

    /**
     * Provides a mechanism for enforcing constraints between members of
     * an annotation (such a mechanism is absent from J2SE 5.0; for example,
     * given an annotation with members 'a' and 'b' there is no way to say
     * that they are mutually exclusive).
     *
     * @see MembershipRuleValues
     */
    @Target({ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MembershipRule
    {
        /** Required, the membership rule.*/
        MembershipRuleValues value();
        /** Optional list of member names to apply rule against.  Empty array implies all members. */
        String[] memberNames() default {};
    }

    /**
     * Defines whether the annotation decorated by this
     * annotation can overriden externally (a marker interface).
     */
    @Target({ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AllowExternalOverride
    {
    }

    /**
     * Specifies the version of the control runtime required by this annotation.
     */
    @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiredRuntimeVersion
    {
        String value(); // no default
    }
}
