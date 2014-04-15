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
package bugs.b15392.nesting;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * @jpf:controller nested="true"
 * @jpf:forward name="begin" path="Begin.jsp"
 */
@Jpf.Controller(
    nested = true,
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
public class Controller extends PageFlowController
{
    public static class Form implements Serializable
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
	
	public String toString() {
	    return _lastName + ", " + _firstName;
	}
    }

    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="done" return-form-type="Form"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "done",
                returnAction = "done",
                outputFormBeanType = Form.class) 
        })
    public Forward submit(Form form)
    {
	return new Forward("done",form);
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward begin(Form form)
    {
	return new Forward("begin",form);
    }
}
