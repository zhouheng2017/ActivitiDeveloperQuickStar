package com.example.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-10
 * @Time: 16:04
 */
public class CreateTaskListener implements TaskListener {
    private Expression content;
    private Expression task;

    public void notify(DelegateTask delegateTask) {
        System.out.println(task.getValue(delegateTask));
        delegateTask.setVariable("setInTaskCreate", delegateTask.getEventName() + ", " + content.getValue(delegateTask));
        System.out.println(delegateTask.getEventName() + ", 任务分配给：" + delegateTask.getAssignee());

        delegateTask.setAssignee("user");

    }
}
