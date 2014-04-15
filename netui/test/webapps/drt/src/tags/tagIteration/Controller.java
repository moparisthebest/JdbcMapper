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
package tags.tagIteration;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

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
    // Checkbox Group
    private String[] _checkGroup;
    public void setCheckGroup(String[] checkGroup) {
	_checkGroup = checkGroup;
    }
    public String[] getCheckGroup() {
	return _checkGroup;
    }

    private String[] _defaultCheckGroup = {"Default Check 1", "Default Check 2"};
    public String[] getDefaultCheckGroup() {
	return _defaultCheckGroup;
    }

    // Select
    private String _defaultSelect = "Default Option";
    public String getDefaultSelect() {
	return _defaultSelect;
    }

    private String _select;
    public void setSelect(String select) {
	_select = select;
    }
    public String getSelect() {
	return _select;
    }

    // Radio Group
    private String _defaultRadioGroup = "Default Radio";
    public String getDefaultRadioGroup() {
	return _defaultRadioGroup;
    }

    private String _radioGroup;
    public void setRadioGroup(String radioGroup) {
	_radioGroup = radioGroup;
    }
    public String getRadioGroup() {
	return _radioGroup;
    }

    private String _options = "Option 1";
    public String getOptions() {
	return _options;
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward postback()
    {
	return new Forward("begin");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward("begin");
    }
}
