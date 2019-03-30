package com.dhu.crawler.weibo;

import com.dhu.common.utils.StringToCollectionUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class WeiboIPUtils {
    private static final Logger logger = LoggerFactory.getLogger(WeiboIPUtils.class);
    @Autowired
    @Qualifier("graduationThreadPool")
    ExecutorService executorService;

    public List<String> execute() {
        List<CompletableFuture> completableFutures = Lists.newArrayList();
        List<String> request = Lists.newArrayList();
        List<String> result = Lists.newArrayList();
        for (String s : Lists.newArrayList("浙江","北京","上海","广东","山东","湖北")) {
            try {
//                Document hostDoc = Jsoup.connect("http://www.89ip.cn/tqdl.html?api=1&num=30&port=&address=" + s + "&isp=").get();
                String url = "http://www.66ip.cn/mo.php?sxb="+ URLEncoder.encode(s, "gbk")+"&tqsl=10&port=&export=&ktip=&sxa=&submit=";
                Document hostDoc = Jsoup.connect(url).url(new URL(url)).get();
                List<String> hosts = StringToCollectionUtils.stringToList(hostDoc.text(), " ");
                if (CollectionUtils.isNotEmpty(hosts)) {
//                    hosts.remove(hosts.size() - 1);
                    hosts.remove(0);
                    request.addAll(hosts);
                }
            } catch (Exception e) {
                logger.error("ip爬虫异常", e);
            }
        }
        try {
            completableFutures = request.stream().map(this::getIP).collect(Collectors.toList());
            CompletableFuture<Void> aVoid = CompletableFuture.allOf(completableFutures.stream().toArray(CompletableFuture[]::new));
            aVoid.get(10, TimeUnit.MINUTES);
            for (CompletableFuture future:completableFutures){
                if (future.isDone() && StringUtils.isNotEmpty((String) future.get())){
                    result.add((String) future.get());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("ip爬取异常",e);
        }
        return result;
    }

    private CompletableFuture<String> getIP(String ip) {
        return CompletableFuture.supplyAsync(() -> {
            Document document = null;
            try {
                document = Jsoup.connect("https://www.baidu.com")
                        .proxy(ip.split(":")[0], NumberUtils.toInt((ip.split(":")[1])))
                        .timeout(5 * 1000)
                        .get();
                return ip;
            } catch (Exception e) {
                return "";
            }
        }, executorService);
    }
}
