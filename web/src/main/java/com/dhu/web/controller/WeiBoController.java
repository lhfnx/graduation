package com.dhu.web.controller;

import com.dhu.common.bean.ResponseResult;
import com.dhu.model.DO.CacheDO;
import com.dhu.model.DO.HotDO;
import com.dhu.model.DO.ListShowDO;
import com.dhu.model.VO.WeiBo.WeiBoListVO;
import com.dhu.model.VO.WeiBo.WeiBoVO;
import com.dhu.service.WeiBoService;
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
@RequestMapping("weibo")
public class WeiBoController {
    @Autowired
    private WeiBoService service;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "cache", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getCache(@RequestBody CacheDO cacheDO) {
        try {
            if (Objects.isNull(cacheDO) || Objects.isNull(cacheDO.getNum())) {
                return ResponseResult.build(500, "Param Error");
            }
            List<WeiBoVO> cacheVOs = service.getInformationFromCache(cacheDO.getNum());
            return ResponseResult.ok(cacheVOs);
        } catch (Exception e) {
            logger.error("weibo get cache fail", e);
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
            WeiBoListVO listVO = service.getInformationForList(showDO);
            return ResponseResult.ok(listVO);
        } catch (Exception e) {
            logger.error("weibo list show fail", e);
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
            List<WeiBoVO> vos = service.getTodayInformationByHot(hotDO);
            return ResponseResult.ok(vos);
        } catch (Exception e) {
            logger.error("toutiao hot show fail", e);
            return ResponseResult.build(500, "hot show fail");
        }
    }
}
