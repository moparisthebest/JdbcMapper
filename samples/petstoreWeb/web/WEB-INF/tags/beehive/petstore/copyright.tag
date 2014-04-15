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
<%@ tag body-content="empty" %>
<%@ attribute name="contextPath" required="true" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%">
    <tr>
        <td colspan="2"><hr noshade="noshade" size="1"/></td>
    </tr>
    <tr>
        <td align="left">
            Licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache License, Version 2.0</a>.
        </td>
        <td align="right">
        <table>
            <tr>
                <td valign="center">
                <netui:anchor href="http://beehive.apache.org">
                    <netui:image src="${contextPath}/images/beehive_logo.gif" style="height:20px;width:20px;border:none;"/>
                </netui:anchor>
                </td>
                <td valign="center">
                    <netui:span value="Powered by"/>
                    <netui:anchor style="font-color:#00cccc;" href="http://beehive.apache.org/" value="Apache Beehive"/>
                </td>
            </tr>
        </table>
        </tr>
</table>
