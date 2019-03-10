package com.dhu.service;

import com.dhu.port.entity.CrawlerForTouTiao;
import com.dhu.port.entity.CrawlerForWeiBo;
import com.dhu.port.repository.TouTiaoRepository;
import com.dhu.port.repository.WeiBoRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class CacheSrevice implements InitializingBean {
    @Autowired
    private WeiBoRepository weiBoRepository;

    @Autowired
    private TouTiaoRepository touTiaoRepository;

    private List<CrawlerForWeiBo> weiBoCache = Lists.newArrayList();

    private List<CrawlerForTouTiao> touTiaoCache = Lists.newArrayList();

    @Override
    public void afterPropertiesSet() throws Exception {
        weiBoCache.addAll(weiBoRepository.queryForCache());
        touTiaoCache.addAll(touTiaoRepository.queryForCache());
    }

    public void refreshCache() {
        List<CrawlerForWeiBo> weiBos = weiBoRepository.queryForCache();
        List<CrawlerForTouTiao> touTiaos = touTiaoRepository.queryForCache();
        if (!CollectionUtils.isEmpty(weiBos)) {
            weiBoCache = weiBos;
        }
        if (!CollectionUtils.isEmpty(touTiaos)) {
            touTiaoCache = touTiaos;
        }
    }

    public List<CrawlerForTouTiao> getTouTiaoCache() {
        return touTiaoCache;
    }

    public List<CrawlerForWeiBo> getWeiBoCache() {
        return weiBoCache;
    }
}
