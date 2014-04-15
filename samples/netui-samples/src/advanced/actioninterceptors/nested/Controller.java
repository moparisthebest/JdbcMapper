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
package advanced.actioninterceptors.nested;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.PageFlowController;

/**
 * Demonstration of Page Flow action interceptors.
 */
@Jpf.Controller(
    nested=true,
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),

        // This is the action that returns from our nested flow. Note that in this particular
        // example, this nested flow is *only* being used from an action interceptor that
        // "injects" it before the begin action of /interceptme/Controller.jpf.  That interceptor
        // doesn't care what action we return ("nestedDone" in this case).  We could have used any
        // other string for returnAction.
        @Jpf.SimpleAction(name="done", returnAction="nestedDone")
    }
)
public class Controller
    extends PageFlowController {
}
