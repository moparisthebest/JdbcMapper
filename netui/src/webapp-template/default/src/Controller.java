/*
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
import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * <p>
 * A controller class that contains logic, exception handlers, and state for the current
 * web directory path. When a request is received for the page flow (/Controller.jpf), an
 * action (/begin.do), or a page (/index.jsp), an instance of this class becomes the
 * <em>current page flow</em>. By default, it is stored in the session while its actions
 * and pages are being accessed, and is removed when another page flow is requested.
 * </p>
 * <p>
 * Properties in the current page flow may be accessed through the <code>pageFlow</code>
 * databinding context in pages and in expression-aware annotations. For example, if this
 * class contains a <code>getSomeProperty</code> method, it can be accessed through the
 * expression <code>${pageFlow.someProperty}</code>.
 * </p>
 * <p>
 * There may be only one page flow in any package.
 * </p>
 */
@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="shared", type=shared.SharedFlow.class)
    }
)
public class Controller 
    extends PageFlowController
{
    /**
     * Method that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
    }

    /**
     * Method that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }
}
