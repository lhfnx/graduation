package com.dhu.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class CookieUtils {
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static Integer retryTimes = 5;
    private static Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    private static JSONObject getTidAndC() throws IOException {
        String result = "";
        String url = "https://passport.weibo.com/visitor/genvisitor";
        RequestBody body = new FormBody.Builder().add("cb", "gen_callback").add("fp",
                "%7B%22os%22%3A%221%22%2C%22browser%22%3A%22Chrome72%2C0%2C3626%2C121%22%2C%22fonts%22%3A" +
                        "%22undefined%22%2C%22screenInfo%22%3A%221920*1080*24%22%2C%22plugins%22%3A%22Portable" +
                        "%20Document%20Format%3A%3Ainternal-pdf-viewer%3A%3AChrome%20PDF%20Plugin%7C%3A" +
                        "%3Amhjfbmdgcfjbbpaeojofohoefgiehjai%3A%3AChrome%20PDF%20Viewer%7C%3A%3Ainternal-nacl-plugin" +
                        "%3A%3ANative%20Client%22%7D")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            result = response.body().string();
            result = result.replaceAll("window.gen_callback && gen_callback\\(", "");
            result = result.replaceAll("\\);", "");
            JSONObject json = JSONObject.parseObject(result).getJSONObject("data");
            return json;
        }
        return null;
    }

    private static JSONObject getSubAndSubp(JSONObject json) throws IOException {
        String t = "";
        String w = "";
        String c = json.containsKey("confidence") ? json.getString("confidence") : "95";
        if (json.containsKey("new_tid")) {
            w = json.getBoolean("new_tid") ? "3" : "2";
        }
        if (json.containsKey("tid")) {
            t = json.getString("tid");
        }
        String url = "https://passport.weibo.com/visitor/visitor?a=incarnate&t=" + t + "&w=" + w + "&c=0" + c +
                "&gc=&cb=cross_domain&from=weibo&_rand=" + Math.random();
        String result = null;
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (response == null) {
            return null;
        }
        String body = response.body().string();
        body = body.replaceAll("window.cross_domain && cross_domain\\(", "");
        body = body.replaceAll("\\);", "");
        JSONObject obj = JSONObject.parseObject(body).getJSONObject("data");
        return obj;
    }

    public static Map<String, String> getCookie() {
        retryTimes--;
        if (retryTimes.compareTo(0) < 0) {
            retryTimes = 5;
            logger.error("CookieUtils fail", "retry over 5 times");
            return Maps.newHashMap();
        }
        try {
            JSONObject j = getTidAndC();
            if (Objects.isNull(j)) {
                return getCookie();
            }
            JSONObject json = getSubAndSubp(j);
            if (Objects.isNull(json) || StringUtils.isEmpty(json.getString("sub")) || StringUtils.isEmpty(json
                    .getString("subp"))) {
                return getCookie();
            }
            Map<String, String> cookie = Maps.newHashMap();
            cookie.put("SUB", json.getString("sub"));
            cookie.put("SUBP", json.getString("subp"));
            retryTimes = 5;
            return cookie;
        } catch (IOException e) {
            logger.error("CookieUtils fail", e);
            return getCookie();
        }

    }
}
