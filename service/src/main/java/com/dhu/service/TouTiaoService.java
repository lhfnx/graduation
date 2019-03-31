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

    List<TouTiaoVO> getInformationFromCache(Integer num);

    TouTiaoListVO getInformationForList(ListShowDO showDO);

    CrawlerForTouTiao getInformation(Long id);

    List<TouTiaoVO> getTodayInformationByHot(HotDO hotDO);

    List<AnaDO> getAnalysis(AnalysisDO analysisDO);
}
