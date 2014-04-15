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
package bugs.j1141;

import java.io.Serializable;
 
import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
 
/**
 * A simple test controller to test different form types to see if
 * parameters are set automatically.
 */
@Jpf.Controller(
    simpleActions = {
        @Jpf.SimpleAction(name = "begin", path = "index.jsp")
    })
public class Controller extends PageFlowController {
    private static final long serialVersionUID = -1799474398L;
 
    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", action = "begin") })
    public Forward getFormData(MyFormData form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("inputValue", form.getSomeParam());
        return forward;
    }
 
    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", action = "begin") })
    public Forward getFormBean(MyFormBean form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("inputValue", form.getSomeParam());
        return forward;
    }
 
    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", action = "begin") })
    public Forward getSerializable(MySerializable form) {
        Forward forward = new Forward("success");
        forward.addActionOutput("inputValue", form.getSomeParam());
        return forward;
    }
 
    public static class MyFormData extends FormData {
        private static final long serialVersionUID = 1L;
 
        private String someParam;
 
        public String getSomeParam() {
            return someParam;
        }
 
        public void setSomeParam(String someParam) {
            this.someParam = someParam;
        }
    }
 
    @Jpf.FormBean
    public static class MyFormBean implements Serializable {
        private static final long serialVersionUID = 1L;
 
        private String someParam;
 
        public String getSomeParam() {
            return someParam;
        }
 
        public void setSomeParam(String someParam) {
            this.someParam = someParam;
        }
    }
 
    public static class MySerializable implements Serializable {
        private static final long serialVersionUID = 1L;
 
        private String someParam;
 
        public String getSomeParam() {
            return someParam;
        }
 
        public void setSomeParam(String someParam) {
            this.someParam = someParam;
        }
    }
}
