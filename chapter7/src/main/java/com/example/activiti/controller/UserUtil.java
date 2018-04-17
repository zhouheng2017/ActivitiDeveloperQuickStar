package com.example.activiti.controller;

import org.activiti.engine.identity.User;

import javax.servlet.http.HttpSession;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description: 工具类
 * @Date: 2018-04-09
 * @Time: 11:21
 */
public class UserUtil {
    public static final String USER = "user";

    /**
     * 设置用户到session
     * @param session
     */
    public static void saveUserToSession(HttpSession session, User user) {
        session.setAttribute(USER, user);

    }

    public static User getUserFormSession(HttpSession session) {
        Object attribute = session.getAttribute(USER);
        return attribute == null ? null : (User) attribute;
    }
}
