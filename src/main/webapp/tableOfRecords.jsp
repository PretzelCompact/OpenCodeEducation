<table>
    <tr>
        <td>id</td>
        <td>link</td>
    </tr>
    <%
        Object records = request.getAttribute("records");

        if(records!=null){
        for(String id : records.toString().split(";")){
            out.println(
            "<tr>" +
                "<td>" + id + "</td>" +
                "<td><form action=\"get\">" +
                    "<input type=\"hidden\" name=\"recordId\" value=\"" + id + "\" />" +
                    "<input type=\"submit\" value = \"open\" />" +
                "</form></td>" +
            "</tr>");
        }
        }
    %>
</table>