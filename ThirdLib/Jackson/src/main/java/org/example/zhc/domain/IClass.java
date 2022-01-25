package org.example.zhc.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public interface IClass {
    void ctor();
    void assertEqual(IClass iClass);
}
