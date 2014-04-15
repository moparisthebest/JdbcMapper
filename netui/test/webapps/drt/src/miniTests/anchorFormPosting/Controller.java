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
package miniTests.anchorFormPosting;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionForm;

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
    private String _value = "[Default PageFlow Value]";

    public String getValue() {
	    return _value;
    }

    public static class Form extends ActionForm
    {
	String _lastName;
	String _firstName;

	public String getLastName() {
	    return _lastName;
	}
	public void setLastName(String lastName) {
	    _lastName = lastName;
	}

	public String getFirstName() {
	    return _firstName;
	}
	public void setFirstName(String firstName) {
	    _firstName = firstName;
	}
    }


    @Jpf.Action(
        )
    public Forward postback(Form form)
    {
        _value = form.getLastName() + ", " + form.getFirstName();
        return new Forward("begin");
    }

    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward("begin");
    }
}
