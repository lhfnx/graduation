package com.dhu.service.impl;

import com.dhu.port.entity.CrawlerForWeiBo;
import com.dhu.port.repository.WeiBoRepository;
import com.dhu.service.CacheSrevice;
import com.dhu.service.WeiBoService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WeiBoServiceImpl implements WeiBoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WeiBoRepository weiBoRepository;

    @Autowired
    private CacheSrevice cacheSrevice;

    @Override
    public List<CrawlerForWeiBo> getInformationFromCache(Integer num) {
        List<CrawlerForWeiBo> weiBos = cacheSrevice.getWeiBoCache();
        if (CollectionUtils.isEmpty(weiBos) || num > weiBos.size()) {
            return weiBos;
        }
        return weiBos.subList(0, num);
    }


    @Override
    public List<CrawlerForWeiBo> getInformationForList(String key, Integer offset, Integer rows) {
        if (StringUtils.isEmpty(key)) {
            return weiBoRepository.queryByPages(offset, rows);
        } else {
            return weiBoRepository.queryByPagesWithKeyWord(key, offset, rows);
        }
    }

    @Override
    public CrawlerForWeiBo getInformation(Long id) {
        if (id != null) {
            return weiBoRepository.queryById(id);
        }
        return null;
    }
}
