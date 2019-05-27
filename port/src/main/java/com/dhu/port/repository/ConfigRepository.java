package com.dhu.port.repository;

import java.util.Map;

/**
 * 配置表
 */
public interface ConfigRepository {
    /**
     * 读取所有
     * @return
     */
    Map<String,String> queryAll();
}
