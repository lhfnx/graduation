package com.dhu.port.mapper;

import com.dhu.port.entity.Config;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigMapper {
    @Select("SELECT * FROM config WHERE is_active = 1")
    @Results(id = "configMapper", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "key", column = "key"),
            @Result(property = "value", column = "value"),
            @Result(property = "DataChangeLastTime", column = "datachange_lasttime"),
            @Result(property = "isActive", column = "is_active"),
            @Result(property = "description", column = "description")
    })
    List<Config> queryAll();
}
