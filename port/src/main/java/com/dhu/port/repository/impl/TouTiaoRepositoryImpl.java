package com.dhu.port.repository.impl;

import com.dhu.port.entity.CrawlerForTouTiao;
import com.dhu.port.mapper.CrawlerForTouTiaoMapper;
import com.dhu.port.repository.TouTiaoRepository;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
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
public class TouTiaoRepositoryImpl implements TouTiaoRepository {
    @Autowired
    private CrawlerForTouTiaoMapper touTiaoMapper;

    @Override
    public List<CrawlerForTouTiao> queryAll() {
        return touTiaoMapper.queryAll();
    }

    @Override
    public List<CrawlerForTouTiao> queryByPages(Integer offset, Integer rows) {
        if (offset == null) {
            offset = 0;
        }
        if (rows == null) {
            rows = 1;
        }
        return touTiaoMapper.queryByPages(offset, rows);
    }

    @Override
    public List<String> queryKeyWords() {
        return touTiaoMapper.queryKeyWords();
    }

    @Override
    public List<CrawlerForTouTiao> queryByPagesWithKeyWord(String keys, Integer offset, Integer rows) {
        if (offset == null) {
            offset = 0;
        }
        if (rows == null) {
            rows = 1;
        }
        return touTiaoMapper.queryByPagesWithKeyWord(keys, offset, rows);
    }

    @Override
    public List<CrawlerForTouTiao> queryForCache() {
        return touTiaoMapper.queryForCache();
    }

    @Override
    public CrawlerForTouTiao queryById(Long id) {
        if (id == null) {
            return null;
        }
        return touTiaoMapper.queryById(id);
    }

    @Override
    public Long queryCount(String keys) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("keys", keys);
        map.put("table", "crawler_toutiao");
        return touTiaoMapper.queryCountByKeys(map);

    }

    @Override
    public List<CrawlerForTouTiao> queryHot(LocalDateTime today) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = today.minusDays(1L).format(timeFormatter);
        return touTiaoMapper.queryHot(time);
    }

    @Override
    public List<CrawlerForTouTiao> queryByPagesWithCondition(String keys, Integer offset, Integer rows) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("keys", keys);
        map.put("offset", offset.toString());
        map.put("rows", rows.toString());
        map.put("table", "crawler_toutiao");
        return touTiaoMapper.queryByPagesWithCondition(map);
    }
}
