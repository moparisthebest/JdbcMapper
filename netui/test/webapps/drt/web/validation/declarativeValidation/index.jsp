<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>Declarative Validation</h3>

        <netui:form action="begin">
            page flow 'str' property: <netui:textBox dataSource="pageFlow.str"/>
            <netui:button value="submit"/>
        </netui:form>
        <hr/>
        
        Which type of validation?
        <netui:form action="begin">
            <netui:radioButtonGroup optionsDataSource="${pageFlow.actionChoices}" dataSource="pageFlow.selectedAction"/>
            <netui:button value="submit"/>
        </netui:form>
        <hr/>

        <netui:form action="${pageFlow.selectedAction}">
            <table>
                <tr>
                    <td>minlength=2:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.minLength"/>
                        <netui:error key="minLength"/>
                    </td>
                </tr>
                <tr>
                    <td>maxlength=2:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.maxLength"/>
                        <netui:error key="maxLength"/>
                    </td>
                </tr>
                <tr>
                    <td>mask=a*b:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.mask"/>
                        <netui:error key="mask"/>
                    </td>
                </tr>
                <tr>
                    <td>type=int:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.typeInt"/>
                        <netui:error key="typeInt"/>
                    </td>
                </tr>
                <tr>
                    <td>type=short:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.typeShort"/>
                        <netui:error key="typeShort"/>
                    </td>
                </tr>
                <tr>
                    <td>type=long:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.typeLong"/>
                        <netui:error key="typeLong"/>
                    </td>
                </tr>
                <tr>
                    <td>type=double:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.typeDouble"/>
                        <netui:error key="typeDouble"/>
                    </td>
                </tr>
                <tr>
                    <td>type=float:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.typeFloat"/>
                        <netui:error key="typeFloat"/>
                    </td>
                </tr>
                <tr>
                    <td>type=byte:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.typeByte"/>
                        <netui:error key="typeByte"/>
                    </td>
                </tr>
                <tr>
                    <td>date=MM-dd-YYYY:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.date"/>
                        <netui:error key="date"/>
                    </td>
                </tr>
                <tr>
                    <td>range 1-10:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.range"/>
                        <netui:error key="range"/>
                    </td>
                </tr>
                <tr>
                    <td>credit card:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.creditCard"/>
                        <netui:error key="creditCard"/>
                    </td>
                </tr>
                <tr>
                    <td>email:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.email"/>
                        <netui:error key="email"/>
                    </td>
                </tr>
                <tr>
                    <td>valid when pageFlow.str==actionForm.validWhenPageFlowProp:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.validWhenPageFlowProp"/>
                        <netui:error key="validWhenPageFlowProp"/>
                    </td>
                </tr>
            </table>
            <netui:button value="${pageFlow.selectedAction}"/>
        </netui:form>
    </netui:body>
</netui:html>
