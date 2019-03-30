package com.dhu.port.mapper;

import com.dhu.port.Provider.SqlProvider;
import com.dhu.port.entity.CrawlerForTouTiao;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
            @Result(property = "information", column = "information"),
            @Result(property = "hotDegree", column = "hot_degree"),
            @Result(property = "imgUrl", column = "img_url")
    })
    List<CrawlerForTouTiao> queryAll();

    @Select("SELECT * FROM crawler_toutiao WHERE is_active = 1 ORDER BY datachange_createtime DESC, id DESC LIMIT 0," +
            "100")
    @ResultMap("crawlerTouTiaoMapper")
    List<CrawlerForTouTiao> queryForCache();

    @Select("SELECT * FROM crawler_toutiao ORDER BY datachange_createtime DESC, id DESC LIMIT #{offset},#{rows}")
    @ResultMap("crawlerTouTiaoMapper")
    List<CrawlerForTouTiao> queryByPages(@Param("offset") Integer offset, @Param("rows") Integer rows);

    @Select("SELECT * FROM crawler_toutiao WHERE is_active = 1 AND key_word LIKE #{keys} ORDER BY " +
            "datachange_createtime DESC, id DESC LIMIT #{offset},#{rows}")
    @ResultMap("crawlerTouTiaoMapper")
    List<CrawlerForTouTiao> queryByPagesWithKeyWord(@Param("keys") String keys, @Param("offset") Integer offset, @Param
            ("rows") Integer rows);

    @Select("SELECT key_word FROM crawler_toutiao WHERE is_active = 1")
    List<String> queryKeyWords();

    @Select("SELECT * FROM crawler_toutiao WHERE id =#{id}  AND is_active = 1")
    @ResultMap("crawlerTouTiaoMapper")
    CrawlerForTouTiao queryById(@Param("id") Long id);

    @SelectProvider(type = SqlProvider.class,method = "getCountByKeys")
    Long queryCountByKeys(Map<String,Object> map);

    @Select("SELECT * FROM crawler_toutiao WHERE is_active = 1 AND datachange_createtime > #{today} ORDER BY " +
            "hot_degree DESC,id DESC LIMIT 20")
    @ResultMap("crawlerTouTiaoMapper")
    List<CrawlerForTouTiao> queryHot(@Param("today") String today);

    @SelectProvider(type = SqlProvider.class,method = "getListByKeys")
    @ResultMap("crawlerTouTiaoMapper")
    List<CrawlerForTouTiao> queryByPagesWithCondition(Map<String,Object> map);
}
