<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ page import="java.sql.*,javax.naming.*,javax.sql.*" %>
<html>
  <head>
    <title>SQL on Tomcat Test</title>
  </head>
  <body>
    <p>
    <b>SQL on Tomcat Test</b>

<%
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver");    

    Context ctx = new InitialContext();
    if(ctx == null) 
        throw new RuntimeException("Could not create an InitialContext");

    DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/TestDB");
    if(ds == null) 
        throw new RuntimeException("Could not create a DataSource");
    out.write("<br/>found ds: " + ds + "<br/>");

    Connection conn = ds.getConnection();
    if(conn == null) 
        throw new RuntimeException("Could not get database connection");
    out.write("got here</br>");

    java.sql.Statement stmt = conn.createStatement();
    try {
      stmt.execute("drop table CATEGORIES");
      stmt.close();
    } catch(Exception ignore) {ignore.printStackTrace(); stmt.close();}
    stmt = conn.createStatement();
    boolean result = stmt.execute(
     "CREATE TABLE Categories (CategoryID int GENERATED ALWAYS AS IDENTITY, CategoryName varchar (15) NOT NULL, Description CLOB (2K), CONSTRAINT PK_Categories PRIMARY KEY(CategoryID))");
    out.write("result: " + result);
    stmt.close();

    stmt = conn.createStatement();
    stmt.execute("insert into categories (CategoryName,Description) VALUES('Beverages','Soft drinks, coffees, teas, beers, and ales')");
    stmt.close();

    stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("select * from categories");
    pageContext.setAttribute("rs", rs);
    while(rs.next()) {
        out.write("name: " + rs.getObject("categoryname") + "<br/>");
        out.write("name: " + rs.getObject("description") + "<br/>");
    }
    rs.close();
    stmt.close();
    conn.close();
%>
    </p>
  </body>
</html>
