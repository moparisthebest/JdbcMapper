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
package miscJpf.bug39028;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 * @jpf:catch     type="org.apache.beehive.netui.pageflow.ActionNotFoundException"
 *                method="exceptionHandler"
 * @jpf:forward   name="gotoDone"   path="/resources/jsp/done.jsp"
 * @jpf:forward   name="gotoError"  path="/resources/jsp/error.jsp"
 */
@Jpf.Controller(
    catches = {
        @Jpf.Catch(
            type = org.apache.beehive.netui.pageflow.ActionNotFoundException.class,
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
   int      _cnter   = 0;
   String   _visit;

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
   public Forward begin()
      {
      _cnter = 0;
      _visit = Integer.toString(++_cnter);
      //System.out.println(">>> Jpf1.begin");
      return new Forward("gotoPg1", "visit", _visit);
      }

   /**
    * @jpf:action
    * @jpf:forward name="currentPage" return-to="currentPage"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "currentPage",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
   public Forward action1()
      {
      _visit = Integer.toString(++_cnter);
      //System.out.println(">>> Jpf1.action1");
      return new Forward("currentPage", "visit", _visit);
      }

   /**
    * @jpf:action
    * @jpf:forward name="badAction" path="badAction.do"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "badAction",
                path = "badAction.do") 
        })
   public Forward action2()
      {
      _visit = Integer.toString(++_cnter);
      //System.out.println(">>> Jpf1.action2");
      return new Forward("badAction", "visit", _visit);
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   public Forward finish()
      {
          //System.out.println(">>> Jpf1.action2");
      return new Forward("gotoDone");
      }

   /**
    * @jpf:exception-handler
    * @jpf:forward name="current" return-to="currentPage"
    */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "current",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
   public Forward exceptionHandler(org.apache.beehive.netui.pageflow.ActionNotFoundException e
                            ,String actionName
                            ,String message
                            ,Object inForm
                           )
      {
          //System.out.println(">>> Jpf1.exceptionHandler");
      return new Forward("current", "visit", _visit);
      }
   }
