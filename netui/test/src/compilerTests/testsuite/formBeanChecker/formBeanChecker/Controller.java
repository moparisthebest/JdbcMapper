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
package formBeanChecker;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.Forward;

import java.io.Serializable;

@Jpf.Controller(
    validatableBeans={
        @Jpf.ValidatableBean(
            type=Controller.MyBean.class,
            validatableProperties={
                @Jpf.ValidatableProperty(
                    propertyName="id",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateMinLength=@Jpf.ValidateMinLength(chars=2)
                )
            }
        )
    },
    simpleActions = {
        @Jpf.SimpleAction(name = "begin", path = "index.jsp")
    })
public class Controller extends PageFlowController {

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward testExternalFormBean(TestFormBean form) {
        Forward forward = new Forward("success");
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward testExternalFormBeanAgain(TestFormBean form) {
        Forward forward = new Forward("success");
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward testExternalBean(TestBean form) {
        Forward forward = new Forward("success");
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward testExternalAgainBean(TestBean form) {
        Forward forward = new Forward("success");
        return forward;
    }


    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward testInnerFormBean(MyFormBean form) {
        Forward forward = new Forward("success");
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward testInnerFormBeanAgain(MyFormBean form) {
        Forward forward = new Forward("success");
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward testInnerBean(MyBean form) {
        Forward forward = new Forward("success");
        return forward;
    }

    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="index.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward testInnerBeanAgain(MyBean form) {
        Forward forward = new Forward("success");
        return forward;
    }

    // also test inner form bean class...
    @Jpf.FormBean()
    public static class MyFormBean implements Serializable {
        private String name = "Form Bean";
        private String id = "12345";

        @Jpf.ValidatableProperty(
            displayName = "name",
            validateType = @Jpf.ValidateType(type=String.class)
        )
        public String getName() { return name; }
        public void setName(String n) { name = n; }

        @Jpf.ValidatableProperty(
            propertyName = "id",
            displayName = "ID",
            validateType=@Jpf.ValidateType(type=int.class)
        )
        public String getId() { return id; }
        public void setId(String i) { id = i; }
    }

    // also test inner class...
    public static class MyBean implements Serializable {
        private String name = "Form One";
        private String id = "12345";

        @Jpf.ValidatableProperty(
            displayName = "name",
            validateType = @Jpf.ValidateType(type=String.class)
        )
        public String getName() { return name; }
        public void setName(String n) { name = n; }

        @Jpf.ValidatableProperty(
            propertyName = "id",
            displayName = "ID",
            validateType=@Jpf.ValidateType(type=int.class)
        )
        public String getId() { return id; }
        public void setId(String i) { id = i; }
    }
}
