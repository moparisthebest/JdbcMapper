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
package returnTo.test47;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;

/**
 * @jpf:controller
 * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp"),
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp") 
    })
public class Jpf1 extends PageFlowController
   {
   private static String   _str     = "uninitialized";
   private        int      _cnter   = 0;
   private        String   _holdStr1;
   private        String   _holdStr2;

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
      _cnter = 0;
      return new Forward("gotoPg1");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg2" path="Jsp2.jsp"
    * @jpf:forward name="gotoPg3" path="Jsp3.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp"),
            @Jpf.Forward(
                name = "gotoPg3",
                path = "Jsp3.jsp") 
        })
   protected Forward action1(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action1");
          //System.out.println("\t>>> Form instance: " + inForm.toString());
      _cnter++;
      if (_cnter == 1)
         {
         _holdStr1 = inForm.getString1();
         _holdStr2 = inForm.getString2();
         return new Forward("gotoPg2");
         }
      if (inForm.getString1().equals(_holdStr1) == false
                  ||
          inForm.getString2().equals(_holdStr2) == false)
         {
         return new Forward("gotoError");
         }
      return new Forward("gotoPg3");
      }

   /**
    * @jpf:action
    * @jpf:forward name="goBack" return-to="previousAction"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "goBack",
                navigateTo = Jpf.NavigateTo.previousAction) 
        })
   protected Forward action2(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action2");
          //System.out.println("\t>>> Form instance: " + inForm.toString());
      return new Forward("goBack");
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward action3(FormA inForm)
      {
          //System.out.println(">>> Jpf1.action3");
          //System.out.println("\t>>> Form instance: " + inForm.toString());
      return new Forward("gotoDone");
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward finish()
      {
          //System.out.println(">>> Jpf1.finish");
      return new Forward("gotoDone");
      }

   /**
    * Getter/Setter for Counter
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
