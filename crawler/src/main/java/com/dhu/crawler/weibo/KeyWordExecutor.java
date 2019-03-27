package com.dhu.crawler.weibo;

import com.dhu.model.DO.KeyWordDO;
import com.dhu.port.repository.CacheService;
import com.google.common.collect.Lists;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyWordExecutor {

    public List<KeyWordDO> execute(CrawlerStoreDO storeDO, Integer keyCount) {
        KeyWordComputer ky = new KeyWordComputer(keyCount);
        if (StringUtils.isEmpty(storeDO.getText())) {
            return Lists.newArrayList();
        }
        List<Keyword> keywordList = ky.computeArticleTfidf(storeDO.getText());
        List<KeyWordDO> keyWordDOS = Lists.newArrayList();
        keywordList.forEach(k -> {
            keyWordDOS.addAll(convert2KeyWordDO(k));
        });
        return keyWordDOS;
    }

    private List<KeyWordDO> convert2KeyWordDO(Keyword keyword) {
        List<Term> terms = HanLP.segment(keyword.getName());
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
