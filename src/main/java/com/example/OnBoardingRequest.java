package com.example;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-03-21
 * @Time: 15:08
 */
public class OnBoardingRequest {
//    private static final Logger logger = LoggerFactory.getLogger(OnBoardingRequest.class);

//    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    ProcessEngineConfiguration config = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("activiti.cfg.xml");
    ProcessEngine processEngine = config.buildProcessEngine();

    @Test
    public void createProcessEngine() {
//        logger;



 /*                   ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();

        engineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        engineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activeDB?createDatabaseIfNotExist=true" + "&useUnicode=true&characterEncoding=utf8");
        engineConfiguration.setJdbcUsername("root");
        engineConfiguration.setJdbcPassword("root");


        engineConfiguration.setDatabaseSchemaUpdate("true");



        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();*/

        ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
        System.out.println("工作流创建成功");

    }

    @Test
    public void deploy() {
        /*ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
        System.out.println("工作流创建成功");

//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addClasspathResource("diagrams/BillActiviti.bpmn")
                .deploy();*/

     /*   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        System.out.println("工作流创建成功");
        *//**
         * * 获取仓库服务 ：管理流程定义
         */


        RepositoryService repositoryService = processEngine.getRepositoryService();

        /**
         * 创建一个部署的构造器
          */


        Deployment deployment = repositoryService.createDeployment().addClasspathResource("diagrams/BillActiviti.bpmn").name("请假").category("办公类").deploy();
        System.out.println("部署id" + deployment.getId());
        System.out.println("部署name" + deployment.getName());

    }

    @Test
    public void startProcess(){

        //指定执行刚才我们部署的工作流程
        String defaultKey = "BillActiviti";
        //获得运行时runtimeService

        RuntimeService runtimeService = processEngine.getRuntimeService();

        //获得运行的实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(defaultKey);

        System.out.println("流程实例name" + processInstance.getName());

        System.out.println("流程实例id" + processInstance.getId());

    }

    @Test
    public void queryTask() {
        //任务的办理人
        String assignee = "钟福成";
        //获取任务服务
        TaskService taskService = processEngine.getTaskService();

        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();

        //办理人列表任务
        List<Task> tasks = taskQuery.taskAssignee(assignee).list();

        if (tasks != null && tasks.size() > 0) {

            for (Task task :
                    tasks) {
                System.out.println("任务办理人   " + task.getAssignee());
                System.out.println("任务id    " + task.getId());
                System.out.println("任务名称   " + task.getName());

            }
        }
    }

    @Test
    public void compileTask() {
        processEngine.getTaskService().complete("180017");
        System.out.println("任务执行完毕");

    }

    @Test
    public void taskQuery() {
        String assignee = "李大钊";

        //创建任务服务
        TaskService taskService = processEngine.getTaskService();

        //创建查询任务
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> list = taskQuery.taskAssignee(assignee).list();

        if (list != null && list.size() > 0) {
            for (Task task :
                    list) {
                System.out.println("任务名称" + task.getName());
                System.out.println("任务id" + task.getId());
                System.out.println("任务办理人"  + task.getAssignee());
            }
        }
    }

    @Test
    public void getPicture() {

        String deploymentId = "5001";
        String imagineName = null;

        //取得某个部署资源的名称
        List<String> resourceNames = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);

        if (resourceNames != null && resourceNames.size() > 0) {
            for (String resourceName: resourceNames
                 ) {
                System.out.println("----------------------------------------------------------------------------------------------->>>" + resourceName);
                if (resourceName.indexOf(".png") > 0) {

                    imagineName = resourceName;
                }

            }
        }

        InputStream resourceAsStream = processEngine.getRepositoryService().getResourceAsStream(deploymentId, imagineName);

        File file = new File("d:/" + imagineName);
        try {
            FileUtils.copyInputStreamToFile(resourceAsStream, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 查看定义
     */
    @Test
    public void queryProcessDefinition() {

        String processDefineKey = "BillActiviti";

        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery()
//                .processDefinitionVersion()
//                .processDefinitionId(prodesId)
                //
                //查询 ，好比where
//		.processDefinitionId(proDefiId) //流程定义id
                // 流程定义id  ： buyBill:2:704   组成 ： proDefikey（流程定义key）+version(版本)+自动生成id
//                .processDefinitionKey(processDefiKey)//流程定义key 由bpmn 的 process 的  id属性决定
//		.processDefinitionName(name)//流程定义名称  由bpmn 的 process 的  name属性决定
//		.processDefinitionVersion(version)//流程定义的版本
                .latestVersion()//最新版本

                //排序
                .orderByProcessDefinitionVersion().desc()//按版本的降序排序

                //结果
//		.count()//统计结果
//		.listPage(arg0, arg1)//分页查询
//                .list();
//                .processDefinitionKey(processDefineKey)
//                .latestVersion()
//                .orderByProcessDefinitionVersion().desc()
                .list();

        if (list != null && list.size() > 0) {
            for (ProcessDefinition processDefinition :
                    list) {
                System.out.println("流程定义的id" + processDefinition.getId());
                System.out.println("流程定义的name" + processDefinition.getName());
                System.out.println("流程定义的key" + processDefinition.getKey());
                System.out.println("流程定义的版本" + processDefinition.getVersion());
                System.out.println("流程定义的部署的id" + processDefinition.getDeploymentId());

            }
        }
    }

    @Test
    public void zipTest() {

        InputStream is = getClass().getClassLoader().getResourceAsStream("BuyBill.zip");



        Deployment deployment = processEngine.getRepositoryService().createDeployment().addZipInputStream(new ZipInputStream(is)).name("name")
                .deploy();

        System.out.println("id" + deployment.getName() + deployment.getId() + deployment.getCategory());


    }

    @Test
    public void deleteProcessDefine() {

        processEngine.getRepositoryService().deleteDeployment("32503", true);

    }

    @Test
    public void startProcess2() {

        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("BillActiviti");

        System.out.println("流程执行对象id" + pi.getId());
        System.out.println("流程实例的id" + pi.getProcessInstanceId());
        System.out.println("流程定义的id" + pi.getName());

    }

    @Test
    public void queryTaskRuntime() {

        List<Task> list = processEngine.getTaskService().createTaskQuery().list();

        if (list != null && list.size() > 0) {
            for (Task li : list
                    ) {
                System.out.println("任务的办理人" + li.getAssignee());
                System.out.println("任务的id" + li.getId());
                System.out.println("任务的名称" + li.getName());

            }
        }
    }

    @Test
    public void getProcessStatus() {

        String processInstanceId = "32501";

        ProcessInstance pi = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId("22504").singleResult();

        if (pi != null) {
            System.out.println("该实例" + processInstanceId + "正在运行" + "当前活动的任务" + pi.getActivityId());
        } else {
            System.out.println("当前流程实例" + processInstanceId + "已经结束");

        }
    }

    @Test
    public void getHistoryInstance() {

        List<HistoricProcessInstance> historicProcessInstances = processEngine.getHistoryService().createHistoricProcessInstanceQuery().list();

        if (historicProcessInstances != null && historicProcessInstances.size() > 0) {
            for (HistoricProcessInstance hs : historicProcessInstances
                    ) {
                System.out.println("历史流程实例id"+hs.getId());
                System.out.println("历史流程定义id"+hs.getProcessDefinitionId());
                System.out.println(hs.getStartTime()+"时间"+hs.getEndTime());
            }
        }
    }

    @Test
    public void queryHistoricTask() {
        String processInstances = "10004";
        List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(processInstances)
                .list();

        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance hi : list
                    ) {
                System.out.println("历史流程实例id"+hi.getId());
                System.out.println("历史流程定义id"+hi.getProcessDefinitionId());
                System.out.println("任务处理人：" + hi.getAssignee());
            }
        }
    }






}
