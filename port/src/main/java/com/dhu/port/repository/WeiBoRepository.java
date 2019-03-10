package com.dhu.port.repository;

import com.dhu.port.entity.CrawlerForWeiBo;

import java.util.List;

public interface WeiBoRepository {
    List<CrawlerForWeiBo> queryAll();

    List<CrawlerForWeiBo> queryByPages(Integer offset, Integer rows);
}
