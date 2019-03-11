package com.dhu.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtils() {}

    /**
     * 将对象转换成json字符串。
     *
     * @param data
     * @return
     */
    public static String serialize(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("serialize data error", e);
        }
        return null;
    }

    /**
     * 将json字符串转换成对象
     *
     * @param content
     * @param clazz
     * @return
     */
    public static <T> T deSerialize(String content, Class<T> clazz) {
        try {
            return MAPPER.readValue(content, clazz);
        } catch (IOException e) {
            Map<String, String> map = Maps.newHashMap();
            map.put("content", content);
            log.error("deserialize object error", e, map);
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     *
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            Map<String, String> map = Maps.newHashMap();
            map.put("content", jsonData);
            log.error("deserialize object error", e, map);
        }
        return Lists.newArrayList();
    }
}
