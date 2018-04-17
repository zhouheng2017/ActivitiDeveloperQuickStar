package com.example.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-10
 * @Time: 16:01
 */
public class ProcessStartExecutionListener implements ExecutionListener {
    public void notify(DelegateExecution delegateExecution) throws Exception {
        delegateExecution.setVariable("setInStartListener", true);
        System.out.println(this.getClass().getSimpleName() + ", " + delegateExecution.getEventName());

    }
}
