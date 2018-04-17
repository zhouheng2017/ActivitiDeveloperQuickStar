package com.example.activiti;

import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.FormEngine;

import javax.swing.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description: 实现自己的表单引擎
 * @Date: 2018-04-10
 * @Time: 9:37
 */
public class MyFormEngine implements FormEngine {
    public String getName() {
        return "myForEngine";
    }

    /**
     * 生成启动流程表单
     * @param startFormData
     * @return
     */
    public Object renderStartForm(StartFormData startFormData) {
        JButton jButton = new JButton();
        jButton.setName("My Start From Button");

        return jButton;
    }

    /**
     * 生成用户任务表单
     * @param taskFormData
     * @return
     */
    public Object renderTaskForm(TaskFormData taskFormData) {
        JButton jButton = new JButton();
        jButton.setName("My Task Form Button");

        return jButton;
    }
}
