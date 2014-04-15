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
package valFwdErr.test57;

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
 * @jpf:message-resources           resources="valFwdErr.test56.messages"
 * @jpf:forward name="gotoError"    path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoDone"     path="/resources/jsp/done.jsp"
 */
@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "valFwdErr.test56.messages") 
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
   public FormB _form1    = new FormB();

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
   protected Forward begin()
      {
          //System.out.println(">>> Jpf1.begin");
          //System.out.println("\t>>> _form1: " + _form1.toString());
      return new Forward("gotoSubJpf1");
      }

   /**
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoPg1" path="Jsp1.jsp"
    */
   protected Forward action1(FormB inForm)
      {
          //System.out.println(">>> Jpf1.action1");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      Forward fwd = new Forward("gotoPg1");
      fwd.addOutputForm(_form1);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:validation-error-forward name="failure" path="action3.do"
    */
   protected Forward action2(FormB inForm)
      {
          //System.out.println(">>> Jpf1.action2");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      return new Forward("gotoDone");
      }

   /**
    * @jpf:action
    * @jpf:validation-error-forward name="failure" path="/resources/jsp/done.jsp"
    */
   protected Forward action3(FormB inForm)
      {
          //System.out.println(">>> Jpf1.action3");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      return new Forward("gotoError");
      }

   /**
    * @jpf:action
    */
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
      //------------------------------------------------------------------------
         public void validate(ActionMapping mapping, HttpServletRequest request, ActionMessages errors)
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
