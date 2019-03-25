package com.dhu.model.DO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnalysisDO {
    private Integer num;
    private List<String> natures;
    private Integer days;
}
