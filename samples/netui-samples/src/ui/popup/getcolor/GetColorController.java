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
package ui.popup.getcolor;

import java.io.Serializable;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    nested = true,
    simpleActions = {
    @Jpf.SimpleAction(name = "begin", path = "index.jsp"),
    @Jpf.SimpleAction(name = "cancel", returnAction = "getColorCancel")
        }
)
public class GetColorController extends PageFlowController {
    private ColorForm _returnFormBean;

    @Jpf.Action(
        useFormBean = "_returnFormBean",
        forwards = {
        @Jpf.Forward(
            name = "confirm",
            path = "confirm.jsp",
            actionOutputs = {
            @Jpf.ActionOutput(name = "chosenColor", type = String.class, required = true)
                }
        )
            }
    )
    public Forward submitColor(ColorForm form) {
        Forward fwd = new Forward("confirm");
        fwd.addActionOutput("chosenColor", form.getColor());
        return fwd;
    }

    @Jpf.Action(
        forwards = {
        @Jpf.Forward(name = "success", returnAction = "getColorSuccess", outputFormBeanType = ColorForm.class)
            }
    )
    public Forward done(ColorForm form) {
        return new Forward("success", _returnFormBean);
    }

    public static class ColorForm
        implements Serializable {
        private String _color;

        public String getColor() {
            return _color;
        }

        public void setColor(String value) {
            _color = value;
        }
    }
}
