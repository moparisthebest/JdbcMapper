<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
  <head>
    <title>Web Application Page</title>
    <netui:base/>
  </head>
  <netui:body>
    <p>
      New Web Application Page

    <table>
    <tr><td>
    
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><td>
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><td>
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><td>
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><td>
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><td>
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><td>
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><td>
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><td>
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><td>
        <netui:form action="postBean">
            <table>
                <tr valign="top">
                    <td>FullText:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.fullText"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextThree:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textThree"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                        <netui:select dataSource="actionForm.select" multiple="true" optionsDataSource="${pageFlow.selectOptions}"></netui:select>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolArray:</td>
                    <td>
                    <netui-data:repeater dataSource="actionForm.boolArray">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="1"> </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="container.item" />
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter> </table> </netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextFour:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textFour"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextOne:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.textOne"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolTwo:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolTwo"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>BoolOne:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boolOne"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.radio" optionsDataSource="${pageFlow.radioOptions}"></netui:radioButtonGroup>
                    </td>
                </tr>

                <tr valign="top">
                    <td>TextTwo:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.textTwo"></netui:textArea>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postBean"/>
        </netui:form>
        </td><tr></table>

        </p>
  </netui:body>
</netui:html>
