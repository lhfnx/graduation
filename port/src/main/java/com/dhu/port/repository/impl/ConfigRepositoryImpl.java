package com.dhu.port.repository.impl;

import com.dhu.port.entity.Config;
import com.dhu.port.mapper.ConfigMapper;
import com.dhu.port.repository.ConfigRepository;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class ConfigRepositoryImpl implements ConfigRepository {
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public Map<String, String> queryAll() {
        List<Config> configs = configMapper.queryAll();
        if (CollectionUtils.isEmpty(configs)) {
            return Maps.newHashMap();
        }
        return configs.stream().collect(Collectors.toMap(Config::getKey, Config::getValue, (c1, c2) -> c2));
    }
}
