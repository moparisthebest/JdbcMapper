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
package pageFlowCore.inheritActions.derived;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="delegateSimple1", action="simpleAction"),
        @Jpf.SimpleAction(name="delegateMethod1", action="methodAction")
    }
)
public class Controller extends pageFlowCore.inheritActions.base.Controller
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="delegateSimple", action="simpleAction")
        }
    )
    public Forward delegateSimple2()
    {
        return new Forward( "delegateSimple" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="delegateMethod", action="methodAction")
        }
    )
    public Forward delegateMethod2()
    {
        return new Forward( "delegateMethod" );
    }
}
