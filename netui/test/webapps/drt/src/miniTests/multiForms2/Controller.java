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
package miniTests.multiForms2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    validatableBeans = { 
        @Jpf.ValidatableBean( 
            type = FooForm.class,
            validatableProperties = { 
                @Jpf.ValidatableProperty(
                    propertyName = "fooText",
                    validateRequired =
                        @Jpf.ValidateRequired(
                            enabled = true,
                            message = "requires 3-9 characters"
                        ),
                    validateMinLength =
                        @Jpf.ValidateMinLength(
                            enabled = true,
                            chars = 3,
                            message = "requires 3-9 characters"
                        ),
                    validateMaxLength =
                        @Jpf.ValidateMaxLength(
                            enabled = true,
                            chars = 9,
                            message = "requires 3-9 characters"
                        )
                )
            }
        )
    }
)
public class Controller extends PageFlowController
{
    private String _text;

    public String getText() {
        return _text;
    }
    public void setText(String text) {
        _text = text;
    }

    public static class BarForm 
            implements java.io.Serializable
    {
        private String _barText = "bar";
        public void setBarText( String barText ) { _barText = barText; }
        public String getBarText() { return _barText; }
    }

    @Jpf.Action( forwards = { @Jpf.Forward( name="index", path="index.jsp") } )
    protected Forward begin()
    {
        setText( "begin" );
        return new Forward( "index" );
    }
    
    @Jpf.Action(
        validationErrorForward =
            @Jpf.Forward( name="error", path="error.jsp"  ),
        forwards = { @Jpf.Forward( name="success", path="index.jsp") }
    )
    protected Forward foo( FooForm form )
    {
        setText( form.getFooText() );
        return new Forward( "success" );
    }
    
    @Jpf.Action(
        forwards = { @Jpf.Forward( name="success", path="index.jsp") }
    )
    protected Forward bar( BarForm form )
    {
        setText( form.getBarText() );
        return new Forward( "success" );
    }
}
