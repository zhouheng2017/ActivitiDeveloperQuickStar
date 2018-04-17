package com.example.activiti.controller;

import jodd.io.http.Http;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description: 发布的controller
 * @Date: 2018-04-03
 * @Time: 10:51
 */

@Controller
@RequestMapping("/workflow")
public class DeploymentController {

    private static final Logger logger = LoggerFactory.getLogger(DeploymentController.class);

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    FormService formService;

    @Autowired
    IdentityService identityService;

    @Autowired
    TaskService taskService;

    /**
     * 获取定义流程
     *
     * @return
     */

    @RequestMapping("/processList")
    public ModelAndView processList() {

        //获取流程定义
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().desc()
                .list();
        ModelAndView modelAndView = new ModelAndView("processList");
        //返回流程定义
        modelAndView.addObject("processDefinitions", list);


        return modelAndView;
    }

    /**
     * 从上传的文件中发布流程
     */
    @RequestMapping("/deploy")
    public String deployByInput(@RequestParam(value = "file", required = false) MultipartFile multipartFile) {


        //获得上传的文件名名
        InputStream inputStream = null;
        String fileName = multipartFile.getOriginalFilename();
        try {
            //获得deployment的捕鼠器
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

            //获得文件的输入流
            inputStream = multipartFile.getInputStream();
            System.out.println("-===================++++++++++++++>>>>>>>>" + fileName);

            //获得文件的扩展名利用FilenameUtils的工具包
            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                //获得zip文件的数据如流
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);

                //发布资源
                deploymentBuilder.addZipInputStream(zipInputStream);
            } else {
                deploymentBuilder.addInputStream(fileName, inputStream);
            }

            //部署，获得部署的对象信息
            Deployment deploy = deploymentBuilder.deploy();

        } catch (IOException e) {
            logger.error("error on deployment process,{}", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return "redirect:/workflow/processList";
    }

    /**
     * 查看资源
     *
     * @return
     */
    @RequestMapping("/readResource")
    public void readResource(@RequestParam(value = "pdid", required = false) String processDefinitionId,
                             @RequestParam(value = "resourceName", required = false) String resourceName,
                             HttpServletResponse response) throws Exception {

        //获取流程定义查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //通过流程定义id查询流程定义
        ProcessDefinition processDefinition = processDefinitionQuery.processDefinitionId(processDefinitionId).singleResult();

        //通过资源名字和部署id获取图片输入流
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);

        byte[] b = new byte[1024];

        int len;
        while ((len = resourceAsStream.read(b)) != -1) {
            //返回图片
            response.getOutputStream().write(b, 0, len);
            response.getOutputStream().flush();

        }


    }

    /**
     * 删除流程的定义
     *
     * @return 重定向页面
     */
    @RequestMapping(value = "/deleteDeployment/{deploymentId}")
    public String deleteProcessDefinition(@PathVariable(value = "deploymentId") String deploymentId) {
        //删除流程的定义
        repositoryService.deleteDeployment(deploymentId, true);

        return "redirect:/workflow/processList";
    }

    /**
     * 获取启动流程的表单字段
     *
     * @return
     */
    @RequestMapping("/getForm/{processDefinitionId}")
    public ModelAndView readStartForm(@PathVariable("processDefinitionId") String processDefinitionId) {

        //获取流程定义实例
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        //判断是否有外置表单
        boolean hasStartFormKey = processDefinition.hasStartFormKey();

        //判断需要读取那个视图
        ModelAndView modelAndView = new ModelAndView("startProcessForm");
        if (hasStartFormKey) {
            //获取外置表单判断
            Object renderedForm = formService.getRenderedStartForm(processDefinitionId);
            modelAndView.addObject("startFormData", renderedForm);
            modelAndView.addObject("processDefinition", processDefinition);
        } else {
            //获取动态表单
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            modelAndView.addObject("startFormData", startFormData);

        }
        modelAndView.addObject("processDefinitionId", processDefinitionId);
        modelAndView.addObject("hasStartFormKey", hasStartFormKey);
        return modelAndView;




       /* ModelAndView modelAndView = new ModelAndView("startProcessForm");
        //获取表单的字段
        StartFormData startFormData = formService.getStartFormData(processDefinitionId);

        Map<String, String> variable = new HashMap<String, String>();

        modelAndView.addObject("startFormData", startFormData);
        modelAndView.addObject("processDefinitionId", processDefinitionId);
        return modelAndView;
*/

    }

    /**
     * 开始启动流程定义
     *
     * @param processDefinitionId
     * @param request
     * @param map
     * @return
     */
    @RequestMapping("/startProcessDefine/{processDefinitionId}")
    public ModelAndView startProcessDefinition(@PathVariable("processDefinitionId") String processDefinitionId, HttpServletRequest request, Map<String, Object> map) {

//        获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
//        判断流程定义是否为外置表单
        boolean hasStartFormKey = processDefinition.hasStartFormKey();

        Map<String, String> formVariable = new HashMap<String, String>();
        if (hasStartFormKey) {//外置表单
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet) {
                String key = entry.getKey();
//
                if (StringUtils.defaultString(key).startsWith("fp_")) {
                    String[] paramSplit = key.split("_");
                    formVariable.put(paramSplit[1], entry.getValue()[0]);
                    System.out.println("KEY---------------------->" + paramSplit[1] +"___" +entry.getValue()[0]);
                }

//                formVariable.put(key, entry.getValue()[0]);
//                System.out.println("KEY---------------------->" + key + entry.getValue()[0]);
            }
        } else {

            //获取表单的字段，再根据表单字段的ID读取请求参数
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);

            //请求中获取表单中的字段的值
            List<FormProperty> formProperties = startFormData.getFormProperties();

            for (FormProperty formProperty : formProperties) {
//            String value = request.getParameter(formProperty.getId());
//            session域中 获取参数
                String val = request.getParameter(formProperty.getId());
                System.out.println("formPformProperty.getId(), valformProperty.getId(), valroperty.getId(), val" + formProperty.getId() + val);
                formVariable.put(formProperty.getId(), val);
            }
        }
        //获取当前登录的用户
//        request.getSession().setAttribute("user", "user" );


        User user = UserUtil.getUserFormSession(request.getSession());
//        User user = "user";
        identityService.setAuthenticatedUserId("user");

        ProcessInstance processInstance = formService.submitStartFormData(processDefinitionId, formVariable);

//        ModelAndView modelAndView = new ModelAndView("startedProcess");
        ModelAndView modelAndView = new ModelAndView("redirect:/workflow/myTask");

        modelAndView.addObject("processInstance", processInstance);
        modelAndView.addObject("processInstanceId", processDefinitionId);

        return modelAndView;

    }

    /**
     * 获取自己的任务列表
     *
     * @return
     */
    @RequestMapping("/myTask")
    public ModelAndView getTask(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView("taskLists");

        User user = UserUtil.getUserFormSession(session);
        //直接读取当前分配的任务，或者是已经签收的任务
        List<Task> doingTasks = taskService.createTaskQuery().taskAssignee(user.getId()).list();
        //需要签收的任务
        List<Task> waitingClaimTasks = taskService.createTaskQuery().taskCandidateUser(user.getId()).list();

        //所有的任务，签收的为签收的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();

        //全部的任务
        List<Task> lists = new ArrayList<Task>();
        lists.addAll(doingTasks);
        lists.addAll(waitingClaimTasks);

//        System.out.println("+++++++++" + tasks + user.getId() + new Date());

        modelAndView.addObject("tasks", tasks);

        return modelAndView;
    }

    /**
     * 获取任务的表单
     *
     * @param taskId
     * @return
     */
    @RequestMapping("/task/getForm/{taskId}")
    public ModelAndView getForm(@PathVariable("taskId") String taskId) {

        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        ModelAndView modelAndView = new ModelAndView("taskForm");

        //若任务表单有外置表
        if (taskFormData.getFormKey() != null) {
//            获取外置表单
            Object renderedStartForm = formService.getRenderedTaskForm(taskId);
            //通过taskId查询任务
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            modelAndView.addObject("task", task);
            modelAndView.addObject("taskFormData", renderedStartForm);
            //外置表单
            modelAndView.addObject("hasFormKey", true);
        } else {

            modelAndView.addObject("taskFormData", taskFormData);
        }


        return modelAndView;
    }


    /**
     * 签收任务
     *
     * @return
     */
    @RequestMapping("/claim/{id}")
    public String claim(@PathVariable("id") String taskId, HttpSession session) {
        //签收任务
        taskService.claim(taskId, UserUtil.getUserFormSession(session).getId());

        return "redirect:/workflow/myTask";
    }


    /**
     * 完成任务
     *
     * @param taskId
     * @param request
     * @return
     */
    @RequestMapping("/task/complete/{taskId}")
    public ModelAndView completeTask(@PathVariable("taskId") String taskId, HttpSession session, HttpServletRequest request) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        System.out.println("task.getFormKey()task.getFormKey()task.getFormKey()task.getFormKey() "+task.getFormKey());
        Map<String, String> formVariable = new HashMap<String, String>();
        if (task.getFormKey() != null) {//外置表单

            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet) {
                String key = entry.getKey();

                if (StringUtils.defaultString(key).startsWith("fp_")) {
                    String[] paramSplit = key.split("_");
                    formVariable.put(paramSplit[1], entry.getValue()[0]);
                    System.out.println("KEY----COMPLETE------------->" + paramSplit[1] +"___" +entry.getValue()[0]);

                }

//                formVariable.put(key, entry.getValue()[0]);
            }
        } else {


            TaskFormData taskFormData = formService.getTaskFormData(taskId);
            List<FormProperty> formProperties = taskFormData.getFormProperties();

            //从请求中获取属性的值
//            Map<String, String> formVariable = new HashMap<String, String>();
            for (FormProperty formProperty : formProperties) {
                if (formProperty.isWritable()) {
                    String attribute = request.getParameter(formProperty.getId());
                    formVariable.put(formProperty.getId(), attribute);

                }
            }
        }
        formService.submitTaskFormData(taskId, formVariable);

        return new ModelAndView("redirect:/workflow/myTask");
    }



//   这个是跳转的控制
  /*  @RequestMapping("/logout")
    public ModelAndView logout() {

        return new ModelAndView("logout");
    }*/
    @RequestMapping("/index2")
    public ModelAndView index() {

        return new ModelAndView("index");
    }



}
