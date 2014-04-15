<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<html>
   <body>
      <center>
         <font color="tomato">
            PageInput value from [%@ include]:
            <netui:span value="${pageInput.string}" />
         </font>
         <br/><br/>
      </center>
   </body>
</html>
