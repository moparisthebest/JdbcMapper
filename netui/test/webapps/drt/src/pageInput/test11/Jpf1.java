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
package pageInput.test11;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller
public class Jpf1 extends PageFlowController
   {
   public static final String  PG_FLD_1        = "Page Input field 1 value";
   public static final String  PG_FLD_2        = "Page Input field 2 value";
   public static final String  FORM_FLD_1      = "Page Input form field 1 value";
   public static final String  FORM_FLD_2      = "Page Input form field 2 value";
   public              String  pageValueOne1   = PG_FLD_1;
   public              String  pageValueTwo1   = PG_FLD_2;
   public              FormOne form1           = new FormOne();

   public static final String  NEW_PG_FLD_1    = "New - Page Input field 1 value";
   public static final String  NEW_PG_FLD_2    = "New - Page Input field 2 value";
   public static final String  NEW_FORM_FLD_1  = "New - Page Input form field 1 value";
   public static final String  NEW_FORM_FLD_2  = "New - Page Input form field 2 value";

   private             int     _cnter          = 0;

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
      _cnter = 0;

      // Set the initial values of the pageInput fields.
      //------------------------------------------------------------------------
      pageValueOne1 = PG_FLD_1;
      pageValueTwo1 = PG_FLD_2;
      form1.setFld1(FORM_FLD_1);
      form1.setFld2(FORM_FLD_2);

      // Add the second set of pageInput fields to the Forward.
      //------------------------------------------------------------------------
      Forward fwd = new Forward("gotoPg1");
      fwd.addPageInput("pgInput1", pageValueOne1);
      fwd.addPageInput("pgInput2", pageValueTwo1);
      fwd.addPageInput("pgInputForm", form1);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg3" path="Jsp3.jsp"
    * @jpf:forward name="gotoSecure1" path="/pageInput/test11/secure1.do"
    * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg3",
                path = "Jsp3.jsp"),
            @Jpf.Forward(
                name = "gotoSecure1",
                path = "/pageInput/test11/secure1.do"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   protected Forward action1()
      {
          //System.out.println(">>> Jpf1.action1");
      _cnter++;

      // If this is the second time thru go, to the finish page.  We're done.
      //------------------------------------------------------------------------
      if (_cnter == 2)
         {
         Forward fwd = new Forward("gotoPg3");
         fwd.addPageInput("pgInput1", pageValueOne1);
         fwd.addPageInput("pgInput2", pageValueTwo1);
         fwd.addPageInput("pgInputForm", form1);
         return fwd;
         }

      // This had better be the first time thru.  If not we have a programming
      // error.
      //------------------------------------------------------------------------
      if (_cnter != 1) return new Forward("gotoError");

      // Update the values of the second set of pageInput fields.
      //------------------------------------------------------------------------
      pageValueOne1 = NEW_PG_FLD_1;
      pageValueTwo1 = NEW_PG_FLD_2;
      form1.setFld1(NEW_FORM_FLD_1);
      form1.setFld2(NEW_FORM_FLD_2);

      return new Forward("gotoSecure1");
      }

   /**
    * This action has been designated as secure in the <security-constraint>
    * section of the web.xml file.
    *
    * @jpf:action
    * @jpf:forward name="gotoPg2" path="Jsp2.jsp"
    * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   protected Forward secure1()
      {
          //System.out.println(">>> Jpf1.secure1");
      if (this.getRequest().isSecure() == false)
         {
         return new Forward("gotoError");
         }
      Forward fwd = new Forward("gotoPg2");
      fwd.addPageInput("pgInput1", pageValueOne1);
      fwd.addPageInput("pgInput2", pageValueTwo1);
      fwd.addPageInput("pgInputForm", form1);
      return fwd;
      }

   /**
    * This action has been designated as unsecure in the <security-constraint>
    * section of the web.xml file.  So this action effectivly "turns off" the
    * ssl connection.
    *
    * @jpf:action
    * @jpf:forward name="returnPage" return-to="previousPage"
    * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "returnPage",
                navigateTo = Jpf.NavigateTo.previousPage),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   protected Forward action2()
      {
          //System.out.println(">>> Jpf1.action2");
      if (this.getRequest().isSecure() == true)
         {
         return new Forward("gotoError");
         }
      return new Forward("returnPage");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoDone",
                path = "/resources/jsp/done.jsp") 
        })
   protected Forward action3()
      {
          //System.out.println(">>> Jpf1.action3");
      if (this.getRequest().isSecure() == true)
         {
         return new Forward("gotoError");
         }
      return new Forward("gotoDone");
      }

   /**
    * FormOne is an inner formBean class.
    */
   public static class FormOne implements Serializable
      {
      private String  fld1    = Jpf1.FORM_FLD_1;
      private String  fld2    = Jpf1.FORM_FLD_2;

      // Fld1 getter and setter methods
      //------------------------------------------------------------------------
      public void setFld1(String inFld1)
         {
         this.fld1 = inFld1;
         }
      public String getFld1()
         {
         return this.fld1;
         }

      // Fld2 getter and setter methods
      //------------------------------------------------------------------------
      public void setFld2(String inFld2)
         {
         this.fld2 = inFld2;
         }
      public String getFld2()
         {
         return this.fld2;
         }
      }
   }
