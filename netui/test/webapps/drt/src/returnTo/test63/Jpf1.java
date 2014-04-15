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
package returnTo.test63;

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
 * @jpf:message-resources resources="returnTo.test63.messages"
 * @jpf:forward name="done" path="/resources/jsp/done.jsp"
 * @jpf:forward name="error" path="/resources/jsp/error.jsp"
 */
@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "returnTo.test63.messages") 
    },
    forwards = {
        @Jpf.Forward(
            name = "done",
            path = "/resources/jsp/done.jsp"),
        @Jpf.Forward(
            name = "error",
            path = "/resources/jsp/error.jsp") 
    })
@Jpf.ViewProperties(
    value = { 
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
        "<view-properties>", 
        "<pageflow-object id='formbean:FormB'/>", 
        "<pageflow-object id='action:begin.do'/>", 
        "<pageflow-object id='action:action1.do'/>", 
        "<pageflow-object id='action:action2.do#returnTo.test63.Jpf1.FormB'/>", 
        "<pageflow-object id='action:action3.do'/>", 
        "<pageflow-object id='action:finish.do'/>", 
        "<pageflow-object id='page:/resources/jsp/done.jsp'/>", 
        "<pageflow-object id='forward:path#done#/resources/jsp/done.jsp'/>", 
        "<pageflow-object id='page:/resources/jsp/error.jsp'/>", 
        "<pageflow-object id='forward:path#error#/resources/jsp/error.jsp'/>", 
        "<pageflow-object id='page:Jsp1.jsp'/>", 
        "<pageflow-object id='forward:path#pg1#Jsp1.jsp#@action:begin.do@'/>", 
        "<pageflow-object id='page:Jsp2.jsp'/>", 
        "<pageflow-object id='forward:path#pg2#Jsp2.jsp#@action:action1.do@'/>", 
        "<pageflow-object id='return-to:@forward:returnTo#prev#previousPage#@action:action2.do#returnTo.test63.Jpf1.FormB@@'/>", 
        "<pageflow-object id='forward:returnTo#prev#previousPage#@action:action2.do#returnTo.test63.Jpf1.FormB@'/>", 
        "<pageflow-object id='return-to:@forward:returnTo#prev#previousPage#@action:action3.do@@'/>", 
        "<pageflow-object id='forward:returnTo#prev#previousPage#@action:action3.do@'/>", 
        "<pageflow-object id='page:StartTest.jsp'/>", 
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:action3.do@'>", 
        "  <property value='304,240,240,176' name='elbowsX'/>", 
        "  <property value='83,83,521,521' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_0' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:finish.do@'>", 
        "  <property value='304,240,240,176' name='elbowsX'/>", 
        "  <property value='83,83,672,672' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:action1.do@'>", 
        "  <property value='304,240,240,176' name='elbowsX'/>", 
        "  <property value='83,83,221,221' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_0' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:Jsp2.jsp@#@action:action2.do#returnTo.test63.Jpf1.FormB@'>", 
        "  <property value='304,240,240,176' name='elbowsX'/>", 
        "  <property value='243,243,361,361' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_0' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:returnTo#failure#currentPage#@action:action2.do#returnTo.test63.Jpf1.FormB@'>", 
        "  <property value='104,100,100,96' name='elbowsX'/>", 
        "  <property value='372,372,32,32' name='elbowsY'/>", 
        "  <property value='West_1' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "  <property value='failure' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='return-to:@forward:returnTo#failure#currentPage#@action:action2.do#returnTo.test63.Jpf1.FormB@@'>", 
        "  <property value='60' name='x'/>", 
        "  <property value='40' name='y'/>", 
        "</pageflow-object>", 
        "</view-properties>"
    }
)
public class Jpf1 extends PageFlowController
   {

   /**
    * @jpf:action
    * @jpf:forward name="pg1" path="Jsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "pg1",
                path = "Jsp1.jsp") 
        })
   protected Forward begin()
      {
          //System.out.println(">>> Jpf1.begin");
      return new Forward("pg1");
      }

   /**
    * @jpf:action
    * @jpf:forward name="pg2" path="Jsp2.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "pg2",
                path = "Jsp2.jsp") 
        })
   protected Forward action1()
      {
          //System.out.println(">>> Jpf1.action1");
      return new Forward("pg2");
      }

   /**
    * @jpf:action
    * @jpf:forward name="prev" return-to="previousPage"
    * @jpf:validation-error-forward name="failure" return-to="currentPage"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "prev",
                navigateTo = Jpf.NavigateTo.previousPage) 
        }, validationErrorForward = @org.apache.beehive.netui.pageflow.annotations.Jpf.Forward(name = "failure", navigateTo = org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo.currentPage))
   protected Forward action2(FormB inForm)
      {
          //System.out.println(">>> Jpf1.action2");
      return new Forward("prev");
      }

   /**
    * @jpf:action
    * @jpf:forward name="prev" return-to="previousPage"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "prev",
                navigateTo = Jpf.NavigateTo.previousPage) 
        })
   protected Forward action3()
      {
          //System.out.println(">>> Jpf1.action3");
      return new Forward("prev");
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward finish()
      {
          //System.out.println(">>> Jpf1.finish");
      return new Forward("done");
      }

   /****************************************************************************
    * FormBean FormB
    ***************************************************************************/
   public static class FormB extends FormA implements Validatable
      {
      // validate method
      //------------------------------------------------------------------------
      public void validate(ActionMapping mapping, HttpServletRequest request, ActionMessages errors)
         {
         // Force validation errors.
         //---------------------------------------------------------------------
         //System.out.println(">>> FormB:validate");
         if ((getString1().equals(FormA.STR_VAL1) == true)
             ||
             (getString2().equals(FormA.STR_VAL2) == true))
            {
            errors.add("changeError", new ActionError("error.mustChange"));
            }
         }
      }
   }
