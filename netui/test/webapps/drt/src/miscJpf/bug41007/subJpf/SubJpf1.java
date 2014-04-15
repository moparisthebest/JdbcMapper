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
package miscJpf.bug41007.subJpf;

import miscJpf.bug41007.Jpf1.FormB;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;

/**
 * @jpf:controller nested="true"
 * @jpf:catch  type="org.apache.beehive.netui.pageflow.IllegalOutputFormTypeException"
 *             method="exceptionHandler"
 */
@Jpf.Controller(
    nested = true,
    catches = {
        @Jpf.Catch(
            type = org.apache.beehive.netui.pageflow.IllegalOutputFormTypeException.class,
            method = "exceptionHandler") 
    })
public class SubJpf1 extends PageFlowController
   {
   public static final String FLD1_VALUE  = "SubJpf1 Fld1 Value";
   public static final String FLD2_VALUE  = "SubJpf1 Fld2 Value";

   private FormA _formA  = new FormA();
   private FormB _formB  = new FormB();

   /**
    * @jpf:action
    * @jpf:forward name="pg1" path="SubJsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "pg1",
                path = "SubJsp1.jsp") 
        })
   protected Forward begin()
       {
           //System.out.println(">>> SubJpf1.begin");
       return new Forward("pg1");
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="return1" return-form-type="shared.FormA"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "return1",
                outputFormBeanType = shared.FormA.class) 
        })
   protected Forward action1()
       {
           //System.out.println(">>> SubJpf1.action1");
       return new Forward("return", _formB);
       }

   /**
    * If you replace the @jpf:forward with the one below and swap the return
    * lines the test works fine.
    *
    * @jpf:exception-handler
    * @jpf:forward name="returnHome" return-action="return2"
    */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "returnHome",
                returnAction = "return2") 
        })
   public Forward exceptionHandler(org.apache.beehive.netui.pageflow.IllegalOutputFormTypeException e
                                   ,String actionName
                                   ,String message
                                   ,Object inForm
                                  )
      {
          //System.out.println(">>> Jpf1.exceptionHandler");
      return new Forward("returnHome");
      }
   }
