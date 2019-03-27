package com.dhu.model.VO.WeiBo;

import com.dhu.model.DO.InformationDO;
import com.dhu.model.DO.KeyWordDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@ToString
public class WeiBoVO {
    private Long id;
    private String summary;
    private String connectUrl;
    private Timestamp DataChangeCreateTime;
    private List<KeyWordDO> keyWords;
    private Integer classify;
    private Boolean isActive;
    private InformationDO informationDO;
    private Integer hotDegree;
    private String imgUrl;
}
