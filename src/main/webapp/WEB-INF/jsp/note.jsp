<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Notebook Authorization</title>
</head>
<body>
    <h1>${username}</h1>

    <form method="POST" action = "save">
        <table>
            <tr>
                <td>Marked:</td>
                <td><input type="checkbox" name="marked"
                <%
                    if(request.getAttribute("marked").toString().equals("true"))
                        out.println("checked /></td>");
                    else
                        out.println("/></td>");
                %>
            </tr>
            <tr>
                <td>Remind:</td>
                <td><input type="checkbox" name="remind"
                <%
                    if(request.getAttribute("remind").toString().equals("true"))
                        out.println("checked /></td>");
                    else
                        out.println("/></td>");
                %>
            </tr>
            <tr>
                <td>Remind time:</td>
                <td><input type="datetime-local" name = "remindTime" value="${remindTime}" /></td>
            </tr>
            <tr>
                <td>Content:</td>
                <td><textarea name = "content" rows = "5" cols = "30">${content}</textarea></td>
            </tr>
            <tr>
                <td>Date:</td>
                <td>${creationTime}</td>
            </tr>
        </table>
        <input type="hidden" name="creationTime" value="${creationTime}" />
        <input type="hidden" name="recordId" value="${recordId}" />
        <input type="submit" value="Save" />
    </form>

    <form method="POST" action="delete">
        <input type="hidden" name="recordId" value="${recordId}" />
        <input type="submit" value="Delete record" />
    </form>
</body>
</html>