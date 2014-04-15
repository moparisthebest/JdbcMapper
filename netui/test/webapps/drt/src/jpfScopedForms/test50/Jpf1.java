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
package jpfScopedForms.test50;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;

/**
 * @jpf:controller
 * @jpf:forward name="gotoError"    path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoDone"     path="/resources/jsp/done.jsp"
 */
@Jpf.Controller(
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
   private static final String   _STR1    = "Jpf1 value 1";
   private static final String   _STR2    = "Jpf1 value 2";
   public               FormA    _form1   = new FormA();
   public               FormA    _form2   = new FormA();

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
          //System.out.println("\t>>> _form2: " + _form2.toString());
      _form1.setString1(_STR1);
      _form1.setString2(_STR2);
      return new Forward("gotoPg1");
      }

   /**
    * This action should get a new instance of FormA.  If it does not the test
    * failed.
    *
    * @jpf:action
    * @jpf:forward name="gotoSecure1" path="secure1.do"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoSecure1",
                path = "secure1.do") 
        })
   protected Forward action1(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action1");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      // Make sure we are NOT in a secure mode
      //------------------------------------------------------------------------
      if (this.getRequest().isSecure() == true)
         {
             //System.out.println("\t>>> We are in secure mode and should not be");
         return new Forward("gotoError");
         }
      if (inForm == _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are the same instance and should not be.");
         return new Forward("gotoError");
         }
      return new Forward("gotoSecure1");
      }

   /**
    * This action should get _form1 and the parameter as the action sets
    * form="_form1".  If it does not the test failed.
    *
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoAction2" path="/jpfScopedForms/test50/action2.do"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoAction2",
                path = "/jpfScopedForms/test50/action2.do") 
        })
   protected Forward secure1(FormA inForm)
      {
          //System.out.println(">>> Jpf1.secure1");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      // Make sure we are in a secure mode
      //------------------------------------------------------------------------
      if (this.getRequest().isSecure() == false)
         {
             //System.out.println("\t>>> We are not in secure mode.");
         return new Forward("gotoError");
         }
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are not the same instance and they should be.");
         return new Forward("gotoError");
         }
      Forward fwd = new Forward("gotoAction2");
      return fwd;
      }

   /**
    * This action should get _form2 and the parameter as the action sets
    * form="_form2".  If it does not the test failed.
    *
    * @jpf:action form="_form2"
    * @jpf:forward name="gotoPg2" path="Jsp2.jsp"
    */
    @Jpf.Action(
        useFormBean = "_form2",
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp") 
        })
   protected Forward action2(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action2");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      // Make sure we are NOT in a secure mode
      //------------------------------------------------------------------------
      if (this.getRequest().isSecure() == true)
         {
             //System.out.println("\t>>> We are in secure mode.");
         return new Forward("gotoError");
         }
      if (inForm != _form2)
         {
             //System.out.println("\t>>> inForm & _form2 are the same instance and they should be.");
         return new Forward("gotoError");
         }
      Forward fwd = new Forward("gotoPg2");
      fwd.addOutputForm(_form1);
      return fwd;
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward finish(FormA inForm)
      {
          //System.out.println(">>> Jpf1.done");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      return new Forward("gotoDone");
      }
   }

