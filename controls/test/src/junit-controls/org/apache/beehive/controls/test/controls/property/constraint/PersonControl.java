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
 * A control interface that declares PropertySets with constraints
 */

@ControlInterface
public interface PersonControl {

    public final static double SAVINGS_MIN_VALUE = 100;
    public final static String ISSUEDATE_MAXVALUE = "2007/01/31";
    public final static String EXPIRYDATE_MINVALUE = "2007/02/28";
    //public final static String ISSUEDATE_MAXVALUE=null;

    /**
     * A propertySet with ALL_IF_ANY
     * Rule is enforced at build time when users instantiate the control declaratively.
     * It is unknow what will happens when instantiating controls programmatically.
     */
    @PropertySet
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @AnnotationConstraints.MembershipRule(AnnotationConstraints.MembershipRuleValues.ALL_IF_ANY)
    public @interface Address {
        @AnnotationMemberTypes.Text(maxLength = 8)
        public String street();

        @AnnotationMemberTypes.Text(maxLength = 8)
        public String city();

        @AnnotationMemberTypes.Text(maxLength = 8)
        public String province();

        @AnnotationMemberTypes.Int(minValue = 0, maxValue = 100000)
        public int zipcode() default 0;

    }

    @PropertySet
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @AnnotationConstraints.AllowExternalOverride
    public @interface Assets {
        //JIRA-203 AnnotationMemberTypes.Decimal should support float

        @AnnotationMemberTypes.Decimal(places = 2, minValue = SAVINGS_MIN_VALUE, maxValue = 10000)
        public String savings() default "0";
    }

    @PropertySet
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ID {
        @AnnotationMemberTypes.Text(isLong = true)
        public String idnumber() default "0";
    }


    @PropertySet
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DriverLicense {
        @AnnotationMemberTypes.Date(maxValue = ISSUEDATE_MAXVALUE)
        public String issuedate() default "";

        @AnnotationMemberTypes.Date(minValue = EXPIRYDATE_MINVALUE)
        public String expirydate() default "";

        /* Test passing a null to revokedate.
           Commented out temporarily
        @AnnotationMemberTypes.Date(minValue=ISSUEDATE_MAXVALUE)
        public String revokedate() default null;
        */
    }

    public String hello();

}
