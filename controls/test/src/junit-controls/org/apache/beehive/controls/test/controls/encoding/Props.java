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

package org.apache.beehive.controls.test.controls.encoding;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.bean.ExternalPropertySets;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.packaging.PropertyInfo;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.beans.PropertyChangeEvent;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A simple control that can be used for property testing of basic primitive properties,
 * as well as nested an array property types.
 */
@ControlInterface
@ExternalPropertySets({ExtPropertySet.class})
public interface Props {
    //
    // A simple enumeration used to test enum annotations
    //
    public enum SimpleEnum {
        ChoiceA, ChoiceB, ChoiceC; }

    //
    // Define static final constants for SimpleProps defaults
    //
    static final int INT_DEFAULT = 87;
    static final String STRING_DEFAULT = "Hello";
    static final Class CLASS_DEFAULT = Object.class;
    static final org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum ENUM_DEFAULT = org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum.ChoiceA;

    //
    //
    // Define a PropertySet that tests simple types
    //
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    public @interface SimpleProps {
        int simpleInt() default org.apache.beehive.controls.test.controls.encoding.Props.INT_DEFAULT;

        String simpleString() default org.apache.beehive.controls.test.controls.encoding.Props.STRING_DEFAULT;

        Class simpleClass() default Object.class;

        org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum simpleEnum() default org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum.ChoiceA;
    }

    //
    // Define static final constants for ArrayProps defaults
    //
    static final int [] ARRAY_INT_DEFAULT = {99, 33, 66, 22};
    static final String [] ARRAY_STRING_DEFAULT = {"How", "are", "you", ",", "today", "?"};
    static final Class [] ARRAY_CLASS_DEFAULT = {java.util.HashMap.class, java.util.Iterator.class};
    static final org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum [] ARRAY_ENUM_DEFAULT = {org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum.ChoiceB, org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum.ChoiceC};

    //
    // Define a PropertySet that tests array types
    //
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    public @interface ArrayProps {
        int [] arrayInt() default {99, 33, 66, 22};

        String [] arrayString() default {"How", "are", "you", ",", "today", "?"};

        Class [] arrayClass() default {java.util.HashMap.class, java.util.Iterator.class};

        org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum [] arrayEnum() default {org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum.ChoiceB, org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum.ChoiceC};
    }

    //
    // Define static final constants for SimpleProps defaults
    //
    static final int ANNOT_INT_DEFAULT = 9999999;
    static final String ANNOT_STRING_DEFAULT = "Hola";
    static final Class ANNOT_CLASS_DEFAULT = java.beans.Beans.class;
    static final org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum ANNOT_ENUM_DEFAULT = org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum.ChoiceC;

    //
    // Define a PropertySet that tests properties that are themselves annotation types
    //
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    public @interface TestAnnot {
        @PropertyInfo(bound = true)   // will generate PropertyChange events
                org.apache.beehive.controls.test.controls.encoding.Props.SimpleProps simpleAnnot()
                default @org.apache.beehive.controls.test.controls.encoding.Props.SimpleProps(
                simpleInt = org.apache.beehive.controls.test.controls.encoding.Props.ANNOT_INT_DEFAULT,
                simpleString = org.apache.beehive.controls.test.controls.encoding.Props.ANNOT_STRING_DEFAULT,
                simpleClass = java.beans.Beans.class,
                simpleEnum = org.apache.beehive.controls.test.controls.encoding.Props.SimpleEnum.ChoiceC);

        org.apache.beehive.controls.test.controls.encoding.Props.ArrayProps arrayAnnot();
    }

    //
    // Exposes PropertyChange events to an external client.
    //
    @EventSet
    public interface PropertyEvents {
        public void onChange(PropertyChangeEvent pce);
    }

    //
    // Define property keys to enable access to test members in a PropertyMap
    //
    public Annotation getControlPropertySet(Class propertySet);
}
