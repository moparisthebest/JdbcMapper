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
package Jira1090;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "aaa",
                path = "./subdir/Controller.jpf")
        })
    public Forward aaa() {
        Forward fwd = new Forward("aaa");
        fwd.addActionOutput("name", "aaa");
        return fwd;
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "bbb",
                path = "../Jira1090/subdir/Controller.jpf")
        })
    public Forward bbb() {
        Forward fwd = new Forward("bbb");
        fwd.addActionOutput("name", "bbb");
        return fwd;
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "ccc",
                path = ".././Jira1090/subdir/Controller.jpf")
        })
    public Forward ccc() {
        Forward fwd = new Forward("ccc");
        fwd.addActionOutput("name", "ccc");
        return fwd;
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "ddd",
                path = "/Jira1090/subdir/Controller.jpf")
        })
    public Forward ddd() {
        Forward fwd = new Forward("ddd");
        fwd.addActionOutput("name", "ddd");
        return fwd;
    }
}
