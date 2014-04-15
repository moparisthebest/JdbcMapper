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
package pageInput.test4;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * @jpf:controller
 * @jpf:forward name="gotoError"    path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoDone"     path="/resources/jsp/done.jsp"
 */
@Jpf.Controller(
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
    private FormOne theForm     = new FormOne();

    /**
     * We are going to add the SAME FormBean "theForm" to the "Forward" once as
     * a formBean and once as pageInput.
     *
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
        Forward fwd = new Forward("gotoPg1");
        fwd.addOutputForm(theForm);
        fwd.addPageInput("theForm", theForm);
        return fwd;
        }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward action1(FormOne inForm)
        {
            //System.out.println(">>> Jpf1.action1");

        // If the form field values have changed then the user was allowed to
        // change the values of the pageInput fields and this is not allowed
        // and is a bug.  There should also be a junit test too that makes sure
        // the run time is doing the right thing and an NEUI ERROR is logged to
        // the console.  Note we check both the form passed in and the original
        // form instance.
        //----------------------------------------------------------------------
        if (FormOne.VALUE1.equals(theForm.getFld1()) != true)
            {
                //System.out.println("theForm.fld1: " + inForm.getFld1());
            return new Forward("gotoError");
            }
        if (FormOne.VALUE2.equals(theForm.getFld2()) != true)
            {
                //System.out.println("theForm.fld2: " + inForm.getFld2());
            return new Forward("gotoError");
            }
        if (FormOne.VALUE1.equals(inForm.getFld1()) != true)
            {
                //System.out.println("inForm.fld1: " + inForm.getFld1());
            return new Forward("gotoError");
            }
        if (FormOne.VALUE2.equals(inForm.getFld2()) != true)
            {
                //System.out.println("inForm.fld2: " + inForm.getFld2());
            return new Forward("gotoError");
            }
        return new Forward("gotoDone");
        }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class FormOne implements Serializable
        {
        public final static String VALUE1 = "Field one value";
        public final static String VALUE2 = "Field two value";

        private String fld1 = VALUE1;
        private String fld2 = VALUE2;

        public void setFld1(String fld1)
            {
            this.fld1 = fld1;
            }

        public String getFld1()
            {
            return this.fld1;
            }

        public void setFld2(String fld2)
            {
            this.fld2 = fld2;
            }

        public String getFld2()
            {
            return this.fld2;
            }
        }
    }
