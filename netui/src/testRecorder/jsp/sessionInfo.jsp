<%@ page import="org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinitions,
                 org.apache.beehive.netui.tools.testrecorder.server.TestRecorderFilter,
                 org.apache.beehive.netui.tools.testrecorder.shared.config.WebappConfig,
                 org.apache.beehive.netui.tools.testrecorder.shared.config.Category,
                 org.apache.beehive.netui.tools.testrecorder.shared.config.Categories,
                 org.apache.beehive.netui.tools.testrecorder.shared.Constants,
                 org.apache.beehive.netui.tools.testrecorder.server.state.Session,
                 java.io.File,
                 java.text.SimpleDateFormat,
                 java.util.Calendar,
                 java.util.Date,
                 java.util.List,
                 java.util.Locale"%>
<%!
    // todo: need to clean this up to handle date formats correctly in one place
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat( "dd MMM, hh:mm:ss aa", Locale.US );
%>
<%
    Calendar calendar = Calendar.getInstance();
    Date date = null;
    TestDefinitions tests = TestRecorderFilter.instance().getTestDefinitions();
    WebappConfig webapp = TestRecorderFilter.instance().getWebapp();
    Category[] categories = tests.getCategories().getCategories();
    Category category = null;
    String catHref = null;
    String resultsHref = null;
    File reportFile = null;
%>

<html>
<title>Test Session Information</title>
<table border="2" cols="2">
<tr><td><b>Tests for Category</b></td><td><b>Results</b></td></tr>

<%
    for ( int i = 0; i < categories.length; i++ ) {
        category = categories[i];
        catHref = new String( "testInfo.jsp?category=" + category.getName() );
        reportFile = new File( category.getReportDirPath(), "/html/junit-noframes.html" );
        if ( reportFile.exists() ) {
            calendar.setTimeInMillis(reportFile.lastModified());
            date = calendar.getTime();
            resultsHref = new String( "<a href=" + webapp.getServletURI() + "?" + Constants.MODE + "=" + Constants.ADMIN +
                "&" + Constants.CMD + "=" + Constants.DISPLAY_REPORT + "&category=" + category.getName() + ">results (" +
                dateFormat.format( date ) + ")</a>");
        }
        else {
            resultsHref = null;
        }
%>
<tr><td><a href="<%=catHref%>"><%=category.getName()%></a></td><td>
<%
        if ( resultsHref == null ) {
%>
            &nbsp;
<%
        }
        else {
%>
        <%=resultsHref%>
<%
        }
%>
        </td></tr>
<%
    }
%>

</table>
</html>
