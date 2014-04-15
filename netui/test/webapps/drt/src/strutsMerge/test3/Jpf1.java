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
package strutsMerge.test3;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * @jpf:controller struts-merge="/strutsMerge/test3/merge-jpf-struts-config.xml"
 */
@Jpf.Controller(
    strutsMerge = "/strutsMerge/test3/merge-jpf-struts-config.xml")
public class Jpf1 extends PageFlowController
    {
    private static final String TEST_STR = "Test Value";

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
    protected Forward jpfAction1(Form1 inForm)
        {
            //System.out.println(">>> Jpf1.jpfAction1 - form instance: ("
            //               + inForm.toString() + ").");
        inForm.setField1(TEST_STR);
        return new Forward("gotoPg2");
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
    protected Forward jpfAction2(Form1 inForm)
        {
            //System.out.println(">>> Jpf1.jpfAction2 - form instance: ("
            //               + inForm.toString() + ").");
        if (inForm.getField1().equals(TEST_STR) != true)
            {
            return new Forward("gotoError");
            }
        return new Forward("gotoDone");
        }

    /***************************************************************************
     *
     **************************************************************************/
    public static class Form1 implements Serializable
        {
        private String field1 = "Default value";
        public Form1()
            {
            super();
//System.out.println(">>> Form1.constructor - instance: ("
//                               + this.toString() + ").");
            }
        public void setField1(String inField1)
            {
    //System.out.println(">>> Form1.setField1 - instance: (" + this.toString()
    //                           + ") old value: (" + field1
    //                           + ") new value: (" + inField1 + ").");
            this.field1 = inField1;
            }
        public String getField1()
            {
    //System.out.println(">>> Form1.getField1 - instance: (" + this.toString()
    //                           + ") field1 value: (" + field1 + ").");
            return this.field1;
            }
        }
    }
