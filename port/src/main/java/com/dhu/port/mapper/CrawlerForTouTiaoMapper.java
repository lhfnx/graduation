package com.dhu.port.mapper;

import com.dhu.port.entity.CrawlerForTouTiao;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrawlerForTouTiaoMapper {
    @Select("SELECT * FROM crawler_toutiao ORDER BY datachange_createtime DESC")
    @Results(id = "crawlerTouTiaoMapper", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "title", column = "title"),
            @Result(property = "summary", column = "summary"),
            @Result(property = "connectUrl", column = "connect_url"),
            @Result(property = "DataChangeCreateTime", column = "datachange_createtime"),
            @Result(property = "keyWord", column = "key_word"),
            @Result(property = "classify", column = "classify"),
            @Result(property = "isActive", column = "is_active"),
            @Result(property = "information", column = "information")
    })
    List<CrawlerForTouTiao> queryAll();

    @Select("SELECT * FROM crawler_toutiao ORDER BY datachange_createtime DESC LIMIT #{offset},#{rows}")
    @ResultMap("crawlerTouTiaoMapper")
    List<CrawlerForTouTiao> queryByPages(@Param("offset")Integer offset,@Param("rows")Integer rows);

}
