<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
   <head>
      <title>MiscJpf Bug 26856 test</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug 26856 test - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <netui-data:declareBundle name="theBundle1" bundlePath="miscJpf.bug26856.bundle1"/>
      <netui-data:declareBundle name="theBundle2" bundlePath="miscJpf/bug26856/bundle2"/>

      <h3>Test 1</h3>
      <em>Print a message from bundle1.</em>
      <br/>
      <netui:span value="${bundle.theBundle1.message1}"/>

      <hr width="95%"/><br/>
      <h3>Test 2</h3>
      <em>Print a message from bundle2.</em>
      <br/>
      <netui:span value="${bundle.theBundle2.message1}"/>

      <hr width="95%"/><br/>
      <h3>Test 3</h3>
      <em>Print a message from and nonexistant bundle An error is expected.</em>
      <br/>
      <% try { %>
      <netui:span value="${bundle.xtheBundle1.message1}"/>
      <% } catch ( Exception e ) { pageContext.getOut().print( e.getMessage() ); } %>

      <hr width="95%"/><br/>
      <h3>Test 4</h3>
      <em>Print a nonexistant message from bundle An error is expected.</em>
      <br/>
      <% try { %>
      <netui:span value="${bundle.theBundle1.message1x}"/>
      <% } catch ( Exception e ) { pageContext.getOut().print( e.getMessage() ); } %>

      <hr width="95%"/><br/>
      <h3>Test 5</h3>
      <em>Declare a bundle with the reserved name "default" An error is expected.</em>
      <br/>
      <netui-data:declareBundle name="default" bundlePath="properties.bundle1"/>

      <hr width="95%"/><br/><br/>
      <center>
         <netui:anchor action="done">Done</netui:anchor>
      </center>
   </body>
</html>
