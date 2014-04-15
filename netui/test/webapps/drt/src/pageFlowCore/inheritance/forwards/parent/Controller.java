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
package pageFlowCore.inheritance.forwards.parent;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    forwards = {
        @Jpf.Forward(name = "global1",
                     path = "results.jsp"),
        @Jpf.Forward(name = "global2",
                     path = "results2.jsp")
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }    
)
public class Controller extends PageFlowController
{
    @Jpf.Action
    protected Forward toGlobalForward1(){
        return new Forward("global1");
    }

    @Jpf.Action
    protected Forward toGlobalForward2(){
        return new Forward("global2");
    }


    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    protected Forward toGlobal1HasActionLevelForward(){
        return new Forward("global1");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    protected Forward toGlobal2HasActionLevelForward(){
        return new Forward("global2");
    }
}
