package com.example.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.io.Serializable;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-10
 * @Time: 16:13
 */
public class ProcessEndExecutionListener implements ExecutionListener, Serializable{
    private static final long serialVersionUID = 4511275126900890423L;

    public void notify(DelegateExecution delegateExecution) throws Exception {
        delegateExecution.setVariable("setInEndListener", true);

        System.out.println(this.getClass().getSimpleName() + ", " + delegateExecution.getEventName());
    }
}
