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

package org.apache.beehive.controls.test.controls.property.constraint;

import org.apache.beehive.controls.api.bean.AnnotationConstraints;
import org.apache.beehive.controls.api.bean.AnnotationMemberTypes;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A control interface for testing Annotation.MembershipRule
 */

@ControlInterface
public interface BookControl {
    /**
     * A propertySet with AT_LEAST_ONE constraint
     * User needs to set at least one value when instantiating controls declaratively;
     * It is unknow what will happens when instantiating controls programmatically.
     */
    @PropertySet
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @AnnotationConstraints.MembershipRule(AnnotationConstraints.MembershipRuleValues.AT_LEAST_ONE)
    public @interface Price {
        @AnnotationMemberTypes.Decimal(minValue = 10)
        public String us_price() default "";

        @AnnotationMemberTypes.Decimal(minValue = 10)
        public String ca_price() default "";

        @AnnotationMemberTypes.Decimal(minValue = 10)
        public String eu_price() default "";
    }

    /**
     * A propertySet with AT_MOST_ONE
     * User can not set more than one value when instantiating controls declaratively;
     * It is unknow what will happens when instantiating controls programmatically.
     */
    @PropertySet
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @AnnotationConstraints.MembershipRule(AnnotationConstraints.MembershipRuleValues.AT_MOST_ONE)
    public @interface Language {
        @AnnotationMemberTypes.Text(maxLength = 10)
        public String coverlanguage() default "";

        @AnnotationMemberTypes.Text(maxLength = 10)
        public String contentlanguage() default "";

        @AnnotationMemberTypes.Text(maxLength = 10)
        public String authorlanguage() default "";
    }

    /**
     * A propertySet with EXACTLY_ONE
     * User must set one value, and only one value when instantiating controls declaratively;
     * It is unknow what will happens when instantiating controls programmatically.
     */
    @PropertySet
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @AnnotationConstraints.MembershipRule(AnnotationConstraints.MembershipRuleValues.EXACTLY_ONE)
    public @interface Intro {
        @AnnotationMemberTypes.Text(maxLength = 8)
        public String title() default "";

        @AnnotationMemberTypes.Text(maxLength = 8)
        public String subject() default "";

        @AnnotationMemberTypes.Text(maxLength = 8)
        public String content() default "";
    }


    /**
     * Negative tests: members without @AnnotationMemberTypes causes compile errors
     */

    @PropertySet
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @AnnotationConstraints.MembershipRule(AnnotationConstraints.MembershipRuleValues.AT_LEAST_ONE)
    public @interface Material {
        /*
        public Object paper;
        public Object cover;
        */
    }


    @PropertySet
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @AnnotationConstraints.MembershipRule(AnnotationConstraints.MembershipRuleValues.EXACTLY_ONE)
    public @interface Publisher {
        /*
        public Object paper;
        public Object cover;
        */
    }

    public String hello();
}
