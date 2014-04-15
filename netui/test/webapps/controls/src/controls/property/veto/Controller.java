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

package controls.property.veto;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.test.controls.property.BoundPropertyControl;
import org.apache.beehive.netui.test.controls.property.BoundPropertyControlBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

@Jpf.Controller(forwards = {@Jpf.Forward(name = "index", path = "../index.jsp")})
public class Controller extends PageFlowController {

    private boolean propertyChanged = false;
    private boolean vetoExceptionCaught = false;

    @Control
    private BoundPropertyControlBean _myControl;

    @Jpf.Action
    protected Forward begin() {
        return new Forward("index");
    }

    @Jpf.Action
    protected Forward testVetoChangeOnConstrainedProperty() {

        propertyChanged = false;
        vetoExceptionCaught = false;

        // Create a new test listener and register it on the test bean
        ChangeTestListener ctl = new ChangeTestListener();
        _myControl.addPropertyChangeListener(ctl);

        // Create a new test listener and register it on the test bean
        VetoableTestListener vtl = new VetoableTestListener();
        _myControl.addVetoableChangeListener(vtl);

        try {
            _myControl.setQuality("New_Quality");
        }
        catch (PropertyVetoException e) {
            vetoExceptionCaught = true;
        }

        if (!vetoExceptionCaught) {
            return new Forward("index", "message", "testVetoChangeOnConstrainedProperty.do: PropertyVetoException not caught.");
        }

        if (propertyChanged) {
            return new Forward("index", "message", "testVetoChangeOnConstrainedProperty.do: PropertyChanged listener is invoked.");
        }

        String theQuality = _myControl.getQuality();
        if (!theQuality.equals(BoundPropertyControl.QUALITY_DEFAULT)) {
            return new Forward("index", "message", "testVetoChangeOnConstrainedProperty.do:" +
                    "Property value changed to:" + theQuality + ". Although PropertyChangedEvent not received.");
        }
        return new Forward("index", "message", "testVetoChangeOnConstrainedProperty.do: PASSED");
    }

    @Jpf.Action
    protected Forward testVetoChangeOnUnConstrainedProperty() {

        propertyChanged = false;
        vetoExceptionCaught = false;

        // Create a new test listener and register it on the test bean
        ChangeTestListener ctl = new ChangeTestListener();
        _myControl.addPropertyChangeListener(ctl);

        // Create a new test listener and register it on the test bean
        VetoableTestListener vtl = new VetoableTestListener();
        _myControl.addVetoableChangeListener(vtl);

        _myControl.setBrand("New_Brand");

        if (vetoExceptionCaught) {
            return new Forward("index", "message", "testVetoChangeOnUnConstrainedProperty.do: PropertyVetoException caught.");
        }

        if (!propertyChanged) {
            return new Forward("index", "message", "testVetoChangeOnUnConstrainedProperty.do: PropertyChanged listener is NOT invoked.");
        }

        String theBrand = _myControl.getBrand();
        if (!theBrand.equals("New_Brand")) {
            return new Forward("index", "message", "testVetoChangeOnUnConstrainedProperty.do: " +
                    "Property value changed to:" + theBrand + " afterPropertyChangedEvent not received.");
        }
        return new Forward("index", "message", "testVetoChangeOnUnConstrainedProperty.do: PASSED");
    }

    @Jpf.Action
    protected Forward testVetoChangeOnConstrainedExtProperty() {

        propertyChanged = false;
        vetoExceptionCaught = false;

        // Create a new test listener and register it on the test bean
        ChangeTestListener ctl = new ChangeTestListener();
        _myControl.addPropertyChangeListener(ctl);

        // Create a new test listener and register it on the test bean
        VetoableTestListener vtl = new VetoableTestListener();
        _myControl.addVetoableChangeListener(vtl);

        try {
            _myControl.setHeight(7.9f);
        }
        catch (PropertyVetoException e) {
            vetoExceptionCaught = true;
        }

        if (!vetoExceptionCaught) {
            return new Forward("index", "message", "testVetoChangeOnConstrainedExtProperty.do: PropertyVetoException not caught.");
        }

        if (propertyChanged) {
            return new Forward("index", "message", "testVetoChangeOnUnConstrainedProperty.do: PropertyChanged listener is invoked.");
        }

        float height = _myControl.getHeight();
        if (height != 0.0f) {
            return new Forward("testVetoChangeOnConstrainedExtProperty: Property value changed to:" + height
                    + ". Although PropertyChangedEvent not received.");
        }
        return new Forward("index", "message", "testVetoChangeOnConstrainedExtProperty.do: PASSED");
    }

    @Jpf.Action
    protected Forward testVetoChangeOnUnConstrainedExtProperty() {
        propertyChanged = false;
        vetoExceptionCaught = false;

        // Create a new test listener and register it on the test bean
        ChangeTestListener ctl = new ChangeTestListener();
        _myControl.addPropertyChangeListener(ctl);

        // Create a new test listener and register it on the test bean
        VetoableTestListener vtl = new VetoableTestListener();
        _myControl.addVetoableChangeListener(vtl);

        _myControl.setAge(22);

        if (vetoExceptionCaught) {
            return new Forward("index", "message", "testVetoChangeOnUnConstrainedExtProperty.do: PropertyVetoException caught.");
        }

        if (!propertyChanged) {
            return new Forward("index", "message", "testVetoChangeOnUnConstrainedExtProperty.do: PropertyChanged listener is NOT invoked.");
        }

        int age = _myControl.getAge();
        if (age != 22) {
            return new Forward("index", "message", "testVetoChangeOnUnConstrainedExtProperty.do: Property is changed to an unexpected value.");
        }
        return new Forward("index", "message", "testVetoChangeOnUnConstrainedExtProperty.do: PASSED");
    }

    class ChangeTestListener implements java.beans.PropertyChangeListener {
        /**
         * Implementation of PropertyChangeListener.propertyChange().
         * Record all the chages
         */
        public void propertyChange(PropertyChangeEvent pce) {
            //record it
            propertyChanged = true;
        }
    }

    class VetoableTestListener implements VetoableChangeListener {
        /**
         * Implementation of PropertyChangeListener.propertyChange().
         * Veto all the change
         */
        public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
            // Veto attempts to set even values
            throw new PropertyVetoException("Sorry", pce);
        }
    }

}
