package com.dhu.port.repository;

import com.dhu.port.entity.CrawlerForWeiBo;

import java.util.List;

public interface WeiBoRepository {
    List<CrawlerForWeiBo> queryAll();

    List<CrawlerForWeiBo> queryByPages(Integer offset, Integer rows);

    List<CrawlerForWeiBo> queryByPagesWithKeyWord(String keys, Integer offset, Integer rows);

    List<String> queryKeyWords();

    CrawlerForWeiBo queryById(Long id);

    List<CrawlerForWeiBo> queryForCache();
}
