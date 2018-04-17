package com.example.activiti.controller.identity;

import com.example.activiti.controller.UserUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.security.util.Password;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description: 登录系统的控制
 * @Date: 2018-04-16
 * @Time: 16:01
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    IdentityService identityService;

    /**
     * 通过用户名和密码判断数据库中是否存在相应的用具
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public String login(@RequestParam("username") String userName, @RequestParam("password") String password, HttpSession session) {

        logger.debug("logon request:{username={}, password={}}", userName, password);

        //检查密码是或正确
        boolean checkPassword = identityService.checkPassword(userName, password);

        if (checkPassword) {
            //查询用户是否存在
            User user = identityService.createUserQuery().userId(userName).singleResult();
            UserUtil.saveUserToSession(session, user);
            //查询用户的角色
            List<Group> groupList = identityService.createGroupQuery().groupMember(user.getId()).list();
            session.setAttribute("groups", groupList);

            //存储用户组的名字
            String[] groupName = new String[groupList.size()];
            for (int i = 0; i < groupName.length; i++) {

                groupName[i] = groupList.get(i).getName();
            }
            session.setAttribute("groupNames", ArrayUtils.toString(groupName));
            return "redirect:/main/index";
        } else {
            return "redirect:/login.jsp?error=true";
        }
    }
}
