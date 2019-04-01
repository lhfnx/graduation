package com.dhu.crawler.weibo;

import com.dhu.model.DO.KeyWordDO;
import com.google.common.collect.Lists;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeyWordExecutor {

    public List<KeyWordDO> execute(CrawlerStoreDO storeDO, Integer keyCount) {
        TfIdfCounter ky = new TfIdfCounter();
        if (StringUtils.isEmpty(storeDO.getText())) {
            return Lists.newArrayList();
        }
        List<String> keywordList = ky.getKeywordsWithTfIdf(storeDO.getText(),keyCount)
                .stream().map(Map.Entry::getKey).collect(Collectors.toList());
        List<KeyWordDO> keyWordDOS = Lists.newArrayList();
        keywordList.forEach(k -> {
            keyWordDOS.addAll(convert2KeyWordDO(k));
        });
        return keyWordDOS;
    }

    private List<KeyWordDO> convert2KeyWordDO(String keyword) {
        List<Term> terms = HanLP.segment(keyword);
        List<KeyWordDO> keyWordDOS = Lists.newArrayList();
        for (Term term : terms) {
            KeyWordDO wordDO = new KeyWordDO();
            wordDO.setKeyWord(term.word);
            wordDO.setNature(term.nature.toString());
            keyWordDOS.add(wordDO);
        }
        return keyWordDOS;
    }
}
