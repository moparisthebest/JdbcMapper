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
package miscJpf.bug26856;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 * @jpf:message-resources  resources="miscJpf.bug26856.struts.StrutsMessages"
 * @jpf:message-resources  key="myMessages"
 *                         resources="miscJpf.bug26856.struts.NamedStrutsMessages"
 * @jpf:message-resources  key="myBundle1"
 *                         resources="miscJpf.bug26856.bundle1"
 */
@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "miscJpf.bug26856.struts.StrutsMessages"),
        @Jpf.MessageBundle(
            bundleName = "myMessages",
            bundlePath = "miscJpf.bug26856.struts.NamedStrutsMessages"),
        @Jpf.MessageBundle(
            bundleName = "myBundle1",
            bundlePath = "miscJpf.bug26856.bundle1") 
    })
public class Jpf1 extends PageFlowController
   {
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
      return new Forward("gotoPg1");
      }

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
   public Forward done()
      {
      return new Forward("gotoPg1");
      }
   }
