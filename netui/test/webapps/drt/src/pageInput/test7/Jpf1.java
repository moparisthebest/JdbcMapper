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
package pageInput.test7;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * @jpf:controller
 * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp"),
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp") 
    })
public class Jpf1 extends PageFlowController
    {
    public  String  pageValue1  = "PageValue 1";
    public  String  pageValue2  = "PageValue 2";
    public  FormOne theForm     = new FormOne();

    /**
     * @jpf:action
     * @jpf:forward name="gotoPg1" path="Jsp1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp") 
        })
    protected Forward begin()
        {
            //System.out.println(">>> Jpf1.begin");
        Forward fwd = new Forward("gotoPg1");
        fwd.addPageInput("pgInputVal_1", pageValue1);
        fwd.addPageInput("pgInputVal_2", pageValue2);
        fwd.addPageInput("pgInputForm", theForm);
        fwd.addPageInput("pgInputFormInner", theForm.getFld3());
        return fwd;
        }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward finish()
        {
            //System.out.println(">>> Jpf1.finish");
        return new Forward("gotoDone");
        }

    /**
     * FormOne is an inner formBean class.
     */
    public static class FormOne implements Serializable
        {
        private String  fld1    = "Field one value";
        private String  fld2    = "Field two value";
        private FormTwo fld3    = new FormTwo();

        // Fld1 getter and setter methods
        //----------------------------------------------------------------------
        public void setFld1(String inFld1)
            { this.fld1 = inFld1; }
        public String getFld1()
            { return this.fld1; }

        // Fld2 getter and setter methods
        //----------------------------------------------------------------------
        public void setFld2(String inFld2)
            { this.fld2 = inFld2; }
        public String getFld2()
            { return this.fld2; }

        // Fld3 getter and setter methods
        //----------------------------------------------------------------------
        public void setFld3(FormTwo inFld3)
            { this.fld3 = inFld3; }
        public FormTwo getFld3()
            { return this.fld3; }

        /**
         * FormTwo is an inner formBean class to the inner FormOne class.
         */
        public static class FormTwo implements Serializable
            {
            private String fld1 = "form2 Field one value";
            private String fld2 = "form2 Field two value";

            // Fld1 getter and setter methods
            //------------------------------------------------------------------
            public void setFld1(String inFld1)
                { this.fld1 = inFld1; }
            public String getFld1()
                { return this.fld1; }

            // Fld2 getter and setter methods
            //------------------------------------------------------------------
            public void setFld2(String inFld2)
                { this.fld2 = inFld2; }
            public String getFld2()
                { return this.fld2; }
            }
        }
    }
