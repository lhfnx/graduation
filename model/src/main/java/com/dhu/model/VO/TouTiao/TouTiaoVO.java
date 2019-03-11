package com.dhu.model.VO.TouTiao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class TouTiaoVO {
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
