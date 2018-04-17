<%@ page import="com.example.activiti.controller.CreateUserAndGroup" %>
<%@ page import="com.example.activiti.controller.UserUtil" %>
<%@ page import="org.activiti.engine.identity.User" %>
<%--
  Created by IntelliJ IDEA.
  User: zhouheng
  Date: 2018/4/3
  Time: 13:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <%@ include file="/common/global.jsp"%>
    <script>
        var notLogon = ${empty user};
        if (notLogon) {
            location.href = '${ctx}/login.jsp?timeout=true';
        }
    </script>
    <%@ include file="/common/meta.jsp" %>
    <title>Activiti Explorer</title>
    <%@ include file="/common/include-base-styles.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/menu.css" />
    <style type="text/css">
        iframe{
            margin-top: .5em;
        }
    </style>

    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/bootstrap.min.js" type="text/javascript"></script>
    <script src="${ctx }/js/modules/main/main.js" type="text/javascript"></script>
</head>
<body>




<div class="container">
    <div class="jumbotron">
        <br/>
        <br/>
        <div>

            <label class="sr-only">显示所有发布流程列表</label>
            <a href="/workflow/processList" class="btn btn-primary btn-lg" role="button" id="123">流程定义列表</a>
        </div>
        <br/>
        <br/>

        <form class="form-inline" role="form" action="/workflow/deploy" method="post" enctype="multipart/form-data">
            部署流程
            <br/>
            <div class="form-group">
                <span class="form-group">部署流程名称</span>
                <input type="text" class="form-control" id="name" placeholder="请输入名称">
            </div>
            <div class="form-group">
                <label class="sr-only" for="inputfile">文件输入</label>
                <input type="file" id="inputfile" name="file">
            </div>
            <button type="submit" class="btn btn-default">提交</button>
        </form>

    </div>
</div>

<div class="container">

    <br/>
    <br/>
    <div>
        <a href="/workflow/myTask">我的任务列表</a>
    </div>

</div>



<%--<%--%>
    <%--String user = request.getParameter("user");--%>

    <%--CreateUserAndGroup createUserAndGroup = new CreateUserAndGroup();--%>
    <%--User userName = createUserAndGroup.createUser(user);--%>
<%--//    Group deptLeader = createUserAndGroup.createGroup("deptLeader");--%>
<%--//    createUserAndGroup.saveUserToGroup(userName, deptLeader);--%>

    <%--UserUtil.saveUserToSession(session, userName);--%>
<%--//    session.setAttribute("deptLeader", deptLeader);--%>

<%--//    session.setAttribute("password", password);--%>

<%--%>--%>
</body>
</html>
