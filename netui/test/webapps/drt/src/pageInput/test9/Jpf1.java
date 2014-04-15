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
package pageInput.test9;

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

    private             int     cnter           = 0;

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
        cnter = 0;              // reset the cnter.
        return new Forward("gotoPg1");
        }

    /**
     * @jpf:action
     * @jpf:forward name="gotoPg2"  path="Jsp2.jsp"
     * @jpf:forward name="gotoPg3"  path="Jsp3.jsp"
     * @jpf:forward name="gotoPg4"  path="Jsp4.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp"),
            @Jpf.Forward(
                name = "gotoPg3",
                path = "Jsp3.jsp"),
            @Jpf.Forward(
                name = "gotoPg4",
                path = "Jsp4.jsp") 
        })
    protected Forward action1()
        {
        //System.out.println(">>> Jpf1.action1");

        // Switch to a different page each time we come thru action1.
        //----------------------------------------------------------------------
        cnter++;
        Forward fwd;
        switch (cnter)
            {
            case 1:
                {
                //System.out.println("\t>>> case:1");
                pageValueOne1 = PG_FLD_1;
                pageValueTwo1 = PG_FLD_2;
                form1.setFld1(FORM_FLD_1);
                form1.setFld2(FORM_FLD_2);
                fwd = new Forward("gotoPg2");
                break;
                }
            case 2:
                {
                    //System.out.println("\t>>> case:2");
                fwd = new Forward("gotoPg3");
                break;
                }
            case 3:
                {
                    //System.out.println("\t>>> case:3");
                fwd = new Forward("gotoPg4");
                break;
                }
            default:
                {
                    //System.out.println("\t>>> default");
                    //System.out.println("\t>>> Restart the browser");
                fwd = new Forward("gotoError");
                break;
                }
            }
        fwd.addPageInput("pgInput1", pageValueOne1);
        fwd.addPageInput("pgInput2", pageValueTwo1);
        fwd.addPageInput("pgInputForm", form1);
        return fwd;
        }

    /**
     * @jpf:action
     * @jpf:forward name="rtnToAction1" return-to="previousAction"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "rtnToAction1",
                navigateTo = Jpf.NavigateTo.previousAction) 
        })
    protected Forward action2()
        {
            //System.out.println(">>> Jpf1.action2");
        return new Forward("rtnToAction1");
        }

    /**
     * @jpf:action
     * @jpf:forward name="rtnToAction1" return-to="previousAction"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "rtnToAction1",
                navigateTo = Jpf.NavigateTo.previousAction) 
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

        // Add the pageInput fields to the Forward.  They should all be
        // ignored.
        //----------------------------------------------------------------------
        Forward fwd = new Forward("rtnToAction1");
        fwd.addPageInput("pgInput1", pageValueOne2);
        fwd.addPageInput("pgInput2", pageValueTwo2);
        fwd.addPageInput("pgInputForm", form2);
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
