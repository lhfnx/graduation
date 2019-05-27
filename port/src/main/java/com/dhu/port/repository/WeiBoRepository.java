package com.dhu.port.repository;

import com.dhu.port.entity.CrawlerForWeiBo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface WeiBoRepository {
    List<CrawlerForWeiBo> queryAll();

    List<CrawlerForWeiBo> queryByPages(Integer offset, Integer rows);

    List<CrawlerForWeiBo> queryByPagesWithKeyWord(String keys, Integer offset, Integer rows);

    List<String> queryKeyWords();

    /**
     * 根据ID
     * @param id
     * @return
     */
    CrawlerForWeiBo queryById(Long id);

    /**
     * 预读
     * @return
     */
    List<CrawlerForWeiBo> queryForCache();

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
    List<CrawlerForWeiBo> queryHot(LocalDateTime today);

    /**
     * 批量插入
     * @param crawlers
     * @return
     */
    int batchInsertCrawler(List<CrawlerForWeiBo> crawlers);

    /**
     * 统计关键词
     * @param today
     * @param keys
     * @return
     */
    List<String> queryKeyWord(LocalDateTime today, String keys);

    /**
     * 动态条件搜索
     * @param keys
     * @param offset
     * @param rows
     * @return
     */
    List<CrawlerForWeiBo> queryByPagesWithCondition(String keys, Integer offset, Integer rows);
}
