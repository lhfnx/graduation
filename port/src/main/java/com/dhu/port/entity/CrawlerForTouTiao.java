package com.dhu.port.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class CrawlerForTouTiao implements Serializable {
    private Long id;
    private String title;
    private String summary;
    private String connectUrl;
    private Timestamp DataChangeCreateTime;
    private String keyWord;
    private Integer classify;
    private Boolean isActive;
    private String information;
}
