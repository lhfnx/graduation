package com.dhu.common.utils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private HttpUtils() {}

    /**
     * OkHttpClient get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        String result = null;
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (response != null) {
            result = response.body().string();
        }
        return result;
    }

    /**
     * post map
     *
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> map) throws IOException {
        FormBody.Builder build = new FormBody.Builder();
        for (String key : map.keySet()) {
            build.add(key, map.get(key));
        }
        RequestBody body = build.build();
        // 创建请求方式
        Request request = new Request.Builder().url(url).post(body).build();

        String result = "";
        // 执行请求操作
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            result = response.body().string();
        }
        return result;
    }

    /**
     * post json
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String postJson(String url, String json) throws IOException {
        String result = "";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            result = response.body().string();
        }
        return result;
    }
}
