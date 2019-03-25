package com.dhu.web.controller;

import com.dhu.crawler.weibo.CrawlerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("crawler")
public class CrawlerController {
    @Autowired
    private CrawlerExecutor executor;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "execute", method = RequestMethod.GET)
    @ResponseBody
    public String executeCrawler() {
        executor.execute();
        return "爬虫运行结束";
    }
}
