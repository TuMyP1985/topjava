<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <link type="text/css"
          href="css/ui-lightness/jquery-ui-1.8.18.custom.css" rel="stylesheet"/>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <title>Edit meal</title>

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>

<form method="POST" action='meals' name="frmAddUser">
    <table>
        <tr>
            <td>DateTime :</td>
            <td><input type="datetime-local" name="dateTime" value="<c:out value="${mealTo.dateTime}" />"/></td>
        </tr>
        <br/>
        <tr>
            <td>Description :</td>
            <td><input type="text" name="description" value="<c:out value="${mealTo.description}" />"/></td>
        </tr>
        <br/>
        <tr>
            <td>Calories : </td>
            <td><input type="number" name="calories" value="<c:out value="${mealTo.calories}" />"/></td>
        </tr>
        <br/>
    </table>

    <button name="mealToId" value="<c:out value="${mealTo.id}" />" onclick="meals">Save</button>
    <button name="mealToId" value="<c:out value="Cancel" />" onclick="meals">Cancel</button>

</form>
</body>
</html>