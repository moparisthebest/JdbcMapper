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
package pageFlowCore.inheritance.sharedFlow;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import pageFlowCore.inheritance.sharedFlow.supersf.SuperSharedFlow;

@Jpf.Controller(
    inheritLocalPaths = true,
    simpleActions={
        @Jpf.SimpleAction(name="simpleSharedFlowAction", path="success.jsp")
    }
)
public class SharedFlow extends SuperSharedFlow
{
    @Jpf.Action(
        loginRequired = false,
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "success.jsp")
        })
    public Forward sharedFlowAction() {
        return new Forward("success");
    }
}
