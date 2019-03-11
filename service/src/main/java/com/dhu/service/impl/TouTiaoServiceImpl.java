package com.dhu.service.impl;

import com.dhu.common.utils.BeanUtil;
import com.dhu.common.utils.DateUtils;
import com.dhu.model.DO.HotDO;
import com.dhu.model.DO.ListShowDO;
import com.dhu.model.VO.TouTiao.TouTiaoListVO;
import com.dhu.model.VO.TouTiao.TouTiaoVO;
import com.dhu.port.entity.CrawlerForTouTiao;
import com.dhu.port.repository.TouTiaoRepository;
import com.dhu.service.CacheService;
import com.dhu.service.TouTiaoService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TouTiaoServiceImpl implements TouTiaoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TouTiaoRepository touTiaoRepository;

    @Autowired
    private CacheService cacheService;

    @Override
    public List<TouTiaoVO> getInformationFromCache(Integer num) {
        List<CrawlerForTouTiao> touTiaos = cacheService.getTouTiaoCache();
        List<TouTiaoVO> cacheVOS = Lists.newArrayList();
        BeanUtil.copyProperties(touTiaos, cacheVOS);
        if (CollectionUtils.isEmpty(cacheVOS) || num > cacheVOS.size()) {
            return cacheVOS;
        }
        return cacheVOS.subList(0, num);
    }


    @Override
    public TouTiaoListVO getInformationForList(ListShowDO showDO) {
        TouTiaoListVO touTiaoListVO = new TouTiaoListVO();
        List<CrawlerForTouTiao> touTiaos = Lists.newArrayList();
        if (StringUtils.isEmpty(showDO.getKey())) {
            touTiaos = touTiaoRepository.queryByPages((showDO.getIndex() - 1) * showDO.getSize(), showDO.getSize());
        } else {
            touTiaos = touTiaoRepository.queryByPagesWithKeyWord(showDO.getKey(), (showDO.getIndex() - 1) * showDO
                    .getSize(), showDO.getSize());
        }
        List<TouTiaoVO> weiBoVOS = BeanUtil.copyProperties(touTiaos, TouTiaoVO.class);
        touTiaoListVO.setCurrentIndex(showDO.getIndex());
        touTiaoListVO.setMaxIndex(touTiaoRepository.queryCount().intValue() / showDO.getSize() + touTiaoRepository
                .queryCount().intValue() % showDO.getSize() == 0 ? 0 : 1);
        touTiaoListVO.setSize(showDO.getSize());
        touTiaoListVO.setTouTiaoVOS(weiBoVOS);
        return touTiaoListVO;
    }

    @Override
    public CrawlerForTouTiao getInformation(Long id) {
        if (id != null) {
            return touTiaoRepository.queryById(id);
        }
        return null;
    }

    @Override
    public List<TouTiaoVO> getTodayInformationByHot(HotDO hotDO) {
        List<CrawlerForTouTiao> crawlers = touTiaoRepository.queryHot(new Timestamp(DateUtils.getTodayStart()));
        List<TouTiaoVO> cacheVOS = Lists.newArrayList();
        BeanUtil.copyProperties(crawlers, cacheVOS);
        if (CollectionUtils.isEmpty(cacheVOS) || hotDO.getNum() > cacheVOS.size()) {
            return cacheVOS;
        }
        return cacheVOS.subList(0, hotDO.getNum());
    }
}
