package com.dhu.port.repository;

import com.dhu.port.entity.CrawlerForTouTiao;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 头条表
 */
public interface TouTiaoRepository {
    List<CrawlerForTouTiao> queryAll();

    List<CrawlerForTouTiao> queryByPages(Integer offset, Integer rows);

    List<CrawlerForTouTiao> queryByPagesWithKeyWord(String keys, Integer offset, Integer rows);

    List<String> queryKeyWords();

    /**
     * 预读
     * @return
     */
    List<CrawlerForTouTiao> queryForCache();

    /**
     * 根据ID
     * @param id
     * @return
     */
    CrawlerForTouTiao queryById(Long id);

    /**
     * 根据Key获取数量
     * @param keys
     * @return
     */
    Long queryCount(String keys);

    /**
     * 热文
     * @param today
     * @return
     */
    List<CrawlerForTouTiao> queryHot(LocalDateTime today);

    /**
     * 动态条件搜索
     * @param keys
     * @param offset
     * @param rows
     * @return
     */
    List<CrawlerForTouTiao> queryByPagesWithCondition(String keys, Integer offset, Integer rows);

    /**
     * 统计关键词
     * @param today
     * @param keys
     * @return
     */
    List<String> queryKeyWord(LocalDateTime today,String keys);
}
