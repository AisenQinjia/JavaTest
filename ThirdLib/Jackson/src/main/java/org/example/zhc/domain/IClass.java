package org.example.zhc.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as=IClassImpl.class)
public interface IClass {
    void ctor();
}
