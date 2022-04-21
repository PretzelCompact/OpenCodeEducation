<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Notebook Finder</title>
</head>

<body>

<form action = "find">
    <table>
        <tr>
            <td>Date diapason:</td>
            <td>
                <input type="datetime-local" name = "leftDate" />
                -
                <input type="datetime-local" name = "rightDate" />
            </td>
        </tr>
        <tr>
            <td>Marked:</td>
            <td><input type="checkbox" name="marked"></td>
        </tr>
        <tr>
            <td>Remind:</td>
            <td><input type="checkbox" name="remind"></td>
        </tr>
        <tr>
            <td>Contains:</td>
            <td><textarea name = "contains" rows = "5" cols = "30"></textarea></td>
        </tr>
    </table>
    <input type="hidden" name="findType" value="selected" />
    <input type="submit" value = "Find with selected params" />
</form>
<form action="find">
    <input type="hidden" name="findType" value="all" />
    <input type="submit" value = "List all notes of user" />
</form>
<br/>
Found notes:
<jsp:include page="tableOfRecords.jsp" />
</body>
</html>