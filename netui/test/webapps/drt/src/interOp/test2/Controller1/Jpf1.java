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
package interOp.test2.Controller1;

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
     * @jpf:forward name="jpf1.jsp" path="Jpf1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "jpf1.jsp",
                path = "Jpf1.jsp") 
        })
    protected Forward begin()
    {

        //System.out.println("Test2: In Jpf1.jpf");
        return new Forward("jpf1.jsp");
    }
}
