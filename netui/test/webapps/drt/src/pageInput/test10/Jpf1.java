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
package pageInput.test10;

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
    public static final String  FORM_FLD_1      = "Page Input form field 1 value";
    public static final String  FORM_FLD_2      = "Page Input form field 2 value";
    public              FormOne form1           = new FormOne();

    public static final String  NEW_FORM_FLD_1  = "New - Page Input form field 1 value";
    public static final String  NEW_FORM_FLD_2  = "New - Page Input form field 2 value";

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
        fwd.addPageInput("pgInputForm", form1);
        return fwd;
        }

    /**
     * @jpf:action
     * @jpf:forward name="gotoSubJpf1" path="sub1/SubJpf1.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoSubJpf1",
                path = "sub1/SubJpf1.jpf") 
        })
    protected Forward action1()
        {
            //System.out.println(">>> Jpf1.action1");
        return new Forward("gotoSubJpf1", form1);
        }

    /**
     * @jpf:action
     * @jpf:forward name="gotoPg2" path="Jsp2.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp") 
        })
    protected Forward action2()
        {
            //System.out.println(">>> Jpf1.action2");
        Forward fwd = new Forward("gotoPg2");
        fwd.addPageInput("pgInputForm", form1);
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
        }
    }
