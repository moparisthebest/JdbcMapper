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
package templateactions.shared;

import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions = {
    @Jpf.SimpleAction(name = "goMain", path = "/templateactions/MainFlow.jpf"),
    @Jpf.SimpleAction(name = "goFlow1", path = "/templateactions/flow1/Flow1.jpf"),
    @Jpf.SimpleAction(name = "goFlow2", path = "/templateactions/flow2/Flow2.jpf")
        }
)
public class SharedFlow extends SharedFlowController {

    private boolean _notesVisible = false;

    public boolean isNotesVisible() {
        return _notesVisible;
    }

    @Jpf.Action(
        forwards = {
        @Jpf.Forward(name = "currentPage", navigateTo = Jpf.NavigateTo.currentPage)
            }
    )
    public Forward toggleNotes() {
        _notesVisible = ! _notesVisible;
        return new Forward("currentPage");
    }
}
