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
package validation.useFormBean;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.Forward;

import java.io.Serializable;

/*
 * Test form bean validation when Action annotations set the useFormBean
 * attribute. See JIRA bug, BEEHIVE-1144.
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(name = "success", path = "results.jsp")
    },
    simpleActions = {
        @Jpf.SimpleAction(name = "begin", path = "index.jsp")
    },
    validatableBeans={
        @Jpf.ValidatableBean(
            type=validation.useFormBean.BeanThree.class,
            validatableProperties={
                @Jpf.ValidatableProperty(
                    propertyName="propertyD",
                    displayName="Property D",
                    validateMinLength=@Jpf.ValidateMinLength(chars=6)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="propertyE",
                    displayName="Property E",
                    validateMinLength=@Jpf.ValidateMinLength(chars=6)
                )
            }
        ),
        @Jpf.ValidatableBean(
            type=validation.useFormBean.BeanFour.class,
            validatableProperties={
                @Jpf.ValidatableProperty(
                    propertyName="propertyF",
                    displayName="Property F",
                    validateMaxLength=@Jpf.ValidateMaxLength(chars=10)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="propertyG",
                    displayName="Property G",
                    validateMaxLength=@Jpf.ValidateMaxLength(chars=10)
                )
            }
        ),
        @Jpf.ValidatableBean(
            type=validation.useFormBean.Controller.InnerBeanThree.class,
            validatableProperties={
                @Jpf.ValidatableProperty(
                    propertyName="propertyK",
                    displayName="Property K",
                    validateMinLength=@Jpf.ValidateMinLength(chars=6)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="propertyL",
                    displayName="Property L",
                    validateMinLength=@Jpf.ValidateMinLength(chars=6)
                )
            }
        ),
        @Jpf.ValidatableBean(
            type=validation.useFormBean.Controller.InnerBeanFour.class,
            validatableProperties={
                @Jpf.ValidatableProperty(
                    propertyName="propertyM",
                    displayName="Property M",
                    validateMaxLength=@Jpf.ValidateMaxLength(chars=10)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="propertyN",
                    displayName="Property N",
                    validateMaxLength=@Jpf.ValidateMaxLength(chars=10)
                )
            }
        )
    }
)
public class Controller extends PageFlowController {

    private BeanOne _beanOne;
    private BeanTwo _beanTwo;
    private BeanThree _beanThree;
    private BeanFour _beanFour;
    private InnerBeanOne _innerBeanOne;
    private InnerBeanTwo _innerBeanTwo;
    private InnerBeanThree _innerBeanThree;
    private InnerBeanFour _innerBeanFour;

    @Jpf.Action(
        useFormBean="_beanOne",
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testBeanOneU(BeanOne form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testBeanTwo(BeanTwo form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        useFormBean="_beanTwo",
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testBeanTwoU(BeanTwo form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        useFormBean="_beanThree",
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testBeanThreeU(BeanThree form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testBeanThree(BeanThree form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        useFormBean="_beanFour",
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testBeanFourU(BeanFour form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testBeanFour(BeanFour form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        useFormBean="_innerBeanOne",
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testInnerBeanOneU(InnerBeanOne form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testInnerBeanTwo(InnerBeanTwo form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        useFormBean="_innerBeanTwo",
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testInnerBeanTwoU(InnerBeanTwo form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        useFormBean="_innerBeanThree",
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testInnerBeanThreeU(InnerBeanThree form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testInnerBeanThree(InnerBeanThree form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        useFormBean="_innerBeanFour",
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testInnerBeanFourU(InnerBeanFour form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp")
    )
    public Forward testInnerBeanFour(InnerBeanFour form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("formname", form.getClass().getName());
        return forward;
    }

    @Jpf.FormBean()
    public static class InnerBeanOne implements java.io.Serializable {

        private String propertyH;

        @Jpf.ValidatableProperty(
            displayName = "Property H",
            validateMinLength=@Jpf.ValidateMinLength(chars=2)
        )
        public String getPropertyH() { return propertyH; }
        public void setPropertyH(String p) { propertyH = p; }
    }

    @Jpf.FormBean()
    public static class InnerBeanTwo implements java.io.Serializable {

        private String propertyI;
        private String propertyJ;

        @Jpf.ValidatableProperty(
            displayName = "Property I",
            validateMinLength=@Jpf.ValidateMinLength(chars=4)
        )
        public String getPropertyI() { return propertyI; }
        public void setPropertyI(String p) { propertyI = p; }

        @Jpf.ValidatableProperty(
            displayName = "Property J",
            validateMinLength=@Jpf.ValidateMinLength(chars=4)
        )
        public String getPropertyJ() { return propertyJ; }
        public void setPropertyJ(String p) { propertyJ = p; }
    }

    @Jpf.FormBean()
    public static class InnerBeanThree implements java.io.Serializable {

        private String propertyK;
        private String propertyL;

        public String getPropertyK() { return propertyK; }
        public void setPropertyK(String p) { propertyK = p; }

        public String getPropertyL() { return propertyL; }
        public void setPropertyL(String p) { propertyL = p; }
    }

    @Jpf.FormBean()
    public static class InnerBeanFour implements java.io.Serializable {

        private String propertyM;
        private String propertyN;

        @Jpf.ValidatableProperty(
            displayName = "Property M",
            validateMinLength=@Jpf.ValidateMinLength(chars=8)
        )
        public String getPropertyM() { return propertyM; }
        public void setPropertyM(String p) { propertyM = p; }

        @Jpf.ValidatableProperty(
            displayName = "Property N",
            validateMinLength=@Jpf.ValidateMinLength(chars=8)
        )
        public String getPropertyN() { return propertyN; }
        public void setPropertyN(String p) { propertyN = p; }
    }
}
