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
package interOp.test1.Controller1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

//-------------------------------------------------------
// See the TestHeader.jsp for the interop path
//-------------------------------------------------------
@Jpf.ViewProperties(
    value = { 
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
        "<view-properties>", 
        "<pageflow-object id='pageflow:/interOp/test1/Controller1/Jpf1.jpf'/>", 
        "<pageflow-object id='action:begin.do'/>", 
        "<pageflow-object id='action:/interOp/tests/test1/gotoStrutsJSP.do'/>", 
        "<pageflow-object id='forward:path#struts1Do#/interOp/tests/test1/gotoStrutsJSP.do#@action:begin.do@'/>", 
        "</view-properties>"
    }
)
@Jpf.Controller
public class Jpf1 extends PageFlowController
{
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "struts1Do",
                path = "/interOp/tests/test1/gotoStrutsJSP.do") 
        })
    protected Forward begin()
    {
        //System.out.println("In Page Flow 1");
        return new Forward("struts1Do");
    }
}
