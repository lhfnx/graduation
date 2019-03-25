package com.dhu.model.VO.WeiBo;

import com.dhu.model.DO.InformationDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class WeiBoVO {
    private Long id;
    private String summary;
    private String connectUrl;
    private Timestamp DataChangeCreateTime;
    private String keyWord;
    private Integer classify;
    private Boolean isActive;
    private InformationDO informationDO;
    private Integer hotDegree;
    private String imgUrl;
}
