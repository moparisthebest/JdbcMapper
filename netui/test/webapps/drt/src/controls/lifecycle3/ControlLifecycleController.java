/**
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 $Header:$
 */
package controls.lifecycle3;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.test.controls.container.ContainerControl;

/**
 *
 */
@Jpf.Controller(
    forwards = {@Jpf.Forward(name="index", path="index.jsp")}
)
public class ControlLifecycleController
    extends PageFlowController {

    @Control
    private ContainerControl _control;

    private String _onCreateMsg;
    private String _beginMsg;

    public String getPropertyMsg() {
        //System.err.println("Inside the property Accessor");
        return  _control.echo("get property -- controls.lifecycle.ControlLifecycleController");
    }

    public void onCreate() {
        //System.err.println("Inside the Controller onCreate");
        //System.err.println("Context:" + _control.getInfo());
        _onCreateMsg = _control.echo("onCreate -- controls.lifecycle.ControlLifecycleController");
    }

    @Jpf.Action()
    public Forward begin() {
        _beginMsg = _control.echo("begin.do -- controls.lifecycle.ControlLifecycleController");
        getRequest().setAttribute("createMsg", _onCreateMsg);
        getRequest().setAttribute("beginMsg", _beginMsg);
        getRequest().setAttribute("info", _control.getInfo());
        return new Forward("index");
    }
}
