package com.dhu.web.controller;

import com.dhu.port.entity.CrawlerForWeiBo;
import com.dhu.port.repository.WeiBoRepository;
import com.dhu.service.WeiBoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("api")
public class TestController {
    @Autowired
    WeiBoService weiBoService;
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
        List<CrawlerForWeiBo> weiBo = weiBoService.getInformationFromCache(1);
        return "welcome";
    }
}
