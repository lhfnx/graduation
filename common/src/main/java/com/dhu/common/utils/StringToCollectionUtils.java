package com.dhu.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

//将字符串变为集合，如1,2,3->{1,2,3}
public class StringToCollectionUtils {
    private static final String DEFAULT_SEP = ",";

    public static List<String> stringToList(String str) {
        return stringToList(str, DEFAULT_SEP);
    }

    public static List<String> stringToList(String str, String sep) {
        if (StringUtils.isEmpty(str)) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(StringUtils.split(str, sep));
    }

    public static Set<String> stringToSet(String str) {
        return stringToSet(str, DEFAULT_SEP);
    }

    public static Set<String> stringToSet(String str, String sep) {
        if (StringUtils.isEmpty(str)) {
            return Sets.newHashSet();
        }
        return Sets.newHashSet(StringUtils.split(str, sep));
    }
}
