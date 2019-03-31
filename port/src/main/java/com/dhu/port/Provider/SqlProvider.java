package com.dhu.port.Provider;

import com.dhu.common.utils.StringToCollectionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class SqlProvider {
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
