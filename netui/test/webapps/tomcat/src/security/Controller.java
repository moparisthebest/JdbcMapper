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
package security;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.jsp") 
        })
    public Forward begin()
    {
        return new Forward( "index" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "secure",
                path = "page2.jsp") 
        })
    public Forward secure()
    {
        return new Forward( "secure" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "unsecure",
                path = "page2.jsp") 
        })
    public Forward unsecure()
    {
        return new Forward( "unsecure" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "back_secure",
                path = "index.jsp") 
        })
    public Forward back_secure()
    {
        return new Forward( "back_secure" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "back_unsecure",
                path = "index.jsp") 
        })
    public Forward back_unsecure()
    {
        return new Forward( "back_unsecure" );
    }

}
