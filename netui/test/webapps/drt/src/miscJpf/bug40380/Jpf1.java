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
package miscJpf.bug40380;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 * @jpf:catch type="org.apache.beehive.netui.pageflow.NoPreviousActionException" method="exceptionHandler"
 * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 */
@Jpf.Controller(
    catches = {
        @Jpf.Catch(
            type = org.apache.beehive.netui.pageflow.NoPreviousActionException.class,
            method = "exceptionHandler") 
    },
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

   /**
    * @jpf:action
    * @jpf:forward name="prevAction" return-to="previousAction"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "prevAction",
                navigateTo = Jpf.NavigateTo.previousAction) 
        })
   protected Forward begin()
      {
          //System.out.println(">>> Jpf1.begin");
      return new Forward("prevAction");
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
    * @jpf:exception-handler
    */
    @Jpf.ExceptionHandler(
        )
   public Forward exceptionHandler(org.apache.beehive.netui.pageflow.NoPreviousActionException e
                                   ,String actionName
                                   ,String message
                                   ,Object inForm
                                  )
      {
          //System.out.println(">>> Jpf1.exceptionHandler");
      return new Forward("gotoDone");
      }
   }
