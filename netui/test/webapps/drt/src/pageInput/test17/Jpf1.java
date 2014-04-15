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
package pageInput.test17;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.ClassA;

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
   private ClassA _a    = new ClassA();
   private String _str;

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
      Forward fwd = new Forward("gotoPg1");
      fwd.addPageInput("ObjectA", _a);
      return fwd;
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
   protected Forward action1()
      {
          //System.out.println(">>> Jpf1.action1");
      Forward fwd = new Forward("gotoPg2");
      fwd.addPageInput("ObjectA", _a);
      return fwd;
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
   protected Forward action2()
      {
          //System.out.println(">>> Jpf1.action2");
      Forward fwd = new Forward("gotoPg3");
      fwd.addPageInput("ObjectA", _a);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg4" path="Jsp4.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg4",
                path = "Jsp4.jsp") 
        })
   protected Forward action3()
      {
          //System.out.println(">>> Jpf1.action3");
      Forward fwd = new Forward("gotoPg4");
      fwd.addPageInput("ObjectA", _a);
      return fwd;
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

   /**
    * Getter/Setter for String field "string"
    */
   public void setString(String newValue)
      {
      _str = newValue;
      return;
      }
   public String getString()
      {
      return _str;
      }
   }
