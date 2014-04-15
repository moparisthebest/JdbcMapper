package miniTests.servletInclude;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;

public class IncludeJPFServlet
    extends HttpServlet
{
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
    {
        request.getRequestDispatcher( "/miniTests/servletInclude/Controller.jpf" ).include( request, response );
    }
}

