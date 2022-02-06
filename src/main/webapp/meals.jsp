<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<br></br>
<p><a href="meals?action=insert">Add Meal</a></p>
<br></br>

<table border="3">
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
    <th>Update</th>
    <th>Delete</th>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="entry" items="${meals}">
        <tr style="color:${entry.excess ? 'red' : 'green'}">
            <td><c:out value="${entry.dateTime.format(DateTimeFormatter.ofPattern('dd.MM.yyyy hh:mm'))}"/></td>
            <td><c:out value="${entry.description}"/></td>
            <td><c:out value="${entry.calories}"/></td>
            <td><a href="meals?action=edit&mealToId=<c:out value="${entry.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&mealToId=<c:out value="${entry.id}"/>">Delete</a></td>

        </tr>
    </c:forEach>
</table>



</body>
</html>