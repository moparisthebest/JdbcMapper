/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   $Header:$
*/
package nesting;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import nesting.chooseairport.ChooseAirport;

/**
 * Main page flow, which invokes a nested page flow and gets data back from it.
 */
@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),

        // This action runs the Choose Airport wizard.  Since the ChooseAirport page flow is marked
        // with nested=true, it will be able to return control to this page flow when it is done.
        @Jpf.SimpleAction(name="chooseAirport", path="/nesting/chooseairport/ChooseAirport.jpf"),

        // This action is raised by the ChooseAirport page flow  The navigateTo attribute here
        // causes flow to return to the current page in this page flow.
        @Jpf.SimpleAction(name="chooseAirportCancelled", navigateTo=Jpf.NavigateTo.currentPage)
    }
)
public class Controller
    extends PageFlowController {
    // This property ("yourName") is used in the JSPs to show that this page flow's state is
    // restored when you return from the nested flow.
    private String _yourName;

    public String getYourName() {
        return _yourName;
    }

    public void setYourName(String yourName) {
        _yourName = yourName;
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="results",
                path="results.jsp",
                actionOutputs={
                    @Jpf.ActionOutput(name="airport", type=String.class, required=true)
                }
            )
        }
    )
    protected Forward chooseAirportDone(ChooseAirport.Results results) {
        Forward fwd = new Forward("results");
        fwd.addActionOutput("airport", results.getAirport());
        return fwd;
    }
}
