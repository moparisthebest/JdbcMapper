<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
</head>

<netui:body>
    <p>Test container-generated control's events, onCreate, onAquire and onRelease.</p>

    <netui:anchor href="client_access/clientAccess.do">Test client property access.</netui:anchor>
    <br>
    <netui:anchor href="client_access/clientAccessP.do">Test client property acces (programmatic instantiation).</netui:anchor>
    <br>
    <netui:anchor href="client_impl/clientImpl.do">Test property access from the control impl using getters/setters.</netui:anchor>
    <br>
    <netui:anchor href="client_impl/clientImplP.do">Test property acces from the control impl using getters/setters (programmatic instantiation).</netui:anchor>
    <br>
    <netui:anchor href="impl_access/implAccess.do">Test property access from the control impl using the control context.</netui:anchor>
    <br>
    <netui:anchor href="impl_access/implAccessP.do">Test property acces from the control impl using the control context (programmatic instantiation).</netui:anchor>
    <br>
    <netui:anchor href="property_constraints/propertyConstraint.do">Test property property constraints.</netui:anchor>
    <br>
    <netui:anchor href="veto/testVetoChangeOnConstrainedProperty.do">Test a change to a vetoable property can be vetoed.</netui:anchor>
    <br>
    <netui:anchor href="veto/testVetoChangeOnUnConstrainedProperty.do">Test a change to an unconstrained property can be made.</netui:anchor>
    <br>
    <netui:anchor href="veto/testVetoChangeOnConstrainedExtProperty.do">Test a change to a vetoable external property can be vetoed.</netui:anchor>
    <br>
    <netui:anchor href="veto/testVetoChangeOnUnConstrainedExtProperty.do">Test a change to an unconstrained external property can be made.</netui:anchor>
    <br>

    <h3>Test Results</h3>
    <table border="1">
        <tr><td>Test Results:</td><td>'${pageInput.message}'</td></tr>
    </table>
</netui:body>
</html>
