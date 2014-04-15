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
package pageInput.test8;

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
    public static final String  PG_FLD_1        = "Page Input field 1 value";
    public static final String  PG_FLD_2        = "Page Input field 2 value";
    public static final String  FORM_FLD_1      = "Page Input form field 1 value";
    public static final String  FORM_FLD_2      = "Page Input form field 2 value";
    public              String  pageValueOne1   = PG_FLD_1;
    public              String  pageValueTwo1   = PG_FLD_2;
    public              MyForm  form1           = new MyForm();

    public static final String  NEW_PG_FLD_1    = "New - Page Input field 1 value";
    public static final String  NEW_PG_FLD_2    = "New - Page Input field 2 value";
    public static final String  NEW_FORM_FLD_1  = "New - Page Input form field 1 value";
    public static final String  NEW_FORM_FLD_2  = "New - Page Input form field 2 value";
    public              String  pageValueOne2   = NEW_PG_FLD_1;
    public              String  pageValueTwo2   = NEW_PG_FLD_2;
    public              MyForm  form2           = new MyForm();

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

        // Set the initial value of the first set of pageInput fields.
        //----------------------------------------------------------------------
        pageValueOne1 = PG_FLD_1;
        pageValueTwo1 = PG_FLD_2;
        form1.setFld1(FORM_FLD_1);
        form1.setFld2(FORM_FLD_2);

        // Add the pageInput fields to the Forward.
        //----------------------------------------------------------------------
        Forward fwd = new Forward("gotoPg1");
        fwd.addPageInput("pgInput1", pageValueOne1);
        fwd.addPageInput("pgInput2", pageValueTwo1);
        fwd.addPageInput("pgInputForm", form1);
        return fwd;
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
    protected Forward action1()
        {
            //System.out.println(">>> Jpf1.action1");

        // Set the values of the second set of pageInput fields.
        //----------------------------------------------------------------------
        pageValueOne2 = NEW_PG_FLD_1;
        pageValueTwo2 = NEW_PG_FLD_2;
        form2.setFld1(NEW_FORM_FLD_1);
        form2.setFld2(NEW_FORM_FLD_2);

        // Add the second set of pageInput fields to the Forward.
        //----------------------------------------------------------------------
        Forward fwd = new Forward("gotoPg2");
        fwd.addPageInput("pgInput1", pageValueOne2);
        fwd.addPageInput("pgInput2", pageValueTwo2);
        fwd.addPageInput("pgInputForm", form2);
        return fwd;
        }

    /**
     * The idea here is that we're going to go back to Jsp1.jsp with a
     * "return-to" to see if the pageInput values for Jsp1.jsp are the original
     * values or the values set in "action1" above.  They should be the original
     * values.
     *
     * @jpf:action
     * @jpf:forward name="rtnToPg1" return-to="previousPage"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "rtnToPg1",
                navigateTo = Jpf.NavigateTo.previousPage) 
        })
    protected Forward action2()
        {
            //System.out.println(">>> Jpf1.action2");
        return new Forward("rtnToPg1");
        }

    /**
     * The difference between action2 and action3 is that we are going to test
     * that the return-to="previousPage" will NOT restore the original values if
     * we add new pageInput values to the forward.  Action2 did not add any
     * pageinput fields and the original fields should be restored but here we
     * are adding new fields so these fields should be used.
     *
     * @jpf:action
     * @jpf:forward name="rtnToPg1" return-to="previousPage"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "rtnToPg1",
                navigateTo = Jpf.NavigateTo.previousPage) 
        })
    protected Forward action3()
        {
            //System.out.println(">>> Jpf1.action3");

        // Set the values of the second set of pageInput fields.
        //----------------------------------------------------------------------
        pageValueOne2 = NEW_PG_FLD_1;
        pageValueTwo2 = NEW_PG_FLD_2;
        form2.setFld1(NEW_FORM_FLD_1);
        form2.setFld2(NEW_FORM_FLD_2);

        // Add the second set of pageInput fields to the Forward.
        //----------------------------------------------------------------------
        Forward fwd = new Forward("rtnToPg1");
        fwd.addPageInput("pgInput1", pageValueOne2);
        fwd.addPageInput("pgInput2", pageValueTwo2);
        fwd.addPageInput("pgInputForm", form2);
        return fwd;
        }

    /**
     * The difference between action4 and action3 is that we are going to change
     * only one of the pageInput fields.
     *
     * @jpf:action
     * @jpf:forward name="rtnToPg1" return-to="previousPage"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "rtnToPg1",
                navigateTo = Jpf.NavigateTo.previousPage) 
        })
    protected Forward action4()
        {
            //System.out.println(">>> Jpf1.action4");

        // Update all of the pageInput values.
        //----------------------------------------------------------------------
        pageValueOne2 = NEW_PG_FLD_1;
        pageValueTwo2 = NEW_PG_FLD_2;
        form2.setFld1(NEW_FORM_FLD_1);
        form2.setFld2(NEW_FORM_FLD_2);

        // Only add the pageValueTwo2 pageInput to the Forward.
        //----------------------------------------------------------------------
        Forward fwd = new Forward("rtnToPg1");
        fwd.addPageInput("pgInput2", pageValueTwo2);
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
     * MyForm is an inner formBean class.
     */
    public static class MyForm implements Serializable
        {
        private String  fld1    = Jpf1.FORM_FLD_1;
        private String  fld2    = Jpf1.FORM_FLD_2;

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
