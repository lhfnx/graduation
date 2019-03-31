package com.dhu.web.controller;

import com.dhu.common.bean.ResponseResult;
import com.dhu.model.DO.*;
import com.dhu.model.VO.TouTiao.TouTiaoListVO;
import com.dhu.model.VO.TouTiao.TouTiaoVO;
import com.dhu.service.TouTiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("toutiao")
public class TouTiaoController {
    @Autowired
    private TouTiaoService service;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "cache", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getCache(@RequestBody CacheDO cacheDO) {
        try {
            if (Objects.isNull(cacheDO) || Objects.isNull(cacheDO.getNum())) {
                return ResponseResult.build(500, "Param Error");
            }
            List<TouTiaoVO> cacheVOs = service.getInformationFromCache(cacheDO.getNum());
            return ResponseResult.ok(cacheVOs);
        } catch (Exception e) {
            logger.error("toutiao get cache fail", e);
            return ResponseResult.build(500, "get cache fail");
        }
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult listShow(@RequestBody ListShowDO showDO) {
        try {
            if (Objects.isNull(showDO) || Objects.isNull(showDO.getIndex()) || showDO.getIndex().compareTo(0) <= 0 ||
                    Objects.isNull(showDO.getSize()) || showDO.getSize().compareTo(0) <= 0) {
                return ResponseResult.build(500, "Param Error");
            }
            TouTiaoListVO listVO = service.getInformationForList(showDO);
            return ResponseResult.ok(listVO);
        } catch (Exception e) {
            logger.error("toutiao list show fail", e);
            return ResponseResult.build(500, "list show fail");
        }
    }

    @RequestMapping(value = "hot",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult hotShow(@RequestBody HotDO hotDO){
        try {
            if (Objects.isNull(hotDO) || Objects.isNull(hotDO.getNum()) || hotDO.getNum().compareTo(0) <= 0) {
                hotDO = new HotDO();
                hotDO.setNum(10);
            }
            List<TouTiaoVO> vos = service.getTodayInformationByHot(hotDO);
            return ResponseResult.ok(vos);
        } catch (Exception e) {
            logger.error("toutiao hot show fail", e);
            return ResponseResult.build(500, "hot show fail");
        }
    }

    @RequestMapping(value = "analysis",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult analysisKey(@RequestBody AnalysisDO analysisDO){
        try {
            if (Objects.isNull(analysisDO) || Objects.isNull(analysisDO.getNum()) || analysisDO.getNum().compareTo(0) <= 0) {
                analysisDO = new AnalysisDO();
                analysisDO.setNum(5);
            }
            List<AnaDO> vos = service.getAnalysis(analysisDO);
            return ResponseResult.ok(vos);
        } catch (Exception e) {
            logger.error("weibo analysis fail", e);
            return ResponseResult.build(500, "analysis show fail");
        }
    }
}
