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
package pageInput.test23;

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
 * @jpf:message-resources           resources="pageInput/test23/messages"
 * @jpf:forward name="gotoError"    path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoDone"     path="/resources/jsp/done.jsp"
 */
@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "pageInput/test23/messages") 
    },
    forwards = {
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp"),
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp") 
    })
public class Jpf1 extends PageFlowController
   {
   public MyForm _form    = new MyForm();

   /**
    * We are going to add the SAME FormBean "_form" to the "Forward" once as
    * a formBean and once as pageInput.
    *
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
      fwd.addOutputForm(_form);
      fwd.addPageInput("theForm", _form);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:validation-error-forward name="failure" return-to="currentPage"
    */
    @Jpf.Action(validationErrorForward = @org.apache.beehive.netui.pageflow.annotations.Jpf.Forward(name = "failure", navigateTo = org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo.currentPage)
        )
   protected Forward action1(MyForm inForm)
      {
          //System.out.println(">>> Jpf1.action1");
      return new Forward("gotoError");
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
    * FormBean MyForm
    ***************************************************************************/
   public static class MyForm extends FormA implements Validatable
      {
      /*************************************************************************
       * Method: validate
       ************************************************************************/
      public void validate(ActionMapping mapping, HttpServletRequest request, ActionMessages errors)
         {
         // Force validation errors.
         //---------------------------------------------------------------------
         //System.out.println(">>> MyForm:validate");

         errors.add("forcedError", new ActionError("error.forced"));
         }
      }
   }
