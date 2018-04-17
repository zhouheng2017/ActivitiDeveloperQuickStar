<%@ page import="org.activiti.engine.task.Task" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: zhouheng
  Date: 2018/4/9
  Time: 16:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <title>TaskLists</title>
</head>
<body>

<div class="container">
    <table cellspacing="0" border="1" cellpadding="8" class="table table-striped">


    <tr>
        <th>任务id</th>
        <th>任务名称</th>
        <th>任务实例</th>
        <th>创建时间</th>
        <th>任务办理人</th>
        <th>操作</th>

    </tr>
    <c:forEach items="${tasks}" var="task">
        <tr>
            <td>${task.id}</td>
            <td>${task.name}</td>
            <td>${task.processInstanceId}</td>
            <td>${task.createTime}</td>
            <th>${task.assignee}</th>

            <td>
                    <%--//若为空则需要签收任务--%>
                <c:if test="${empty task.assignee}"><a href="/workflow/claim/${task.id}" >签收</a> </c:if>
                    <%--若不为空则需要完成任务--%>
                <c:if test="${not empty task.assignee}"><a href="/workflow/task/getForm/${task.id}" >办理任务</a> </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
</div>
</body>
</html>
