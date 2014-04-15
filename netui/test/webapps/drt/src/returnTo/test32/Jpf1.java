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
package returnTo.test32;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

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
   private  String _str ="uninitialized";

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
      _str = "1";
      return new Forward("gotoPg2");
      }

   /**
    * @jpf:action
    * @jpf:forward name="goSub" path="sub1/SubJpf1.jpf"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "goSub",
                path = "sub1/SubJpf1.jpf") 
        })
   protected Forward action2()
      {
          //System.out.println(">>> Jpf1.action2");
      return new Forward("goSub");
      }

   /**
    * @jpf:action
    * @jpf:forward name="goPg3" path="Jsp3.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "goPg3",
                path = "Jsp3.jsp") 
        })
   protected Forward action3()
      {
          //System.out.println(">>> Jpf1.action3");
      _str = "2";
      return new Forward("goPg3");
      }

   /**
    * @jpf:action
    * @jpf:forward name="goPrev" return-to="previousPage"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "goPrev",
                navigateTo = Jpf.NavigateTo.previousPage) 
        })
   protected Forward action4()
      {
          //System.out.println(">>> Jpf1.action4");
      _str = "2";
      return new Forward("goPrev");
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
    * Getter/Setter for String
    */
   public String getString()
      { return _str; }
   public void setString(String inVal)
      { _str = inVal; }
   }
