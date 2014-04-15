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
package pageFlowCore.anyBeanOverload;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.util.HashMap;

@Jpf.Controller
public class AnyBeanOverloadController extends PageFlowController
{
    private String _message;

    @Jpf.Action( forwards={ @Jpf.Forward( name="index", path="index.jsp") } )
    public Forward begin()
    {
        return new Forward( "index" );
    }

    @Jpf.Action( forwards={ @Jpf.Forward( name="overload", path="overload.do" ) } )
    public Forward chainToString( String form )
    {
        return new Forward( "overload", "hello" );
    }

    @Jpf.Action( forwards={ @Jpf.Forward( name="overload", path="overload.do" ) } )
    public Forward chainToHashMap( String form )
    {
        return new Forward( "overload", new HashMap() );
    }

    @Jpf.Action( forwards={ @Jpf.Forward( name="index", path="index.jsp" ) } )
    public Forward overload( String form )
    {
        _message = "in String overload: " + form;
        return new Forward( "index" );
    }

    @Jpf.Action( forwards={ @Jpf.Forward( name="index", path="index.jsp" ) } )
    public Forward overload( HashMap form )
    {
        _message = "in HashMap overload";
        return new Forward( "index" );
    }

    public String getMessage()
    {
        return _message;
    }
}
