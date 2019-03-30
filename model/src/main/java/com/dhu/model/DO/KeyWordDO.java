package com.dhu.model.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyWordDO {
    private String keyWord;
    private String nature;

    @Override
    public int hashCode() {
        return this.getKeyWord().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyWordDO)){
            return false;
        }
        return this.getKeyWord().equals(((KeyWordDO) obj).getKeyWord());
    }
}
