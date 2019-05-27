package com.dhu.common.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@ComponentScan("com.dhu.common.configuration")
public class ThreadPoolConfig {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);
    private static final AtomicInteger weiBoCount = new AtomicInteger(1);
    private static final AtomicInteger touTiaoCount = new AtomicInteger(1);

    //微博爬虫线程池
    @Bean(name = "weiBoPool")
    public ExecutorService wiBoExecutor() {
        logger.info("开始加载线程池:wiBoPool");
        return new ThreadPoolExecutor(10, 10, 1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1000),
                r -> {
                    Thread t = new Thread(r, "weiBoThread-" + weiBoCount.getAndIncrement());
                    t.setDaemon(false);
                    logger.info("weiBoPool启动新线程:" + t.getName());
                    return t;
                }, (r, executor) -> {
            if (!executor.isShutdown()) {
                logger.error("weiBoPool触发拒绝策略");
                r.run();
            }
        });
    }

    //头条爬虫线程池
    @Bean(name = "touTiaoPool")
    public ExecutorService touTiaoExecutor() {
        logger.info("开始加载线程池:touTiaoPool");
        return new ThreadPoolExecutor(2, 2, 1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1000),
                r -> {
                    Thread t = new Thread(r, "touTiaoThread-" + touTiaoCount.getAndIncrement());
                    t.setDaemon(false);
                    logger.info("touTiaoPool启动新线程:" + t.getName());
                    return t;
                }, (r, executor) -> {
            if (!executor.isShutdown()) {
                logger.error("touTiaoPool触发拒绝策略");
                r.run();
            }
        });
    }
}
