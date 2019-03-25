package com.dhu.port.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class Config {
    private Long id;
    private String key;
    private String value;
    private Timestamp DataChangeLastTime;
    private Boolean isActive;
    private String description;
}
