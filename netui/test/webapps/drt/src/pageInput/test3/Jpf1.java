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
package pageInput.test3;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 * @jpf:catch type="java.lang.Throwable" method="exceptionHandler"
 * @jpf:forward name="gotoError"    path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoDone"     path="/resources/jsp/done.jsp"
 */
@Jpf.Controller(
    catches = {
        @Jpf.Catch(
            type = java.lang.Throwable.class,
            method = "exceptionHandler") 
    },
    forwards = {
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp"),
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp") 
    })
public class Jpf1 extends PageFlowController
    {
    public  String  pageValue1  = "PageValue 1";
    public  String  pageValue2  = "PageValue 2";
    private int     cnter       = 0;

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
        cnter = 0;
        return new Forward("gotoPg1");
        }

    /**
     * This should fail as the forwardName parameter value is null;
     *
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward action1()
        {
            //System.out.println(">>> Jpf1.action1");
        cnter = 1;
        return new Forward(null, "pgInput", pageValue1);
        }

    /**
     * This should fail as the pageInputName parameter value is null;
     *
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward action2()
        {
            //System.out.println(">>> Jpf1.action2");
        cnter = 2;
        return new Forward("gotoError", null, pageValue1);
        }

    /**
     * This should succeed.  It is valid to pass a null pageInput value.
     *
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
        cnter = 3;
        return new Forward("gotoPg4", "pgInput", null);
        }

    /**
     * This should fail as an empty string is not valid as a pageInput name.
     *
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward action4()
        {
            //System.out.println(">>> Jpf1.action4");
        cnter = 4;
        return new Forward("", "pgInput", pageValue1);
        }

    /**
     * This should fail as an empty string is not a valid pageInputName
     * parameter value.
     *
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward action5()
        {
            //System.out.println(">>> Jpf1.action5");
        cnter = 5;
        return new Forward("gotoError", "", pageValue1);
        }

    /**
     * This should succeed as an empty string is a valid value for the
     * pageInputValue.
     *
     * @jpf:action
     * @jpf:forward name="gotoPg7" path="Jsp7.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg7",
                path = "Jsp7.jsp") 
        })
    protected Forward action6()
        {
            //System.out.println(">>> Jpf1.action6");
        cnter = 6;
        return new Forward("gotoPg7", "pgInput", "");
        }

    /**
     * This should fail as the pageInputName parameter value is null.
     *
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward action7()
        {
            //System.out.println(">>> Jpf1.action7");
        cnter = 7;
        Forward fwd = new Forward("gotoError");
        fwd.addPageInput(null, pageValue1);
        return fwd;
        }

    /**
     * This should pass as the pageInputValue parameter can be null.
     *
     * @jpf:action
     * @jpf:forward name="gotoPg9" path="Jsp9.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg9",
                path = "Jsp9.jsp") 
        })
    protected Forward action8()
        {
            //System.out.println(">>> Jpf1.action8");
        cnter = 8;
        Forward fwd = new Forward("gotoPg9");
        fwd.addPageInput("pgInput", null);
        return fwd;
        }

    /**
     * This should fail as an empty string is not a valid pageInputName
     * parameter value.
     *
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward action9()
        {
            //System.out.println(">>> Jpf1.action9");
        cnter = 9;
        Forward fwd = new Forward("gotoPg10");
        fwd.addPageInput("", null);
        return fwd;
        }

    /**
     * This should succeed as an empty string is a valid value for the
     * pageInputValue.
     *
     * @jpf:action
     * @jpf:forward name="gotoPg11" path="Jsp11.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg11",
                path = "Jsp11.jsp") 
        })
    protected Forward action10()
        {
            //System.out.println(">>> Jpf1.action10");
        cnter = 10;
        Forward fwd = new Forward("gotoPg11");
        fwd.addPageInput("pgInput", "");
        return fwd;
        }

    /**
     * This should succeed.  The runtime will log a warning that the value is
     * being overwritten.
     *
     * @jpf:action
     * @jpf:forward name="gotoPg12" path="Jsp12.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg12",
                path = "Jsp12.jsp") 
        })
    protected Forward action11()
        {
            //System.out.println(">>> Jpf1.action11");
        cnter = 11;
        Forward fwd = new Forward("gotoPg12");
        fwd.addPageInput("value1", pageValue1);
        fwd.addPageInput("value1", pageValue2);
        return fwd;
        }

    /**
     * This should succeed.  This is normal, expected behavior.
     *
     * @jpf:action
     * @jpf:forward name="gotoPg13" path="Jsp13.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg13",
                path = "Jsp13.jsp") 
        })
    protected Forward action12()
        {
            //System.out.println(">>> Jpf1.action12");
        cnter = 12;
        Forward fwd = new Forward("gotoPg13");
        fwd.addPageInput("value1", pageValue1);
        fwd.addPageInput("value2", pageValue2);
        return fwd;
        }

    /**
     *
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward finish()
        {
            //System.out.println(">>> Jpf1.finish");
        cnter = 13;
        return new Forward("gotoDone");
        }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="exGotoPg2" path="Jsp2.jsp"
     * @jpf:forward name="exGotoPg3" path="Jsp3.jsp"
     * @jpf:forward name="exGotoPg5" path="Jsp5.jsp"
     * @jpf:forward name="exGotoPg6" path="Jsp6.jsp"
     * @jpf:forward name="exGotoPg7" path="Jsp7.jsp"
     * @jpf:forward name="exGotoPg8" path="Jsp8.jsp"
     * @jpf:forward name="exGotoPg10" path="Jsp10.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "exGotoPg2",
                path = "Jsp2.jsp"),
            @Jpf.Forward(
                name = "exGotoPg3",
                path = "Jsp3.jsp"),
            @Jpf.Forward(
                name = "exGotoPg5",
                path = "Jsp5.jsp"),
            @Jpf.Forward(
                name = "exGotoPg6",
                path = "Jsp6.jsp"),
            @Jpf.Forward(
                name = "exGotoPg7",
                path = "Jsp7.jsp"),
            @Jpf.Forward(
                name = "exGotoPg8",
                path = "Jsp8.jsp"),
            @Jpf.Forward(
                name = "exGotoPg10",
                path = "Jsp10.jsp") 
        })
    protected Forward exceptionHandler(Throwable inEx
                                       ,String inActionName
                                       ,String inMessage
                                       ,Object inForm)
        {
            //System.out.println(">>> Jpf1.exceptionHandler");
            //System.out.println("\t>>> Exception message: "
            //               + inEx.getLocalizedMessage());

        switch (cnter)
            {
            case 1:
                {
                    //System.out.println("\t>>> case:1");
                return new Forward("exGotoPg2");
                }
            case 2:
                {
                    //System.out.println("\t>>> case:2");
                return new Forward("exGotoPg3");
                }
            case 3:
                {
                    //System.out.println("\t>>> case:3");
                return new Forward("exGotoError");
                }
            case 4:
                {
                    //System.out.println("\t>>> case:4");
                return new Forward("exGotoPg5");
                }
            case 5:
                {
                    //System.out.println("\t>>> case:5");
                return new Forward("exGotoPg6");
                }
            case 6:
                {
                    //System.out.println("\t>>> case:6");
                return new Forward("exGotoError");
                }
            case 7:
                {
                    //System.out.println("\t>>> case:7");
                return new Forward("exGotoPg8");
                }
            case 8:
                {
                    //System.out.println("\t>>> case:8");
                return new Forward("exGotoError");
                }
            case 9:
                {
                    //System.out.println("\t>>> case:9");
                return new Forward("exGotoPg10");
                }
            case 10:
                {
                    //System.out.println("\t>>> case:10");
                return new Forward("exGotoError");
                }
            case 11:
                {
                    //System.out.println("\t>>> case:11");
                return new Forward("exGotoError");
                }
            case 12:
                {
                    //System.out.println("\t>>> case:12");
                return new Forward("exGotoError");
                }
            case 13:
                {
                    //System.out.println("\t>>> case:13");
                return new Forward("exGotoError");
                }
            default:
                {
                    //System.out.println("\t>>> case:default");
                return new Forward("exGotoError");
                }
            }
        }
    }
