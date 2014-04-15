<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        <netui:anchor action="secureAction">secureAction</netui:anchor>
        <br>
        <netui:anchor href="/tomcatWeb/tags/secureLinks/secure/begin.do">begin in /secure</netui:anchor>
        <br>
        <netui:anchor href="../secure/begin.do">begin in /secure (relative)</netui:anchor>
        <br>
        image under /secure: <netui:image src="/tomcatWeb/tags/secureLinks/secure/image.gif"/>
        <br>
        image under /secure (relative): <netui:image src="../secure/image.gif"/>
        <br>
        imageAnchor under /secure: <netui:imageAnchor action="secureAction"
                                                      src="/tomcatWeb/tags/secureLinks/secure/image.gif"
                                                      rolloverImage="/tomcatWeb/tags/secureLinks/secure/image.gif" />
        <br>
        imageAnchor under /secure (relative): <netui:imageAnchor action="secureAction"
                                                                 src="../secure/image.gif"
                                                                 rolloverImage="../secure/image.gif"/>
        <br>
        imageButton under /secure: <netui:imageButton src="/tomcatWeb/tags/secureLinks/secure/image.gif"
                                                      rolloverImage="/tomcatWeb/tags/secureLinks/secure/image.gif" />
        <br>
        imageButton under /secure (relative): <netui:imageButton src="../secure/image.gif"
                                                                 rolloverImage="../secure/image.gif"/>
        
    </body>
</netui:html>
