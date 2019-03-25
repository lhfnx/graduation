package com.dhu.port.repository;

import com.dhu.port.entity.CrawlerForWeiBo;

import java.time.LocalDateTime;
import java.util.List;

public interface WeiBoRepository {
    List<CrawlerForWeiBo> queryAll();

    List<CrawlerForWeiBo> queryByPages(Integer offset, Integer rows);

    List<CrawlerForWeiBo> queryByPagesWithKeyWord(String keys, Integer offset, Integer rows);

    List<String> queryKeyWords();

    CrawlerForWeiBo queryById(Long id);

    List<CrawlerForWeiBo> queryForCache();

    Long queryCount(String keys);

    List<CrawlerForWeiBo> queryHot(LocalDateTime today);

    int batchInsertCrawler(List<CrawlerForWeiBo> crawlers);

    List<String> queryKeyWord(LocalDateTime today);
}
