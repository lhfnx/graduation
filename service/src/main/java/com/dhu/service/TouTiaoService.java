package com.dhu.service;

import com.dhu.model.DO.AnaDO;
import com.dhu.model.DO.AnalysisDO;
import com.dhu.model.DO.HotDO;
import com.dhu.model.DO.ListShowDO;
import com.dhu.model.VO.TouTiao.TouTiaoListVO;
import com.dhu.model.VO.TouTiao.TouTiaoVO;
import com.dhu.port.entity.CrawlerForTouTiao;

import java.util.List;

public interface TouTiaoService {

    //首页预读
    List<TouTiaoVO> getInformationFromCache(Integer num);

    //列表页获取
    TouTiaoListVO getInformationForList(ListShowDO showDO);

    //指定获取
    CrawlerForTouTiao getInformation(Long id);

    //热文
    List<TouTiaoVO> getTodayInformationByHot(HotDO hotDO);

    //数据分析
    List<AnaDO> getAnalysis(AnalysisDO analysisDO);
}
