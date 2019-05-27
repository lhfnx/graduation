package com.dhu.port.repository;

import com.dhu.port.entity.CrawlerForTouTiao;
import com.dhu.port.entity.CrawlerForWeiBo;
import com.dhu.port.repository.TouTiaoRepository;
import com.dhu.port.repository.WeiBoRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 内存缓存
 */
@Component
public class CacheService implements InitializingBean {
    @Autowired
    private WeiBoRepository weiBoRepository;
    @Autowired
    private TouTiaoRepository touTiaoRepository;
    @Autowired
    private ConfigRepository configRepository;

    private List<CrawlerForWeiBo> weiBoCache = Lists.newArrayList();//微博首页预读
    private List<CrawlerForTouTiao> touTiaoCache = Lists.newArrayList();//头条首页预读
    private Map<String,String> configCache = Maps.newHashMap();//爬虫配置缓存

    /**
     * 项目启动时执行该方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        weiBoCache.addAll(weiBoRepository.queryForCache());
        touTiaoCache.addAll(touTiaoRepository.queryForCache());
        configCache = configRepository.queryAll();
    }

    //刷新缓存
    public void refreshCache() {
        List<CrawlerForWeiBo> weiBos = weiBoRepository.queryForCache();
        List<CrawlerForTouTiao> touTiaos = touTiaoRepository.queryForCache();
        if (!CollectionUtils.isEmpty(weiBos)) {
            weiBoCache = weiBos;
        }
        if (!CollectionUtils.isEmpty(touTiaos)) {
            touTiaoCache = touTiaos;
        }
        Map<String,String> configs = configRepository.queryAll();
        if (!CollectionUtils.isEmpty(configs)){
            configCache = configs;
        }
    }

    //刷新配置
    public void refreshConfig() {
        Map<String,String> configs = configRepository.queryAll();
        if (!CollectionUtils.isEmpty(configs)){
            configCache = configs;
        }
    }

    public List<CrawlerForTouTiao> getTouTiaoCache() {
        return touTiaoCache;
    }

    public List<CrawlerForWeiBo> getWeiBoCache() {
        return weiBoCache;
    }

    public String getConfig(String key) {
        return configCache.get(key);
    }
}
