package com.dhu.common.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

@Getter
public enum StationEnum {
    WEI_BO(1,"微博"),
    TOU_TIAO(2,"今日头条");

    private Integer code;
    private String value;

    StationEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    private static Map<Integer,StationEnum> map= Maps.newConcurrentMap();
    static {
        for (StationEnum c: StationEnum.values()){
            map.put(c.code,c);
        }
    }

    public static StationEnum valueOfByCode(Integer code){
        if (map.containsKey(code)){
            return map.get(code);
        }
        throw new IllegalArgumentException(String.format("No StationEnum founded with code : %s!!", code));
    }

}
