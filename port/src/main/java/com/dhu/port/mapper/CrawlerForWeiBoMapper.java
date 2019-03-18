package com.dhu.port.mapper;

import com.dhu.port.entity.CrawlerForWeiBo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CrawlerForWeiBoMapper {
    @Select("SELECT * FROM crawler_weibo WHERE is_active = 1 ORDER BY datachange_createtime DESC, id DESC")
    @Results(id = "crawlerWeiBoMapper", value = {
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
    List<CrawlerForWeiBo> queryAll();

    @Select("SELECT * FROM crawler_weibo WHERE is_active = 1 ORDER BY datachange_createtime DESC, id DESC LIMIT 0,100")
    @ResultMap("crawlerWeiBoMapper")
    List<CrawlerForWeiBo> queryForCache();

    @Select("SELECT * FROM crawler_weibo WHERE is_active = 1 ORDER BY datachange_createtime DESC, id DESC LIMIT " +
            "#{offset},#{rows}")
    @ResultMap("crawlerWeiBoMapper")
    List<CrawlerForWeiBo> queryByPages(@Param("offset") Integer offset, @Param("rows") Integer rows);

    @Select("SELECT * FROM crawler_weibo WHERE is_active = 1 AND key_word LIKE #{keys} ORDER BY datachange_createtime" +
            " DESC, id DESC LIMIT #{offset},#{rows}")
    @ResultMap("crawlerWeiBoMapper")
    List<CrawlerForWeiBo> queryByPagesWithKeyWord(@Param("keys") String keys, @Param("offset") Integer offset, @Param
            ("rows") Integer rows);

    @Select("SELECT key_word FROM crawler_weibo WHERE is_active = 1")
    List<String> queryKeyWords();

    @Select("SELECT * FROM crawler_weibo WHERE id =#{id}  AND is_active = 1")
    @ResultMap("crawlerWeiBoMapper")
    CrawlerForWeiBo queryById(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM crawler_weibo WHERE is_active = 1")
    Long queryCount();

    @Select("SELECT * FROM crawler_weibo WHERE is_active = 1 AND datachange_createtime > #{today} ORDER BY " +
            "hot_degree DESC,id DESC LIMIT 20")
    @ResultMap("crawlerWeiBoMapper")
    List<CrawlerForWeiBo> queryHot(@Param("today") String today);
}
