package com.example.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.io.Serializable;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-10
 * @Time: 16:20
 */
public class TaskAssigneeListener implements TaskListener, Serializable {
    private static final long serialVersionUID = -4195494712101198335L;

    public void notify(DelegateTask delegateTask) {
        System.out.println("任务分配给：" + delegateTask.getAssignee());

    }
}
