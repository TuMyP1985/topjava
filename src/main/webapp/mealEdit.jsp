<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <link type="text/css"
          href="css/ui-lightness/jquery-ui-1.8.18.custom.css" rel="stylesheet" />
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <title>Edit meal</title>

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>

<form method="POST" action='meals' name="frmAddUser">
<%--    DateTime : <input type="datetime" name="dateTime" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${meal.dateTime}" />" /> <br />--%>
    DateTime : <input type="datetime-local" name="dateTime" value="<c:out value="${mealTo.dateTime}" />" /> <br />
    Description : <input type="text" name="description" value="<c:out value="${mealTo.description}" />" /> <br />
    Calories : <input type="number" name="calories" value="<c:out value="${mealTo.calories}" />" /> <br />


    <button name="mealToId" value="<c:out value="${mealTo.id}" />" onclick="meals">Save</button>
    <button name="mealToId" value="<c:out value="Cancel" />" onclick="meals">Cancel</button>

</form>
</body>
</html>