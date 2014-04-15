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
package validation.validateURL;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validatableProperties={
            @Jpf.ValidatableProperty(
                propertyName="URL",
                displayName="This",
                validateURL=@Jpf.ValidateURL()
            )
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward validateURL(MyForm form)
    {
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validatableProperties={
            @Jpf.ValidatableProperty(
                propertyName="URL",
                displayName="This",
                validateURL=@Jpf.ValidateURL(allowAllSchemes=true)
            )
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward validateURLAllowAllSchemes(MyForm form)
    {
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validatableProperties={
            @Jpf.ValidatableProperty(
                propertyName="URL",
                displayName="This",
                validateURL=@Jpf.ValidateURL(allowTwoSlashes=true)
            )
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward validateURLAllowTwoSlashes(MyForm form)
    {
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validatableProperties={
            @Jpf.ValidatableProperty(
                propertyName="URL",
                validateURL=@Jpf.ValidateURL(disallowFragments=true, message="fragments disallowed")
            )
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward validateURLDisallowFragments(MyForm form)
    {
        return new Forward("index");
    }


    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validatableProperties={
            @Jpf.ValidatableProperty(
                propertyName="URL",
                validateURL=@Jpf.ValidateURL(
                    schemes={ "special", "sauce" },
                    message="only the schemes 'special' and 'sauce' are allowed"
                )
            )
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward validateURLAllowOnlySpecialSauceScheme(MyForm form)
    {
        return new Forward("index");
    }

    public static class MyForm implements java.io.Serializable
    {
        private String _url;

        public String getURL()
        {
            return _url;
        }

        public void setURL(String url)
        {
            _url = url;
        }
    }
}
