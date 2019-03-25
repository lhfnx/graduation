package com.dhu.service;

import com.dhu.model.DO.AnalysisDO;
import com.dhu.model.DO.HotDO;
import com.dhu.model.DO.ListShowDO;
import com.dhu.model.VO.WeiBo.WeiBoAnaVO;
import com.dhu.model.VO.WeiBo.WeiBoListVO;
import com.dhu.model.VO.WeiBo.WeiBoVO;
import com.dhu.port.entity.CrawlerForWeiBo;

import java.util.List;

public interface WeiBoService {

    List<WeiBoVO> getInformationFromCache(Integer num);

    WeiBoListVO getInformationForList(ListShowDO showDO);

    CrawlerForWeiBo getInformation(Long id);

    List<WeiBoVO> getTodayInformationByHot(HotDO hotDO);

    List<WeiBoAnaVO> getAnalysis(AnalysisDO analysisDO);
}