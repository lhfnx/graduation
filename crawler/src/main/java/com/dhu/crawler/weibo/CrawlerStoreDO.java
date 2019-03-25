package com.dhu.crawler.weibo;

import com.dhu.model.DO.KeyWordDO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CrawlerStoreDO {
    private String author;
    private String text;
    private String url;
    private String imgUrl;
    private String like;
    private String comment;
    private String forward;
    private String favorite;
    private Integer hotDegree;
    private List<KeyWordDO> keyWord;
}
