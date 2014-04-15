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
package miscJpf.bug37949;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.scoping.internal.ScopedRequestImpl;
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
   private final String _scopeKey   = "scopeKey";
   private final String _attrKey1   = "attrKey1";
   private final String _attrKey2   = "attrKey2";
   private       String _str        = "Blah Blah";

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
    */
    @Jpf.Action(
        )
   protected Forward action1()
      {
          //System.out.println(">>> Jpf1.action1");
      ScopedRequestImpl scopedRequest
                        = new ScopedRequestImpl(getRequest()
                                               ,null
                                               ,_scopeKey
                                               ,getServlet().getServletContext()
                                               ,false);
      scopedRequest.setAttribute(_attrKey1, Runtime.getRuntime());
      scopedRequest.setAttribute(_attrKey2, _str);
      scopedRequest.persistAttributes();
      scopedRequest.removeAttribute(_attrKey1);
      scopedRequest.removeAttribute(_attrKey2);
      scopedRequest.restoreAttributes();

      Object attr = (Object) scopedRequest.getAttribute(_attrKey1);
      if (attr != null)
         {
             //System.out.println(">>> attr: " + attr.toString());
         return new Forward("gotoError");
         }

      attr = (Object) scopedRequest.getAttribute(_attrKey2);
      if (attr == null)
         {
             //System.out.println(">>> attr: is null.");
         return new Forward("gotoError");
         }
      return new Forward("gotoDone");
      }
   }
