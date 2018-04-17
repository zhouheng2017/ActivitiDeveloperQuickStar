<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: zhouheng
  Date: 2018/4/3
  Time: 13:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>


<html>
<head>
    <%@ include file="/common/global.jsp"%>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>

    <title>ProcessList</title>

    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>

    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<fieldset id="deployFieldset">
    <legend>部署流程资源</legend>
    <form action="/workflow/myTask" method="post" enctype="multipart/form-data" style="margin-top:1em;">
        <input type="file" name="file" />
        <input type="submit" value="Submit" class="btn" />
    </form>
    <hr class="soften" />
</fieldset>

<div class="container">
    <table width="100%" class="table table-bordered table-hover table-condensed">
        <tr>
            <th>流程定义id</th>
            <th>部署id</th>
            <th>流程定义名称</th>
            <th>流程定义key</th>
            <th>版本号</th>
            <th>XML资源名称</th>
            <th>图片资源名称</th>
            <th>删除</th>
            <th>启动</th>
            <th>操作</th>
        </tr>

        <c:forEach items="${processDefinitions}" var="pd">
            <tr>
                <td>${pd.id}</td>
                <td>${pd.deploymentId}</td>
                <td>${pd.name}</td>
                <td>${pd.key}</td>
                <td>${pd.version}</td>
                <td><a target="_blank" href="/workflow/readResource?pdid=${pd.id}&resourceName=${pd.resourceName}">${pd.resourceName}</a> </td>
                <td><a target="_blank" href="/workflow/readResource?pdid=${pd.id}&resourceName=${pd.diagramResourceName}">${pd.diagramResourceName}</a></td>
                <td><a target="_blank" href="/workflow/deleteDeployment/${pd.deploymentId}">删除</a></td>
                <td><a target="_blank" href="/workflow/getForm/${pd.id}">启动</a></td>
                <td>
                    <a class="btn" href="/workflow/deleteDeployment/${pd.deploymentId}"><i class="icon-trash"></i>删除</a>
                    <a class="btn" href="/workflow/getForm/${pd.id}"><i class="icon-play"></i>启动</a>
                </td>
            </tr>


        </c:forEach>

    </table>


</div>
<div style="padding: 10px 10px 10px;" >
    <form class="bs-example bs-example-form" role="form" action="/workflow/logout">

        <div class="input-group">
            <br/>
            <input type="submit" class="btn btn-primary" value="退出登录">
        </div>


    </form>
</div>

<div class="container">

    <br/>
    <br/>
    <div>
        <a href="/workflow/myTask">我的任务列表</a>
    </div>

</div>
<%--<a href="/workflow/logout.jsp">logout</a>--%>


</body>
</html>
