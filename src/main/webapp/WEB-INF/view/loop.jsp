<%--
  Created by IntelliJ IDEA.
  User: alik72908
  Date: 06.03.2016
  Time: 14:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <style type="text/css">
        table {
            font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
            font-size: 14px;
            border-collapse: collapse;
            text-align: center;
        }
        th {
            background: #AFCDE7;
            color: black;
            padding: 10px 20px;
        }
        th, td {
            border-style: solid;
            border-width: 0 1px 1px 0;
            border-color: white;
        }
        td {
            padding: 10px 20px;
            background: #D8E6F3;
        }
        th:first-child, td:first-child {
            text-align: left;
        }
    </style>
</head>
<body>
<h1> Table Of user meals with exceed</h1>

<c:if test="${requestScope.list != null}">
    <table align="center">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
    <c:forEach var="meal" items="${list}">
        <c:if test="${meal.exceed}">
            <tr style="color: red">
                <td>${meal.dateTime}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:if>
        <c:if test="${! meal.exceed}">
            <tr style="color: black">
                <td>${meal.dateTime}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:if>
    </c:forEach>
    </table>
</c:if>

</body>
</html>
