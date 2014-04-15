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
package Nested_NewReturnTo;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.lang.String;
import org.apache.beehive.netui.pageflow.*;

/**
 *******************************************************************************
 *
 * @desc: This PageFlowController file contains several tests:
 *    - Test the new "return-to" values "previousPage", "currentPage", and
 *      "previousAction"
 *    - Test that the old "return-to" values, "page", and "action" have been
 *      depricated.
 *
 * @result: Throws a warning and generates a config file.
 *
 *
 ******************************************************************************/
@Jpf.Controller(
    nested = true)
public class Controller extends PageFlowController
{
    /**
     * This is valid.
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page1",
                path = "Page1.jsp")
        })
    protected Forward begin()
    {
        return new Forward("page1");
    }

    /**
     * This is valid.
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page1",
                navigateTo = Jpf.NavigateTo.previousPage)
        })
    protected Forward action1()
    {
        return new Forward("page1");
    }

    /**
     * This is valid.
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page1",
                navigateTo = Jpf.NavigateTo.currentPage)
        })
    protected Forward action2()
    {
        return new Forward("page1");
    }

    /**
     * This is valid.
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page1",
                navigateTo = Jpf.NavigateTo.previousAction)
        })
    protected Forward action3()
    {
        return new Forward("page1");
    }

    /**
     * This should be depricated.
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page1",
                navigateTo = Jpf.NavigateTo.previousAction)
        })
    protected Forward action4()
    {
        return new Forward("page1");
    }

    /**
     * This is should be depricated.
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page1",
                navigateTo = Jpf.NavigateTo.page)
        })
    protected Forward action5()
    {
        return new Forward("page1");
    }

    /**
     * This is invalid.
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "callingJpf",
                returnAction = "someAction")
        })
    protected Forward action8()
    {
        return new Forward("callingJpf");
    }
}
