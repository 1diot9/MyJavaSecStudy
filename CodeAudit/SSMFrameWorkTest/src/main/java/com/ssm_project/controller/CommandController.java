package com.ssm_project.controller;

import com.ssm_project.service.BookService;
import com.ssm_project.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/command")
public class CommandController {


    @Autowired
    @Qualifier("CommandServiceImpl")
    private CommandService commandService;

    @RequestMapping("/exec")
    public ModelAndView exec(@RequestParam("cmd") String cmd) {
        String result = commandService.exec(cmd);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("result",result);
        modelAndView.setViewName("execResult");
        return modelAndView;
    }

}
