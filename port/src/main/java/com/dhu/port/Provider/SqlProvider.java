package com.dhu.port.Provider;

import com.dhu.common.utils.StringToCollectionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 动态条件SQL
 */
public class SqlProvider {
    //动态关键词获取信息
    public String getListByKeys(Map<String, Object> map) {
        String table = map.get("table").toString();
        String keys = map.get("keys").toString();
        Integer offset = NumberUtils.toInt(map.get("offset").toString());
        Integer rows = NumberUtils.toInt(map.get("rows").toString());
        List<String> keyWords = StringToCollectionUtils.stringToList(keys);
        return new SQL() {
            {
                SELECT("*");
                FROM(table);
                WHERE("is_active = 1");
                for (String keyWord : keyWords) {
                    WHERE("key_word LIKE \"%" + keyWord + "%\"");
                }
                ORDER_BY("datachange_createtime DESC LIMIT " + offset + "," + rows + "");
            }
        }.toString();
    }

    //动态关键词获取数量
    public String getCountByKeys(Map<String, Object> map) {
        String table = map.get("table").toString();
        String keys = map.get("keys").toString();
        List<String> keyWords = StringToCollectionUtils.stringToList(keys);
        return new SQL() {
            {
                SELECT("COUNT(*)");
                FROM(table);
                WHERE("is_active = 1");
                for (String keyWord : keyWords) {
                    WHERE("key_word LIKE \"%" + keyWord + "%\"");
                }
            }
        }.toString();
    }

    //动态词性获取关键词
    public String getKeyWords(Map<String, Object> map) {
        String table = map.get("table").toString();
        String keys = map.get("keys").toString();
        String today = map.get("today").toString();
        List<String> keyWords = StringToCollectionUtils.stringToList(keys);
        return new SQL() {
            {
                SELECT("key_word");
                FROM(table);
                WHERE("is_active = 1");
                for (String keyWord : keyWords) {
                    WHERE("key_word LIKE \"%" + keyWord + "%\"");
                }
                WHERE("datachange_createtime > '" + today + "'");
            }
        }.toString();
    }
}
