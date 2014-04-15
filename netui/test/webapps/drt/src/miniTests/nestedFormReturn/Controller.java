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
package miniTests.nestedFormReturn;

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
    private String _value;

    public String getValue() {
	return _value;
    }
    public void setValue(String value) {
	_value = value;
    }

    public static class Form implements Serializable
    {
	String _val;
	public String getVal() {
	    return _val;
	}
	public void setVal(String val) {
	    _val = val;
	}
    }

    /**
     * @jpf:action
     * @jpf:forward name="nest" path="nest1/Controller.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nest",
                path = "nest1/Controller.jpf") 
        })
    public Forward nest1()
    {
	return new Forward("nest");
    }

    /**
     * @jpf:action
     * @jpf:forward name="nest" path="nest2/Controller.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nest",
                path = "nest2/Controller.jpf") 
        })
    public Forward nest2()
    {
	return new Forward("nest");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward done(miniTests.nestedFormReturn.nest1.Controller.Form form)
    {
	_value = "Name: " + form.getLastName() + ", " + form.getFirstName();
	return new Forward("begin");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward done(miniTests.nestedFormReturn.nest2.Controller.Form form)
    {
	_value = "Type: " + form.getType();
	return new Forward("begin");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward done(Form form)
    {
	_value = "Value: " + form.getVal();
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
