package com.dhu.port.repository.impl;

import com.dhu.port.entity.CrawlerForTouTiao;
import com.dhu.port.mapper.CrawlerForTouTiaoMapper;
import com.dhu.port.repository.TouTiaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
