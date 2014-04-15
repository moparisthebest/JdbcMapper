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
package miscJpf.bug30303;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.io.Serializable;

@Jpf.Controller
public class Jpf1 extends PageFlowController
   {
   private static final String STRUTS_VALUE = "Struts Value";
   private static final String PAGEFLOW_VALUE = "PageFlow Value";

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
    * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
    * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoDone",
                path = "/resources/jsp/done.jsp"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   public Forward jpfAction1(UploadForm form)
      {
      if (form.getStrutsText().equals(STRUTS_VALUE) == false)
         {
         return new Forward("gotoError");
         }
      if (form.getPageflowText().equals(PAGEFLOW_VALUE) == false)
         {
         return new Forward("gotoError");
         }
      return new Forward( "gotoDone");
      }

   /****************************************************************************
    * Form bean
    ***************************************************************************/
   public static class UploadForm implements Serializable
      {
      private String strutsText;
      private String pageflowText;

      public String getStrutsText()
         {
             //System.out.println(">>> UploadForm.getStrutsText");
         return strutsText;
         }

      public void setStrutsText(String strutsText)
         {
             //System.out.println(">>> UploadForm.setStrutsText");
         this.strutsText = strutsText;
         }

      public String getPageflowText()
         {
             //System.out.println(">>> UploadForm.setPageFlowText");
         return pageflowText;
         }

      public void setPageflowText(String pageflowText)
         {
             //System.out.println(">>> UploadForm.getPageFlowText");
         this.pageflowText = pageflowText;
         }
      }
   }
