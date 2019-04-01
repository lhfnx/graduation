package com.dhu.port.mapper;

import com.dhu.port.Provider.SqlProvider;
import com.dhu.port.entity.CrawlerForWeiBo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public interface CrawlerForWeiBoMapper {
    @Select("SELECT * FROM crawler_weibo WHERE is_active = 1 ORDER BY datachange_createtime DESC, id DESC")
    @Results(id = "crawlerWeiBoMapper", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "summary", column = "summary"),
            @Result(property = "connectUrl", column = "connect_url"),
            @Result(property = "DataChangeCreateTime", column = "datachange_createtime"),
            @Result(property = "keyWord", column = "key_word"),
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

    @SelectProvider(type = SqlProvider.class,method = "getCountByKeys")
    Long queryCountByKeys(Map<String,Object> map);

    @Select("SELECT * FROM crawler_weibo WHERE is_active = 1 AND datachange_createtime > #{today} ORDER BY " +
            "hot_degree DESC,id DESC LIMIT 20")
    @ResultMap("crawlerWeiBoMapper")
    List<CrawlerForWeiBo> queryHot(@Param("today") String today);

    @Insert("INSERT INTO crawler_weibo(summary,connect_url,datachange_createtime,key_word,is_active,information,hot_degree,img_url) " +
            "VALUES(#{summary},#{connectUrl},CURRENT_TIMESTAMP,#{keyWord},1,#{information},#{hotDegree},#{imgUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCrawler(CrawlerForWeiBo weiBo);

    @SelectProvider(type = SqlProvider.class,method = "getKeyWords")
    List<String> queryKeyWord(Map<String,Object> map);

    @SelectProvider(type = SqlProvider.class,method = "getListByKeys")
    @ResultMap("crawlerWeiBoMapper")
    List<CrawlerForWeiBo> queryByPagesWithCondition(Map<String,Object> map);
}
