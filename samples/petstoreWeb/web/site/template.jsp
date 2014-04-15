<%--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE-2.0
   
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  
   $Header:$
--%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="beehive-petstore" tagdir="/WEB-INF/tags/beehive/petstore" %>

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui:html>
    <head>
        <title>
	        <%-- Set the title based on the title attribute --%>
            ${bundle.view.indexTitle} - <netui-template:attribute name="title"/>
        </title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/site/default.css" type="text/css"/>
    </head>
    <netui:body>
        <%-- Always show the header --%>
        <beehive-petstore:headerMenu contextPath="${pageContext.request.contextPath}" />

        <%-- The top menu may be the category menu (default), the account menu, or neither --%>
       	<netui-template:includeSection name="topMenu" defaultPage="/site/categoryMenu.jsp"  />

        <table cellpadding="5" cellspacing="0" width="100%">
	        <tr><td valign="top" width="250" id="leftnav">
				    <%-- Include the template's "leftnav" section --%>
	            	<netui-template:includeSection name="leftnav" defaultPage="/site/emptyLeftNav.jsp"/>
		        </td>
	        	<td valign="top"><br/>
			        <%-- Include the template's "body" section --%>
            		<netui-template:includeSection name="body"/>
            	</td>
        	</tr>
        </table>
        <div id="footer">
            <%-- dynamically display the banner --%>
            <beehive-petstore:footer contextPath="${pageContext.request.contextPath}"/>
            <beehive-petstore:copyright contextPath="${pageContext.request.contextPath}"/>
        </div>
    </netui:body>
</netui:html>
