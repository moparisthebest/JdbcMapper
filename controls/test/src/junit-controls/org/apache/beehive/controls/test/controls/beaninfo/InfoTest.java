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

package org.apache.beehive.controls.test.controls.beaninfo;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.packaging.FeatureAttribute;
import org.apache.beehive.controls.api.packaging.FeatureInfo;
import org.apache.beehive.controls.api.packaging.ManifestAttribute;
import org.apache.beehive.controls.api.packaging.PropertyInfo;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A simple test class that tests using JSR-175 annotations to inject BeanInfo attributes
 * for a Control type.
 */
@ControlInterface
/* THE B51 VANILLA APT PROCESSOR BARFS ON THIS RIGHT NOW
@ManifestAttributes (value={
    @ManifestAttribute(name="Attribute1", value="Value1"),
    @ManifestAttribute(name="Attribute2", value="Value2"),
    @ManifestAttribute(name="Attribute3", value="Value3")
}) */
@ManifestAttribute(name = "Attribute1", value = "Value1")
@FeatureInfo(
        name = "%org.apache.beehive.controls.test.controls.beaninfo.InfoTestBundle.name%",
        displayName = "%org.apache.beehive.controls.test.controls.beaninfo.InfoTestBundle.displayName%",
        attributes =
                {
                @FeatureAttribute(name = "fa1", value = "fv1"),
                @FeatureAttribute(name = "fa2", value = "fv2")
                        })
public interface InfoTest {
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    public @interface TestProps {
        @PropertyInfo(bound = true, constrained = false)
        @FeatureInfo(
                name = "%org.apache.beehive.controls.test.controls.beaninfo.InfoTestBundle.prop1.name%",
                displayName = "InfoTest prop1")
        public int prop1() default 0;

        public boolean prop2() default false;
    }

    @EventSet(unicast = true)
    public interface TestEvents {
        @FeatureInfo(name = "InfoTest eventMethod1", displayName = "InfoTest eventMethod1",
                     isHidden = true, isExpert = true)
        public void eventMethod1();

        public int eventMethod2(String stringArg);
    }

    @FeatureInfo(
            name = "infoTestMethod name",
            displayName = "%org.apache.beehive.controls.test.controls.beaninfo.InfoTestBundle.infoTestMethod.displayName%",
            isHidden = true,
            isExpert = true,
            attributes =
                    {
                    @FeatureAttribute(name = "methodFA1", value = "methodFV2"),
                    @FeatureAttribute(name = "methodFA2", value = "methodFV2")
                            })
    public void infoTestMethod(int anIntArg, Class aClassArg);
}
