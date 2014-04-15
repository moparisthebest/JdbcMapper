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
package miscJpf.bug26990;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionServlet;

import javax.servlet.http.HttpSession;

@Jpf.Controller
public class Jpf1 extends PageFlowController
   {
   private  int   eCnt  = 0;

   /****************************************************************************
    * onCreate()
    ***************************************************************************/
   public void onCreate()
      {
      ActionServlet  aServ = null;
      //System.out.println(">>> Jpf1.onCreate");
      try
         {
         aServ  = getServlet();
         }
      catch (Throwable e)
         {
             //System.out.println(">>> Jpf1.onCreate - caught exception:");
         e.printStackTrace();
         }
      if (aServ == null) eCnt++;
      return;
      }

   /****************************************************************************
    * beforeAction()
    ***************************************************************************/
   public void beforeAction()
      {
      ActionServlet  aServ = null;
      //System.out.println(">>> Jpf1.beforeAction");
      try
         {
         aServ  = getServlet();
         }
      catch (Throwable e)
         {
             //System.out.println(">>> Jpf1.beforeAction - caught exception:");
         e.printStackTrace();
         }
      if (aServ == null) eCnt++;
      return;
      }

   /****************************************************************************
    * afterAction()
    ***************************************************************************/
   public void afterAction()
      {
      ActionServlet  aServ = null;
      //System.out.println(">>> Jpf1.afterAction");
      try
         {
         aServ  = getServlet();
         }
      catch (Throwable e)
         {
             //System.out.println(">>> Jpf1.afterAction - caught exception:");
         e.printStackTrace();
         }
      if (aServ == null) eCnt++;
      return;
      }

   /****************************************************************************
    * refresh()
    ***************************************************************************/
   public void refresh()
      {
      ActionServlet  aServ = null;
      //System.out.println(">>> Jpf1.refresh");
      try
         {
         aServ  = getServlet();
         }
      catch (Throwable e)
         {
             //System.out.println(">>> Jpf1.refresh - caught exception:");
         e.printStackTrace();
         }
      if (aServ == null) eCnt++;
      return;
      }

   /****************************************************************************
    * onDestroy()
    ***************************************************************************/
   public void onDestroy(HttpSession session)
      {
      ActionServlet  aServ = null;
      //System.out.println(">>> Jpf1.onDestroy");
      try
         {
         aServ  = getServlet();
         }
      catch (Throwable e)
         {
             //System.out.println(">>> Jpf1.onDestroy - caught exception:");
         e.printStackTrace();
         }
      if (aServ == null) eCnt++;
      return;
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
   protected Forward begin()
      {
          //System.out.println(">>> Jpf1.begin");
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
   protected Forward jpfAction1()
      {
          //System.out.println(">>> Jpf1.jpfAction1");
      if (eCnt != 0)
         {
         return new Forward("gotoError");
         }
      return new Forward("gotoDone");
      }
   }
