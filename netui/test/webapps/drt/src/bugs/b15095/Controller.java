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
package bugs.b15095;

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
    // CheckBox
    private boolean _defaultCheck = true;
    public boolean getDefaultCheck() {
	return _defaultCheck;
    }

    private String _value = "";
    public String getValue() {
	return _value;
    }

    public static class Form implements Serializable
    {
	// Checkbox
	private String _check;
	public void setCheck(String check) {
	    _check = check;
	}
	public String getCheck() {
	    return _check;
	}
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward begin(){
        return new Forward("begin");
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
    protected Forward postback(Form form)
    {
        _value = form.getCheck();
        return new Forward("begin",form);
    }
}


