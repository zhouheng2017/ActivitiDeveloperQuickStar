<%--
  Created by IntelliJ IDEA.
  User: zhouheng
  Date: 2018/4/11
  Time: 8:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <%@ include file="/common/global.jsp" %>
    <%@ include file="/common/meta.jsp"%>
    <%@ include file="/common/include-base-styles.jsp"%>
</head>
<body style="margin-top: 3em;">
<%--<div>
    <form action="index.jsp" method="post">
        请登录：
        <br/>
        用户名：<input type="text" name="name" value="user">
        <br/>
        <br/>
        密码：<input type="text" name="password" value="password">
        <input type="submit" class="btn btn-primary" value="submit">


    </form>
</div>--%>

<center>
    <h2>外置表单</h2>
    <c:if test="${not empty param.error}">
        <h2 id="error" class="alert alert-error">用户名或密码错误</h2>
    </c:if>
    <c:if test="${not empty param.timeout}">
        <h2 id="error" class="alert alert-error">未登录或登陆超时！！！！</h2>
    </c:if>

<div style="width: 500px">
    <%--<form class="bs-example bs-example-form" role="form" action="${ctx}/workflow/index" method="post">--%>
    <form class="bs-example bs-example-form" role="form" action="${ctx}/user/login" method="post">

        <br>
        <div class="input-group">
            <span class="input-group-addon">用户</span>
            <input type="text" style="width:20%" class="form-control" name="username" value="zhouheng">
        </div>
        <br>
        <div class="input-group">
            <span class="input-group-addon">密码</span>
            <input type="password" style="width:20%" class="form-control" name="password" value="zhouheng">
        </div>
        <div class="input-group">
            <br/>
            <input type="submit" class="btn btn-primary" value="submit">
        </div>


    </form>
</div>

<div class="row">
    <hr />
    <h4 class="text-info">用户与角色列表（密码：000000）</h4>
    <table class="table table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>用户名</th>
            <th>角色</th>
            <th>部门</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>1</td>
            <td>henry</td>
            <td>系统管理员</td>
            <th>IT部</th>
        </tr>
        <tr>
            <td>2</td>
            <td>bill</td>
            <td>总经理</td>
            <td>总经理室</td>
        </tr>
        <tr>
            <td>3</td>
            <td>jenny</td>
            <td>人事经理</td>
            <td>人事部</td>
        </tr>
        <tr class="success">
            <td>4</td>
            <td>kermit</td>
            <td>部门经理</td>
            <td>市场部</td>
        </tr>
        <tr class="success">
            <td>5</td>
            <td>eric</td>
            <td>销售人员</td>
            <td>市场部</td>
        </tr>
        <tr class="success">
            <td>6</td>
            <td>tom</td>
            <td>销售人员</td>
            <td>市场部</td>
        </tr>
        <tr class="info">
            <td>7</td>
            <td>andy</td>
            <td>普通职员</td>
            <td>业务部</td>
        </tr>
        <tr class="info">
            <td>8</td>
            <td>amy</td>
            <td>普通职员</td>
            <td>业务部</td>
        </tr>
        <tr class="danger">
            <td>9</td>
            <td>tony</td>
            <td>财务人员</td>
            <td>财务部</td>
        </tr>
        <tr class="danger">
            <td>10</td>
            <td>lily</td>
            <td>出纳员</td>
            <td>财务部</td>
        </tr>
        <tr>
            <td>11</td>
            <td>thomas</td>
            <td>后勤人员</td>
            <td>后勤部</td>
        </tr>
        </tbody>
    </table>
</div>
</center>


</body>
</html>