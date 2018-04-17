package com.example.activiti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description: 关于activiti的的相关事宜
 * @Date: 2018-04-03
 * @Time: 9:25
 */
@Controller
@RequestMapping(value = "/workflow")
public class ActivitiController {

    /**
     * 流程的定义列表
     * @return
     */
    @RequestMapping("/processList2")
    public ModelAndView processList() {
        ModelAndView modelAndView = new ModelAndView("processList");
        List<Object> objects = new ArrayList<Object>();
        modelAndView.addObject("objects", objects);
        return modelAndView;
    }

    /**
     * 部署全部流程
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/redeploy/all")
    public String reployAll() throws Exception {

        return "redirect:/workflow/process-list";
    }
}
