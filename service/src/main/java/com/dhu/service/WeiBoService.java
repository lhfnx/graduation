package com.dhu.service;

import com.dhu.port.entity.CrawlerForWeiBo;

import java.util.List;

public interface WeiBoService {

    List<CrawlerForWeiBo> getInformationFromCache(Integer num);

    List<CrawlerForWeiBo> getInformationForList(String key, Integer offset, Integer rows);

    CrawlerForWeiBo getInformation(Long id);
}