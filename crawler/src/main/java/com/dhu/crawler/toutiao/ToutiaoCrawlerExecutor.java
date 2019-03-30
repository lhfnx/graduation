package com.dhu.crawler.toutiao;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ToutiaoCrawlerExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoCrawlerExecutor.class);
    @Autowired
    @Qualifier("graduationThreadPool")
    ExecutorService executorService;

    @Scheduled(cron = "0 0 0/5 * * ?")
    public void execute() {
        File file = new File("src\\main\\java\\com\\dhu\\crawler\\toutiao");
        String c = "scrapy crawl toutiao";
        InputStream is2 = null;
        BufferedReader br2 = null;
        List<CompletableFuture> completableFutures = Lists.newArrayListWithCapacity(2);
        try {
            Process process = Runtime.getRuntime().exec(c, null, file);
            completableFutures.add(inputStreamLog(process.getInputStream()));
            completableFutures.add(inputStreamLog(process.getErrorStream()));
            CompletableFuture.allOf(completableFutures.stream().toArray(CompletableFuture[]::new))
                    .get(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("头条爬虫异常", e);
        }
    }

    private CompletableFuture<Void> inputStreamLog(InputStream inputStream) {
        return CompletableFuture.runAsync(() -> {
            String line = null;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"gbk"));
                while ((line = br.readLine()) != null) {
                    logger.info(line);
                }
            } catch (Exception e) {
                logger.error("Process异常", e);
            }
        }, executorService);
    }
}
