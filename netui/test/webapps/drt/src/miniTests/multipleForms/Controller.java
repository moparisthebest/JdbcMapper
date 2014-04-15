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
package miniTests.multipleForms;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionForm;

@Jpf.Controller
public class Controller extends PageFlowController
{
    private String _action;
    private FormOne f1;
    private FormTwo f2;

    public String getAction() {
	return _action;
    }
    public void setAction(String action) {
	_action = action;
    }

    public static class FormOne extends ActionForm {
	private String name;
	public String getName() {
	    return name;
	}
	public void setName(String n) {
	    name = n;
	}
    }

    public static class FormTwo extends ActionForm {
	private String stuff;
	public String getStuff() {
	    return stuff;
	}
	public void setStuff(String s) {
	    stuff = s;
	}
    }

    /**
     * @jpf:action
     * @jpf:forward name="return" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                path = "Begin.jsp") 
        })
    public Forward formOne(FormOne form)
    {
	_action = "formOne";

	f1.setName(form.getName());

	Forward f = new Forward("return");
	f.addOutputForm(f1);
	f.addOutputForm(f2);

	return f;
    }

    /**
     * @jpf:action
     * @jpf:forward name="return" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                path = "Begin.jsp") 
        })
    public Forward formTwo(FormTwo form)
    {
	_action = "formTwo";

	f2.setStuff(form.getStuff());

	Forward f = new Forward("return");
	f.addOutputForm(f1);
	f.addOutputForm(f2);
	
        return f;
    }

    /**
     * @jpf:action
     * @jpf:forward name="begin" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    public Forward begin()
    {
	_action = "begin";
	Forward f = new Forward("begin");
	f1 = new FormOne();
	f1.setName("Name from Begin");

	f2 = new FormTwo();
	f2.setStuff("Stuff from Begin");
	
	f.addOutputForm(f1);
	f.addOutputForm(f2);
	
        return f;
    }
}
