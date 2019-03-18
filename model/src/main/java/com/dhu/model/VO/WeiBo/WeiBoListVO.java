package com.dhu.model.VO.WeiBo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class WeiBoListVO {
    private Integer currentIndex;
    private Integer maxIndex;
    private Integer size;
    private List<WeiBoVO> voList;
}
