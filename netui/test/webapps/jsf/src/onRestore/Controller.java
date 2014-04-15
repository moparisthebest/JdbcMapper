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
package onRestore;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="go2", path="page2.faces"),
        @Jpf.SimpleAction(name="goCurrentNoNewMessage", navigateTo=Jpf.NavigateTo.currentPage)
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="page1",
                path="page1.faces",
                actionOutputs={
                    @Jpf.ActionOutput(name="message", type=String.class, required=true)
                }
            )
        }
    )
    public Forward begin()
    {
        return new Forward("page1", "message", "Got action output from begin().");
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="currentPage",
                navigateTo=Jpf.NavigateTo.currentPage,
                actionOutputs={
                    @Jpf.ActionOutput(name="message", type=String.class, required=true)
                }
            )
        }
    )
    public Forward goCurrent()
    {
        return new Forward("currentPage", "message", "Got action output from goCurrent().");
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="prevPage",
                navigateTo=Jpf.NavigateTo.previousPage,
                actionOutputs={
                    @Jpf.ActionOutput(name="message", type=String.class, required=true)
                }
            )
        }
    )
    public Forward goPrev()
    {
        return new Forward("prevPage", "message", "Got action output from goPrev().");
    }
}
