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
package conditionalForwardExpression;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(
            name = "anAction", path = "index.jsp",
            conditionalForwards = {
                @Jpf.ConditionalForward(
                    condition = "${pageFlow.choice=='page1'}",
                    path = "page1.jsp"),
                @Jpf.ConditionalForward(
                    condition = "${pageFlow.choice=='page1'}",
                    path = "index.jsp"),
                @Jpf.ConditionalForward(
                    condition = "${pageFlow.choice=='page2'}",
                    path = "page2.jsp")
            }
        ),
        @Jpf.SimpleAction(
            name = "anotherAction", path = "index.jsp", 
            conditionalForwards = {
                @Jpf.ConditionalForward(
                    condition = "${pageFlow.choice=='page1'}", 
                    path = "page1.jsp"),
                @Jpf.ConditionalForward(
                    condition = "${pageFlow.choice=='page2'}", 
                    path = "index.jsp"),
                @Jpf.ConditionalForward(
                    condition = "${pageFlow.choice=='page2'}", 
                    path = "page2.jsp")
            }
        ),
        @Jpf.SimpleAction(name="goPrev", navigateTo=Jpf.NavigateTo.previousPage)
    }
)
public class Controller 
    extends PageFlowController
{
    private String _choice;

    public String getChoice() {
        return _choice;
    }

    public void setChoice(String choice) {
        _choice = choice;
    }
}
