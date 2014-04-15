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
package formBeanRePost.test52;

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
   private static final String   _STR3    = "Jpf1 value 3";

   private              String   _str     = _STR1;
   public               FormA    _form1   = new FormA();

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
    * inForm will be a new instance of the FormA formBean.  The value of
    * inForm's string1 property will be the default value set in the FormA
    * constructor.
    *
    * @jpf:action
    * @jpf:forward name="gotoAction2" path="action2.do"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoAction2",
                path = "action2.do") 
        })
   protected Forward action1(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action1");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      inForm.setString1(_STR2);                  // This value will be replace on the forward.
      _str = _STR2;                              // This value should not be replaced on the forward.
      return new Forward("gotoAction2");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg2" path="Jsp2.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp") 
        })
   protected Forward action2(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action2");
          //System.out.println("\t>>> inForm: " + inForm.toString());

      // The formbean should have been re-populated by struts from the request
      // on the forward from action1 above replaceing string1 with the value
      // in the request.
      //------------------------------------------------------------------------
      if (inForm.getString1().equals(_STR2) == true)
         {
             //System.out.println("\t>>> Form bean was not re-populated as it should have been.");
         return new Forward("gotoError");
         }

      // The pageFlow properties should not be re-populated by struts on the
      // forward.  So _str should have the value set in action1 above.
      //------------------------------------------------------------------------
      if (_str.equals(_STR2) == false)
         {
             //System.out.println("\t>>> _str: " + _str);
             //System.out.println("\t>>> The Page Flow value was re-populated and should not have been.");
         return new Forward("gotoError");
         }
      _str = _STR1;        // Reset this for the next test.
      return new Forward("gotoPg2");
      }

   /**
    * inForm will be a new instance of the FormA formBean.  The value of
    * inForm's string1 property will be the default value set in the FormA
    * constructor.
    *
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoAction4" path="action4.do"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoAction4",
                path = "action4.do") 
        })
   protected Forward action3(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action3");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      inForm.setString1(_STR2);
      _str = _STR2;
      return new Forward("gotoAction4");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg3" path="Jsp3.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg3",
                path = "Jsp3.jsp") 
        })
   protected Forward action4(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action4");
          //System.out.println("\t>>> inForm: " + inForm.toString());

      // The formbean should have been re-populated by struts from the request
      // on the forward from action1 above replaceing string1 with the value
      // in the request.
      //------------------------------------------------------------------------
      if (inForm.getString1().equals(_STR2) == true)
         {
             //System.out.println("\t>>> Form bean was not re-populated as it should have been.");
         return new Forward("gotoError");
         }

      // The pageFlow properties should not be re-populated by struts on the
      // forward.  So _str should have the value set in action1 above.
      //------------------------------------------------------------------------
      if (_str.equals(_STR2) == false)
         {
             //System.out.println("\t>>> _str: " + _str);
             //System.out.println("\t>>> The Page Flow value was re-populated and should not have been.");
         return new Forward("gotoError");
         }
      Forward fwd = new Forward("gotoPg3");
      fwd.addOutputForm(inForm);
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
   /**
    * Getter/Setter for String
    */
   public String getString()
      {
      return _str;
      }
   public void setString(String inVal)
      {
      _str = inVal;
      }
   }
