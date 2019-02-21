package com.dhu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("api")
public class TestController {
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
        return "welcome";
    }
}
