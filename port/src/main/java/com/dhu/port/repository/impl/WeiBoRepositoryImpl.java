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
        return weiBoMapper.queryByPages(offset, rows);
    }
}
