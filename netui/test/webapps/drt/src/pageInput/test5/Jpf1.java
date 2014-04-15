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
package pageInput.test5;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

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
    public  String  pageValue1  = "PageValue 1";
    public  String  pageValue2  = "PageValue 2";
    public  FormOne form1       = new FormOne();

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
        Forward fwd = new Forward("gotoPg1", form1);
        fwd.addPageInput("pgInput1", pageValue1);
        fwd.addPageInput("pgInput2", pageValue2);
        return fwd;
        }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward finish()
        {
            //System.out.println(">>> Jpf1.done");
        return new Forward("gotoDone");
        }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class FormOne implements Serializable
        {
        private String fld1 = "Field one value";
        private String fld2 = "Field two value";

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
