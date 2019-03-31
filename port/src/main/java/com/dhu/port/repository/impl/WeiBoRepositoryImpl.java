package com.dhu.port.repository.impl;

import com.dhu.common.utils.JsonUtils;
import com.dhu.port.entity.CrawlerForWeiBo;
import com.dhu.port.mapper.CrawlerForWeiBoMapper;
import com.dhu.port.repository.WeiBoRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class WeiBoRepositoryImpl implements WeiBoRepository {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CrawlerForWeiBoMapper weiBoMapper;

    @Override
    public List<CrawlerForWeiBo> queryAll() {
        return weiBoMapper.queryAll();
    }

    @Override
    public List<CrawlerForWeiBo> queryByPages(Integer offset, Integer rows) {
        if (offset == null) {
            offset = 0;
        }
        if (rows == null) {
            rows = 1;
        }
        return weiBoMapper.queryByPages(offset, rows);
    }

    @Override
    public List<String> queryKeyWords() {
        return weiBoMapper.queryKeyWords();
    }

    @Override
    public List<CrawlerForWeiBo> queryByPagesWithKeyWord(String keys, Integer offset, Integer rows) {
        if (offset == null) {
            offset = 0;
        }
        if (rows == null) {
            rows = 1;
        }
        return weiBoMapper.queryByPagesWithKeyWord("%" + keys + "%", offset, rows);
    }

    @Override
    public CrawlerForWeiBo queryById(Long id) {
        if (id == null) {
            return null;
        }
        return weiBoMapper.queryById(id);
    }

    @Override
    public List<CrawlerForWeiBo> queryForCache() {
        return weiBoMapper.queryForCache();
    }

    @Override
    public Long queryCount(String keys) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("keys", keys);
        map.put("table", "crawler_weibo");
        return weiBoMapper.queryCountByKeys(map);
    }

    @Override
    public List<CrawlerForWeiBo> queryHot(LocalDateTime today) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = today.minusDays(1L).format(timeFormatter);
        return weiBoMapper.queryHot(time);
    }

    @Override
    public int batchInsertCrawler(List<CrawlerForWeiBo> crawlers) {
        if (CollectionUtils.isEmpty(crawlers)) {
            return 0;
        }
        int count = 0;
        for (CrawlerForWeiBo weiBo : crawlers) {
            try {
                int id = weiBoMapper.insertCrawler(weiBo);
                if (id > 0) {
                    count++;
                }
            } catch (Exception e) {
                logger.error("微博爬虫存储失败", "数据：" + JsonUtils.serialize(weiBo), e);
            }
        }
        return count;
    }

    @Override
    public List<String> queryKeyWord(LocalDateTime today, String keys) {
        Map<String, Object> map = Maps.newHashMap();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = today.minusDays(1L).format(timeFormatter);
        map.put("keys", keys);
        map.put("table", "crawler_weibo");
        map.put("today", time);
        return weiBoMapper.queryKeyWord(map);
    }

    @Override
    public List<CrawlerForWeiBo> queryByPagesWithCondition(String keys, Integer offset, Integer rows) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("keys", keys);
        map.put("offset", offset.toString());
        map.put("rows", rows.toString());
        map.put("table", "crawler_weibo");
        return weiBoMapper.queryByPagesWithCondition(map);
    }
}
