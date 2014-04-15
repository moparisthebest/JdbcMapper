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
package miscJpf.bug40854;

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
   private int    _cnt     = 0;
   private String _key     = "paramKey";
   private String _value   = "paramValue";

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg1" path="Jsp1.jsp" redirect="true"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp",
                redirect = true) 
        })
   protected Forward begin()
      {
          //System.out.println(">>> Jpf1.begin");
      Forward fwd = new Forward("gotoPg1");
      fwd.addQueryParam(_key, _value);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="currPg" return-to="currentPage"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "currPg",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
   protected Forward action1()
      {
          //System.out.println(">>> Jpf1.action1");
      return new Forward("currPg");
      }

   /**
    * @jpf:action
    * @jpf:forward name="currPg" return-to="currentPage"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "currPg",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
   protected Forward action2()
      {
          //System.out.println(">>> Jpf1.action2");
      return new Forward("currPg");
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

   public int getCounter()
      {
      return _cnt;
      }
   }
