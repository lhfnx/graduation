package com.dhu.port.repository.impl;

import com.dhu.port.entity.CrawlerForWeiBo;
import com.dhu.port.mapper.CrawlerForWeiBoMapper;
import com.dhu.port.repository.WeiBoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class WeiBoRepositoryImpl implements WeiBoRepository {
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
        return weiBoMapper.queryByPagesWithKeyWord(keys, offset, rows);
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
}
