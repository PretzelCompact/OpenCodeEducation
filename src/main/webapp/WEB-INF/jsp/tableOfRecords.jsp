<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:forEach var="r" items="${randomResult}">
    <li> ${num} </li>
</c:forEach>

<table>
    <tr>
        <td>id</td>
        <td>link</td>
    </tr>

    <c:forEach var="rec" items="${records}">
        <tr>
            <td>${rec.getId()}</td>
            <td>
                <form action="get">
                    <input type="hidden" name="recordId" value="${rec.getId()}" />
                    <input type="submit" value = "open" />
                </form>
            </td>
        </tr>
    </c:forEach>

</table>