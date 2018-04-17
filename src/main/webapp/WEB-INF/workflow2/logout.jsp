<%@ page import="org.activiti.engine.identity.User" %><%--
  Created by IntelliJ IDEA.
  User: zhouheng
  Date: 2018/4/11
  Time: 9:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logout</title>
</head>
<body>

<%--<div style="padding: 10px 10px 10px;" >
    <form class="bs-example bs-example-form" role="form" action="../../login.jsp" method="get">

        <div class="input-group">
            <br/>
            <input type="submit" class="btn btn-primary" value="退出登录">
        </div>


    </form>
</div>--%>
<%
    session.removeAttribute("user");
    request.getRequestDispatcher("../../login.jsp").forward(request, response);

%>

</body>
</html>
