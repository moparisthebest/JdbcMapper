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
package template.xhtml;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    private String _hiddenIn = "hidden input";
    private String[] _selectOptions = {"Select One", "Select Two", "Select Three"};
    private String _selectedValue;
    private String _hiddenValue;
    private String _search;

    public String getHiddenIn() {
	return _hiddenIn;
    }
    public String[] getSelectOptions() {
	return _selectOptions;
    }
    
    public String getSelectedValue() {
	return _selectedValue;
    }
    public void setSelectedValue(String value) {
	_selectedValue = value;
    }

    public String getHiddendValue() {
	return _hiddenValue;
    }
    public void setHiddenValue(String value) {
	_hiddenValue = value;
    }

    public String getSearch() {
	return _search;
    }
    public void setSearch(String value) {
	_search = value;
    }


    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="Test.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("index");
    }

    @Jpf.Action(forwards = { 
	@Jpf.Forward(name = "success", path = "Test.jsp")
    })
    protected Forward post()        {
        Forward forward = new Forward("success");
        
                return forward;
    }

    @Jpf.Action(forwards = { 
	@Jpf.Forward(name = "success", path = "Test.jsp")
    })
    protected Forward search()        {
        Forward forward = new Forward("success");
        
                return forward;
    }
}

