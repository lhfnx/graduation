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
public class HomeController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/weibo",method = RequestMethod.GET)
    public String weibo(){return "weibo";};

    @RequestMapping(value = "/toutiao",method = RequestMethod.GET)
    public String toutiao(){return "toutiao";};

    @RequestMapping(value = "/analysis",method = RequestMethod.GET)
    public String analysis(){
        return "analysis";
    }
}
