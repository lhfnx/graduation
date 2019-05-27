package com.dhu.web.controller;

import com.dhu.crawler.toutiao.ToutiaoCrawlerExecutor;
import com.dhu.crawler.weibo.WeiboCrawlerExecutor;
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
    private WeiboCrawlerExecutor weiboCrawlerExecutor;
    @Autowired
    private ToutiaoCrawlerExecutor toutiaoCrawlerExecutor;

    private Logger logger = LoggerFactory.getLogger(getClass());

    //手动运行微博爬虫
    @RequestMapping(value = "weibo/execute", method = RequestMethod.GET)
    @ResponseBody
    public String executeWeiboCrawler() {
        weiboCrawlerExecutor.execute();
        return "微博爬虫运行结束";
    }

    //手动运行头条爬虫
    @RequestMapping(value = "toutiao/execute", method = RequestMethod.GET)
    @ResponseBody
    public String executeToutiaoCrawler() {
        toutiaoCrawlerExecutor.execute();
        return "微博爬虫运行结束";
    }
}
