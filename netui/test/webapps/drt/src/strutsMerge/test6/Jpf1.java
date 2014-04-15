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
package strutsMerge.test6;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * @jpf:controller struts-merge="/strutsMerge/test6/merge-jpf-struts-config.xml"
 */
@Jpf.Controller(
    strutsMerge = "/strutsMerge/test6/merge-jpf-struts-config.xml")
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
    protected Forward begin()
        {
            //System.out.println(">>> Jpf1.begin");
        return new Forward("gotoPg1");
        }

    /**
     * Special note:  I have added the forward "gotoDone" here in this method
     * as the exception handler "xHandler2" will forward to "gotoDone" if the
     * "struts-merge" works as it should.  And since exception handlers
     * "forwards" are actually added to config xml for the action that threw
     * the error I needed to add the "gotoDone" forward here.  The
     * exception-handlers forwards are added based based on the "jpf:catch"
     * annotation.  This is a bit of a hack but I did it to simply this test.
     *
     * @jpf:action
     * @jpf:forward name="gotoError1" path="/resources/jsp/error.jsp"
     * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
     * @jpf:catch type="strutsMerge.test6.Jpf1$Test1Exception"
     *              method="xHandler1"
     *              message="Test message text from jpfAction1"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoError1",
                path = "/resources/jsp/error.jsp"),
            @Jpf.Forward(
                name = "gotoDone",
                path = "/resources/jsp/done.jsp")
        },
        catches = {
            @Jpf.Catch(
                type = strutsMerge.test6.Jpf1.Test1Exception.class,
                method = "xHandler1",
                message = "Test message text from jpfAction1")
        })
    protected Forward jpfAction1(Form1 inForm) throws Exception
        {
            //System.out.println(">>> Jpf1.jpfAction1");
        if (inForm.getField1() == true)
            {
                //System.out.println(">>> Jpf1.jpfAction1 - Throw exception.");
            throw new Test1Exception();
            }
        //System.out.println(">>> Jpf1.jpfAction1 - Going to gotoError1.");
        return new Forward("gotoError1");
        }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="gotoError2" path="/resources/jsp/error.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "gotoError2",
                path = "/resources/jsp/error.jsp")
        })
    protected Forward xHandler1(Test1Exception InX, String inActionName
                                ,String inMessage, Object inForm)
        {
            //System.out.println(">>> Jpf1.xHandler1");
        return new Forward( "gotoError2" );
        }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "gotoDone",
                path = "/resources/jsp/done.jsp")
        })
    protected Forward xHandler2(Test1Exception InX, String inActionName
                                ,String inMessage, Object inForm)
        {
            //System.out.println(">>> Jpf1.xHandler2");
        return new Forward( "gotoDone" );
        }

    /***************************************************************************
     *
     **************************************************************************/
    public static class Test1Exception extends Exception
        {
        }

    /***************************************************************************
     *
     **************************************************************************/
    public static class Form1 implements Serializable
        {
        private boolean field1 = true;

        public Form1()
            {
            super();
            //System.out.println(">>> Form1.constructor - instance: ("
            //                   + this.toString() + ").");
            }
        public void setField1(boolean inField1)
            {
                //System.out.println(">>> Form1.setField1 - instance: ("
                //               + this.toString() + ").");
            this.field1 = inField1;
            }
        public boolean getField1()
            {
                //System.out.println(">>> Form1.getField1 - instance: ("
                //               + this.toString() + ").");
            return this.field1;
            }
        }
    }
