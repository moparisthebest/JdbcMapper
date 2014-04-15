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
package tags.popupFormBean;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import tags.popupFormBean.getModel.GetModel;

import java.io.Serializable;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    String[] styles = new String[] {GetModel.SHORT, GetModel.LONGBOARD, 
            GetModel.HYBRID, GetModel.FISH, GetModel.SOFT_TOP};
    public String[] getStyles() {
        return styles;
    }

    String defaultStyle = "Short";
    public String getDefaultStyle() {
        return defaultStyle;
    }

    String[] colors =
        new String[] {"Red", "Blue", "Green", "Yellow", "White", "Black"};
    public String[] getColors() {
        return colors;
    }

    String defaultColor = "White";
    public String getDefaultColor() {
        return defaultColor;
    }

    @Jpf.Action()
    protected Forward modelSuccess( GetModel.ModelForm model ) {
        return new Forward( "_auto" );
    }

    @Jpf.Action()
    protected Forward modelCancel() {
        return new Forward( "_auto" );  // THIS IS THE MAGIC GLOBAL FORWARD
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="continue",
                path = "getModel/displayModel.do"
            )
        }
    )
    protected Forward getModel(SubmitForm form) {
        GetModel.ModelForm modelForm = new GetModel.ModelForm();
        modelForm.setStyle(form.getStyle());

    	return new Forward("continue", modelForm);
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name = "success",
                path = "results.jsp"
            )
        }
    )
    protected Forward submit(SubmitForm form) {
        return new Forward("success", "form", form );
    }


    public static class SubmitForm implements Serializable
    {
        private String _dealer;
        private String _style;
        private String _model;
        private String _color;

        public String getDealer() {
            return _dealer;
        }

        public void setDealer(String value) {
            _dealer = value;
        }

        public String getStyle() {
            return _style;
        }

        public void setStyle(String value) {
            _style = value;
        }

        public String getModel() {
            return _model;
        }

        public void setModel(String value) {
            _model = value;
        }

        public String getColor() {
            return _color;
        }

        public void setColor(String value) {
            _color = value;
        }
    }
}
