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
package pageFlowCore.inheritance.forwards.override;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    inheritLocalPaths=true,
    forwards = {
        @Jpf.Forward(name = "global1",
                     path = "success.jsp")
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }    
)
public class ForwardController extends pageFlowCore.inheritance.forwards.parent.Controller
{
    @Jpf.Action
    protected Forward myForward1(){
        return new Forward("global1");
    }

    @Jpf.Action
    protected Forward myForward2(){
        return new Forward("global2");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    protected Forward localToGlobal1HasActionLevelForward(){
        return new Forward("global1");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    protected Forward localToGlobal2HasActionLevelForward(){
        return new Forward("global2");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    protected Forward useActionLevelForward(){
        return new Forward("success");
    }
}

