<%@ page import="org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinitions,
                 org.apache.beehive.netui.tools.testrecorder.server.TestRecorderFilter,
                 org.apache.beehive.netui.tools.testrecorder.shared.config.WebappConfig,
                 org.apache.beehive.netui.tools.testrecorder.shared.config.Category,
                 org.apache.beehive.netui.tools.testrecorder.shared.config.Categories,
                 org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinition,
                 org.apache.beehive.netui.tools.testrecorder.shared.Constants,
                 org.apache.beehive.netui.tools.testrecorder.shared.util.DateHelper,
                 java.io.File,
                 java.util.Calendar,
                 java.util.Date,
                 java.util.List"%>
<%
    Calendar calendar = Calendar.getInstance();
    Date date = null;
    TestDefinitions tests = TestRecorderFilter.instance().getTestDefinitions();
    WebappConfig webapp = TestRecorderFilter.instance().getWebapp();
    String categoryString = request.getParameter( "category");
    Category category = tests.getCategories().getCategory( categoryString );
    List testList = tests.getCategories().getTests( category );
    TestDefinition test = null;
    String detailHref = null;
    String recordHref = null;
    String playbackHref = null;
    String diffHref = null;
    File file = null;
%>


<html>
<title>Category Test Information</title>

<%
    if ( category == null ) {
%>
        <b>Unable to find category( <%=categoryString%> )</b>
        </html>
<%
        return;
    }
%>

<table border="2" cols="2">
<tr>
    <td><b>Test</b></td>
    <td><b>Details</b></td></td>
    <td><b>Diff</b></td>
    <td><b>Record File</b></td></td>
    <td><b>Playback File</b></td>
</tr>

<%
    for ( int i = 0; i < testList.size(); i++ ) {
        test = (TestDefinition) testList.get( i );
        file = new File( test.getTestFilePath() );
        if ( file.exists() ) {
            calendar.setTimeInMillis(  file.lastModified());
            date = calendar.getTime();
            recordHref = new String( "<a href=" + webapp.getServletURI() + "?" + Constants.MODE + "=" + Constants.ADMIN +
                "&" + Constants.CMD + "=" + Constants.DISPLAY_RECORD + "&test=" + test.getName() + ">" + test.getName() +
                " Record File</a>" );
            detailHref = new String( "<a href=" + webapp.getServletURI() + "?" + Constants.MODE + "=" + Constants.DPY_DETAILS + "&" +
                Constants.FILE + "=" + test.getName() + ">" + test.getName() + " Details</a>" );
        }
        else {
            recordHref = "&nbsp;";
            detailHref = "file not found( " + file.getAbsolutePath() + " )";
        }
        file = new File( test.getResultFilePath() );
        if ( file.exists() ) {
            calendar.setTimeInMillis(  file.lastModified());
            date = calendar.getTime();
            playbackHref = new String( "<a href=" + webapp.getServletURI() + "?" + Constants.MODE + "=" + Constants.ADMIN +
                "&" + Constants.CMD + "=" + Constants.DISPLAY_PLAYBACK + "&test=" + test.getName() + ">" + test.getName() +
                " Playback File (" + DateHelper.formatToReadableString(date) + ")</a>");
        }
        else {
            playbackHref =  "&nbsp;";

        }
        file = new File( test.getResultDiffFilePath() );
        if ( file.exists() ) {
            diffHref = new String( "<a href=" + webapp.getServletURI() + "?" + Constants.MODE + "=" + Constants.DPY_DIFF + "&" +
                Constants.FILE + "=" + test.getName() + ">" + test.getName() + "</a>");
        }
        else {
            diffHref =  "&nbsp;";
        }
%>
        <tr>
            <td><%=test.getName()%></td>

            <td><%=detailHref%></td>

            <td><%=diffHref%></td>

            <td><%=recordHref%></td>

            <td><%=playbackHref%></td>
        </tr>
<%
    }
%>

</table>
</html>
