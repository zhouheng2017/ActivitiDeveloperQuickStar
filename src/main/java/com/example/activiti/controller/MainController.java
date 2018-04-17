package com.example.activiti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description: 首页控制器
 * @Date: 2018-04-16
 * @Time: 15:14
 */
@Controller
@RequestMapping("/main")
public class MainController {

    @RequestMapping("/index")
    public String index() {
        return "/main/index";
    }


    @RequestMapping("/welcome")
    public String welcome() {
        return "/main/welcome";
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {

        return new ModelAndView("logout");
    }



}
