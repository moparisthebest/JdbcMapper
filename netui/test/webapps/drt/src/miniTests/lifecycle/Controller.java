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
package miniTests.lifecycle;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpSession;

@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
public class Controller extends PageFlowController
{
    protected void onCreate() {
        HttpSession session = getSession();
        session.setAttribute("pageFlow",this);
        
        _lifecycle += "onCreate<br />";
    }

    protected void onDestroy(HttpSession session) {
        session.removeAttribute("pageFlow");
        
        System.err.println("inside onDestroy [lifecycle]");
        _lifecycle += "onDestroy<br />";
        globalApp.setResults(_lifecycle);
    }

    protected transient global.Global globalApp;

    String _lifecycle = "";
    public String getLifecycle() {
        return _lifecycle;
    }

    /**
     * Callback that occurs before any action execution.
     */ 
    protected void beforeAction()
    {
	_lifecycle += "beforeAction<br />";
    }
    
    /**
     * Callback that occurs after any action execution.
     */ 
    protected void afterAction()
    {
	_lifecycle += "afterAction<br />";
    }    

    @Jpf.Action(
        )
    protected Forward begin(){
	_lifecycle += "begin<br />";
        return new Forward("begin");
    }

    @Jpf.Action(
        )
    protected Forward postback(){
	_lifecycle += "postback<br />";
        return new Forward("begin");
    }
}


