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
package tags.styleOptions;

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
    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward("begin");
    }

    @Jpf.Action(
        )
    public Forward postback(Form form)
    {
        return new Forward("begin");
    }

    public static class Form implements Serializable
    {
        private String[] _checkBoxGroup;
        private String[] _checkBoxGroup1;
        private String[] _checkBoxGroup2;
        private String[] _checkBoxGroup3;

        private String _radio = "Radio 2";
        private String _radio1 = "Radio 1";
        private String _radio2 = "Radio 3";
        private String _radio3 = "Radio 1";

	public String[] getCheckBoxGroup() {
	    return _checkBoxGroup;
	}
	public void setCheckBoxGroup(String checkBoxGroup[]) {
	    _checkBoxGroup = checkBoxGroup;
	}

	public String[] getCheckBoxGroup1() {
	    return _checkBoxGroup1;
	}
	public void setCheckBoxGroup1(String checkBoxGroup[]) {
	    _checkBoxGroup1 = checkBoxGroup;
	}

	public String[] getCheckBoxGroup2() {
	    return _checkBoxGroup2;
	}
	public void setCheckBoxGroup2(String checkBoxGroup[]) {
	    _checkBoxGroup2 = checkBoxGroup;
	}

	public String[] getCheckBoxGroup3() {
	    return _checkBoxGroup3;
	}
	public void setCheckBoxGroup3(String checkBoxGroup[]) {
	    _checkBoxGroup3 = checkBoxGroup;
	}

        public String[] getCheckOptions() {
            return new String[] {"Check 1", "Check 2", "Check 3"};
        }

        public String[] getRadioOptions() {
            return new String[] {"Radio 1", "Radio 2", "Radio 3"};
        }

	public String getRadio() {
	    return _radio;
	}
	public void setRadio(String radio) {
	    _radio = radio;
	}

	public String getRadio1() {
	    return _radio1;
	}
	public void setRadio1(String radio) {
	    _radio1 = radio;
	}

	public String getRadio2() {
	    return _radio2;
	}
	public void setRadio2(String radio) {
	    _radio2 = radio;
	}

	public String getRadio3() {
	    return _radio3;
	}
	public void setRadio3(String radio) {
	    _radio3 = radio;
	}
    }
}
