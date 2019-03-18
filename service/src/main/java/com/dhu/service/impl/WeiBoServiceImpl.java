package com.dhu.service.impl;

import com.dhu.common.utils.BeanUtil;
import com.dhu.common.utils.DateUtils;
import com.dhu.common.utils.JsonUtils;
import com.dhu.model.DO.HotDO;
import com.dhu.model.DO.InformationDO;
import com.dhu.model.DO.ListShowDO;
import com.dhu.model.VO.WeiBo.WeiBoListVO;
import com.dhu.model.VO.WeiBo.WeiBoVO;
import com.dhu.port.entity.CrawlerForWeiBo;
import com.dhu.port.repository.WeiBoRepository;
import com.dhu.service.CacheService;
import com.dhu.service.WeiBoService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeiBoServiceImpl implements WeiBoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WeiBoRepository weiBoRepository;

    @Autowired
    private CacheService cacheService;

    @Override
    public List<WeiBoVO> getInformationFromCache(Integer num) {
        List<CrawlerForWeiBo> weiBos = cacheService.getWeiBoCache();
        List<WeiBoVO> cacheVOS = weiBos.stream().map(this::copyProperties).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cacheVOS) || num > cacheVOS.size()) {
            return cacheVOS;
        }
        return cacheVOS.subList(0, num);
    }


    @Override
    public WeiBoListVO getInformationForList(ListShowDO showDO) {
        WeiBoListVO weiBoListVO = new WeiBoListVO();
        List<CrawlerForWeiBo> weiBos = Lists.newArrayList();
        if (StringUtils.isEmpty(showDO.getKey())) {
            weiBos = weiBoRepository.queryByPages((showDO.getIndex() - 1) * showDO.getSize(), showDO.getSize());
        } else {
            weiBos = weiBoRepository.queryByPagesWithKeyWord(showDO.getKey(), (showDO.getIndex() - 1) * showDO
                    .getSize(), showDO.getSize());
        }
        List<WeiBoVO> weiBoVOS = weiBos.stream().map(this::copyProperties).collect(Collectors.toList());
        weiBoListVO.setCurrentIndex(showDO.getIndex());
        weiBoListVO.setMaxIndex(weiBoRepository.queryCount().intValue() / showDO.getSize() + (weiBoRepository
                .queryCount().intValue() % showDO.getSize() == 0 ? 0 : 1));
        weiBoListVO.setSize(showDO.getSize());
        weiBoListVO.setVoList(weiBoVOS);
        return weiBoListVO;
    }

    @Override
    public CrawlerForWeiBo getInformation(Long id) {
        if (id != null) {
            return weiBoRepository.queryById(id);
        }
        return null;
    }

    @Override
    public List<WeiBoVO> getTodayInformationByHot(HotDO hotDO) {
        List<CrawlerForWeiBo> crawlers = weiBoRepository.queryHot(new Timestamp(DateUtils.getTodayStart()));
        List<WeiBoVO> cacheVOS = crawlers.stream().map(this::copyProperties).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cacheVOS) || hotDO.getNum() > cacheVOS.size()) {
            return cacheVOS;
        }
        return cacheVOS.subList(0, hotDO.getNum());
    }

    private WeiBoVO copyProperties(CrawlerForWeiBo crawler) {
        WeiBoVO vo = BeanUtil.copyProperties(crawler, WeiBoVO.class);
        if (StringUtils.isNotEmpty(crawler.getInformation())) {
            vo.setInformationDO(JsonUtils.deSerialize(crawler.getInformation(), InformationDO.class));
        }else {
            vo.setInformationDO(new InformationDO());
        }
        return vo;
    }
}
