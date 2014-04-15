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

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.netui.resources.site" name="site"/>

<netui:html>
    <head>
        <title>
            ${bundle.site.browserTitle} -- <netui-template:attribute name="sampleTitle" defaultValue="Sample"/>
        </title>
    </head>
    <netui:body>
        <div>
            <div id="header">
                <table style="background-color:#FFE87C;width:100%;">
                  <tr>
                    <td style="font-size:x-large;">NetUI Feature Samples: <netui-template:attribute name="sampleTitle"/></td>
                    <td style="text-align:right;"><a href="${pageContext.request.contextPath}" target="_top">Samples Home</a></td>
                  </tr>
                </table><br/>
            </div>
            <div id="main">
                <netui-template:includeSection name="main"/>
            </div>
            <div id="footer">
                <table style="background-color:#FFE87C;width:100%;">
                  <tr>
                    <td>&nbsp;</td>
                    <td style="text-align:right;"><a href="${pageContext.request.contextPath}" target="_top">Samples Home</a></td>
                  </tr>
                </table>
            </div>
        </div>
    </netui:body>
</netui:html>
