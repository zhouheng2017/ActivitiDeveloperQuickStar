package com.example.activiti.controller;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-11
 * @Time: 9:22
 */
@Controller
public class CreateUserAndGroup {
    public CreateUserAndGroup() {
    }

    public ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    public IdentityService identityService = processEngine.getIdentityService();

//    ProcessEngines.getDefaultProcessEngine()

    /**
     * 创建用户
     * @param user
     * @return
     */
    public User createUser(String user) {

//        IdentityService identityService = activitiRule.getIdentityService();
        identityService.deleteUser(user);
        User jackChen = identityService.newUser(user);
        jackChen.setLastName("chen2");
        jackChen.setFirstName("jack2");
        jackChen.setEmail("sfdsd@163.com");

        identityService.saveUser(jackChen);
        return jackChen;
    }

    /**
     * 创建组
     * @param group
     * @return
     */
    public Group createGroup(String group) {
        identityService.deleteGroup("deptLeader");
        Group deptLeader = identityService.newGroup(group);
        deptLeader.setName("部门领导");
        deptLeader.setType("assignment");
        identityService.saveGroup(deptLeader);

        return deptLeader;
    }

    /**
     * 用户存储到组中
     *
     * @param user
     * @param group
     */
    public void saveUserToGroup(User user, Group group) {
//        identityService.deleteMembership("jinyi", "deptLeader");
        identityService.createMembership(user.getId(), group.getId());

    }
}
