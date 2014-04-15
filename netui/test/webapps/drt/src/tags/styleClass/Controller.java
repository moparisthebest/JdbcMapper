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
package tags.styleClass;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * @jpf:forward name="begin" path="Begin.jsp"
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
public class Controller extends PageFlowController
{
    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward("begin");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward postback(Form form)
    {
        return new Forward("begin");
    }

    public static class Form implements Serializable
    {
	private String _text = "";
	private String _textArea = "";
        private boolean _checkBox1 = false;
        private String[] _checkBoxGroup;
        private String[] _checkBoxGroup2;
        private String _select = "2";
        private String _radio = "Choice 2";
        private String _radio2 = "Check 3";
        private String _hidden = "hidden";

	public String getText() {
	    return _text;
	}
	public void setText(String text) {
	    _text = text;
	}

	public String getTextArea() {
	    return _textArea;
	}
	public void setTextArea(String textArea) {
	    _textArea = textArea;
	}

	public boolean getCheckBox1() {
	    return _checkBox1;
	}
	public void setCheckBox1(boolean checkBox1) {
	    _checkBox1 = checkBox1;
	}

	public String[] getCheckBoxGroup() {
	    return _checkBoxGroup;
	}
	public void setCheckBoxGroup(String checkBoxGroup[]) {
	    _checkBoxGroup = checkBoxGroup;
	}

	public String[] getCheckBoxGroup2() {
	    return _checkBoxGroup2;
	}
	public void setCheckBoxGroup2(String checkBoxGroup[]) {
	    _checkBoxGroup2 = checkBoxGroup;
	}

        public String[] getCheckOptions() {
            return new String[] {"Check 1", "Check 2", "Check 3"};
        }

	public String getSelect() {
	    return _select;
	}
	public void setSelect(String select) {
	    _select = select;
	}

	public String getRadio() {
	    return _radio;
	}
	public void setRadio(String radio) {
	    _radio = radio;
	}

	public String getRadio2() {
	    return _radio2;
	}
	public void setRadio2(String radio) {
	    _radio2 = radio;
	}

        
	public String getHidden() {
	    return _hidden;
	}
	public void setHidden(String hidden) {
	    _hidden = hidden;
	}

    }
}
