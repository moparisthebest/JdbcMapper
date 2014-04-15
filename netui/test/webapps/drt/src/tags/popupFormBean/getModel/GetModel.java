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
package tags.popupFormBean.getModel;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller(
    nested=true,
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class GetModel extends PageFlowController
{
    // Silly data for this test case
    public static final String SHORT = "Short";
    public static final String LONGBOARD = "Longboard";
    public static final String HYBRID = "Hybrid";
    public static final String FISH = "Fish";
    public static final String SOFT_TOP = "Soft Top";

    private static String[] shortboards = {
        "6'0", "6'2", "6'4", "6'6", "6'8", "7'0", "7'6"
    };

    private static String[] longboards = {
        "9'0 tri", "9'0 classic", "9'2 tri", "9'6 tri",
        "9'6 classic", "10'0 tri", "10'6 classic"
    };

    private static String[] hybrids = {
        "6'6", "6'8", "7'0", "7'6"
    };

    private static String[] fish = {
        "6'0", "6'2", "6'4", "6'6", "6'8"
    };

    private static String[] soft = {
        "9'0 tri", "9'6 tri", "10'0 tri"
    };

    String _style = SHORT;
    public String getStyle() {
        return _style;
    }

    String[] _models = shortboards;
    public String[] getModels() {
        return _models;
    }

    String _defaultModel = shortboards[0];
    public String getDefaultModel() {
        return _defaultModel;
    }

    /**
     * This is the model code and state that will be returned in {@link #done}.
     */
    private ModelForm _modelForm;

    @Jpf.Action(
        useFormBean="_modelForm",
        forwards={
            @Jpf.Forward(name="success", path="index.jsp")
        }
    )
    protected Forward displayModel(ModelForm form) {
        String style = form.getStyle();
        if (style != null && style.length() > 0) {
            _style = style;
        }

        if (_style.equals(LONGBOARD)) {
            _models = longboards;
        } else if (_style.equals(HYBRID)) {
            _models = hybrids;
        } else if (_style.equals(FISH)) {
            _models = fish;
        } else if (_style.equals(SOFT_TOP)) {
            _models = soft;
        } else {
            _models = shortboards;
        }

        _defaultModel = _models[0];

        Forward fwd = new Forward("success");
        return fwd;
    }

    @Jpf.Action(
        useFormBean="_modelForm",
        forwards={
            @Jpf.Forward(
                name="done",
                returnAction="modelSuccess",
                outputFormBean="_modelForm"
            )
        }
    )
    protected Forward done(ModelForm form) {
    	return new Forward("done");
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="done", returnAction="modelCancel")
        }
    )
    protected Forward cancel(ModelForm form) {
        return new Forward("done");
    }

    public static class ModelForm implements Serializable
    {
        private String _style;
        private String _model;

        public ModelForm() {
            _style = SHORT;
        }

        public String getStyle() {
            return _style;
        }

        public void setStyle(String style) {
            _style = style;
        }

        public String getModel() {
            return _model;
        }

        public void setModel(String model) {
            _model = model;
        }
    }
}
