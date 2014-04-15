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
<%@ tag import="java.util.Date"%>
<%@ tag import="org.apache.beehive.samples.netui.beans.PetType"%>
<%@ variable name-given="pets" variable-class="org.apache.beehive.samples.netui.beans.PetType[]" scope="AT_END" %>

<%
  PetType[] petBeans = new PetType[] {
      new PetType(10, "Golden Retriever", "Dog", 99.99, new Date()),
      new PetType(11, "Cocker spaniel", "Cocker spaniel", 89.99, new Date()),
      new PetType(12, "Labrador Retriever", "Black Labrador dog", 79.99, new Date()),
      new PetType(13, "Koi", "Fish", 199.99, new Date()),
      new PetType(14, "Barracuda", "Fish", 199.99, new Date())
  };
  getJspContext().setAttribute("pets", petBeans);
%>
