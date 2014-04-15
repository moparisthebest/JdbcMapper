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
package returnTo.test41.sub1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller nested="true"
 * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 */
@Jpf.Controller(
    nested = true,
    forwards = {
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp"),
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp") 
    })
public class SubJpf1 extends PageFlowController
   {
   private  String _str ="uninitialized";

   /**
     * @jpf:action
     * @jpf:forward name="gotoSubPg1" path="SubJsp1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoSubPg1",
                path = "SubJsp1.jsp") 
        })
   protected Forward begin()
      {
          //System.out.println(">>> SubJpf1.begin");
      _str = "1";
      return new Forward("gotoSubPg1");
      }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="action2"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "action2") 
        })
   protected Forward action1()
      {
          //System.out.println(">>> SubJpf1.action1");
      _str = "2";
      return new Forward("return");
      }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="action3"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "action3") 
        })
   protected Forward finish()
      {
          //System.out.println(">>> SubJpf1.finish");
      return new Forward("return");
      }

   /**
    * Getter/Setter for String
    */
   public String getString()
      { return _str; }
   public void setString(String inVal)
      { _str = inVal; }
   }
