package org.example.zhc.domain.getter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractGetterClass implements IGetClass {
    @Override
    @JsonIgnore
    public String getType() {
        return "2";
    }
}
