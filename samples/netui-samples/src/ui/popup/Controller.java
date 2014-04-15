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
package ui.popup;

import java.io.Serializable;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import ui.popup.getcolor.GetColorController;

@Jpf.Controller(
    simpleActions = {
    @Jpf.SimpleAction(name = "begin", path = "index.jsp"),
    @Jpf.SimpleAction(name = "getColor", path = "getcolor/GetColorController.jpf"),
    @Jpf.SimpleAction(name = "getColorCancel", forwardRef = "_auto")
        },
    messageBundles = {
    @Jpf.MessageBundle(bundlePath = "org.apache.beehive.samples.netui.resources.popup.messages")
        }
)
public class Controller
    extends PageFlowController {
    /**
     * Returning the special "_auto" Forward causes the popup window to close, and automatically
     * runs the JavaScript to map return values from the nested page flow into fields on the
     * original page.  The simple action "getColorCancel" shows another way to return the "_auto"
     * forward.
     */
    @Jpf.Action()
    public Forward getColorSuccess(GetColorController.ColorForm colorForm) {
        return new Forward("_auto");
    }

    @Jpf.Action(
        forwards = {
        @Jpf.Forward(name = "success", path = "show.jsp")
            },
        validationErrorForward = @Jpf.Forward(name = "failure", navigateTo = Jpf.NavigateTo.currentPage)
    )
    public Forward submit(SubmitForm form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("form", form);
        return forward;
    }


    @Jpf.FormBean()
    public static class SubmitForm
        implements Serializable {

        private String _name;
        private String _address;
        private String _city;
        private String _color;

        @Jpf.ValidatableProperty(
            validateRequired = @Jpf.ValidateRequired(messageKey = "errors.nameRequired")
        )
        public String getName() {
            return _name;
        }

        public void setName(String value) {
            _name = value;
        }

        public String getAddress() {
            return _address;
        }

        public void setAddress(String address) {
            _address = address;
        }

        public String getCity() {
            return _city;
        }

        public void setCity(String city) {
            _city = city;
        }

        @Jpf.ValidatableProperty(
            validateRequired = @Jpf.ValidateRequired(messageKey = "errors.colorRequired")
        )
        public String getColor() {
            return _color;
        }

        public void setColor(String color) {
            _color = color;
        }
    }
}
