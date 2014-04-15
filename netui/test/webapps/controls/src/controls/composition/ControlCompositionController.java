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

package controls.composition;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.test.controls.composition.InnerControl;
import org.apache.beehive.netui.test.controls.composition.InnerControlBean;
import org.apache.beehive.netui.test.controls.composition.OuterControlBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * Test control composition by invoking methods on a control that has nested controls
 */
@Jpf.Controller(
        forwards = {
        @Jpf.Forward(name = "result", path = "result.jsp")
                })
public class ControlCompositionController extends PageFlowController {

    @Control
    private OuterControlBean outercontrol;

    @Jpf.Action()
    protected Forward begin() {
        return new Forward("index.jsp");
    }

    /**
     * Verifies the instantiation of nested controls.
     * The outer control is instantiated declaratively.
     */
    @Jpf.Action()
    protected Forward testInstantiation() {
        InnerControlBean declaredNestedControl = outercontrol.getDeclaredNestedControl();
        if (declaredNestedControl == null) {
            return new Forward("result", "message", "testInstantiation.do: the nested control instantiated declaratively is NULL.");
        }

        InnerControlBean programNestedControl = outercontrol.instantiateNestedControlProgrammatically();
        if (programNestedControl == null) {
            return new Forward("result", "message", "testInstantiation.do: The nested control instantiated programmatically is NULL.");
        }

        return new Forward("result", "message", "testInstantiation.do: PASSED");
    }

    /**
     * Verifies the instantiation of nested controls.
     * The outer control is instantiated programmatically.
     */
    @Jpf.Action()
    protected Forward testInstantiationP() {

        OuterControlBean outerbean = null;
        try {
            outerbean = (OuterControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.composition.OuterControlBean");
        }
        catch (Exception e) {
            return new Forward("result", "message", "testInstantiationP.do: Instantiation failed.");
        }

        InnerControlBean declaredNestedControl = outerbean.getDeclaredNestedControl();
        if (declaredNestedControl == null) {
            return new Forward("result", "message",
                               "testInstantiateP.do: the nested control instantiated declaratively is NULL.");
        }

        InnerControlBean programNestedControl = outerbean.instantiateNestedControlProgrammatically();
        if (programNestedControl == null) {
            return new Forward("result", "message",
                               "testInstantiateP.do: The nested control instantiated programmatically is NULL.");
        }

        return new Forward("result", "message", "testInstantiateP.do: PASSED");
    }

    /**
     * Verifies the instantiation of nested control, if the property of the nested control is
     * is reconfigured when instantiated.
     * The outer control is instantiated declaratively.
     */
    @Jpf.Action()
    protected Forward testInstantiationWithProperty() {
        InnerControlBean declaredNestedControl = outercontrol.getDeclaredNestedControl2();
        if (declaredNestedControl == null) {
            return new Forward("result", "message",
                               "testInstantiationWithProperty.do: The nested control instantiated declaratively is NULL");
        }

        InnerControlBean programNestedControl = outercontrol.instantiateNestedControlWithProperty();
        if (programNestedControl == null) {
            return new Forward("result", "message", "testInstantiationWithProperty.do: Missing API:Controls. JIRA-BEEHIVE-113");
        }
        return new Forward("result", "message", "testInstantiationWithProperty.do: PASSED");
    }

    /**
     * Verifies the instantiation of nested control, if the property of the nested control is
     * is reconfigured when instantiated.
     * The outer control is instantiated programmatically.
     */
    @Jpf.Action()
    protected Forward testInstantiationWithPropertyP() {

        OuterControlBean outerbean = null;
        try {
            outerbean = (OuterControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.composition.OuterControlBean");
        }
        catch (Exception e) {
            return new Forward("result", "message", "testInstantiationWithPropertyP.do: Instantiation failed.");
        }

        InnerControlBean declaredNestedControl = outerbean.getDeclaredNestedControl2();
        if (declaredNestedControl == null) {
            return new Forward("result", "message",
                               "testInstantiationWithPropertyP.do: The nested control instantiated declaratively is NULL");
        }

        InnerControlBean programNestedControl = outerbean.instantiateNestedControlWithProperty();
        if (programNestedControl == null) {
            return new Forward("result", "message", "testInstantiationWithPropertyP.do: Missing API:Controls. JIRA-BEEHIVE-113");
        }
        return new Forward("result", "message", "testInstantiationWithPropertyP.do: PASSED");
    }

    /**
     * Tests getting control property from the context of the nested control.
     * The outer control is instantiated declaratively.
     */
    @Jpf.Action()
    protected Forward testGetPropertyByContext() {

        InnerControlBean declaredNestedControl = outercontrol.getDeclaredNestedControl();
        String property_value = declaredNestedControl.getNameFromContext();

        if (property_value == null || !property_value.equals("Bob")) {
            return new Forward("result", "message",
                               "testGetPropertyByContext.do: property retrieved from context is:" + property_value);
        }

        InnerControlBean programNestedControl = outercontrol.instantiateNestedControlProgrammatically();
        String property_value2 = programNestedControl.getNameFromContext();

        if (property_value2 == null || !property_value2.equals("Bob")) {
            return new Forward("result", "message",
                               "testGetPropertyByContext.do: property retrieved from context is:" + property_value);
        }

        return new Forward("result", "message", "testGetPropertyByContext.do: PASSED");
    }

    /**
     * Tests getting control property from the context of the nested control.
     * The outer control is instantiated programmatically.
     */
    @Jpf.Action()
    protected Forward testGetPropertyByContextP() {

        OuterControlBean outerbean = null;
        try {
            outerbean = (OuterControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.composition.OuterControlBean");
        }
        catch (Exception e) {
            return new Forward("result", "message", "testGetPropertyByContextP.do: Instantiation failed.");
        }

        InnerControlBean declaredNestedControl = outerbean.getDeclaredNestedControl();
        String property_value = declaredNestedControl.getNameFromContext();

        if (property_value == null || !property_value.equals("Bob")) {
            return new Forward("result", "message",
                               "testGetPropertyByContextP.do: property retrieved from context is:" + property_value);
        }

        InnerControlBean programNestedControl = outerbean.instantiateNestedControlProgrammatically();
        String property_value2 = programNestedControl.getNameFromContext();

        if (property_value2 == null || !property_value2.equals("Bob")) {
            return new Forward("result", "message",
                               "testGetPropertyByContextP.do: property retrieved from context is:" + property_value);
        }

        return new Forward("result", "message", "testGetPropertyByContextP.do: PASSED");
    }

    /**
     * Tests getting control property from getter method of the nested control bean.
     * The outer control is instantiated declaratively.
     */
    @Jpf.Action()
    protected Forward testGetPropertyByGetter() {

        InnerControlBean innerbean = outercontrol.getDeclaredNestedControl();
        String name = innerbean.getName();
        String job = innerbean.getJob();

        if (!name.equals(InnerControl.DEFAULT_NAME) || (job != null)) {
            return new Forward("result", "message",
                               "testGetPropertyByGetter.do: The property from getter:name=" + name + "and job=" + job);
        }

        InnerControlBean innerbean2 = outercontrol.getDeclaredNestedControl2();
        String name2 = innerbean2.getName();
        String job2 = innerbean2.getJob();

        if (job2 == null) {
            return new Forward("result", "message",
                               "testGetPropertyByGetter.do: The property reset at declaration is NULL.");
        }

        if (!name.equals(InnerControl.DEFAULT_NAME) || !job2.equals("farmer")) {
            return new Forward("result", "message",
                               "The property from getter:name=" + name2 + "and job=" + job2);
        }
        return new Forward("result", "message", "testGetPropertyByGetter.do: PASSED");
    }

    /**
     * Tests getting control property from getter method of the nested control bean.
     * The outer control is instantiated programmatically.
     */
    @Jpf.Action()
    protected Forward testGetPropertyByGetterP() {

        OuterControlBean outerbean = null;
        try {
            outerbean = (OuterControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.composition.OuterControlBean");
        }
        catch (Exception e) {
            return new Forward("result", "message", "testGetPropertyByGetterP.do: Instantiation failed.");
        }

        InnerControlBean innerbean = outerbean.getDeclaredNestedControl();
        String name = innerbean.getName();
        String job = innerbean.getJob();

        if (!name.equals(InnerControl.DEFAULT_NAME) || (job != null)) {
            return new Forward("result", "message",
                               "testGetPropertyByGetterP.do: The property from getter:name=" + name + "and job=" + job);
        }

        InnerControlBean innerbean2 = outerbean.getDeclaredNestedControl2();
        String name2 = innerbean2.getName();
        String job2 = innerbean2.getJob();

        if (job2 == null) {
            return new Forward("result", "message",
                               "testGetPropertyByGetterP.do: The property reset at declaration is NULL.");
        }

        if (!name.equals(InnerControl.DEFAULT_NAME) || !job2.equals("farmer")) {
            return new Forward("result", "message",
                               "testGetPropertyByGetterP.do: The property from getter:name=" + name2 + "and job=" + job2);
        }
        return new Forward("result", "message", "testGetPropertyByGetterP.do: PASSED");
    }

    /**
     * Tests setting control property using setter method of the nested control bean.
     * The outer control is instantiated declaratively.
     */
    @Jpf.Action()
    protected Forward testSetPropertyBySetter() {

        InnerControlBean innerbean = outercontrol.getDeclaredNestedControl();
        innerbean.setName("A_NEW_NAME");
        innerbean.setJob("A_NEW_JOB");

        String name = innerbean.getNameFromContext();
        String job = innerbean.getJobFromContext();

        if (!name.equals("A_NEW_NAME") || !job.equals("A_NEW_JOB")) {
            return new Forward("result", "message",
                               "testSetPropertyBySetter.do: The property from getter:name=" + name + "and job=" + job);
        }
        return new Forward("result", "message", "testSetPropertyBySetter.do: PASSED");
    }

    /**
     * Tests setting control property using setter method of the nested control bean.
     * The outer control is instantiated programmatically.
     */
    @Jpf.Action()
    protected Forward testSetPropertyBySetterP() {
        OuterControlBean outerbean = null;
        try {
            outerbean = (OuterControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.composition.OuterControlBean");
        }
        catch (Exception e) {
            return new Forward("result", "message", "testSetPropertyBySetterP.do: Instantiation failed.");
        }

        InnerControlBean innerbean = outerbean.getDeclaredNestedControl();
        innerbean.setName("A_NEW_NAME");
        innerbean.setJob("A_NEW_JOB");

        String name = innerbean.getNameFromContext();
        String job = innerbean.getJobFromContext();

        if (!name.equals("A_NEW_NAME") || !job.equals("A_NEW_JOB")) {
            return new Forward("result", "message",
                               "testSetPropertyBySetterP.do: The property from getter:name=" + name + "and job=" + job);
        }
        return new Forward("result", "message", "testSetPropertyBySetterP.do: PASSED");
    }

    /**
     * Verifies outer control receiving events raised by the nested control.
     * Outer control listens to the events by EventHandler.
     * The outer control is instantiated declaratively.
     */
    @Jpf.Action()
    protected Forward testEventHandler() {
        String wakeupResult = outercontrol.testActivityWakeup();
        String readMessageResult = outercontrol.testActivityReadMessage();
        String reportResult = outercontrol.testActivityReport();
        String shoppingResult = outercontrol.testActionShopping();
        String dostuffResult = outercontrol.testActionDostuff();

        if (wakeupResult.equals("0") && readMessageResult.equals("0") && reportResult.equals("0")
                && shoppingResult.equals("0") && dostuffResult.equals("0")) {
            return new Forward("result", "message", "testEventHandler.do: PASSED");
        }

        return new Forward("result", "message", "testEventHandlerP.do: ActivityWakeup:" + wakeupResult +
                ". ActivityReadMessage:" + readMessageResult +
                ". ActivityReport:" + reportResult +
                ". ActionShopping:" + shoppingResult +
                ". ActionDodtuff:" + dostuffResult);
    }

    /**
     * Verifies outer control receiving events raised by the nested control.
     * Outer control listens to the events by EventHandler.
     * The outer control is instantiated programmatically.
     */
    @Jpf.Action()
    protected Forward testEventHandlerP() {

        OuterControlBean outerbean = null;
        try {
            outerbean = (OuterControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.composition.OuterControlBean");
        }
        catch (Exception e) {
            return new Forward("result", "message", "testEventHandlerP.do: Instantiation failed.");
        }
        String wakeupResult = outerbean.testActivityWakeup();
        String readMessageResult = outerbean.testActivityReadMessage();
        String reportResult = outerbean.testActivityReport();
        String shoppingResult = outerbean.testActionShopping();
        String dostuffResult = outerbean.testActionDostuff();

        if (wakeupResult.equals("0") && readMessageResult.equals("0") && reportResult.equals("0")
                && shoppingResult.equals("0") && dostuffResult.equals("0")) {
            return new Forward("result", "message", "testEventHandlerP.do: PASSED");
        }

        return new Forward("result", "message", "testEventHandlerP.do: ActivityWakeup:" + wakeupResult +
                ". ActivityReadMessage:" + readMessageResult +
                ". ActivityReport:" + reportResult +
                ". ActionShopping:" + shoppingResult +
                ". ActionDodtuff:" + dostuffResult);
    }

    /**
     * Verifies outer control receiving events raised by the nested control.
     * Outer control listens to the events by EventListener.
     * The outer control is instantiated declaratively.
     */
    @Jpf.Action()
    protected Forward testEventListener() {

        String eventListener = outercontrol.testEventListener();
        if (!eventListener.equals("0")) {
            return new Forward("result", "message", "testEventListener.do: EventListener: " + eventListener);
        }

        return new Forward("result", "message", "testEventListener.do: PASSED");
    }

    /**
     * Verifies outer control receiving events raised by the nested control.
     * Outer control listens to the events by EventListener.
     * The outer control is instantiated programmatically.
     */
    @Jpf.Action()
    protected Forward testEventListenerP() {

        OuterControlBean outerbean = null;
        try {
            outerbean = (OuterControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.composition.OuterControlBean");
        }
        catch (Exception e) {
            return new Forward("result", "message", "testEventListenerP.do: Instantiation failed.");
        }

        String eventListener = outerbean.testEventListener();
        if (!eventListener.equals("0")) {
            return new Forward("result", "message", "testEventListenerP.do: EventListener: " + eventListener);
        }

        return new Forward("result", "message", "testEventListenerP.do: PASSED");
    }

    /**
     * Verifies outer control receiving events raised by the nested control.
     * Outer control listens to the events by an inner class listener.
     * The outer control is instantiated declaratively.
     */
    @Jpf.Action()
    protected Forward testInnerClassListener() {

        String innerClassListener = outercontrol.testInnerClassListener();
        if (!innerClassListener.equals("0")) {
            return new Forward("result", "message", "testInnerClassListener.do: InnerClassListener: " + innerClassListener);
        }
        return new Forward("result", "message", "testInnerClassListener.do: PASSED");
    }

    /**
     * Verifies outer control receiving events raised by the nested control.
     * Outer control listens to the events by an inner class listener.
     * The outer control is instantiated programmatically.
     */
    @Jpf.Action()
    protected Forward testInnerClassListenerP() {

        OuterControlBean outerbean = null;
        try {
            outerbean = (OuterControlBean) java.beans.Beans.instantiate(
                    Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.netui.test.controls.composition.OuterControlBean");
        }
        catch (Exception e) {
            return new Forward("result", "message", "testInnerClassListenerP.do: Instantiation failed.");
        }

        String innerClassListener = outerbean.testInnerClassListener();
        if (!innerClassListener.equals("0")) {
            return new Forward("result", "message", "testInnerClassListener.do: InnerClassListener: " + innerClassListener);
        }
        return new Forward("result", "message", "testInnerClassListener.do: PASSED");
    }
}
