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
package bugs.b13797;


import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
@Jpf.Controller
public class Controller extends PageFlowController
{
    private String _string = "String";

    public String getString() {
        return _string;
    }
    
    public void setString(String string) {
	_string = string;
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "next1",
                path = "Page1.jsp") 
        })
    public Forward next1(FormBean bean)
    {
        return new Forward("next1");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    public Forward begin()
    {
        return new Forward("begin");
    }

    public static class FormBean implements Serializable
    {
        private String name;

        public String getName()
        { return this.name; }

        public void setName(String name)
        { this.name = name; }
    }

}
