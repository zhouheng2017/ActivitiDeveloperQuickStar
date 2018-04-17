package com.example.activiti.chapter7.service;

import com.example.activiti.chapter7.entity.Leave;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-17
 * @Time: 16:14
 */
@Service
public class LeaveWorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveWorkflowService.class);

    @Autowired
    LeaveManager leaveManager;

    @Autowired
    IdentityService identityService;

    @Autowired
    RuntimeService runtimeService;

    /**
     * 启动工作流并返回i流程实例
     * @param entity
     * @param userId
     * @param variable
     * @return
     */
    public ProcessInstance startWorkflow(Leave entity, String userId, Map<String, Object> variable) {

        //如果实体为空
        if (entity.getId() == null) {
            entity.setApplyTime(new Date());
            entity.setUserId(userId);
        }

        //保存实体
        leaveManager.save(entity);

        //设置启动流程的人员id，
        identityService.setAuthenticatedUserId(userId);

        String businessKey = entity.getId().toString();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", businessKey, variable);

        String processInstanceId = processInstance.getId();

        entity.setProcessInstanceId(processInstanceId);
        logger.debug("start process of  {key={}, bkey={}, pid={}, variables={}}", new Object[]{"leave", businessKey, processInstanceId, variable});

        leaveManager.save(entity);
        return processInstance;
    }
}
