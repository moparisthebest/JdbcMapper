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
package tags.encoding;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions = {
        @Jpf.SimpleAction( name="begin", path="index.jsp" ),
        @Jpf.SimpleAction( name="beginUTF8", path="beginUTF8.jsp" ),
        @Jpf.SimpleAction( name="beginEUCJP", path="beginEUCJP.jsp" ),
        @Jpf.SimpleAction( name="escaping", path="escaping.jsp" )
    }
)
public class Controller extends PageFlowController
{
    private final String _foo = "\u63d0\u51fa\u6e08\u307f";
    private String _barUTF8 = "";
    private String _barEUCJP = "";

    public final String getFoo() {
        return _foo;
    }

    public final String getBarUTF8() {
        return _barUTF8;
    }

    public final String getBarEUCJP() {
        return _barEUCJP;
    }

    @Jpf.Action(
        forwards = { 
            @Jpf.Forward( name = "success", path = "utf8.jsp" )
        }
    )
    public Forward navigateUTF8()
    {
        _barUTF8 = getRoundTripData("UTF8");
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = { 
            @Jpf.Forward( name = "success", path = "eucjp.jsp" )
        }
    )
    public Forward navigateEUCJP()
    {
        _barEUCJP = getRoundTripData("EUC_JP");
        return new Forward("success");
    }

    private String getRoundTripData(String encoding)
    {
        StringBuffer result = new StringBuffer();
        String name = "foo";
        String value = getRequest().getParameter(name);
        if (value == null) return result.toString();
        try {
            //
            // encode the bytes correctly for a Java unicode String
            // ...depends on the environment.
            // For example, this assumes that the URL encoding of the
            // parameters is "ISO-8859-1" as in a default configuration
            // of Tomcat. I.E. the attribute, URIEncoding="UTF-8", has
            // not been set on the <Connector> element in the server.xml
            //
            byte[] bytes = value.getBytes("ISO-8859-1");
            result.append("\nRequest parameter " + name + " encoded using "
                          + encoding + ": " + new String(bytes, encoding));
        } catch (Exception e) {
            result.append("\nException: " + e.getMessage());
        }

        return result.toString();
    }


}

