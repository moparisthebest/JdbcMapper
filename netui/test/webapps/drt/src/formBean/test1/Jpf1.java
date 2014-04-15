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
package formBean.test1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionMapping;

/**
 * @jpf:controller struts-merge="/formBean/test1/merge-jpf-struts-config.xml"
 */
@Jpf.Controller(
    strutsMerge = "/formBean/test1/merge-jpf-struts-config.xml")
public class Jpf1 extends PageFlowController
    {
//    protected transient global.Global globalApp;
//    protected static FormBeanTest1Form1 theForm = new FormBeanTest1Form1();
    public static final String JPF_FORM_VALUE = "Jpf form value";

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
        ActionMapping mapping = getMapping();
        //System.out.println(">>> Jpf1.begin");
        return new Forward("gotoPg1");
        }

    /**
     * @jpf:action
     * @jpf:forward name="gotoStruts" path="/formBeanTest1/strutsAction1.do"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoStruts",
                path = "/formBeanTest1/strutsAction1.do") 
        })
    protected Forward jpfAction1(FormBeanTest1Form1 inForm)
        {
        ActionMapping mapping = getMapping();
        //System.out.println(">>> Jpf1.jpfAction1 - ("
        //                   + inForm.toString() + ").");
        inForm.setField1(JPF_FORM_VALUE);
        return new Forward("gotoStruts");
        }

    /**
     * @jpf:action
     * @jpf:forward name="gotoPg3" path="Jsp3.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg3",
                path = "Jsp3.jsp") 
        })
    protected Forward jpfAction2(FormBeanTest1Form1 inForm)
        {
        ActionMapping mapping = getMapping();
        //System.out.println(">>> Jpf1.jpfAction2 - ("
        //                   + inForm.toString() + ").");
        return new Forward("gotoPg3");
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
    protected Forward jpfAction3(FormBeanTest1Form1 inForm)
        {
        ActionMapping mapping = getMapping();
//System.out.println(">>> Jpf1.jpfAction3 - form instance: ("
//                           + inForm.toString() + ").");
        if (inForm.getField1().equals(Struts2.STRUTS2_FORM_VALUE) == true)
            {
            return new Forward("gotoDone");
            }
        return new Forward("gotoError");
        }
    }
