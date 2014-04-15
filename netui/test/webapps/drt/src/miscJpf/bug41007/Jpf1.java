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
package miscJpf.bug41007;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;

import java.io.Serializable;

/**
 * @jpf:controller
 * @jpf:forward   name="gotoDone" path="/resources/jsp/done.jsp"
 * @jpf:forward   name="gotoError" path="/resources/jsp/error.jsp"
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
   FormA    _forma      = new FormA();

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
      return new Forward("gotoPg1");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoSubJpf1" path="subJpf/SubJpf1.jpf"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoSubJpf1",
                path = "subJpf/SubJpf1.jpf") 
        })
   protected Forward action1()
      {
          //System.out.println(">>> Jpf1.action1");
      return new Forward("gotoSubJpf1");
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward return1()
      {
          //System.out.println(">>> Jpf1.return1");
      return new Forward("gotoError");
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward return1(FormA inForm)
      {
          //System.out.println(">>> Jpf1.return1(FormA)");
      return new Forward("gotoError");
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward return1(FormB inForm)
      {
          //System.out.println(">>> Jpf1.return1(FormB)");
      return new Forward("gotoError");
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward return2()
      {
          //System.out.println(">>> Jpf1.return2");
      return new Forward("gotoDone");
      }

   /****************************************************************************
    * FormBean FormB
    ***************************************************************************/
   public static class FormB implements Serializable
      {
      public static final  String   STR_VAL3 = "String 3 value";
      private              String   _str     = STR_VAL3;

      // String1 getter/setter
      //------------------------------------------------------------------------
      public void setString3(String inVal)
         {
         _str = inVal;
         }
      public String getString3()
         {
         return _str;
         }
      }
   }
