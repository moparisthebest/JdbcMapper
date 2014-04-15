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
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%-- This menu bar refers to actions in tiles.shared.SharedFlow. --%>
    <div id="menubar">
        <table style="background-color:#00DDEE;width:100%;">
            <tr>
                <td/>
                <td style="font-size:large; text-align:center">Menu Bar</td>
                <td/>
            </tr>
            <tr>
                <td style="text-align:center">
                    <netui:anchor action="tilesSharedFlow.goMain">Go to Main Flow</netui:anchor></td>
                </td>
                <td style="text-align:center">
                    <netui:anchor action="tilesSharedFlow.goFlow1">Go to Flow 1</netui:anchor>
                </td>
                <td style="text-align:center">
                    <netui:anchor action="tilesSharedFlow.goFlow2">Go to Flow 2</netui:anchor>
                </td>
            </tr>
        </table>
        <br/>

    </div>
