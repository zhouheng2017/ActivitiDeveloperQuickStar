<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page import="org.apache.commons.lang3.ObjectUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: zhouheng
  Date: 2018/4/11
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TaskForm</title>
</head>
<body>

<h3>任务办理--[${hasFormKey ? task.name : taskFormData.task.name}],流程定义ID：
    [${hasFormKey? task.processDefinitionId:taskFormData.task.processDefinitionId}]


<form method="post" action="/workflow/task/complete/${hasFormKey? task.id:taskFormData.task.id}">

    <c:if test="${hasFormKey}">
        ${taskFormData}
    </c:if>
    <c:if test="${!hasFormKey}">
        <c:forEach items="${taskFormData.formProperties}" var="fp">
            <c:set var="fpo" scope="request" value="${fp}"/>
            <%
                //把需要获取的属性读取并设置到pageContext域中
                //需要获取日期字段设置的格式需要调用org.activiti.engine.impl.form.AbstractFormType类的
                //getInformation()方法 但是EL不支持直接调用，所以需要转化

                FormType type = ((FormProperty) request.getAttribute("fpo")).getType();
                String[] keys = {"datePattern"};//定义需要读取的其他扩展的属性的名字
                for (String key : keys) {
                    System.out.println("key" + key);
                    session.setAttribute(key, ObjectUtils.toString(type.getInformation(key)));

                }
            %>
            <c:if test="${fp.type.name=='string' || fp.type.name=='long'}">
                <label for="${fp.id}">${fp.name}:</label>
                <input type="text" id="${fp.id}" name="${fp.id}" />
            </c:if>

            <c:if test="${fp.type.name == 'date'}">
                <label for="${fp.id}">${fp.name}</label>
                <input type="text" id="${fp.id}" name="${fp.id}" data-date-format="${fn:toLowerCase(datePattern)}"


            </c:if>


        </c:forEach>
    </c:if>

    <button type="submit" class="btn"><i class="icon-play"></i>完成任务</button>
</form>


</body>
</html>
