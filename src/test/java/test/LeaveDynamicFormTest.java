package test;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-08
 * @Time: 9:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activitiContent.xml")
public class LeaveDynamicFormTest {

    public static void main(String[] args) {

        LeaveDynamicFormTest leaveDynamicFormTest = new LeaveDynamicFormTest();
        leaveDynamicFormTest.allApproved();
    }

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    FormService formService;

    @Autowired
    IdentityService identityService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Test
    @Deployment(resources = "/diagrams/dynamic/bpmn25.bpmn")
    public void allApproved() {
        String currentUser = "user";
//        设置当前用户
        identityService.setAuthenticatedUserId(currentUser);

        //获取流程定义的id
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("Leave").singleResult();


        //设置流程变量
        Map<String, String> variables = new HashMap<String, String>();
        //设置日期的格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取Calender的对象，设置开始结束时间
        Calendar calendar = Calendar.getInstance();
        String startDate = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        String endDate = simpleDateFormat.format(calendar.getTime());
        variables.put("startDate", startDate);
        variables.put("endDate", endDate);
        variables.put("reason", "公休");

        //具体的流程实例通过formServie来启动流程实例
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variables);

//        判断流程实例是否为空

        assertNotNull(processInstance);

        //获取部门领导的任务
        Task deptLeaderTask = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
//        设置变量
        Map<String, String> variable = new HashMap<String, String>();
        variable.put("deptLeaderApproved", "true");
        //部门领导审批通过,taskId,map
        formService.submitTaskFormData(deptLeaderTask.getId(), variable);


        Task hrTask = taskService.createTaskQuery().taskCandidateGroup("hr").singleResult();
        //人事同意
        Map<String, String> variableHr = new HashMap<String, String>();
        variableHr.put("hrApproved", "true");
        //人事审批
        formService.submitTaskFormData(hrTask.getId(), variableHr);

        //销假（根据申请人的用户ID读取）
        Task reportBackTask = taskService.createTaskQuery().taskAssignee(currentUser).singleResult();

        Map<String, String> varReport = new HashMap<String, String>();
        varReport.put("reportBackDate", String.valueOf(calendar.getTime()));
        formService.submitTaskFormData(reportBackTask.getId(), varReport);

        //查询任务是否结束
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().finished().singleResult();
        assertNotNull(historicProcessInstance);

        //读取历史变量
        Map<String, Object> historyVariables = packageVariables(processInstance);

        assertEquals("ok", historyVariables.get("result"));

    }

    private Map<String,Object> packageVariables(ProcessInstance processInstance) {
        Map<String, Object> historyVariables = new HashMap<String, Object>();
        List<HistoricDetail> list = historyService.createHistoricDetailQuery().processInstanceId(processInstance.getId()).list();

        for (HistoricDetail historicDetail : list) {
            if (historicDetail instanceof HistoricFormProperty) {
                HistoricFormProperty field = (HistoricFormProperty) historicDetail;
                historyVariables.put(field.getPropertyId(), field.getPropertyValue());
                System.out.println("form field: taskId=" + field.getTaskId() + "," + field.getPropertyId() + "=" + field.getPropertyValue());


            }//表单中的字段
            else if (historicDetail instanceof HistoricVariableUpdate) {
                //普通的变量
                HistoricDetailVariableInstanceUpdateEntity variableUpdateEntity = (HistoricDetailVariableInstanceUpdateEntity) historicDetail;
                historyVariables.put(variableUpdateEntity.getName(), variableUpdateEntity.getValue());

                System.out.println("variable:" + variableUpdateEntity.getName() + "= " + variableUpdateEntity.getVariableName());

            }

        }
        return historyVariables;
    }
}
























