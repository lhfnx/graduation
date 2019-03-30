package com.dhu.service.impl;

import com.dhu.common.utils.BeanUtil;
import com.dhu.common.utils.JsonUtils;
import com.dhu.common.utils.StringToCollectionUtils;
import com.dhu.model.DO.*;
import com.dhu.model.DO.AnaDO;
import com.dhu.model.VO.WeiBo.WeiBoListVO;
import com.dhu.model.VO.WeiBo.WeiBoVO;
import com.dhu.port.entity.CrawlerForWeiBo;
import com.dhu.port.repository.WeiBoRepository;
import com.dhu.port.repository.CacheService;
import com.dhu.service.WeiBoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeiBoServiceImpl implements WeiBoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WeiBoRepository weiBoRepository;

    @Autowired
    private CacheService cacheService;

    private List<String> otherFilter = Lists.newArrayList("w", "v", "nz", "d", "c", "cc", "f", "m", "mg", "Mg", "mq",
            "q", "qg", "qt", "qv");
    private List<String> prefix = Lists.newArrayList("n", "a");
    private List<String> filterPrefix = Lists.newArrayList("nx");

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
            weiBos = weiBoRepository.queryByPagesWithCondition(StringUtils.EMPTY, (showDO.getIndex() - 1) * showDO
                    .getSize(), showDO.getSize());
        } else {
            weiBos = weiBoRepository.queryByPagesWithCondition(showDO.getKey(), (showDO.getIndex() - 1) * showDO
                    .getSize(), showDO.getSize());
        }
        List<WeiBoVO> weiBoVOS = weiBos.stream().map(this::copyProperties).collect(Collectors.toList());
        weiBoListVO.setCurrentIndex(showDO.getIndex());
        Long cnt = weiBoRepository.queryCount(showDO.getKey());
        weiBoListVO.setMaxIndex(cnt.intValue() / showDO.getSize() + (cnt.intValue() % showDO.getSize() == 0 ? 0 : 1));
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
        List<CrawlerForWeiBo> crawlers = weiBoRepository.queryHot(LocalDateTime.now());
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
        } else {
            vo.setInformationDO(new InformationDO());
        }
        if (StringUtils.isNotEmpty(crawler.getKeyWord())) {
            vo.setKeyWords(JsonUtils.jsonToList(crawler.getKeyWord(), KeyWordDO.class)
                    .stream().filter(k -> fitStartWith(k.getNature())).distinct().collect(Collectors.toList()));
        }
        return vo;
    }

    private boolean fitStartWith(String str) {
        for (String p : prefix) {
            if (str.startsWith(p) && !filterPrefix.contains(str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AnaDO> getAnalysis(AnalysisDO analysisDO) {
        List<String> keyWords = weiBoRepository.queryKeyWord(LocalDateTime.now().minusDays(analysisDO.getDays()));
        if (CollectionUtils.isEmpty(keyWords)) {
            return Lists.newArrayList();
        }
        List<KeyWordDO> keyWordDOList = Lists.newArrayList();
        keyWords.forEach(k -> {
            keyWordDOList.addAll(JsonUtils.jsonToList(k, KeyWordDO.class));
        });
        Map<String, Integer> map = Maps.newHashMap();
        keyWordDOList.forEach(k -> {
            if (CollectionUtils.isEmpty(analysisDO.getNatures()) && !otherFilter.contains(k.getNature())) {
                map.put(k.getKeyWord(), Optional.ofNullable(map.get(k.getKeyWord())).orElse(0) + 1);
            } else if (analysisDO.getNatures().contains(k.getNature())) {
                map.put(k.getKeyWord(), Optional.ofNullable(map.get(k.getKeyWord())).orElse(0) + 1);
            }
        });
        List<String> filterKey = StringToCollectionUtils.stringToList(cacheService.getConfig("FilterKey"));
        List<AnaDO> sorted = map.entrySet().stream()
                .map(this::convert2WeiBoAnaVO)
                .filter(w -> !filterKey.contains(w.getName()))
                .sorted(Comparator.comparingInt(AnaDO::getFeq).reversed())
                .collect(Collectors.toList());
        List<AnaDO> result = Lists.newArrayList();
        for (AnaDO anaVO : sorted) {
            boolean flag = true;
            for (AnaDO res : result) {
                if (res.getName().contains(anaVO.getName())) {
                    flag = false;
                    res.setFeq(res.getFeq() + anaVO.getFeq());
                    break;
                }
            }
            if (flag) {
                result.add(anaVO);
            }
        }
        if (result.size() < analysisDO.getNum()) {
            return result;
        }
        return result.subList(0, analysisDO.getNum());
    }

    private AnaDO convert2WeiBoAnaVO(Map.Entry<String, Integer> entry) {
        AnaDO anaVO = new AnaDO();
        anaVO.setName(entry.getKey());
        anaVO.setFeq(entry.getValue());
        return anaVO;
    }
}
