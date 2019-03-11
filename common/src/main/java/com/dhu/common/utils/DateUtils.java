package com.dhu.common.utils;

public class DateUtils {

    /**
     * 获取当天开始时间（北京时间）
     */
    public static long getTodayStart() {
        return getDayStart(System.currentTimeMillis());
    }

    /**
     * 获取一天开始时间（北京时间）
     */
    public static long getDayStart(long timeMillis) {
        long now = timeMillis / 1000l;
        long daySecond = 86400;
        long dayTime = now - (now + 8 * 3600) % daySecond;
        return dayTime * 1000;
    }
}
