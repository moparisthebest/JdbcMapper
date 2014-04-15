<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<html>
    <body>
        in production mode: <b><%= org.apache.beehive.netui.pageflow.internal.AdapterManager.getServletContainerAdapter(pageContext.getServletContext()).isInProductionMode() %></b>
        <br/>
        <br/>
        Production mode must be <code>false</code> when running our automated tests; many of them
        depend this to display dev-mode error pages and the like.
    </body>
</html>
