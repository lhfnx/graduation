package com.dhu.service.impl;

import com.dhu.port.entity.CrawlerForTouTiao;
import com.dhu.port.repository.TouTiaoRepository;
import com.dhu.service.CacheSrevice;
import com.dhu.service.TouTiaoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class TouTiaoServiceImpl implements TouTiaoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TouTiaoRepository touTiaoRepository;

    @Autowired
    private CacheSrevice cacheSrevice;

    @Override
    public List<CrawlerForTouTiao> getInformationFromCache(Integer num) {
        List<CrawlerForTouTiao> weiBos = cacheSrevice.getTouTiaoCache();
        if (CollectionUtils.isEmpty(weiBos) || num > weiBos.size()) {
            return weiBos;
        }
        return weiBos.subList(0, num - 1);
    }


    @Override
    public List<CrawlerForTouTiao> getInformationForList(String key, Integer offset, Integer rows) {
        if (StringUtils.isEmpty(key)) {
            return touTiaoRepository.queryByPages(offset, rows);
        } else {
            return touTiaoRepository.queryByPagesWithKeyWord(key, offset, rows);
        }
    }

    @Override
    public CrawlerForTouTiao getInformation(Long id) {
        if (id != null) {
            return touTiaoRepository.queryById(id);
        }
        return null;
    }
}
