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
package jpfScopedForms.test49;

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
   private static final String   _STR1 = "Jpf1 value 1";
   private static final String   _STR2 = "Jpf1 value 2";
   public FormA _form1    = new FormA();
   public FormA _form2;

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
      _form1.setString1(_STR1);
      _form1.setString2(_STR2);
      return new Forward("gotoPg1");
      }

   /**
    * The inForm should be the same object as _form1.  If not the test failed.
    * Also page Jps1.jsp rendered with a different instance of FormA and the
    * values that displayed and were submited were the FormA default values not
    * the values we set above in the "begin" method.  So inForm should have the
    * default FormA values not what we set above.  It this is not the case the
    * test fails.
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
   protected Forward action1(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action1");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form1)
         {
             System.out.println("\t>>> inForm & _form1 are not the same instance and should be.");
         return new Forward("gotoError");
         }
      if ((inForm.getString1().equals(_STR1) == true)
            ||
          (inForm.getString2().equals(_STR2) == true))
         {
             System.out.println("\t>>> inForm.String1: (" + inForm.getString1() + ").");
             System.out.println("\t>>> inForm.String2: (" + inForm.getString2() + ").");
         return new Forward("gotoError");
         }
      _form1.setString1(_STR1);        // Set the values to a specific string
      _form1.setString2(_STR2);
      return new Forward("gotoPg2");
      }

   /**
    * This inForm should be a new instance and have the FormA's default values
    * in the String fields. When this method forwards it adds _form1 to the
    * forward so Jsp3.jsp will render with _form1 and not a new instance.
    *
    * @jpf:action
    * @jpf:forward name="gotoPg3" path="Jsp3.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg3",
                path = "Jsp3.jsp") 
        })
   protected Forward action2(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action2");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm == _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are the same instance and should not be.");
         return new Forward("gotoError");
         }
      if ((inForm.getString1().equals(_STR1) == true)
            ||
          (inForm.getString2().equals(_STR2) == true))
         {
             //System.out.println("\t>>> inForm.String1: (" + inForm.getString1() + ").");
             //System.out.println("\t>>> inForm.String2: (" + inForm.getString2() + ").");
         return new Forward("gotoError");
         }
      _form1.setString1(_STR1);
      _form1.setString2(_STR2);
      Forward fwd = new Forward("gotoPg3");
      fwd.addOutputForm(_form1);
      return fwd;
      }

   /**
    * action2 above added _form1 to the forward so Jsp3.jsp will render with
    * _form1 and not a new instance.  We will know if this worked because the
    * inForm we recieve will have the _STR1 and _STR2 values.  If it doesn't the
    * test failed.
    *
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoPg4" path="Jsp4.jsp"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoPg4",
                path = "Jsp4.jsp") 
        })
   protected Forward action3(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action3");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are not the same instance and should be.");
         return new Forward("gotoError");
         }
      if ((inForm.getString1().equals(_STR1) != true)
            ||
          (inForm.getString2().equals(_STR2) != true))
         {
             //System.out.println("\t>>> inForm.String1: (" + inForm.getString1() + ").");
             //System.out.println("\t>>> inForm.String2: (" + inForm.getString2() + ").");
         return new Forward("gotoError");
         }
      return new Forward("gotoPg4");
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
