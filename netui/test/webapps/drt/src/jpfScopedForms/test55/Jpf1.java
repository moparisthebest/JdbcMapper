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
package jpfScopedForms.test55;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMapping;
import shared.FormA;

import javax.servlet.http.HttpServletRequest;

/**
 * @jpf:controller
 * @jpf:message-resources           resources="jpfScopedForms.test55.messages"
 * @jpf:forward name="gotoError"    path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoDone"     path="/resources/jsp/done.jsp"
 */
@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "jpfScopedForms.test55.messages") 
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
        "<pageflow-object id='formbean:FormB'/>", 
        "<pageflow-object id='action:begin.do'/>", 
        "<pageflow-object id='action:action1.do#jpfScopedForms.test55.Jpf1.FormB'/>", 
        "<pageflow-object id='action:action2.do#jpfScopedForms.test55.Jpf1.FormB'/>", 
        "<pageflow-object id='action:finish.do'/>", 
        "<pageflow-object id='page:/resources/jsp/error.jsp'/>", 
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>", 
        "<pageflow-object id='page:/resources/jsp/done.jsp'/>", 
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>", 
        "<pageflow-object id='page:Jsp1.jsp'/>", 
        "<pageflow-object id='forward:path#gotoPg1#Jsp1.jsp#@action:begin.do@'/>", 
        "<pageflow-object id='page:Jsp2.jsp'/>", 
        "<pageflow-object id='forward:path#gotoPg2#Jsp2.jsp#@action:action1.do#jpfScopedForms.test55.Jpf1.FormB@'/>", 
        "<pageflow-object id='page:Jsp3.jsp'/>", 
        "<pageflow-object id='forward:path#gotoPg3#Jsp3.jsp#@action:action2.do#jpfScopedForms.test55.Jpf1.FormB@'/>", 
        "<pageflow-object id='page:StartTest.jsp'/>", 
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:action1.do#jpfScopedForms.test55.Jpf1.FormB@'>", 
        "  <property value='304,240,240,176' name='elbowsX'/>", 
        "  <property value='83,83,221,221' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_0' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:Jsp2.jsp@#@action:action2.do#jpfScopedForms.test55.Jpf1.FormB@'>", 
        "  <property value='304,240,240,176' name='elbowsX'/>", 
        "  <property value='243,243,361,361' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_0' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:Jsp3.jsp@#@action:finish.do@'>", 
        "  <property value='304,240,240,176' name='elbowsX'/>", 
        "  <property value='383,383,532,532' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:returnTo#failure#currentPage#@action:action1.do#jpfScopedForms.test55.Jpf1.FormB@'>", 
        "  <property value='104,104,116,116' name='elbowsX'/>", 
        "  <property value='221,150,150,52' name='elbowsY'/>", 
        "  <property value='West_0' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "  <property value='failure' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='return-to:@forward:returnTo#failure#currentPage#@action:action1.do#jpfScopedForms.test55.Jpf1.FormB@@'>", 
        "  <property value='60' name='x'/>", 
        "  <property value='40' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='exit:sss'>", 
        "  <property value='80' name='x'/>", 
        "  <property value='60' name='y'/>", 
        "</pageflow-object>", 
        "</view-properties>"
    }
)
public class Jpf1 extends PageFlowController
   {
   public FormB _form1 = new FormB();

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
          //System.out.println("\t>>> _form1: " + _form1.toString());
      Forward fwd = new Forward("gotoPg1");
      return fwd;
      }

   /**
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoPg2" path="Jsp2.jsp"
    * @jpf:validation-error-forward name="failure" return-to="currentPage"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp") 
        }, validationErrorForward = @org.apache.beehive.netui.pageflow.annotations.Jpf.Forward(name = "failure", navigateTo = org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo.currentPage))
   protected Forward action1(FormB inForm)
      {
          //System.out.println(">>> Jpf1.action1");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are not the same object and should be.");
         return new Forward("gotoError");
         }
      return new Forward("gotoPg2");
      }

   /**
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoPg3" path="Jsp3.jsp"
    * @jpf:validation-error-forward name="failure" return-to="currentPage"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoPg3",
                path = "Jsp3.jsp") 
        })
   protected Forward action2(FormB inForm)
      {
          //System.out.println(">>> Jpf1.action2");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are not the same object and should be.");
         return new Forward("gotoError");
         }
      if ((inForm.getString1().equals(FormB.STR_VAL1) == true)
           ||
           (inForm.getString2().equals(FormB.STR_VAL2) == true))
         {
         return new Forward("gotoError");
         }
      return new Forward("gotoPg3");
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward finish()
      {
          //System.out.println(">>> Jpf1.done");
      return new Forward("gotoDone");
      }

   /****************************************************************************
    * FormBean FormB
    ***************************************************************************/
   public static class FormB extends FormA implements Validatable
      {
      // validate method
      public void validate(ActionMapping mapping, HttpServletRequest request, ActionMessages errors)
      //------------------------------------------------------------------------
         {
         // Force validation errors.
         //---------------------------------------------------------------------
         //System.out.println(">>> FormB:validate");
         if ((getString1().equals(FormB.STR_VAL1) == true)
             ||
             (getString2().equals(FormB.STR_VAL2) == true))
            {
            errors.add("changeError", new ActionError("error.mustChange"));
            }
         }
      }
   }
