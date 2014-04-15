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
package validation;

import java.io.Serializable;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;

/**
 * Demonstration of Page Flow declarative field validation.
 */
@Jpf.Controller(
    simpleActions = {
    @Jpf.SimpleAction(name = "begin", path = "index.jsp")
        },
    messageBundles = {
    @Jpf.MessageBundle(bundlePath = "org.apache.beehive.samples.netui.resources.validation.messages")
        }
)
public class Controller extends PageFlowController {
    /**
     * Basic submit action -- uses validation rules defined on MyForm.
     */
    @Jpf.Action(
        forwards = {
        @Jpf.Forward(name = "success", path = "success.jsp")
            },
        validationErrorForward = @Jpf.Forward(name = "fail", navigateTo = Jpf.NavigateTo.currentPage)
    )
    public Forward submit(MyForm form) {
        Forward fwd = new Forward("success");
        fwd.addActionOutput("value", form.getValue());
        return fwd;
    }

    /**
     * This action adds an additional validation rule for the 'value' property.
     */
    @Jpf.Action(
        forwards = {
        @Jpf.Forward(name = "success", path = "success.jsp")
            },
        validationErrorForward = @Jpf.Forward(name = "fail", navigateTo = Jpf.NavigateTo.currentPage),

        // This is the extra validation rule.
        validatableProperties = {
        @Jpf.ValidatableProperty(
            propertyName = "value",
            validateMaxLength = @Jpf.ValidateMaxLength(chars = 4, messageKey = "errors.toolong")
        )
            }
    )
    public Forward submitWithExtraRule(MyForm form) {
        Forward fwd = new Forward("success");
        fwd.addActionOutput("value", form.getValue());
        return fwd;
    }

    /**
     * The form bean class.  Note that form beans do <i>not</i> have to be inner classes; validation
     * annotations work on external form bean classes, too.
     * <p/>
     * This form bean class uses message resources from the page flow's message bundle (defined
     * inside the @Jpf.Controller annotation).  A form bean class can also define its <i>own</i>
     * message bundle through the @Jpf.FormBean annotation.
     */
    @Jpf.FormBean()
    public static class MyForm
        implements Serializable {
        private String _value;
        private String _confirmValue;

        @Jpf.ValidatableProperty(
            displayNameKey = "displayname.value",
            validateRequired = @Jpf.ValidateRequired(),
            validateMinLength = @Jpf.ValidateMinLength(chars = 3)
        )
        public String getValue() {
            return _value;
        }

        public void setValue(String value) {
            _value = value;
        }

        @Jpf.ValidatableProperty(
            displayNameKey = "displayname.confirmvalue",
            validateRequired = @Jpf.ValidateRequired(),
            validateValidWhen = @Jpf.ValidateValidWhen(
                condition = "${actionForm.confirmValue == actionForm.value}",
                messageKey = "errors.nomatch"
            )
        )
        public String getConfirmValue() {
            return _confirmValue;
        }

        public void setConfirmValue(String confirmValue) {
            _confirmValue = confirmValue;
        }
    }
}
