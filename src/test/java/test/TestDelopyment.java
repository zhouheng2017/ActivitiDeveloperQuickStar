package test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-03-28
 * @Time: 10:56
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activitiContent.xml")
public class TestDelopyment {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    @Rule
    public ActivitiRule activitiRule;

    @Test
    @Deployment
    public void simpleProcessTest() {
        runtimeService.startProcessInstanceByKey("BillActiviti");
        List<Task> list = taskService.createTaskQuery().list();
        Task task = (Task) list;
        System.out.println(task.getName());

        taskService.complete(task.getId());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());

    }
}
