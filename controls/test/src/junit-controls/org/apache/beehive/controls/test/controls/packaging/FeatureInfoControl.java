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

package org.apache.beehive.controls.test.controls.packaging;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.packaging.FeatureAttribute;
import org.apache.beehive.controls.api.packaging.FeatureInfo;

/**
 * A control interface with FeatureInfo annotations
 */

@ControlInterface
@FeatureInfo(
        displayName = "A Control to test packaging",
        shortDescription = "This control is to test control packaging",
        isExpert = true,
        isHidden = true,
        isPreferred = true,
        attributes = {
        @FeatureAttribute(name = "FirstFeatureOfThisControl", value = "valueOf the first feature"),
        @FeatureAttribute(name = "SecondFeatureOfThisControl", value = "valueOf the second feature")
                }

)
public interface FeatureInfoControl {

    @FeatureInfo(
            displayName = "The only method on this control",
            shortDescription = "The feature info about the method on the control",
            isExpert = true,
            isHidden = true,
            isPreferred = true,
            attributes = {
            @FeatureAttribute(name = "FirstFeatureOfThisMethod", value = "valueOf the first feature of the method"),
            @FeatureAttribute(name = "SecondFeatureOfThisMethod", value = "valueOf the second feature of the method")
                    }

    )
    public String hello(String input);
}