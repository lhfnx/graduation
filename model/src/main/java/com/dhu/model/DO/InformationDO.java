package com.dhu.model.DO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class InformationDO {
    private String author = "佚名";
    private String comment = "0";
    private String like = "0";
    private String reading = "0";
    private String forward = "0";
    private String favorite = "0";
}
