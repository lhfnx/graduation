package com.dhu.model.VO.TouTiao;

import com.dhu.model.VO.WeiBo.WeiBoVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class TouTiaoListVO {
    private Integer currentIndex;
    private Integer maxIndex;
    private Integer size;
    private List<TouTiaoVO> touTiaoVOS;
}
