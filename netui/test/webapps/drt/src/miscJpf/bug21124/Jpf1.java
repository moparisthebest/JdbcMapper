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
package miscJpf.bug21124;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    catches = {
        @Jpf.Catch(
            type = java.lang.ClassNotFoundException.class,
            method = "handler1"),
        @Jpf.Catch(
            type = java.lang.UnknownError.class,
            method = "handler2"),
        @Jpf.Catch(
            type = java.lang.reflect.UndeclaredThrowableException.class,
            method = "handler3"),
        @Jpf.Catch(
            type = java.lang.IllegalArgumentException.class,
            method = "handler4"),
        @Jpf.Catch(
            type = java.lang.Throwable.class,
            method = "handler5") 
    })
public class Jpf1 extends PageFlowController
{
    @org.apache.beehive.controls.api.bean.Control()
    private Ctrl myCtrl;

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

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
    protected Forward act1()
        {
            //System.out.println(">>> Jpf1.act1");
        myCtrl.throwWrappedChecked();
        return new Forward("gotoError");
        }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   protected Forward act2()
       {
           //System.out.println(">>> Jpf1.act2");
       myCtrl.throwWrappedRuntime();
       return new Forward("gotoError");
       }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   protected Forward act3()
       {
           //System.out.println(">>> Jpf1.act3");
       myCtrl.throwUnwrappedRuntime();
       return new Forward("gotoError");
       }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   protected Forward act4()
       {
           //System.out.println(">>> Jpf1.act4");
       myCtrl.throwWrappedError();
       return new Forward("gotoError");
       }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   protected Forward act5()
       {
           //System.out.println(">>> Jpf1.act5");
       myCtrl.throwUnwrappedError();
       return new Forward("gotoError");
       }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   protected Forward act6()
       {
           //System.out.println(">>> Jpf1.act6");
       myCtrl.throwWrappedUnhandled();
       return new Forward("gotoError");
       }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp"),
            @Jpf.Forward(
                name = "gotoError",
                path = "/resources/jsp/error.jsp") 
        })
   protected Forward act7()
       {
           //System.out.println(">>> Jpf1.act7");
       myCtrl.throwUnwrappedUnhandled();
       return new Forward("gotoError");
       }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoDone",
                path = "/resources/jsp/done.jsp") 
        })
   protected Forward done()
       {
           //System.out.println(">>> Jpf1.done");
       return new Forward("gotoDone");
       }

    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2a",
                path = "Jsp2.jsp") 
        })
    protected Forward handler1(java.lang.ClassNotFoundException ex
                               ,String actionName
                               ,String message
                               ,Object form
                               )
        {
            //System.out.println(">>> Jpf1.handler1 - (java.lang.ClassNotFoundException)");
        return new Forward("gotoPg2a");
        }

    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2b",
                path = "Jsp2.jsp") 
        })
    protected Forward handler2(java.lang.UnknownError ex
                               ,String actionName
                               ,String message
                               ,Object form
                               )
        {
            //System.out.println(">>> Jpf1.handler2 - (java.lang.UnknownError)");
        return new Forward("gotoPg2b");
        }

    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2c",
                path = "Jsp2.jsp") 
        })
    protected Forward handler3(java.lang.reflect.UndeclaredThrowableException ex
                               ,String actionName
                               ,String message
                               ,Object form
                               )
        {
            //System.out.println(">>> Jpf1.handler3 - (java.lang.reflect.UndeclaredThrowableException)");
        return new Forward("gotoPg2c");
        }

    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2d",
                path = "Jsp2.jsp") 
        })
    protected Forward handler4(java.lang.IllegalArgumentException ex
                               ,String actionName
                               ,String message
                               ,Object form
                               )
        {
            //System.out.println(">>> Jpf1.handler4 - (java.lang.IllegalArgumentException)");
        return new Forward("gotoPg2d");
        }

    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2e",
                path = "Jsp2.jsp") 
        })
    protected Forward handler5(java.lang.Throwable ex
                               ,String actionName
                               ,String message
                               ,Object form
                               )
        {
            //System.out.println(">>> Jpf1.handler5 - (java.lang.Throwable)");
        return new Forward("gotoPg2e");
        }
    }
