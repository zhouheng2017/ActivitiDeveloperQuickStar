<%@ page import="com.example.activiti.controller.CreateUserAndGroup" %>
<%@ page import="org.activiti.engine.identity.User" %>
<%@ page import="com.example.activiti.controller.UserUtil" %>
<%@ page import="org.activiti.engine.identity.Group" %><%--
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
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <title>Index</title>
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



<%
    String user = request.getParameter("user");

    CreateUserAndGroup createUserAndGroup = new CreateUserAndGroup();
    User userName = createUserAndGroup.createUser(user);
    Group deptLeader = createUserAndGroup.createGroup("deptLeader");
//    Group deptManagerment = createUserAndGroup.createGroup("hr");
//    Group deptLeader = createUserAndGroup.createGroup("deptLeader");
//    if (userName.equals("jinyi")) {
////
        createUserAndGroup.saveUserToGroup(userName, deptLeader);
//    createUserAndGroup.saveUserToGroup(userName, deptManagerment);
////
//    }
    UserUtil.saveUserToSession(session, userName);
//    session.setAttribute("deptLeader", deptLeader);

//    session.setAttribute("password", password);

%>
</body>
</html>
