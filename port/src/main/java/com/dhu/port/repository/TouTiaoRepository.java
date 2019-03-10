package com.dhu.port.repository;

import com.dhu.port.entity.CrawlerForTouTiao;

import java.util.List;

public interface TouTiaoRepository {
    List<CrawlerForTouTiao> queryAll();

    List<CrawlerForTouTiao> queryByPages(Integer offset, Integer rows);

    List<CrawlerForTouTiao> queryByPagesWithKeyWord(String keys, Integer offset, Integer rows);

    List<String> queryKeyWords();

    List<CrawlerForTouTiao> queryForCache();

    CrawlerForTouTiao queryById(Long id);
}
