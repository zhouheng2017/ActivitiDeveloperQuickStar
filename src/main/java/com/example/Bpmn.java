package com.example;

import groovy.transform.ToString;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.cxf.helpers.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-03-27
 * @Time: 14:32
 */
public class Bpmn {

    /**
     * 获取processEngine的引擎
     */
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    /**
     * 发布流程
     */
    @Test
    public void deploymentProcess() {

        //创建一个deploymentBuilder对象
        DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment();
        //获取
        Deployment deployment = deploymentBuilder.name("IAM账号申请")
                .addClasspathResource("diagrams/IAM.bpmn")
//                .addClasspathResource("diagrams/IAM.png")
                .deploy();

        System.out.println("部署id" + deployment.getId());
        System.out.println("部署名称" + deployment.getName());

    }

    /**
     * 通过key启动流程
     */
    @Test
    public void startProcess() {

        String pricessDefineKey = "ApplyForAnAccount";
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(pricessDefineKey);

        System.out.println("流程定义的id" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例的id" + processInstance.getId());
        System.out.println("流程实例的名字" + processInstance.getName());
    }

    /**
     * 查询个人任务
     */

    @Test
    public void queryTask() {

        //获取任务的服务
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
//        String taskAssignee = "员工张三";
        String taskAssignee = "崔总";

        //获取所有的张三的任务列表
        List<Task> list = taskQuery.taskAssignee(taskAssignee).list();

        if (list != null && list.size() > 0) {

            for (Task task : list
                    ) {
                System.out.println("办理人   "+task.getAssignee());
                System.out.println("流程定义的id   "+task.getProcessDefinitionId());
                System.out.println("任务id   "+task.getId());
                System.out.println("任务名称  "+task.getName());
                System.out.println("任务定义id  " + task.getTaskDefinitionKey());
                System.out.println("流程实例id  " + task.getProcessInstanceId());
                System.out.println("执行对象id  " + task.getExecutionId());
                System.out.println("创建时间" + task.getCreateTime());
            }
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {

        String taskId = "70004";
        processEngine.getTaskService().complete(taskId);

        System.out.println("完成任务" + taskId);

    }

    /**
     * 查看资源图片
     */
    @Test
    public void viewImagine() {
        String deploymentId = "67501";
        String imagineName = "";

        List<String> list = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);

        if (list != null && list.size() > 0) {
            for (String li : list
                    ) {
                if (li.indexOf(".png") > 0) {
                    imagineName = li;

                }
            }
        }

        //部署id deploymentid
        //图片名称
        InputStream resourceAsStream = processEngine.getRepositoryService().getResourceAsStream(deploymentId, imagineName);

        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(resourceAsStream, new File("d:/" + imagineName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看流程的定义
     */
    @Test
    public void findProcessDefine() {

        String processDefinitionKey = "ApplyForAnAccount";
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionVersion()
                .processDefinitionKey(processDefinitionKey)
                .desc().list();

        if (list != null && list.size() > 0) {

            for (ProcessDefinition pd : list
                    ) {
                System.out.println(pd.getDeploymentId());
                System.out.println(pd.getId());
                System.out.println(pd.getVersion());
                System.out.println(pd.getName());
                System.out.println(pd.getKey());

            }
        }
    }

    @Test
    public void deleteDefine() {

        String deploymentId = "62501";
        processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
        System.out.println("删除成功");

    }








}
