<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.apache.commons.lang3.ObjectUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page pageEncoding="UTF-8" %>
<%--
  Created by IntelliJ IDEA.
  User: zhouheng
  Date: 2018/4/9
  Time: 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>startProcessForm</title>
    <link href="/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.css" type="text/css" rel="stylesheet" />

    <script src="/js/common/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
    <script src="/js/common/plugins/jui/extends/i18n/jquery-ui-date_time-picker-zh-CN.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function() {
            $('#startTime,#endTime').datetimepicker({
                stepMinute: 5
            });
        });
    </script>
</head>
<body>

<h3>启动流程--
    <c:if test="${hasStartFormKey}">
        [${processDefinition.name}],版本号：${processDefinition.version}

    </c:if>
    <c:if test="${!hasStartFormKey}">
        [${startFormData.processDefinition.name}],版本号：
        ${startFormData.processDefinition.version}
    </c:if>
</h3>

<form method="post" action="/workflow/startProcessDefine/${processDefinitionId}">

    <c:if test="${hasStartFormKey}">
        ${startFormData}
    </c:if>
    <c:if test="${!hasStartFormKey}">
        <c:forEach items="${startFormData.formProperties}" var="fp">
            <%--<c:set var="fpo" scope="session" value="${fp}"/>--%>
            <c:set var="fpo" scope="request" value="${fp}"/>
            <%
                //把需要获取的属性读取并设置到pageContext域中
                //需要获取日期字段设置的格式需要调用org.activiti.engine.impl.form.AbstractFormType类的
                //getInformation()方法 但是EL不支持直接调用，所以需要转化

//                FormType type = ((FormProperty) session.getAttribute("fpo")).getType();
                %>
            <%
                FormType type = ((FormProperty) request.getAttribute("fpo")).getType();

                String[] keys = {"datePattern"};//定义需要读取的其他扩展的属性的名字
                for (String key : keys) {
                    System.out.println("key" + key);
                    session.setAttribute(key, ObjectUtils.toString(type.getInformation(key)));

                }
            %>
            <c:if test="${fp.type.name=='string' || fp.type.name=='long'}">
                <label for="${fp.id}">${fp.name}:</label>
                <input type="text" id="${fp.id}" name="${fp.id}"/>
            </c:if>

            <c:if test="${fp.type.name == 'date'}">
                <label for="${fp.id}">${fp.name}</label>
                <input type="text" id="${fp.id}" name="${fp.id}" data-date-format="${fn:toLowerCase(datePattern)}"


            </c:if>


        </c:forEach>
    </c:if>
<%--    <%
        ActivitiRule activitiRule = new ActivitiRule();
        User user = new User() {
            @Override
            public String getId() {
                return "user";
            }

            @Override
            public void setId(String s) {

            }

            @Override
            public String getFirstName() {
                return null;
            }

            @Override
            public void setFirstName(String s) {

            }

            @Override
            public void setLastName(String s) {

            }

            @Override
            public String getLastName() {
                return null;
            }

            @Override
            public void setEmail(String s) {

            }

            @Override
            public String getEmail() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public void setPassword(String s) {

            }
        };
        session.setAttribute("user", user);

    %>--%>
    <button type="submit" class="btn"><i class="icon-play"></i>启动流程</button>
</form>

</body>
</html>