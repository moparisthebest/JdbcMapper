<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="miniTests.pageFlowUtils.pageFlowUtilsController.TestForm"%>
<%@page import="org.apache.beehive.netui.pageflow.PageFlowController"%>
<%@page import="org.apache.beehive.netui.pageflow.PageFlowUtils"%>
<%@page import="org.apache.struts.action.ActionForm"%>
<%@page import="javax.servlet.ServletContext"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            PageFlowUtils
        </title>
    </head>
    <body>
        <h3>PageFlowUtils</h3>
        
        <% PageFlowController curJpf = PageFlowUtils.getCurrentPageFlow( request ); %>
        <% ServletContext cxt = pageContext.getServletContext(); %>
        
        
        <code>getCurrentPageFlow( request )</code>:
            <%= curJpf.getClass().getName() %><br>

        <code>getModulePath( request )</code>:
            <%= PageFlowUtils.getModulePath( request ) %><br>

        <code>getModulePath( request, "/coreWeb/foo/bar/baz.jpf" )</code>:
            <%= PageFlowUtils.getModulePath( request, "/coreWeb/foo/bar/baz.jpf" ) %><br>

        <code>getRelativeURI( request, curJPF )</code>:
            <%= PageFlowUtils.getRelativeURI( request, curJpf ) %><br>
            
        <code>getRelativeURI( request, "/coreWeb/foo/bar/baz.jsp", curJPF )</code>:
            <%= PageFlowUtils.getRelativeURI( request, "/coreWeb/foo/bar/baz.jsp", curJpf ) %><br>
            
        <code>getRelativeURI( request, "/coreWeb/miniTests/pageFlowUtils/foo.jsp", curJPF )</code>:
            <%= PageFlowUtils.getRelativeURI( request, "/coreWeb/miniTests/pageFlowUtils/foo.jsp", curJpf ) %><br>
            
        <code>getModulePathForRelativeURI( null )</code>:
            <%= PageFlowUtils.getModulePathForRelativeURI( null ) %><br>

        <code>getBeginActionURI( "/coreWeb/miniTests/pageFlowUtils/pageFlowUtilsController.jpf" )</code>:
            <%= PageFlowUtils.getBeginActionURI( "/coreWeb/miniTests/pageFlowUtils/pageFlowUtilsController.jpf" ) %><br>

        <code>ensureCurrentPageFlow( request, response )</code>:
            <%= PageFlowUtils.ensureCurrentPageFlow( request, response ).getClass().getName() %><br>

        <code>ensureCurrentPageFlow( request, response, servletContext )</code>:
            <%= PageFlowUtils.ensureCurrentPageFlow( request, response, cxt ).getClass().getName() %><br>

        <code>getCurrentActionResolver( request )</code>:
            <%= PageFlowUtils.getCurrentActionResolver( request ).getClass().getName() %><br>

        <code>ensureGlobalApp( request, response )</code>:
            <%= PageFlowUtils.ensureGlobalApp( request, response ).getClass().getName() %><br>

        <code>ensureGlobalApp( request, response, cxt )</code>:
            <%= PageFlowUtils.ensureGlobalApp( request, response, cxt ).getClass().getName() %><br>

        <code>getGlobalApp( request )</code>:
            <%= PageFlowUtils.getGlobalApp( request ).getClass().getName() %><br>

        <code>getSharedFlows( request ).get( "tempSharedFlow" )</code>:
            <%= PageFlowUtils.getSharedFlows( request ).get( "tempSharedFlow" ).getClass().getName() %><br>

        <code>getSharedFlow( "webappRoot.SharedFlow", request )</code>:
            <%= PageFlowUtils.getSharedFlow( "miniTests.pageFlowUtils.TempSharedFlow", request ).getClass().getName() %><br>

        <code>removeSharedFlow( "webappRoot.SharedFlow", request )</code>:
            <% PageFlowUtils.removeSharedFlow( "miniTests.pageFlowUtils.TempSharedFlow", request ); %><br>

        <code>getSharedFlow( "webappRoot.SharedFlow", request )</code>:
            <%= PageFlowUtils.getSharedFlow( "miniTests.pageFlowUtils.TempSharedFlow", request ) %><br>

        <code>getFormBeanName( new TestForm(), request )</code>:
            <%= PageFlowUtils.getFormBeanName( new TestForm(), request ) %><br>

        <code>getFormBeanName( TestForm.class, request )</code>:
            <%= PageFlowUtils.getFormBeanName( TestForm.class, request ) %><br>

        <code>getFormBeanName( ActionForm.class, request )</code>:
            <%= PageFlowUtils.getFormBeanName( ActionForm.class, request ) %><br>

        <code>getJpfClassName( "/foo/bar/barController.jpf" )</code>:
            <%= PageFlowUtils.getJpfClassName( "/foo/bar/barController.jpf" ) %><br>

        <code>getPageFlowClassName( "/foo/bar/barController.jpf" )</code>:
            <%= PageFlowUtils.getPageFlowClassName( "/foo/bar/barController.jpf" ) %><br>

        <code>getPageFlowURI( "foo.bar.barController" )</code>:
            <%= PageFlowUtils.getPageFlowURI( "foo.bar.barController" ) %><br>

        <code>getModuleConfPath( "/foo/bar" )</code>:
            <%= PageFlowUtils.getModuleConfPath( "/foo/bar" ) %><br>

        <code>getModuleConfPath( "/" )</code>:
            <%= PageFlowUtils.getModuleConfPath( "/" ) %><br>

        <code>getModuleConfPath( "" )</code>:
            <%= PageFlowUtils.getModuleConfPath( "" ) %><br>

        <code>getActionURI( request )</code>:
            <%= PageFlowUtils.getActionURI( (javax.servlet.http.HttpServletRequest) request ) %><br>

        <code>getGlobalAppConfig( servletContext )</code>:
            <%= PageFlowUtils.getGlobalAppConfig( cxt ).getPrefix() %><br>

        <code>ensureModuleConfig( "/miniTests/pageFlowUtils/another", request, servletContext ).getPrefix()</code>:
            <%= PageFlowUtils.ensureModuleConfig( "/miniTests/pageFlowUtils/another",
                    (javax.servlet.http.HttpServletRequest) request, cxt ).getPrefix() %><br>

        <code>getModuleConfig( "/miniTests/pageFlowUtils/another", servletContext ).getPrefix()</code>:
            <%= PageFlowUtils.getModuleConfig( "/miniTests/pageFlowUtils/another", cxt ).getPrefix() %><br>

        <code>isAbsoluteURI( "foo" )</code>:
            <%= PageFlowUtils.isAbsoluteURI( "foo" ) %><br>

        <code>isAbsoluteURI( "/foo" )</code>:
            <%= PageFlowUtils.isAbsoluteURI( "/foo" ) %><br>

        <code>isAbsoluteURI( "http://www.foo.com" )</code>:
            <%= PageFlowUtils.isAbsoluteURI( "http://www.foo.com" ) %><br>

        <code>getFileExtension( "foo.txt" )</code>:
            <%= PageFlowUtils.getFileExtension( "foo.txt" ) %><br>

        <code>getFileExtension( "foo" )</code>:
            <%= PageFlowUtils.getFileExtension( "foo" ) %><br>

        <code>addPageInput( "foo", "Foo!", request );</code>
            <% PageFlowUtils.addPageInput( "foo", "Foo!", request ); %><br>

        <code>getPageInput( "foo", request )</code>:
            <%= PageFlowUtils.getPageInput( "foo", request ) %><br>

        <code>addActionOutput( "bar", "Bar!", request );</code>:
            <% PageFlowUtils.addActionOutput( "bar", "Bar!", request ); %><br>

        <code>getActionOutput( "bar", request )</code>:
            <%= PageFlowUtils.getActionOutput( "bar", request ) %><br>

        <code>getActionURI( cxt, request, response, "begin" )</code>:
            <%= PageFlowUtils.getActionURI( cxt, request, response, "begin" ).getURIString( null ) %><br>

        <code>getActionURI( cxt, request, response, "/relative/to/webapp/root" )</code>:
            <%= PageFlowUtils.getActionURI( cxt, request, response, "/relative/to/webapp/root" ).getURIString( null ) %><br>

        <% java.util.HashMap params = new java.util.HashMap(); params.put( "foo", "bar" ); %>
        <code>PageFlowUtils.getRewrittenActionURI( cxt, request, response, "begin", params, "frag", true )</code>:
            <%= PageFlowUtils.getRewrittenActionURI( cxt, request, response, "begin", params, "frag", true ) %><br>

        <code>PageFlowUtils.getRewrittenResourceURI( cxt, request, response, "index.jsp", params, "frag", true )</code>:
            <%= PageFlowUtils.getRewrittenResourceURI( cxt, request, response, "index.jsp", params, "frag", true ) %><br>

        <br>
        <br>

        <netui:anchor action="goNested">go to nested/nestedController.jpf</netui:anchor>
        <br>
        <netui:anchor action="validation">tests of addValidationError()</netui:anchor>
    </body>
</netui:html>
