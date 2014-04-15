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

<netui-data:declarePageInput name="totalPrice" type="java.lang.Double" required="true"/>

<netui-template:template templatePage="/resources/template/template.jsp">
  <netui-template:setAttribute name="sampleTitle" value="Editable Repeater"/>
  <netui-template:section name="main">
      <netui:form action="update">
          <table border="0" cellspacing="5" cellpadding="5" width="50%">
              <tr>
                  <td colspan="4">
                      <table border="0" width="100%">
                          <tr>
                              <td align="right">
                                  Subtotal: <netui:label value="${pageInput.totalPrice}">
                                                <netui:formatNumber pattern="$###,###.00"/>
                                            </netui:label>
                                  <br/>
                                  <netui:anchor formSubmit="true">Update</netui:anchor>
                              </td>
                          </tr>
                          <tr bgcolor="#eeeeff">
                              <td>Items in Cart:</td>
                          </tr>
                      </table>
                  </td>
              </tr>
              <tr>
                  <td width="30%">Product</td><td>Price</td><td>Quantity</td><td>Gift Wrap <font size="1">(adds $4.96 / item)</font></td>
              </tr>
              <netui-data:repeater dataSource="actionForm.items">
              <tr cellpadding="5" ${container.index % 2 == 1 ? "style=\"background-color:#eeeeff\"" : ""}>
                  <td>${container.item.name}</td>
                  <td>
                      <netui:span value="${container.item.price}">
                          <netui:formatNumber pattern="$#####.00"/>
                      </netui:span>
                  </td>
                  <td>
                      <netui:textBox dataSource="container.item.quantity" size="5"/><br/>
                      <font size="2" color="#ee0000"><netui:error key="invalidQuantity${container.index}"/></font>
                  </td>
                  <td>
                      <netui:checkBox dataSource="container.item.giftWrap"/>
                  </td>
              </tr>
          </netui-data:repeater>
          <tr>
              <td colspan="4" align="right">
                  <netui:anchor formSubmit="true">Update</netui:anchor>
              </td>
          </tr>
      </table>
  </netui:form>
  </netui-template:section>
</netui-template:template>