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
            ${bundle.site.browserTitle}
        </title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" type="text/css"/>
    </head>
    <netui:body>
        <div>
            <div id="header">
                <table style="background-color:#FFE87C;width:100%;">
                  <tr>
                    <td style="font-size:x-large;">Page Flow Feature Samples: <netui-template:attribute name="sampleTitle"/></td>
                    <td style="text-align:right;"><a href="${pageContext.request.contextPath}" target="_top">Samples Home</a></td>
                  </tr>
                </table><br/>
            </div>
            
            <%-- This menu bar refers to actions in templateactions.shared.SharedFlow. --%>
            <div id="menubar">
                <table style="background-color:#00DDEE;width:100%;">
                    <tr>
                        <td/>
                        <td style="font-size:large; text-align:center">Menu Bar</td>
                        <td style="font-size:large; text-align:right;">
                            <span style="font-size:small">
                                <netui:anchor action="templateSharedFlow.toggleNotes">
                                    <c:choose>
                                        <c:when test="${sharedFlow.templateSharedFlow.notesVisible}">hide notes</c:when>
                                        <c:otherwise>show notes</c:otherwise>
                                    </c:choose>
                                </netui:anchor>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align:center">
                            <netui:anchor action="templateSharedFlow.goMain">Go to Main Flow</netui:anchor></td>
                        </td>
                        <td style="text-align:center">
                            <netui:anchor action="templateSharedFlow.goFlow1">Go to Flow 1</netui:anchor>
                        </td>
                        <td style="text-align:center">
                            <netui:anchor action="templateSharedFlow.goFlow2">Go to Flow 2</netui:anchor>
                        </td>
                    </tr>
                    <c:if test="${sharedFlow.templateSharedFlow.notesVisible}">
                        <tr>
                            <td style="padding:30 0 0 0;" colspan="3">
                                <table style="border-style:solid; border-width:thin;width:100%">
                                    <tr>
                                        <td style="text-align:center; font-style:italic;">Notes</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            Each of the links in this menu bar refers to an action in the shared flow
                                            <code>${sharedFlow.templateSharedFlow.class.name}</code>; for example, the "Go to Flow 1"
                                            link looks like this:
                                            <blockquote>
                                                <code>&lt;netui:anchor action="templateSharedFlow.goFlow1"&gt;Go to Flow 1&lt;/netui:anchor&gt;
                                            </blockquote>

                                            <i>Each page flow</i> declares that it wants to use the shared flow (and that it should be
                                            called "templateSharedFlow"), like this:
                                            <blockquote>
                                                <code>
                                                    @Jpf.SharedFlowRef(name="templateSharedFlow",
                                                                       type=${sharedFlow.templateSharedFlow.class.name})
                                                </code>
                                            </blockquote>

                                            So, the pages refer to /templateactions/shared/template.jsp for the UI, and the page flows
                                            refer to ${sharedFlow.templateSharedFlow.class.name} for the actions.
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </c:if>
                </table>
                <br/>

            </div>

            <div id="main">
                <blockquote>
                    <netui-template:includeSection name="main"/>
                </blockquote>
            </div>
            <div id="footer">
                <table style="background-color:#FFE87C;width:100%;">
                  <tr>
                    <td>&nbsp;</td>
                    <td style="text-align:right;"><a href="${pageContext.request.contextPath}">Samples Home</a></td>
                  </tr>
                </table>
            </div>
        </div>
    </netui:body>
</netui:html>
