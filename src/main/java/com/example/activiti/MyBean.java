package com.example.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

import java.io.Serializable;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-10
 * @Time: 15:26
 */
public class MyBean implements Serializable {
    private static final long serialVersionUID = -5900615502054822093L;

    public void print() {
        System.out.println("print content by print()");

    }

    public String print(String name) {
        System.out.println("print content by print(String name), value is:" + name);
        return name += ", added by print(String name)";
    }

    public void invokeTask(DelegateTask task) {
//        接收名字为task的引擎内置变量
        task.setVariable("setByTask", "I'm setted by DelegateTask, " + task.getVariable("name"));

    }

    public void printBKey(DelegateExecution execution) {

        String processBusinessKey = execution.getProcessBusinessKey();
        System.out.println("process instance id:" + execution.getProcessInstanceId() + " , business key" + processBusinessKey);

//        接收名字为execution的引擎内置变量

    }
}
