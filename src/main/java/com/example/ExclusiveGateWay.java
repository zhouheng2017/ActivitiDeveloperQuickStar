package com.example;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-03-23
 * @Time: 14:38
 */
public class ExclusiveGateWay {

    private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    public static void main(String[] args) {
        System.out.println("建表成功--------------------------->" + processEngine);

        deployProcess();
        startProcess();
        String taskId = queryTask();
        completeTask(taskId);
    }

    /**
     * 部署流程定义，资源来自bpmn格式
     */
    public static void deployProcess() {

        Deployment deployment = processEngine.getRepositoryService().createDeployment()
                .name("排他网关流程")
                .addClasspathResource("diagrams/ExclusiveGateWay.bpmn")
                .deploy();
        System.out.println("流程部署id" + deployment.getId());
        System.out.println("流程部署名称" + deployment.getName());

    }


    /**
     * 执行流程，开始流程
     */
    public static void startProcess() {

        String processInstanceKey = "ExclusiveGateWay";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processInstanceKey);

        System.out.println("流程实例id" + processInstance.getProcessInstanceId());
        System.out.println("流程定义id" + processInstance.getProcessDefinitionId());
        System.out.println("流程执行对象id" + processInstance.getId());

    }

    /**
     * 查询正在运行的任务
     */
    public static String queryTask() {
        String taskId="";

        //获取任务服务
        TaskService taskService = processEngine.getTaskService();

        //创建任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();

        //创建班里人任务列表
        List<Task> list = taskQuery.list();

        if (list != null && list.size() > 0) {
            for (Task task : list
                    ) {
                System.out.println("任务名称" + task.getName());
                System.out.println("任务的id" + task.getId());
                taskId = task.getId();
                System.out.println("任务办理的人" + task.getAssignee());

            }
        }

        return taskId;
    }

    public static void completeTask(String taskId) {

//        String taskId = "";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("visitor", 6);
        processEngine.getTaskService().complete(taskId, params);
        System.out.println("任务完成");


    }
}
