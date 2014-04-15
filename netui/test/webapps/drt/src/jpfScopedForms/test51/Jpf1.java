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
package jpfScopedForms.test51;

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
   public               FormA    _hldForm;

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
      return new Forward("gotoPg1");
      }

   /**
    * This action should get a new instance of FormA.  If it doesn't the test
    * failed.
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
      if (inForm == _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are the same instance and should not be.");
         return new Forward("gotoError");
         }
      _hldForm = inForm;
      return new Forward("gotoAction2");
      }

   /**
    * This action should get the instance that action1 used above.  Rich says
    * this is pure struts.
    *
    * @jpf:action
    * @jpf:forward name="gotoAction3" path="action3.do"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoAction3",
                path = "action3.do") 
        })
   protected Forward action2(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action2");
          //System.out.println("\t>>> inForm: " + inForm.toString());

      if (inForm != _hldForm)
         {
             //System.out.println("\t>>> inForm & _hldForm are not the same instance and should be.");
         return new Forward("gotoError");
         }
      _hldForm = inForm;
      Forward fwd = new Forward("gotoAction3");
      fwd.addOutputForm(_form2);
      return fwd;
      }

   /**
    * This action should get the _form2 instance that action2 explicitly put
    * in the Forward. If it doesn't the test failed.
    *
    * @jpf:action
    * @jpf:forward name="gotoAction4" path="action4.do"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoAction4",
                path = "action4.do") 
        })
   protected Forward action3(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action3");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form2)
         {
             //System.out.println("\t>>> inForm & _hldForm are the same instance and should not be.");
         return new Forward("gotoError");
         }
      _hldForm = inForm;
      Forward fwd = new Forward("gotoAction4");
      fwd.addOutputForm(inForm);
      return fwd;
      }

   /**
    * Action3 above explicitly passed it's inForm in the Forward and this
    * action is scoped to form1.  The runtime will set _form1 to be the instance
    * that was explicitly put into the Forward by action3.  So _form1 now points
    * to a new form.
    *
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoAction5" path="action5.do"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoAction5",
                path = "action5.do") 
        })
   protected Forward action4(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action4");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _hldForm)
         {
             //System.out.println("\t>>> inForm & _hldForm are not the same instance and should be.");
         return new Forward("gotoError");
         }
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are not the same instance and should be.");
         return new Forward("gotoError");
         }
      return new Forward("gotoAction5");
      }

   /**
    * This action should get the _form1 instance of FormA.  The Forward did not
    * contain an instance of FormA but the action declared form="_form1".  If
    * it's not _form1 the test failed.
    *
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoAction6" path="action6.do"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoAction6",
                path = "action6.do") 
        })
   protected Forward action5(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action5");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are not the same instance and should be.");
         return new Forward("gotoError");
         }
      // Above thru testing _form1 and _form2 are now the same object. Make them
      // different object for the testing below.
      _form1 = new FormA();
      _form2 = new FormA();
      return new Forward("gotoAction6");
      }

   /**
    * Action5 is scoped to _form1 and did not explicitly pass a form on the
    * Forward.  This action is scoped to _form2 so it will use _form2.
    *
    * @jpf:action form="_form2"
    * @jpf:forward name="gotoAction7" path="action7.do"
    */
    @Jpf.Action(
        useFormBean = "_form2",
        forwards = {
            @Jpf.Forward(
                name = "gotoAction7",
                path = "action7.do") 
        })
   protected Forward action6(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action6");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form2)
         {
             //System.out.println("\t>>> inForm & _form2 are not the same instance and should be.");
         return new Forward("gotoError");
         }
      _hldForm = inForm;
      return new Forward("gotoAction7");
      }

   /**
    * This action should get a new instance of FormA.
    *
    * @jpf:action
    * @jpf:forward name="gotoAction8" path="action8.do"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoAction8",
                path = "action8.do") 
        })
   protected Forward action7(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action7");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm == _hldForm)
         {
             //System.out.println("\t>>> inForm & _hldForm are the same instance and should not be.");
         return new Forward("gotoError");
         }
      if (inForm == _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are the same instance and should not be.");
         return new Forward("gotoError");
         }
      if (inForm == _form2)
         {
             //System.out.println("\t>>> inForm & _form2 are the same instance and should not be.");
         return new Forward("gotoError");
         }
      return new Forward("gotoAction8");
      }

   /**
    * This action will use _form1.
    *
    * @jpf:action form="_form1"
    * @jpf:forward name="gotoAction9" path="action9.do"
    */
    @Jpf.Action(
        useFormBean = "_form1",
        forwards = {
            @Jpf.Forward(
                name = "gotoAction9",
                path = "action9.do") 
        })
   protected Forward action8(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action8");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are not the same instance and should be.");
         return new Forward("gotoError");
         }
      Forward fwd = new Forward("gotoAction9");
      fwd.addOutputForm(_form1);
      return fwd;
      }

   /**
    *
    * @jpf:action
    * @jpf:forward name="gotoPg2" path="Jsp2.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp") 
        })
   protected Forward action9(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action9");
          //System.out.println("\t>>> inForm: " + inForm.toString());
      if (inForm != _form1)
         {
             //System.out.println("\t>>> inForm & _form1 are not the same instance and should be.");
         return new Forward("gotoError");
         }
      return new Forward("gotoPg2");
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
