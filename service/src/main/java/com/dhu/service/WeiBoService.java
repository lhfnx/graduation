package com.dhu.service;

import com.dhu.model.DO.AnalysisDO;
import com.dhu.model.DO.HotDO;
import com.dhu.model.DO.ListShowDO;
import com.dhu.model.DO.AnaDO;
import com.dhu.model.VO.WeiBo.WeiBoListVO;
import com.dhu.model.VO.WeiBo.WeiBoVO;
import com.dhu.port.entity.CrawlerForWeiBo;

import java.util.List;

public interface WeiBoService {

    //首页预读
    List<WeiBoVO> getInformationFromCache(Integer num);

    //列表页获取
    WeiBoListVO getInformationForList(ListShowDO showDO);

    //指定获取
    CrawlerForWeiBo getInformation(Long id);

    //热文
    List<WeiBoVO> getTodayInformationByHot(HotDO hotDO);

    //数据分析
    List<AnaDO> getAnalysis(AnalysisDO analysisDO);
}