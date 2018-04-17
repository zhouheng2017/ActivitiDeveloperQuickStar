<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: zhouheng
  Date: 2018/4/9
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>StartedProcess</title>
</head>
<body>
hello:${processInstance}流程已经启动
<a href="/workflow/processList">返回流程列表</a>
<div class="container">

        <br/>
        <br/>
        <div>
            <a href="/workflow/myTask">我的任务列表</a>
        </div>

</div>

</body>
</html>
