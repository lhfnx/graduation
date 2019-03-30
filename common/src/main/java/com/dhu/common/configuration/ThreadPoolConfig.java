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
    private static final AtomicInteger count = new AtomicInteger(1);

    @Bean(name = "graduationThreadPool")
    public ExecutorService graduationServiceExecutor() {
        logger.info("开始加载线程池:graduationThreadPool");
        return new ThreadPoolExecutor(10, 10, 1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1000),
                r -> {
                    Thread t = new Thread(r, "GraduationThread-" + count.getAndIncrement());
                    t.setDaemon(false);
                    logger.info("graduationThreadPool启动新线程:" + t.getName());
                    return t;
                }, (r, executor) -> {
            if (!executor.isShutdown()) {
                logger.error("graduationThreadPool触发拒绝策略");
                r.run();
            }
        });
    }
}
