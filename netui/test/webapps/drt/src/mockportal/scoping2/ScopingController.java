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
package mockportal.scoping2;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.IOException;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="submit", path="results.jsp"),
        @Jpf.SimpleAction(name="launchPopUp", path="window1.jsp"),
        @Jpf.SimpleAction(name="showResults", path="window1Results.jsp")
    }
)
public class ScopingController extends PageFlowController
{
    private String _data;

    public String getData()
    {
        return _data;
    }

    public void setData( String data )
    {
        _data = data;
    }

    @Jpf.Action
    public Forward resetMockPortal()
        throws IOException
    {
        mockportal.MockPortletTag.reset( getRequest() );
        getResponse().getWriter().println( "OK -- reset MockPortal" );
        return null;
    }
}
