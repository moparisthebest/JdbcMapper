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
package pageInput.test10.sub1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import pageInput.test10.Jpf1;


/**
 * @jpf:controller nested="true"
 * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 */
@Jpf.Controller(
    nested = true,
    forwards = {
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp"),
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp") 
    })
public class SubJpf1 extends PageFlowController
    {
    /**
     * @jpf:action
     * @jpf:forward name="gotoSubPg1" path="SubJsp1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoSubPg1",
                path = "SubJsp1.jsp") 
        })
    protected Forward begin(pageInput.test10.Jpf1.FormOne inForm)
        {
        //System.out.println(">>> SubJpf1.begin");
        inForm.setFld1(Jpf1.NEW_FORM_FLD_1);
        inForm.setFld2(Jpf1.NEW_FORM_FLD_2);
        Forward fwd = new Forward("gotoSubPg1");
        fwd.addPageInput("pgInputForm", inForm);
        return fwd;
        }

    /**
     * @jpf:action
     * @jpf:forward name="continue" return-action="action2"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "continue",
                returnAction = "action2") 
        })
    protected Forward action1()
        {
        //System.out.println(">>> Jpf1.action1");
        return new Forward("continue");
        }
    }
