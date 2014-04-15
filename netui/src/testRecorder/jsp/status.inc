
<%@ page import="org.apache.beehive.netui.tools.testrecorder.server.state.Session,
                 org.apache.beehive.netui.tools.testrecorder.shared.util.DateHelper" %>
<%@ page import="org.apache.beehive.netui.tools.testrecorder.server.TestRecorderFilter" %>
<%@ page import="org.apache.beehive.netui.tools.testrecorder.server.state.RecordSession" %>
<%@ page import="org.apache.beehive.netui.tools.testrecorder.server.state.RecordSession" %>
<%@ page import="java.text.MessageFormat" %>
<%@ page import="java.util.*" %>

<%!
    private static final String property =
        "<strong style=\"color: blue\">{0}</strong>";
    private static final String value =
        "<b style=\"color: {1}\">{2}</b><br>";
    private static final String RED = "red";
    private static final String GREEN = "green";
    private static final String ON = "On";
    private static final String OFF = "Off";

    private static final MessageFormat status = new MessageFormat( property + value );

    private Object[] testModeObjects = { "Test Mode: ", null, null };
    private Object[] recordingObjects = { "Recording: ", null, null };
    private Object[] playbackObjects = { "Playback: ", null, null };

    private static String getPlaybackSessionString( Map playbackMap ) {
        StringBuffer sb = new StringBuffer( 64 * playbackMap.size() );
        Set entrySet = playbackMap.entrySet();
        List list = new ArrayList( entrySet.size() );
        list.addAll( entrySet );

        // TODO sort by session name and start time
        for ( int i = 0; i < list.size(); i++ ) {
            sb.append( "Playback Session [" + i + "]( " + list.get(i) + " )" + "<br><br>" );
        }
        return sb.toString();
    }
%>                                            

<%
    TestRecorderFilter filter = TestRecorderFilter.instance();
    String dateString = DateHelper.formatToString(new Date());
    boolean testMode = false;
    RecordSession recordingSession = null;
    String recSessionInfo = null;
    String playSessionInfo = null;
    Map playbackMap = null;
    
    if ( filter != null ) {
       testMode = filter.getState().isTestMode();
       recordingSession = filter.getState().getRecordingSession();
       playbackMap = filter.getState().getPlaybackMap();

       if ( testMode ) {
           testModeObjects[1] = GREEN;
           testModeObjects[2] = ON;
       }
       else {
           testModeObjects[1] = RED;
           testModeObjects[2] = OFF;
       }
   
       if ( recordingSession == null ) {
           recordingObjects[1] = RED;
           recordingObjects[2] = OFF;
           recSessionInfo = null;
       }
       else {
           recordingObjects[1] = GREEN;
           recordingObjects[2] = ON;
           recSessionInfo = recordingSession.toString();
       }
   
       if ( playbackMap == null ) {
           playbackObjects[1] = RED;
           playbackObjects[2] = OFF;
       }
       else {
           playbackObjects[1] = GREEN;
           playbackObjects[2] = ON;
           playSessionInfo = getPlaybackSessionString( playbackMap );
       }
    }
%>

<h3><strong style="color: orange">Current Status ( </strong>
<%= dateString %><strong style="color: orange"> )</strong></h3>

<%
   if ( filter != null ) {
      // all is well
%>

&nbsp;&nbsp;&nbsp;&nbsp;<%=status.format( testModeObjects ) %>
&nbsp;&nbsp;&nbsp;&nbsp;<%=status.format( recordingObjects ) %>
<br>
<%= ( recSessionInfo == null ) ? "" : "&nbsp;&nbsp;&nbsp;&nbsp;" + recSessionInfo + "<br><br>" %>
&nbsp;&nbsp;&nbsp;&nbsp;<%=status.format( playbackObjects ) %>
<br>
<%= ( playSessionInfo == null ) ? "" : "&nbsp;&nbsp;&nbsp;&nbsp;" + playSessionInfo + "<br>" %>

<%
   }
   else {
%>

<strong style="color: red">TestRecorderFilter is not properly deployed!!</strong>

<%
   }
%>   
