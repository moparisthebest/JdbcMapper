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
package bugs.b15590;

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
    public static class Form implements Serializable
    {
    String[] _strings = new String[4];
	String _name = "Name";

	public Form() {
	    super();
	}

	public Form(String[] strings) {
	    _strings = strings;
	}
	
	public String[] getStrings() {
	    return _strings;
	}
	public void setStrings(String[] strings) {
	    _strings = strings;
	}

	public String getName() {
	    return _name;
	}
	public void setName(String name) {
	    _name = name;
	}
    }

    String[] _strings = {"String 1", "String 2", "String 3", "String 4"};
    public String[] getStrings() {
	return _strings;
    }
    public void setStrings(String[] strings) {
	_strings = strings;
    }


    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward postback(Form form)
    {
	_strings = form.getStrings();
        return new Forward("begin",form);
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward("begin",new Form(_strings));
    }
}
