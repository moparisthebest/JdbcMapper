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
package ui.select;

import java.util.Map;
import java.util.LinkedHashMap;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.samples.controls.pets.Pets;
import org.apache.beehive.samples.netui.beans.PetType;
import org.apache.beehive.controls.api.bean.Control;

@Jpf.Controller(
    simpleActions=@Jpf.SimpleAction(name="begin",path="index.jsp")
    )
public class Controller
    extends PageFlowController {

    @Control
    private Pets _petControl;

    private Map _colorOptions = null;
    private Map _colorOptionsIntKeys = null;

    private PetType[] _pets = null;

    protected void onCreate()
        throws Exception {
        _colorOptions = new LinkedHashMap();
        _colorOptions.put("blue", "Blue");
        _colorOptions.put("orange", "Orange");
        _colorOptions.put("white", "White");
        _colorOptions.put("black", "Black");

        _colorOptionsIntKeys = new LinkedHashMap();
        _colorOptionsIntKeys.put(1, "Blue");
        _colorOptionsIntKeys.put(2, "Orange");
        _colorOptionsIntKeys.put(3, "White");
        _colorOptionsIntKeys.put(4, "Black");

        _pets = _petControl.getPetList();
    }

    @Jpf.Action(forwards=
        @Jpf.Forward(
            name="success",
            path="simpleSelect.jsp",
            actionOutputs={
                @Jpf.ActionOutput(name="colorMap", type=Map.class)
            }
    ))
    public Forward showSimple() {
        Forward forward = new Forward("success");
        forward.addActionOutput("colorMap", _colorOptions);
        return forward;
    }

    @Jpf.Action(forwards=
        @Jpf.Forward(
            name="success", path="simpleSelect.jsp",
            actionOutputs = {
                @Jpf.ActionOutput(name="colorMap", type=Map.class)
            }
        )
    )
    public Forward updateSimple(ColorForm form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("colorMap", _colorOptions);
        return forward;
    }

    @Jpf.Action(forwards=
        @Jpf.Forward(
            name="success",
            path="simpleSelectWithIntKeys.jsp",
            actionOutputs={
                @Jpf.ActionOutput(name="colorMapWithIntKeys", type=Map.class)
            }
    ))
    public Forward showSimpleWithIntKeys() {
        Forward forward = new Forward("success");
        forward.addActionOutput("colorMapWithIntKeys", _colorOptionsIntKeys);
        return forward;
    }

    @Jpf.Action(forwards=
        @Jpf.Forward(
            name="success", path="simpleSelectWithIntKeys.jsp",
            actionOutputs = {
                @Jpf.ActionOutput(name="colorMapWithIntKeys", type=Map.class)
            }
        )
    )
    public Forward updateSimpleWithIntKeys(ColorIndexForm form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("colorMapWithIntKeys", _colorOptionsIntKeys);
        return forward;
    }

    @Jpf.Action(forwards=
        @Jpf.Forward(
            name="success",
            path="repeatingSelect.jsp",
            actionOutputs={
                @Jpf.ActionOutput(name="petList", type=PetType.class)
            }
    ))
    public Forward showRepeating() {
        Forward forward = new Forward("success");
        forward.addActionOutput("petList", _pets);
        return forward;
    }

    @Jpf.Action(forwards=
        @Jpf.Forward(
            name="success", path="repeatingSelect.jsp",
            actionOutputs = {
                @Jpf.ActionOutput(name="petList", type=PetType.class)
            }
        )
    )
    public Forward updateRepeating(PetForm form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("petList", _pets);
        return forward;
    }

    @Jpf.Action(forwards=
        @Jpf.Forward(
            name="success",
            path="repeatingSelectWithOptionBody.jsp",
            actionOutputs={
                @Jpf.ActionOutput(name="petList", type=PetType.class)
            }
    ))
    public Forward showRepeatingWithOptionBody() {
        Forward forward = new Forward("success");
        forward.addActionOutput("petList", _pets);
        return forward;
    }

    @Jpf.Action(forwards=
        @Jpf.Forward(
            name="success", path="repeatingSelectWithOptionBody.jsp",
            actionOutputs = {
                @Jpf.ActionOutput(name="petList", type=PetType.class)
            }
        )
    )
    public Forward updateRepeatingWithOptionBody(PetForm form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("petList", _pets);
        return forward;
    }

    public static class ColorForm {
        private String _color = null;

        public String getColor() {
            return _color;
        }

        public void setColor(String color) {
            _color = color;
        }
    }

    public static class ColorIndexForm {
        private Integer _colorIndex = null;

        public Integer getColorIndex() {
            return _colorIndex;
        }

        public void setColorIndex(Integer color) {
            _colorIndex = color;
        }
    }

    public static class PetForm {
        private String _name = null;

        public String getName() {
            return _name;
        }

        public void setName(String name) {
            _name = name;
        }
    }
}