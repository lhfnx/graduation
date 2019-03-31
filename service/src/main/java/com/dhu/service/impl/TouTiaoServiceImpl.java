package com.dhu.service.impl;

import com.dhu.common.utils.BeanUtil;
import com.dhu.common.utils.JsonUtils;
import com.dhu.common.utils.StringToCollectionUtils;
import com.dhu.model.DO.*;
import com.dhu.model.VO.TouTiao.TouTiaoListVO;
import com.dhu.model.VO.TouTiao.TouTiaoVO;
import com.dhu.port.entity.CrawlerForTouTiao;
import com.dhu.port.repository.CacheService;
import com.dhu.port.repository.TouTiaoRepository;
import com.dhu.service.TouTiaoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TouTiaoServiceImpl implements TouTiaoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TouTiaoRepository touTiaoRepository;

    @Autowired
    private CacheService cacheService;

    private List<String> prefix = Lists.newArrayList("n", "a");
    private List<String> filterPrefix = Lists.newArrayList("nx");
    private List<String> otherFilter = Lists.newArrayList("w", "v", "nz", "d", "c", "cc", "f", "m", "mg", "Mg", "mq",
            "q", "qg", "qt", "qv");


    @Override
    public List<TouTiaoVO> getInformationFromCache(Integer num) {
        List<CrawlerForTouTiao> touTiaos = cacheService.getTouTiaoCache();
        List<TouTiaoVO> cacheVOS = touTiaos.stream().map(this::copyProperties).collect(Collectors.toList());
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
            touTiaos = touTiaoRepository.queryByPagesWithCondition(StringUtils.EMPTY, (showDO.getIndex() - 1) * showDO
                    .getSize(), showDO.getSize());
        } else {
            touTiaos = touTiaoRepository.queryByPagesWithCondition(showDO.getKey(), (showDO.getIndex() - 1) * showDO
                    .getSize(), showDO.getSize());
        }
        List<TouTiaoVO> weiBoVOS = touTiaos.stream().map(this::copyProperties).collect(Collectors.toList());
        touTiaoListVO.setCurrentIndex(showDO.getIndex());
        Long cnt = touTiaoRepository.queryCount(showDO.getKey());
        touTiaoListVO.setMaxIndex(cnt.intValue() / showDO.getSize() + (cnt.intValue() % showDO.getSize() == 0 ? 0 : 1));
        touTiaoListVO.setSize(showDO.getSize());
        touTiaoListVO.setVoList(weiBoVOS);
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
        List<CrawlerForTouTiao> crawlers = touTiaoRepository.queryHot(LocalDateTime.now());
        List<TouTiaoVO> cacheVOS = crawlers.stream().map(this::copyProperties).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cacheVOS) || hotDO.getNum() > cacheVOS.size()) {
            return cacheVOS;
        }
        return cacheVOS.subList(0, hotDO.getNum());
    }

    private TouTiaoVO copyProperties(CrawlerForTouTiao crawler) {
        TouTiaoVO vo = BeanUtil.copyProperties(crawler, TouTiaoVO.class);
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
        List<String> keyWords = touTiaoRepository.queryKeyWord(LocalDateTime.now().minusDays(analysisDO.getDays()),
                analysisDO.getKey());
        if (CollectionUtils.isEmpty(keyWords)) {
            return Lists.newArrayList();
        }
        List<KeyWordDO> keyWordDOList = Lists.newArrayList();
        keyWords.forEach(k -> {
            keyWordDOList.addAll(JsonUtils.jsonToList(k, KeyWordDO.class));
        });
        Map<String, Integer> map = Maps.newHashMap();
        keyWordDOList.forEach(k -> {
            if (CollectionUtils.isEmpty(analysisDO.getNatures()) && !otherFilter
                    .contains(k.getNature())) {
                map.put(k.getKeyWord(), Optional.ofNullable(map.get(k.getKeyWord())).orElse(0) + 1);
            } else if (analysisDO.getNatures().stream().anyMatch(a -> k.getNature().startsWith(a))) {
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
