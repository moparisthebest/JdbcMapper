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
package interOp.test5.Controller2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * PageFlow class generated from control Connie
 * @jpf:controller nested="true"
 */
@Jpf.Controller(
    nested = true)
public class JpfNestable extends PageFlowController
{
    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="struts1Do" path="/interOp/tests/test5/gotoStrutsJSP.do"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "struts1Do",
                path = "/interOp/tests/test5/gotoStrutsJSP.do") 
        })
    protected Forward begin()
    {
        //System.out.println("In Nestable Page Flow 2");
        return new Forward("struts1Do");
    }

    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="done"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "done",
                returnAction = "done") 
        })
    public Forward exitProperly()
    {
        return new Forward( "done" );
    }
}
