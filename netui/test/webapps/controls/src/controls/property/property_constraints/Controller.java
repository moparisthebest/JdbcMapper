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

package controls.property.property_constraints;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.test.controls.property.PropertyConstraintControlBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * Tests control property constraint validator
 */
@Jpf.Controller(forwards = {@Jpf.Forward(name = "index", path = "../index.jsp")})
public class Controller extends PageFlowController {

    @Control
    private PropertyConstraintControlBean myControl;

    @Jpf.Action()
    protected Forward begin() {
        return new Forward("index");
    }

    @Jpf.Action()
    protected Forward propertyConstraint() {

        // Assigning a valid value to a text property
        try {
            myControl.setName("Bob");
        }
        catch (IllegalArgumentException e) {
            return new Forward("index", "message", "propertyConstraint.do: ERROR: " + e.getMessage());
        }

        // Assigning a value longer than a text property's max length
        try {
            myControl.setName("Some name longer than 10 characters");
            return new Forward("index", "message", "propertyConstraint.do: ERROR: " +
                    "Assigning a value longer than a text property's max length does not generate an exception.");
        }
        catch (IllegalArgumentException e) {
            // expected exception
        }

        // Assigning a valid date to a date property
        try {
            myControl.setDob("1990/2/12");
        }
        catch (IllegalArgumentException e) {
            return new Forward("index", "message", "propertyConstraint.do: ERROR: " +
                    "Assigning a valid date to a date property generated an exception.");
        }

        // Assigning an invalid date to a date property
        try {
            myControl.setName("Jan 12, 1993");
            return new Forward("index", "message", "propertyConstraint.do: ERROR: " +
                    "Assigning a value with the an incorrect date format to a date property does not generate an exception.");
        }
        catch (IllegalArgumentException e) {
            //expected exception
        }

        // Assigning a valid date to a date property
        try {
            myControl.setDob("1990/2/12");
        }
        catch (IllegalArgumentException e) {
            return new Forward("index", "message", "propertyConstraint.do: ERROR: " +
                    "Assigning a valid date to a date property generated an exception.");
        }

        // Assigning an invalid number to an int property
        try {
            myControl.setAge(-1);
            return new Forward("index", "message", "propertyConstraint.do: ERROR: " +
                    "Assigning a value less than an int property's min. value does not generate an exception.");
        }
        catch (IllegalArgumentException e) {
            //expected exception
        }
        return new Forward("index", "message", "propertyConstraint.do: PASSED");
    }
}
