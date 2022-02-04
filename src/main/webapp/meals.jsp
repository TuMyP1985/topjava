<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<br></br>

<table border="3">
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
    <th>Update</th>
    <th>Delete</th>
    <jsp:useBean id="name" scope="request" type="java.util.List"/>
    <c:forEach var="entry" items="${name}">
        <tr style="color:${entry.excess ? 'red' : ''}">
            <td><c:out value="${entry.getDateFormatted()}"/></td>
            <td><c:out value="${entry.description}"/></td>
            <td><c:out value="${entry.calories}"/></td>
            <td><c:out value="${entry.calories}"/></td>
            <td><c:out value="${entry.calories}"/></td>
        </tr>
    </c:forEach>
</table>


<br></br>

<script>
    function drowTable() {
        var mealToList = "${name}";
        var html = '<tr>\n' +
            '        <th>Date</th>\n' +
            '        <th>Description</th>\n' +
            '        <th>Calories</th>\n' +
            '        <th>Update</th>\n' +
            '        <th>Delete</th>\n' +
            '    </tr>';

        for (var i = 0; i < mealToList.length(); i++) {
            var meal = mealToList[i];//excess
            var meal1 = mealToList.get(i);//excess
            html = html + '<tr><td>' + city.dateTime + '</td>\n' +
                '<tr><td>' + city.description + '</td>\n' +
                '<tr><td>' + city.calories + '</td>\n' +
                '        <td><button onclick="Update(' + city.id + ')">Update</button></td>\n' +
                '        <td><button onclick="Delete(' + city.id + ')">Delete</button></td></tr>';

        }

        document.getElementById("ListMeals").innerHTML = html;
    }

    // drowTable();

</script>

</body>
</html>