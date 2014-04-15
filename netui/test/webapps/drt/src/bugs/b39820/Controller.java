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
package bugs.b39820;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;


@Jpf.Controller()
public class Controller extends PageFlowController {
    
    private String prop1;
    private String prop2;
    private String formValue;

    public String getProp1() {
        return prop1;
    }
    public String getProp2() {
        return prop2;
    }
    public String getFormValue() {
        return formValue;
    }


    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "sample.jsp") 
        })
    protected Forward begin()
    {
        return new Forward( "success" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "sampleResult.jsp") 
        })
    protected Forward theAction(theFormBean form) {
        prop1 = form.getProp1();
        prop2 = form.getProp2();
        HttpServletRequest req = getRequest();
        formValue = req.getParameter("form");
        return new Forward("success");
    }




    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class theFormBean extends ActionForm
    {
        private String prop2;

        private String prop1;


        public void setProp1(String prop1)
        {
            this.prop1 = prop1;
        }

        public String getProp1()
        {
            return this.prop1;
        }

        public void setProp2(String prop2)
        {
            this.prop2 = prop2;
        }

        public String getProp2()
        {
            return this.prop2;
        }
    }
}
