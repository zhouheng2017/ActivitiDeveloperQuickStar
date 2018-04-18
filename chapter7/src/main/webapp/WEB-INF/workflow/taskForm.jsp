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
    <%@ include file="/common/global.jsp"%>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>任务办理</title>
    <script type="text/javascript" src="${ctx }/js/common/jquery.js"></script>
    <script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx }/js/common/bootstrap-datepicker.js"></script>
    <script type="text/javascript">
        $(function() {
            $('.datepicker').datepicker();
        });
    </script>
</head>
<body>

<h3>任务办理--[${hasFormKey ? task.name : taskFormData.task.name}],流程定义ID：
    [${hasFormKey? task.processDefinitionId:taskFormData.task.processDefinitionId}]


<form method="post" action="/workflow/task/complete/${hasFormKey? task.id:taskFormData.task.id}" class="form-horizontal">

    <c:if test="${hasFormKey}">
        ${taskFormData}
    </c:if>
    <c:if test="${!hasFormKey}">
        <c:forEach items="${taskFormData.formProperties}" var="fp" >
            <c:set var="fpo" value="${fp}"/>
            <c:set var="disabled" value="${fp.writable ? '' : 'disabled'}" />
            <c:set var="readonly" value="${fp.writable ? '' : 'readonly'}" />
            <c:set var="required" value="${fp.required ? 'required' : ''}" />
            <%
                // 把需要获取的属性读取并设置到pageContext域
                FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();
                String[] keys = {"datePattern", "values"};
                for (String key: keys) {
                    pageContext.setAttribute(key, type.getInformation(key));
                }
            %>
            <div class="control-group">
                    <%-- 文本或者数字类型 --%>
                <c:if test="${fp.type.name == 'string' || fp.type.name == 'long'}">
                    <label class="control-label" for="${fp.id}">${fp.name}:</label>
                    <div class="controls">
                        <input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value="${fp.value}" ${readonly} ${required} />
                    </div>
                </c:if>

                    <%-- 日期 --%>
                <c:if test="${fp.type.name == 'date'}">
                    <label class="control-label" for="${fp.id}">${fp.name}:</label>
                    <div class="controls">
                        <input type="text" id="${fp.id}" name="${fp.id}" class="datepicker" value="${fp.value}" data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}" ${readonly} ${required}/>
                    </div>
                </c:if>

                    <%-- 下拉框 --%>
                <c:if test="${fp.type.name == 'enum'}">
                    <label class="control-label" for="${fp.id}">${fp.name}:</label>
                    <div class="controls">
                        <select name="${fp.id}" id="${fp.id}" ${disabled} ${required}>
                            <c:forEach items="${values}" var="item">
                                <option value="${item.key}" <c:if test="${item.value == fp.value}">selected</c:if>>${item.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>

                    <%-- Javascript --%>
                <c:if test="${fp.type.name == 'javascript'}">
                    <script type="text/javascript">${fp.value};</script>
                </c:if>

            </div>
        </c:forEach>
    </c:if>

  <%--  <button type="submit" class="btn"><i class="icon-play"></i>完成任务</button>--%>
    <%-- 按钮区域 --%>
    <div class="control-group">
        <div class="controls">
            <a href="javascript:history.back();" class="btn"><i class="icon-backward"></i>返回列表</a>
            <button type="submit" class="btn"><i class="icon-ok"></i>完成任务</button>
        </div>
    </div>
</form>


</body>
</html>
