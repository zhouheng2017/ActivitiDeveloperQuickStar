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
    <%@ include file="/common/global.jsp"%>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>待办任务列表</title>

    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
</head>
<body>


    <table width="100%" class="table table-bordered table-hover table-condensed">


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

<%--            <td>
                    &lt;%&ndash;//若为空则需要签收任务&ndash;%&gt;
                <c:if test="${empty task.assignee}"><a href="/workflow/claim/${task.id}" >签收</a> </c:if>
                    &lt;%&ndash;若不为空则需要完成任务&ndash;%&gt;
                <c:if test="${not empty task.assignee}"><a href="/workflow/task/getForm/${task.id}" >办理任务</a> </c:if>
            </td>--%>

            <td>
                <c:if test="${empty task.assignee }">
                    <a class="btn" href="/workflow/claim/${task.id}"><i class="icon-eye-open"></i>签收</a>
                </c:if>
                <c:if test="${not empty task.assignee }">
                    <a class="btn" href="/workflow/task/getForm/${task.id}"><i class="icon-user"></i>办理</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
