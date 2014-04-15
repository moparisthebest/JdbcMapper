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
package pageInput.test1;

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
   public   String   pageValue1  = "PageValue 1";
   public   String   pageValue2  = "PageValue 2";

   /**
    * This should be okay as the Forward names should have nothing to do with
    * the pageInput names.
    *
    * @jpf:action
    * @jpf:forward name="pgKey" path="Jsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "pgKey",
                path = "Jsp1.jsp") 
        })
   protected Forward begin()
      {
          //System.out.println(">>> Jpf1.begin");
      return new Forward("pgKey", "pgKey", pageValue1);
      }

   /**
    * This will give no warning.  If you add a pageInput name twice the second
    * one will just replace the first and no warning will be issued.
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
   protected Forward action1()
      {
          //System.out.println(">>> Jpf1.action1");
      Forward fwd = new Forward("gotoPg2", "pgKey", pageValue1);
      fwd.addPageInput("pgKey", pageValue2);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoAction3" path="action3.do"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoAction3",
                path = "action3.do") 
        })
   protected Forward action2()
      {
          //System.out.println(">>> Jpf1.action2");
      Forward fwd = new Forward("gotoAction3", "pgKey", pageValue1);
      return fwd;
      }

   /**
    * This however this will write a warning message to netui.log that the
    * pageInput name is being added to the request twice.  Action2 above added
    * "pgKey" to the pageInput then forwared to action3 and now action3 is
    * trying to add the same name again.
    *
    * @jpf:action
    * @jpf:forward name="gotoPg3" path="Jsp3.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg3",
                path = "Jsp3.jsp") 
        })
   protected Forward action3()
      { 
          //System.out.println(">>> Jpf1.action3");
      Forward fwd = new Forward("gotoPg3", "pgKey", pageValue1);
      return fwd;
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
   }
