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
package jpfScopedForms.test59;

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
   public               FormA    _hldForm;
   private              String   _hldFld1;
   private              String   _hldFld2;

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
      return new Forward("gotoPg1");
      }

   /**
    * This action should be using _form1 and the inForm parameter.  It will save
    * what the user entered the set some new values and forward directly to
    * action2.
    *
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoAction2" path="action2.do"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoAction2",
                path = "action2.do") 
        })
   protected Forward action1(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action1");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are not the same instance and should be.");
         return new Forward("gotoError");
         }
      _hldFld1 = inForm.getString1();     // Save what the user entered.
      _hldFld2 = inForm.getString2();
      inForm.setString1(_STR1);           // Put a new value in the fields.
      inForm.setString2(_STR2);
      _hldForm = inForm;
      return new Forward("gotoAction2");
      }

   /**
    * This action should get a new instance of FormA.  If it doesn't the test
    * failed.
    *
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoPg2" path="Jsp2.jsp"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp") 
        })
   protected Forward action2(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action2");
          //System.out.println("\t>>> inForm: " + inForm.toString());
          //System.out.println("\t>>> inForm.getString1(): " + inForm.getString1());
          //System.out.println("\t>>> inForm.getString2(): " + inForm.getString2());
          //System.out.println("\t>>> _form1.getString1(): " + _form1.getString1());
          //System.out.println("\t>>> _form1.getString2(): " + _form1.getString2());
          //System.out.println("\t>>> _hlfFld1: " + _hldFld1);
          //System.out.println("\t>>> _hlfFld2: " + _hldFld2);
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & form1 are not the same instance and should be.");
         return new Forward("gotoError");
         }
      if (inForm.getString1().equals(_hldFld1) == false)
         {
             //System.out.println("\t>>> inForm.getString1() is not equal to _hldFld1.");
         return new Forward("gotoError");
         }
      if (inForm.getString2().equals(_hldFld2) == false)
         {
             //System.out.println("\t>>> inForm.getString2() is not equal to _hldFld2.");
         return new Forward("gotoError");
         }
      if (inForm.getString1().equals(_form1.getString1()) == false)
         {
             //System.out.println("\t>>> inForm.getString1() is not equal to _form1.getString1().");
         return new Forward("gotoError");
         }
      if (inForm.getString2().equals(_form1.getString2()) == false)
         {
             //System.out.println("\t>>> inForm.getString2() is not equal to _form1.getString2().");
         return new Forward("gotoError");
         }

      return new Forward("gotoPg2");
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
   }
