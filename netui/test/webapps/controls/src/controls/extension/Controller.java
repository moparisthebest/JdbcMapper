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

package controls.extension;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.netui.test.controls.extension.ExtensibleControl;
import org.apache.beehive.netui.test.controls.extension.SubControl;
import org.apache.beehive.netui.test.controls.extension.SubControlBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * Test control inheritance by invoking methods on a sub control instance
 */
@Jpf.Controller(forwards = {@Jpf.Forward(name = "result", path = "result.jsp")})
public class Controller extends PageFlowController {

    private boolean superClassEventReceived = false;
    private boolean subClassEventReceived = false;

    @Control
    public SubControlBean _subcontrol;

    /**
     * EventHandler that receives SuperClassEvent from _subcontrol
     */
    @EventHandler(field = "_subcontrol", eventSet = SubControl.SuperClassEvent.class, eventName = "method1")
    public void subcontrolMessageHandler() {
        superClassEventReceived = true;
    }

    /**
     * EventHandler that receives SubClassEvent from _subcontrol
     */
    @EventHandler(field = "_subcontrol", eventSet = SubControl.SubClassEvent.class, eventName = "method1")
    public void subcontrolMessageHandler2() {
        subClassEventReceived = true;
    }

    @Jpf.Action
    protected Forward begin() {
        return new Forward("result");
    }

    /**
     * Invokes an inherited method on a _subcontrol instantiated declaratively
     */
    @Jpf.Action()
    protected Forward testInheritedMethod() {
        String s = _subcontrol.hello();
        if (!s.equals("Hello from super control")) {
            return new Forward("result", "message", "testInheritedMethod.do: ERROR: " + s);
        }
        return new Forward("result", "message", "testInheritedMethod: PASSED");
    }

    /**
     * Invokes an inherited method on a _subcontrol instantiated programmatically
     */
    @Jpf.Action()
    protected Forward testInheritedMethodP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");

            String s = subbean.hello();
            if (!s.equals("Hello from super control")) {
                return new Forward("result", "message", "testInheritedMethodP.do: ERROR: " + s);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testInheritedMethodP: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testInheritedMethodP: PASSED");
    }

    /**
     * Invokes an extended method on a _subcontrol instantiated declaratively
     */
    @Jpf.Action()
    protected Forward testExtendedMethod() {
        String s = _subcontrol.hello2();
        if (s == null || !s.equals("Hello from _subcontrol")) {
            return new Forward("result", "message", "testExtendedMethod.do: ERROR: " + s);
        }
        return new Forward("result", "message", "testExtendedMethod: PASSED");
    }

    /**
     * Invokes an extended method on a _subcontrol instantiated programmatically
     */
    @Jpf.Action()
    protected Forward testExtendedMethodP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");

            String s = subbean.hello2();
            if (s == null || !s.equals("Hello from _subcontrol")) {
                return new Forward("result", "message", "testExtendedMethodP.do: ERROR: " + s);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testExtendedMethodP: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testExtendedMethodP: PASSED");
    }


    @Jpf.Action()
    protected Forward testGetInheritedPropertyByContext() {
        String s = _subcontrol.accessInheritedProperty();
        if (s == null || !s.equals("In_ExtensibleControl_Interface")) {
            return new Forward("result", "message", "testGetInheritedPropertyByContext.do: ERROR: " + s);
        }
        return new Forward("result", "message", "testGetInheritedPropertyByContext.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testGetInheritedPropertyByContextP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");

            String s = subbean.accessInheritedProperty();
            if (s == null || !s.equals("In_ExtensibleControl_Interface")) {
                return new Forward("result", "message", "testGetInheritedPropertyByContextP.do: ERROR: " + s);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testGetInheritedPropertyByContext.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testGetInheritedPropertyByContext.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testGetInheritedPropertyByGetter() {

        String position = _subcontrol.getPosition();
        if (position == null || !position.equals(ExtensibleControl.CURRENT_POSITION)) {
            return new Forward("result", "message", "testGetInheritedPropertyByGetter.do: ERROR: The property from getter:Position=" + position);
        }
        return new Forward("result", "message", "testGetInheritedPropertyByGetter.do: PASSED");
    }


    @Jpf.Action()
    protected Forward testGetInheritedPropertyByGetterP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");
            String position = subbean.getPosition();
            if (position == null || !position.equals(ExtensibleControl.CURRENT_POSITION)) {
                return new Forward("result", "message", "testGetInheritedPropertyByGetterP.do: ERROR: The property from getter:Position=" + position);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testGetInheritedPropertyByGetter.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testGetInheritedPropertyByGetter.do: PASSED");
    }


    @Jpf.Action()
    protected Forward testSetInheritedPropertyBySetter() {
        _subcontrol.setPosition("A_NEW_POSITION");
        _subcontrol.setLayer("A_NEW_LAYER");

        String position = _subcontrol.accessInheritedProperty();
        if (position == null || !position.equals("A_NEW_POSITION")) {
            return new Forward("result", "message", "testSetInheritedPropertyBySetter.do: ERROR: The property from setter:Position=" + position);
        }
        return new Forward("result", "message", "testSetInheritedPropertyBySetter.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testSetInheritedPropertyBySetterP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");

            subbean.setPosition("A_NEW_POSITION");
            subbean.setLayer("A_NEW_LAYER");
            String position = subbean.accessInheritedProperty();
            if (position == null || !position.equals("A_NEW_POSITION")) {
                return new Forward("result", "message", "testSetInheritedPropertyBySetterP.do: ERROR: The property from setter:Position=" + position);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testSetInheritedPropertyBySetterP.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testSetInheritedPropertyBySetterP.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testGetExtendedPropertyByContext() {

        String new_property_value = _subcontrol.getExtendedPropertyByContext();
        if (new_property_value == null || !new_property_value.equals("New Property Declared by Sub Control")) {
            return new Forward("result", "message", "testGetExtendedPropertyByContext.do: ERROR: Extended property retrieved from context:" + new_property_value);
        }
        return new Forward("result", "message", "testGetExtendedPropertyByContext.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testGetExtendedPropertyByContextP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");

            String new_property_value = subbean.getExtendedPropertyByContext();
            if (new_property_value == null || !new_property_value.equals("New Property Declared by Sub Control")) {
                return new Forward("result", "message", "testGetExtendedPropertyByContextP.do: ERROR: Extended property retrieved from context:" + new_property_value);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testGetExtendedPropertyByContextP.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testGetExtendedPropertyByContextP.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testGetExtendedPropertyByGetter() {
        String s = _subcontrol.getMessage();
        if (s == null || !s.equals(SubControl.A_MESSAGE)) {
            return new Forward("result", "message", "testGetExtendedPropertyByGetter.do: ERROR: " + s);
        }
        return new Forward("result", "message", "testGetExtendedPropertyByGetter.do: PASSED");
    }


    @Jpf.Action()
    protected Forward testGetExtendedPropertyByGetterP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");
            String s = subbean.getMessage();
            if (s == null || !s.equals(SubControl.A_MESSAGE)) {
                return new Forward("result", "message", "testGetExtendedPropertyByGetterP.do: ERROR: " + s);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testGetExtendedPropertyByGetterP.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testGetExtendedPropertyByGetter.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testSetExtendedPropertyBySetter() {
        _subcontrol.setMessage("NEW_VALUE_FOR_EXTENDED_PROPERTY");
        String the_new_value = _subcontrol.getExtendedPropertyByContext();
        if (!the_new_value.equals("NEW_VALUE_FOR_EXTENDED_PROPERTY")) {
            return new Forward("result", "message", "testSetExtendedPropertyBySetter.do: ERROR: The result value: " + the_new_value);
        }
        return new Forward("result", "message", "testSetExtendedPropertyBySetter.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testSetExtendedPropertyBySetterP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");

            subbean.setMessage("NEW_VALUE_FOR_EXTENDED_PROPERTY");
            String the_new_value = subbean.getExtendedPropertyByContext();
            if (!the_new_value.equals("NEW_VALUE_FOR_EXTENDED_PROPERTY")) {
                return new Forward("result", "message", "testSetExtendedPropertyBySetter.do: ERROR: The result value: " + the_new_value);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testSetExtendedPropertyBySetterP.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testSetExtendedPropertyBySetterP.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testGetReconfiguredPropertyByContext() {
        _subcontrol.setMessage("NEW_VALUE_FOR_EXTENDED_PROPERTY");
        String the_new_value = _subcontrol.getExtendedPropertyByContext();
        if (!the_new_value.equals("NEW_VALUE_FOR_EXTENDED_PROPERTY")) {
            return new Forward("result", "message", "testGetReconfiguredPropertyByContext.do: ERROR: The result value: " + the_new_value);
        }
        return new Forward("result", "message", "testGetReconfiguredPropertyByContext.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testGetReconfiguredPropertyByContextP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");

            subbean.setMessage("NEW_VALUE_FOR_EXTENDED_PROPERTY");
            String the_new_value = subbean.getExtendedPropertyByContext();
            if (!the_new_value.equals("NEW_VALUE_FOR_EXTENDED_PROPERTY")) {
                return new Forward("result", "message", "testGetReconfiguredPropertyByContextP.do: ERROR: The result value: " + the_new_value);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testGetReconfiguredPropertyByContextP.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testGetReconfiguredPropertyByContext.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testGetReconfiguredPropertyByGetter() {
        String layer = _subcontrol.getLayer();
        if (!layer.equals("On_SubControl_Interface_Layer")) {
            return new Forward("result", "message", "testGetReconfiguredPropertyByGetter.do: ERROR: The property from getter:layer=" + layer);
        }
        return new Forward("result", "message", "testGetReconfiguredPropertyByGetter.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testGetReconfiguredPropertyByGetterP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");
            String layer = subbean.getLayer();
            if (!layer.equals("On_SubControl_Interface_Layer")) {
                return new Forward("result", "message", "testGetReconfiguredPropertyByGetterP.do: ERROR: The property from getter:layer=" + layer);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testGetReconfiguredPropertyByGetterP.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testGetReconfiguredPropertyByGetterP.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testSetReconfiguredPropertyBySetter() {
        _subcontrol.setLayer("NEW_VALUE_FOR_LAYER");
        try {
            String the_layer = _subcontrol.getLayerByContext();
            if (!the_layer.equals("NEW_VALUE_FOR_LAYER")) {
                return new Forward("result", "message", "testSetReconfiguredPropertyBySetter.do: ERROR: " + the_layer);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testSetReconfiguredPropertyBySetter.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testSetReconfiguredPropertyBySetter.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testSetReconfiguredPropertyBySetterP() {

        try {
            SubControlBean subbean = (SubControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.extension.SubControlBean");
            subbean.setLayer("NEW_VALUE_FOR_LAYER");

            String the_layer = _subcontrol.getLayerByContext();
            if (!the_layer.equals("NEW_VALUE_FOR_LAYER")) {
                return new Forward("result", "message", "testSetReconfiguredPropertyBySetterP.do: ERROR: " + the_layer);
            }
        }
        catch (Exception e) {
            return new Forward("result", "message", "testSetReconfiguredPropertyBySetterP.do: Exception: " + e.getMessage());
        }
        return new Forward("result", "message", "testSetReconfiguredPropertyBySetter.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testInvokeExtendedEvent() {

        int result = _subcontrol.invokeExtendedEventFromSubControl();
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            // noop
        }

        if (result != 0) {
            return new Forward("result", "message", "testInvokeExtendedEvent.do: ERROR: The event on subcontrol was not triggered.");
        }
        if (!subClassEventReceived) {
            return new Forward("result", "message", "testInvokeExtendedEvent.do: ERROR: The extended event is NOT received.");
        }
        return new Forward("result", "message", "testInvokeExtendedEvent.do: PASSED");
    }

    @Jpf.Action()
    protected Forward testInvokeInheritedEvent() {

        int result = _subcontrol.invokeInheritedEventFromSubControl();
        if (result != 0) {
            return new Forward("result", "message", "testInvokeInheritedEvent.do: ERROR: The event on subcontrol was not triggered.");
        }
        if (!superClassEventReceived) {
            return new Forward("result", "message", "testInvokeExtendedEvent.do: ERROR: The inherited event is NOT received.");
        }
        return new Forward("result", "message", "testInvokeInheritedEvent.do: PASSED");
    }

}
