<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>


<html>
<head>
<title>Index Page for <netui:content value="${pageFlow.URI}"/></title>
</head>
<body bgcolor="white">

<h3>Index Page for <netui:content value="${pageFlow.URI}"/></h3>

<br><netui:anchor action="begin">forward to this page</netui:anchor>
<pre>
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     */
    protected Forward begin()
    {
        return new Forward( "index" );
    }
</pre>


<br><netui:anchor action="redirect">redirect to this page</netui:anchor>
<pre>
    /**
     * @jpf:action
     * @jpf:forward name="indexRedirect" path="index.jsp" redirect="true"
     */
    protected Forward redirect()
    {
        return new Forward( "indexRedirect" );
    }
</pre>


<br><netui:anchor action="withQueryParams">redirect to this page, with query params</netui:anchor>
<pre>
    /**
     * @jpf:action
     * @jpf:forward name="indexRedirect" path="index.jsp" redirect="true"
     */
    protected Forward withQueryParams()
    {
        Forward fw = new Forward( "indexRedirect" );
        fw.addQueryParam( "foo" );
        fw.addQueryParam( "bar", "baz" );
        return fw;
    }
</pre>

<br><netui:anchor action="randomURI">redirect to google</netui:anchor>
<pre>
    /**
     * @jpf:action
     */
    protected Forward randomURI()
        throws Exception
    {
        return new Forward( new URI( "http://www.google.com/search?q=xmlbeans" ) );
    }
</pre>


<br><netui:anchor action="uriRedirect">redirect to the netui webapp</netui:anchor>
<pre>
    /**
     * @jpf:action
     */
    protected Forward uriRedirect()
        throws Exception
    {
        return new Forward( new URI( "/netui/index.jsp", true ) );
    }
</pre>

<br><netui:anchor action="uriWebappForward">forward to this page, using a webapp-relative URI</netui:anchor>
<pre>
    /**
     * @jpf:action
     */
    protected Forward uriWebappForward()
        throws Exception
    {
        return new Forward( new URI( "/forwardTest/Controller.jpf" ) );
    }
</pre>

<br><netui:anchor action="uriRelativeForward">forward to this page, using a relative URI</netui:anchor>
<pre>
    /**
     * @jpf:action
     */
    protected Forward uriRelativeForward()
        throws Exception
    {
        return new Forward( new URI( "Controller.jpf" ) );
    }
</pre>

<br><netui:anchor action="uriRelativeRedirect">redirect to this page, using a relative URI</netui:anchor>
<pre>
    /**
     * @jpf:action
     */
    protected Forward uriRelativeRedirect()
        throws Exception
    {
        return new Forward( new URI( "Controller.jpf" ), true );
    }
</pre>

<br><netui:anchor action="setPath">forward to this page using Forward.setPath()</netui:anchor>
<pre>
    /**
     * @jpf:action
     * @jpf:forward name="unused" path=""
     */
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
        fwd.setPath( "/forwardTest/index.jsp" );
        fwd.setContextRelative( true );
        return fwd;
    }
</pre>


</body>
</html>
