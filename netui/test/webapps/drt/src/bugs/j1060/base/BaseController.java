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
package bugs.j1060.base;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.controls.api.bean.Control;

import org.apache.beehive.netui.test.controls.pflifecycle.PfControlLifecycle;

@Jpf.Controller(
    forwards = {@Jpf.Forward(name="index", path="index.jsp")}
)
public class BaseController extends PageFlowController
{
    protected String _onCreateMsg;
    protected String _actionMsg;

    @Control
    PfControlLifecycle _control;

    protected String echo(String value) {
        return _control.echo(getName() + " - " + value);
    }

    protected String getName() {
        return getClass().getName();
    }

    public String getPropertyMsg() {
        return echo("Inside the property accessor");
    }

    protected void onCreate() throws Exception {
        _onCreateMsg = echo("Inside the Controller onCreate()");
    }

    @Jpf.Action()
    public Forward begin() {
        _actionMsg = echo("Inside the Controller action begin.do");
        getRequest().setAttribute("createMsg", _onCreateMsg);
        getRequest().setAttribute("actionMsg", _actionMsg);
        return new Forward("index");
    }

    @Jpf.Action()
    public Forward testAction() {
        _actionMsg = echo("Inside the Controller action testAction.do");
        getRequest().setAttribute("createMsg", _onCreateMsg);
        getRequest().setAttribute("actionMsg", _actionMsg);
        return new Forward("index");
    }
}
