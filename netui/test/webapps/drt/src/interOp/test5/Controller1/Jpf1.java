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
package interOp.test5.Controller1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

//-------------------------------------------------------
// See the TestHeader.jsp for the interop path
//-------------------------------------------------------
@Jpf.Controller
public class Jpf1 extends PageFlowController
{
    /**
     * @jpf:action
     * @jpf:forward name="nestableDo" path="/interOp/test5/Controller2/JpfNestable.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nestableDo",
                path = "/interOp/test5/Controller2/JpfNestable.jpf") 
        })
    protected Forward begin()
    {
        //System.out.println("In Page Flow 1");
        return new Forward("nestableDo");
    }

       /**
     * @jpf:action
     * @jpf:forward name="done" path="/interOp/test5/Done.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "done",
                path = "/interOp/test5/Done.jsp") 
        })
    public Forward jpf1Done()
    {
        return new Forward( "done" );
    }
}
