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
package pageFlowCore.forwards;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.handler.BaseHandler;
import org.apache.beehive.netui.pageflow.handler.FlowControllerHandlerContext;
import org.apache.beehive.netui.pageflow.handler.ForwardRedirectHandler;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;


@Jpf.Controller
public class Controller extends PageFlowController
{
    public static class FakeRedirector
        extends BaseHandler
        implements ForwardRedirectHandler
    {
        public void forward( FlowControllerHandlerContext context, String uri )
            throws IOException, ServletException
        {
            ( ( ForwardRedirectHandler ) getPreviousHandler() ).forward( context, uri );
        }

        public void redirect( FlowControllerHandlerContext context, String uri )
            throws IOException, ServletException
        {
            PrintWriter writer = context.getResponse().getWriter();

            writer.println( "<html><head><title>Fake Redirect</title></head><body>" );
            writer.println( "Fake redirect to: <b>" + uri + "</b><br><br>" );
            writer.println( "<a href=\"begin.do\">go back</a>" );
            writer.println( "</body></html>" );
        }
    }    


    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        return new Forward( "index" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "indexRedirect",
                path = "index.jsp",
                redirect = true) 
        })
    protected Forward redirect()
    {
        return new Forward( "indexRedirect" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "indexRedirect",
                path = "index.jsp",
                redirect = true) 
        })
    protected Forward withQueryParams()
    {
        Forward fw = new Forward( "indexRedirect" );
        fw.addQueryParam( "foo" );
        fw.addQueryParam( "bar", "baz" );
        return fw;
    }


    @Jpf.Action(
        )
    protected Forward randomURI()
        throws Exception
    {
        return new Forward( new URI( "http://www.google.com/search?q=xmlbeans" ) );
    }

    @Jpf.Action(
        )
    protected Forward uriRedirect()
        throws Exception
    {
        return new Forward( new URI( "/netui/index.jsp" ), true );
    }

    @Jpf.Action(
        )
    protected Forward uriWebappForward()
        throws Exception
    {
        return new Forward( new URI( "/pageFlowCore/forwards/Controller.jpf" ) );
    }

    @Jpf.Action(
        )
    protected Forward uriRelativeForward()
        throws Exception
    {
        return new Forward( new URI( "Controller.jpf" ) );
    }

    @Jpf.Action(
        )
    protected Forward uriRelativeRedirect()
        throws Exception
    {
        return new Forward( new URI( "Controller.jpf" ), true );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "unused",
                path = "") 
        })
    protected Forward setPath()
        throws Exception
    {
        //
        // Note that setPath() works differently than the URI/URL constructors.
        // It acts just like setPath() on the base class -- sets a path that
        // eventually gets the current context path prepended, and respects the
        // redirect and context-sensitive flags.
        //
        Forward fwd = new Forward( "unused" );
        fwd.setPath( "/pageFlowCore/forwards/index.jsp" );
        fwd.setContextRelative( true );
        return fwd;
    }
}
