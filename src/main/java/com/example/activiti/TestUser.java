package com.example.activiti;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-04-02
 * @Time: 10:07
 */
public class TestUser {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试用户创建和删除的操作
     */
    @Test
    public void testUser() {

        //获取activiti.cfg.xml文件的对象
        IdentityService identityService = activitiRule.getIdentityService();

        //创建用户
        User zhouheng = identityService.newUser("zhouheng");
        zhouheng.setFirstName("heng");
        zhouheng.setLastName("zhou");
        zhouheng.setEmail("15893883880@163.com");

        //存储用户
        identityService.saveUser(zhouheng);

        //查询用户
        User user = identityService.createUserQuery().userId("zhouheng").singleResult();

        assertNotNull(user);

        //删除用户
        identityService.deleteUser("zhouheng");

        User zhouheng1 = identityService.createUserQuery().userId("zhouheng").singleResult();

        assertNull(zhouheng1);
    }

    /**
     * 测试用户组
     */
    @Test
    public void testUserGroup() {

        //获取identityService对象
        IdentityService identityService = activitiRule.getIdentityService();

        //创建用户组
        Group detLeader = identityService.newGroup("detLeader");
        detLeader.setName("领导审批");
        detLeader.setType("assignment");
        
        //将用户组存储到数据库
        identityService.saveGroup(detLeader);
        
        //查询是否创建成功
        List<Group> leader = identityService.createGroupQuery().groupId("detLeader").list();

        assertEquals(1, leader.size());

        //删除用户组
        identityService.deleteGroup("detLeader");

        List<Group> groupList = identityService.createGroupQuery().groupId("detLeader").list();

        assertEquals(0, groupList.size());

    }

    /**
     * 测试用户和组的关系
     */
    @Test
    public void testUserAndGroup() {
        //获取identityService的服务
        IdentityService identityService = activitiRule.getIdentityService();

        //创建用户组
        Group detLeader = identityService.newGroup("detLeader");
        detLeader.setName("部门领导");
        detLeader.setType("assignment");
        identityService.saveGroup(detLeader);

        //创建用户并保存
        User clerk = identityService.newUser("clerk");
        clerk.setLastName("heng");
        clerk.setFirstName("zhou");
        clerk.setEmail("sfdsfd@163.com");
        identityService.saveUser(clerk);

        //关联用户和组
        identityService.createMembership("clerk", "detLeader");

        //查询用户所属的组
        Group clerk1 = identityService.createGroupQuery().groupMember("clerk").singleResult();
        System.out.println("clerk用户所属的组" + clerk1.getId());
        //查询组中的用户
        User detLeader1 = identityService.createUserQuery().memberOfGroup("detLeader").singleResult();
        System.out.println("detLeader中的用户名字" + detLeader1.getId());


    }

    @Test
    public void testString () {
        String[] keys = {"dataPattern"};
        System.out.println("******************" + keys.length);

        for (String key : keys) {
            System.out.println("____________________________&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+key);
        }
    }
}
