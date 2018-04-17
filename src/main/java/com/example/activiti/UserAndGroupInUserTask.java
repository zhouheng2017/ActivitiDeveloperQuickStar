package com.example.activiti;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-02
 * @Time: 13:49
 */
public class UserAndGroupInUserTask {

    @Rule
    public static ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 设置两个用户，并分配到一个用户组当中
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //初始化7个service对象

        //创建组并保存
        Group deptLeader = activitiRule.getIdentityService().newGroup("deptLeader");
        deptLeader.setName("部门领导");
        deptLeader.setType("assignment");
        activitiRule.getIdentityService().saveGroup(deptLeader);

        //再添加一个用户

        IdentityService identityService = activitiRule.getIdentityService();
        User jackChen = identityService.newUser("jackChen");
        jackChen.setLastName("chen2");
        jackChen.setFirstName("jack2");
        jackChen.setEmail("sfdsd@163.com");

//        identityService.deleteUser("jackChen");
//
        identityService.saveUser(jackChen);

        //将新建的用户添加到组当中
        identityService.createMembership("jackChen", "deptLeader");


        //创建用户并保存
        User user = activitiRule.getIdentityService().newUser("user2");
        user.setFirstName("heng");
        user.setLastName("zhou");
        user.setEmail("zhouheng@163.com");
        activitiRule.getIdentityService().saveUser(user);

        //将用户与组关联起来

        activitiRule.getIdentityService().createMembership("user2", "deptLeader");


        //创建两个候选人
        User jack = identityService.newUser("jack");
        identityService.saveUser(jack);
        User zhouheng = identityService.newUser("zhouheng");
        identityService.saveUser(zhouheng);

    }


    public void afterInvokeTestMethod() throws Exception {
        //删除数据
        activitiRule.getIdentityService().deleteMembership("user", "deptLeader");
        activitiRule.getIdentityService().deleteUser("user");
        activitiRule.getIdentityService().deleteGroup("deptLeader");
        activitiRule.getIdentityService().deleteUser("jackChen");
        activitiRule.getIdentityService().deleteUser("jack");
        activitiRule.getIdentityService().deleteUser("zhouheng");

    }

    /**
     * 启动流程，一个候选组中的人可以签收任务
     * @throws Exception
     */
    @Test
    @Deployment(resources = {"diagrams/userandgroup/UserAndGroup.bpmn"})
    public void testUserAndGroupInUserTask() throws Exception {

        //启动路程通过Key
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("UserAndGroupInUserTask");
        System.out.println("###########################ProcessInstanceId" + processInstance.getId());

        //查询id为user的任务列表
//        user接受任务
//        完成任务
        Task task = activitiRule.getTaskService().createTaskQuery().taskCandidateUser("user").singleResult();
        activitiRule.getTaskService().claim(task.getId(), "user");
        activitiRule.getTaskService().complete(task.getId());

    }


    /**
     * 候选组中有两个成员，每个成员都可以查询到任务列表，当其中某一个人签收任务后，另外一个候选人将没有需要处理的任务
     * @throws Exception
     */
    @Test
    @Deployment(resources = {"diagrams/userandgroup/UserAndGroup.bpmn"})
    public void testUserAndGroupContainTwoUser() throws Exception {


        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("UserAndGroupInUserTask");
        assertNotNull(processInstance);

        //没有签收前两者都可以查询到任务
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateUser("jackChen").singleResult();
        assertNotNull(task);

        Task task1 = taskService.createTaskQuery().taskCandidateUser("user").singleResult();
        assertNotNull(task1);

        //签收任务
        taskService.claim(task.getId(), "jackChen");

        //完成任务
        taskService.complete(task.getId());

        Task task2 = taskService.createTaskQuery().taskCandidateUser("user").singleResult();
        assertNull(task2);

    }

    /**
     * 一个任务可以指定多个候选人
     */
    @Test
    @Deployment(resources = {"diagrams/userandgroup/UserAndGroup.bpmn"})
    public void testCandidateTwoUser() {

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        //启动流程

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("UserAndGroupInUserTask");
        assertNotNull(processInstance);

        //获取taskService的服务
        TaskService taskService = activitiRule.getTaskService();
        //判断两个候选人都可以获取任务
        Task task = taskService.createTaskQuery().taskCandidateUser("zhouheng").singleResult();
        assertNotNull(task.getId());

        Task task1 = taskService.createTaskQuery().taskCandidateUser("jack").singleResult();
        assertNotNull(task1);

        // zhouheng签收任务并完成
        taskService.claim(task.getId(), "zhouheng");
        taskService.complete(task.getId());

        //此时jack中没有任务
        Task task2 = taskService.createTaskQuery().taskCandidateUser("jack").singleResult();
        assertNull(task2);

    }

    /**
     * 以类的形式部署流程定义
     */
    @Test
    public void testClasspathDeployment() {

        //构建部署构造器
        DeploymentBuilder deploymentBuilder = activitiRule.getRepositoryService().createDeployment();
        //定义classpath
        String classpath = "diagrams/userandgroup/UserAndGroup.bpmn";

        //部署流程定义
        org.activiti.engine.repository.Deployment deploy = deploymentBuilder.addClasspathResource(classpath).deploy();

        //查询流程定义
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        long userAndGroupInUserTask = processDefinitionQuery.processDefinitionKey("UserAndGroupInUserTask").count();


        //判断部署是否成功
        assertEquals(1, userAndGroupInUserTask);



        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();

        //获取流程定义的名字
        String resourceName = processDefinition.getResourceName();

        assertEquals(classpath, resourceName);

    }

    /**
     * 以绝对不经的方式部署流程定义
     */
    @Test
    public void testDeployByInputStream() throws FileNotFoundException {

        //获取文件的绝对路径
        String absolutePath = "E:\\maven\\workspace\\java2\\ActivitiDeveloperQuickStar\\src\\main\\resources\\diagrams\\userandgroup\\UserAndGroup.bpmn";

        //以流的形式读入文件
        FileInputStream fileInputStream = new FileInputStream(new File(absolutePath));

        //以流的形式发布流程定义
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        org.activiti.engine.repository.Deployment deploy = repositoryService.createDeployment().addInputStream("UserAndGroup.bpmn", fileInputStream).deploy();

        List<ProcessDefinition> userAndGroupInUserTask = repositoryService.createProcessDefinitionQuery().processDefinitionKey("UserAndGroupInUserTask").list();

        if (userAndGroupInUserTask != null) {
            for (ProcessDefinition pd : userAndGroupInUserTask
                    ) {

                String resourceName = pd.getResourceName();

                System.out.println("#####################"+resourceName);
            }
        }


    }

    /**
     * 可以将文件打包，一次部署多个流程
     */
    @Test
    public void testZipInputDeployment() {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("xxx.zip");

        //一次性部署多个流程
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        org.activiti.engine.repository.Deployment deploy = repositoryService.createDeployment().addZipInputStream(new ZipInputStream(inputStream)).deploy();

    }
}
