<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mls.task.core.model.response.PartResponseModel" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<body>

<form action="/">
    PN:<br>
    <input type="text" name="number">
    <br>

    Part name:<br>
    <input type="text" name="name">
    <br>
    VENDOR:<br>
    <input type="text" name="vendor">
    <br>

    QTY:<br>
    <input type="text" name="qty">
    <br>
    Shipped after <input type="text" name="shipped_after"> before <input type="text" name="shipped_before">
    <br><br>
    Received after <input type="text" name="received_after"> before <input type="text" name="received_before">

    <br>
    <input type="submit" value="Filter">
</form>


<table style="width:100%">
    <tr>
        <th>PN</th>
        <th>Part Name</th>
        <th>Vendor</th>
        <th>Shipped</th>
        <th>Received</th>
    </tr>

    <% if (request.getAttribute("parts") != null) {
        List<PartResponseModel> parts = (ArrayList<PartResponseModel>) request.getAttribute("parts");
        for (PartResponseModel part : parts) { %>
    <tr>
        <td><%=part.getNumber()%>
        </td>
        <td><%=part.getName()%>
        </td>
        <td><%=part.getVendor()%>
        </td>
        <td><%=part.getShipped()%>
        </td>
        <td><%=part.getReceived()%>
        </td>
    </tr>
    <% }
    } %>
</table>

</body>
</html>

