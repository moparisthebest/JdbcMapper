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
package miscJpf.bug27848;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpServletRequest;

@Jpf.Controller
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
     * @jpf:action
     * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoDone",
                path = "/resources/jsp/done.jsp") 
        })
    protected Forward jpfAction1()
        {
            //System.out.println(">>> Jpf1.jpfAction1");
        return new Forward("gotoDone");
        }

    public String getField1()
        {
        HttpServletRequest tmpReq = getRequest();
        return "some string";
        }
    }
