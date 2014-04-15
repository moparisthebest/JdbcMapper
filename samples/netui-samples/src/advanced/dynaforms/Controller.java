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
package advanced.dynaforms;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.DynaActionForm;

// Note: the merge file "merge-struts-config-dynaforms.xml" is in this directory
// to make the example more encapsulated.  It could also reside in WEB-INF,
// and be referenced as "/WEB-INF/merge-struts-config-dynaforms.xml".

@Jpf.Controller(
    strutsMerge="merge-struts-config-dynaforms.xml",
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="input.jsp")
    }
)
public class Controller
    extends PageFlowController {

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="output", path="output.jsp")
        },
        // Note: the validation annotations are optional!
        validatableProperties={
            @Jpf.ValidatableProperty(
                displayName="The name",  // could use displayNameKey for internationalization
                propertyName="name",
                validateRequired=@Jpf.ValidateRequired(),
                validateMinLength=@Jpf.ValidateMinLength(chars=2),
                validateMask=@Jpf.ValidateMask(regex="^[A-Za-z ]*$")
            ),
            @Jpf.ValidatableProperty(
                displayName="The age",  // could use displayNameKey for internationalization
                propertyName="age",
                validateRequired=@Jpf.ValidateRequired(),
                validateRange=@Jpf.ValidateRange(minInt=1, maxInt=150)
            )
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="input.jsp")
    )
    public Forward submit(DynaActionForm form) {
        Forward fwd = new Forward("output");
        fwd.addActionOutput("name", form.get("name"));
        fwd.addActionOutput("age", form.get("age"));
        return fwd;
    }
}
