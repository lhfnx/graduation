package com.dhu.service;

import com.dhu.port.entity.CrawlerForTouTiao;

import java.util.List;

public interface TouTiaoService {

    List<CrawlerForTouTiao> getInformationFromCache(Integer num);

    List<CrawlerForTouTiao> getInformationForList(String key, Integer offset, Integer rows);

    CrawlerForTouTiao getInformation(Long id);
}
