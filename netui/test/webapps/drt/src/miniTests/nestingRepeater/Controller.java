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
package miniTests.nestingRepeater;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller
public class Controller extends PageFlowController
{
    public static class Info implements java.io.Serializable {
	String name;
	int[] values;

	public String getName() {
	    return name;
	}

	public int[] getValues() {
	    return values;
	}

	public String getEnd() {
	    return "end";
	}
    }

    Info[] _info;

    public Info[] getData() {
	return _info;
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
	_info = new Info[2];
	for (int i=0;i<_info.length;i++)
	    _info[i] = new Info();
	_info[0].name = "foo";
	_info[0].values = new int[] {2,4,5};
	_info[1].name = "bar";
	_info[1].values = new int[] {12,14,15};

        return new Forward("begin");
    }
}
