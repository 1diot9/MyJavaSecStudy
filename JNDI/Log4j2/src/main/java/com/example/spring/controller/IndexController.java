package com.example.spring.controller;

import com.example.App;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    private static final Logger logger = LogManager.getLogger(IndexController.class);

    @RequestMapping("/")
    @ResponseBody
    public String index(@RequestParam(value = "name", defaultValue = "World") String name, HttpServletRequest request) {
        Configurator.setLevel("com.example.spring.controller.IndexController", Level.DEBUG);

        String referer = request.getHeader("Referer");
        logger.error("Referer: {}", referer);
        String header = request.getHeader("User-Agent");
        logger.error("User-Agent: {}", header);
//        logger.error("Hello, {}", name);
        return String.format("Hello, %s!", name);
    }
}
