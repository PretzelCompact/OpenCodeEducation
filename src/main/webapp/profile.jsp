<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Notebook Authorization</title>
</head>

<body>
<h1>Profile of <%= request.getSession().getAttribute("userName") %> of <%= request.getSession().getAttribute("userId") %></h1>

<form action="authorization.html">
    <input type="submit" value="Change user" />
</form>
<form action="find-notebook.jsp">
    <input type="submit" value="Find notes" />
</form>
<form action="get">
    <input type="submit" value="Create note" />
</form>

</br>
Remind notes:
<jsp:include page="tableOfRecords.jsp" />
</body>
</html>