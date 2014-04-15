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
package miniTests.overloadedActions.nested;

import miniTests.overloadedActions.Controller.Form1;
import miniTests.overloadedActions.Controller.Form2;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.io.Serializable;


/**
 * @jpf:controller nested="true"
 */
@Jpf.Controller(
    nested = true)
public class Controller extends PageFlowController
{
    private miniTests.overloadedActions.Controller.Form2 _form2;


    /**
     * @jpf:action
     * @jpf:forward name="returnNoForm" return-action="overloaded"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "returnNoForm",
                returnAction = "overloaded") 
        })
    public Forward begin()
    {
        return new Forward( "returnNoForm" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="returnForm1" return-action="overloaded" return-form-type="Form1"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "returnForm1",
                returnAction = "overloaded",
                outputFormBeanType = Form1.class) 
        })
    public Forward begin( Form1 form )
    {
        return new Forward( "returnForm1" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="returnForm2" return-action="overloaded" return-form="_form2"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "returnForm2",
                returnAction = "overloaded",
                outputFormBean = "_form2") 
        })
    public Forward begin( Form2 form )
    {
        return new Forward( "returnForm2" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="returnUnknownForm" return-action="overloaded" return-form-type="UnknownForm"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "returnUnknownForm",
                returnAction = "overloaded",
                outputFormBeanType = UnknownForm.class) 
        })
    public Forward returnUnknownForm()
    {
        return new Forward( "returnUnknownForm" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="return" return-action="normalReturnWithForm" return-form-type="Form1"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "normalReturnWithForm",
                outputFormBeanType = Form1.class) 
        })
    public Forward returnNormalWithForm()
    {
        return new Forward( "return" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="return" return-action="normalReturnWithoutForm"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "normalReturnWithoutForm") 
        })
    public Forward returnNormalWithoutForm()
    {
        return new Forward( "return" );
    }

    public static class UnknownForm implements Serializable {}
}
