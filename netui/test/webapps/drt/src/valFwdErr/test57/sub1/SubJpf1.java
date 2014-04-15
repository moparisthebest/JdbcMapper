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
package valFwdErr.test57.sub1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import valFwdErr.test57.Jpf1.FormB;

/**
 * @jpf:controller nested="true"
 * @jpf:message-resources           resources="valFwdErr.test57.messages"
 * @jpf:forward name="gotoError"    path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoDone"     path="/resources/jsp/done.jsp"
 */
@Jpf.Controller(
    nested = true,
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "valFwdErr.test57.messages") 
    },
    forwards = {
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp"),
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp") 
    })
public class SubJpf1 extends PageFlowController
   {
   public FormB _form1    = new FormB();

   /**
     * @jpf:action
     * @jpf:forward name="gotoPg1" path="SubJsp1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "SubJsp1.jsp") 
        })
   protected Forward begin()
      {
          //System.out.println(">>> SubJpf1.begin");
      Forward fwd = new Forward("gotoPg1");
      fwd.addOutputForm(_form1);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="continue" return-action="action2"
    * @jpf:validation-error-forward name="failure" return-action="action1"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                returnAction = "action2") 
        })
   protected Forward action1(FormB inForm)
      {
          //System.out.println(">>> Jpf1.action1");
      return new Forward("gotoError");
      }
   }
