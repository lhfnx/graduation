package com.dhu.crawler.weibo;

import com.dhu.model.DO.KeyWordDO;
import com.google.common.collect.Lists;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 关键词抽取
 */
@Component
public class KeyWordExecutor {

    private TfIdfCounter ky;
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 构造方法导入语料库
     */
    public KeyWordExecutor() {
        ky = new TfIdfCounter();
        File file;
        try {
            file = new File("crawler\\src\\main\\resources\\语料库.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                ky.add(s);
            }
            br.close();
        } catch (Exception e) {
            logger.error("语料库导入异常", e);
        }
    }

    /**
     * 关键词抽取TFIDF
     * @param storeDO
     * @param keyCount
     * @return
     */
    public List<KeyWordDO> execute(CrawlerStoreDO storeDO, Integer keyCount) {
        if (StringUtils.isEmpty(storeDO.getText())) {
            return Lists.newArrayList();
        }
        List<String> keywordList = ky.getKeywordsWithTfIdf(storeDO.getText(), keyCount)
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
