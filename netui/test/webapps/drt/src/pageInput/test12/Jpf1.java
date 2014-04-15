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
package pageInput.test12;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @jpf:controller
 * @jpf:message-resources resources="pageInput/test12/messages"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoDone"  path="/resources/jsp/done.jsp"
 */
@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "pageInput/test12/messages") 
    },
    forwards = {
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp"),
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp") 
    })
@Jpf.ViewProperties(
    value = { 
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
        "<view-properties>", 
        "<pageflow-object id='formbean:FormOne'/>", 
        "<pageflow-object id='action:begin.do'/>", 
        "<pageflow-object id='action:action1.do#pageInput.test12.Jpf1.FormOne'/>", 
        "<pageflow-object id='page:/resources/jsp/error.jsp'/>", 
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>", 
        "<pageflow-object id='page:/resources/jsp/done.jsp'/>", 
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>", 
        "<pageflow-object id='page:Jsp1.jsp'/>", 
        "<pageflow-object id='forward:path#gotoPg1#Jsp1.jsp#@action:begin.do@'/>", 
        "<pageflow-object id='page:StartTest.jsp'/>", 
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:action1.do#pageInput.test12.Jpf1.FormOne@'>", 
        "  <property value='304,240,240,176' name='elbowsX'/>", 
        "  <property value='83,83,232,232' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:returnTo#failure#currentPage#@action:action1.do#pageInput.test12.Jpf1.FormOne@'>", 
        "  <property value='104,100,100,96' name='elbowsX'/>", 
        "  <property value='232,232,32,32' name='elbowsY'/>", 
        "  <property value='West_1' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "  <property value='failure' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='return-to:@forward:returnTo#failure#currentPage#@action:action1.do#pageInput.test12.Jpf1.FormOne@@'>", 
        "  <property value='60' name='x'/>", 
        "  <property value='40' name='y'/>", 
        "</pageflow-object>", 
        "</view-properties>"
    }
)
public class Jpf1 extends PageFlowController
   {
   public static final String    PG_FLD_1       = "Page Input field 1 value";
   public static final String    PG_FLD_2       = "Page Input field 2 value";
   public static final String    PASS_PG_FLD_1  = "Field 1 value - pass";
   public static final String    PASS_PG_FLD_2  = "Field 2 value - pass";
   public              String    pageValueOne1  = PG_FLD_1;
   public              String    pageValueTwo1  = PG_FLD_2;
   public              FormOne   form1          = new FormOne();

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

      // Set the initial values of the pageInput fields.
      //------------------------------------------------------------------------
      pageValueOne1 = PG_FLD_1;
      pageValueTwo1 = PG_FLD_2;

      // Add the form and the pageInput fields to the Forward.
      //------------------------------------------------------------------------
      Forward fwd = new Forward("gotoPg1");
      fwd.addOutputForm(form1);
      fwd.addPageInput("pgInput1", pageValueOne1);
      fwd.addPageInput("pgInput2", pageValueTwo1);
      fwd.addPageInput("pgInputForm", form1);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:validation-error-forward name="failure" return-to="currentPage"
    */
    @Jpf.Action(validationErrorForward = @org.apache.beehive.netui.pageflow.annotations.Jpf.Forward(name = "failure", navigateTo = org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo.currentPage)
        )
   protected Forward action1(FormOne inForm)
      {
          //System.out.println(">>> Jpf1.action1");

      return new Forward("gotoDone");
      }

   /****************************************************************************
    * FormBean FormOne
    ***************************************************************************/
   public static class FormOne implements Serializable, Validatable
      {
      public final static String VALUE1 = "Field one value";
      public final static String VALUE2 = "Field two value";

      private String _fld1 = VALUE1;
      private String _fld2 = VALUE2;

      public void setFld1(String inFld1)
         {
         this._fld1 = inFld1;
         }

      public String getFld1()
         {
         return this._fld1;
         }

      public void setFld2(String inFld2)
         {
         this._fld2 = inFld2;
         }

      public String getFld2()
         {
         return this._fld2;
         }

      /*************************************************************************
       * Method: reset
       ************************************************************************/
      public void reset(ActionMapping mapping, HttpServletRequest request)
         {
             //System.out.println(">>> FormOne:reset");
         return;
         }

      /*************************************************************************
       * Method: validate
       ************************************************************************/
      public void validate(ActionMapping mapping, HttpServletRequest request, ActionMessages errors)
         {
         // Did we get a valid Username and Password?
         //---------------------------------------------------------------------
         //System.out.println(">>> FormOne:validate");

         if (_fld1.equals(PASS_PG_FLD_1) == false)
            {
                //System.out.println("Field one is invalid");
            _fld1 = PASS_PG_FLD_1;
            errors.add("field1", new ActionError("error.fld1"));
            }

         if (_fld2.equals(PASS_PG_FLD_2) == false)
            {
                //System.out.println("Field two is invalid");
            _fld2 = PASS_PG_FLD_2;
            errors.add("field2", new ActionError("error.fld2"));
            }
         }
      }
   }
