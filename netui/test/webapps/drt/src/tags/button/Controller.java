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
package tags.button;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="cancel", path="cancel.jsp")
    }
)
public class Controller extends PageFlowController
{
    SubmitForm _submitForm;

    String[] styles = new String[] {"Short", "Longboard",
            "Hybrid", "Fish"};
    public String[] getStyles() {
        return styles;
    }

    String defaultStyle = "Short";
    public String getDefaultStyle() {
        return defaultStyle;
    }

    @Jpf.Action(
        useFormBean="_submitForm",
        forwards={
            @Jpf.Forward(
                name = "success",
                path = "results.jsp"
            )
        }
    )
    protected Forward submitIt(SubmitForm form) {
        return new Forward("success", "form", form );
    }


    public static class SubmitForm implements Serializable
    {
        private String _shaper;
        private String _style;

        public String getShaper() {
            return _shaper;
        }

        public void setShaper(String value) {
            _shaper = value;
        }

        public String getStyle() {
            return _style;
        }

        public void setStyle(String value) {
            _style = value;
        }
    }
}
