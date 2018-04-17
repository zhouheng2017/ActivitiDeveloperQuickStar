package com.example.activiti;

import org.activiti.engine.FormService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-09
 * @Time: 17:00
 */
public class FormKeyTest {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();


    /**
     * 对外置表单的测试
     */
    @Test
    @Deployment(resources = {"diagrams/formkey/dept-leader-audit.form",
            "diagrams/formkey/hr-audit.form",
            "diagrams/formkey/leave-formkey.bpmn",
            "diagrams/formkey/leave-formkey.png",
            "diagrams/formkey/modify-apply.form",
            "diagrams/formkey/modifyApply.form",
            "diagrams/formkey/report-back.form",
            "diagrams/formkey/start.form"})
    public void allPassTest() {

        FormService formService = activitiRule.getFormService();

        //获取流程定义
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("leave-formkey").singleResult();
        //读取启动外置表单
        Object renderedStartForm = formService.getRenderedStartForm(processDefinition.getId());

        //部门领导通过审批
        Task task = activitiRule.getTaskService().createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        //读取任务表单
        Object renderedStartForm1 = formService.getRenderedStartForm(task.getId());


    }

    @Test
    @Deployment(resources = {"diagrams/el/expression.bpmn"})
    public void testExpression() {

        MyBean myBean = new MyBean();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("myBean", myBean);

        //流程启东人
        String name = "zhouheng";

        variables.put("name", name);
        activitiRule.getIdentityService().setAuthenticatedUserId("user");
        String businessKey = "9999";

        //获取流程实例
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("expression", businessKey, variables);

        assertEquals("user", activitiRule.getRuntimeService().getVariable(processInstance.getId(), "authenticatedUserIdForTest"));

    }
}
