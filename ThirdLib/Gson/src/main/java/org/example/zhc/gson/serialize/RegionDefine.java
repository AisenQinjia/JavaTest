package org.example.zhc.gson.serialize;

import lombok.Getter;

import java.util.List;

/**
 * 区服定义对应object
 * @author aisen
 */
public class RegionDefine {

    String apolloIdc;
    ConfigCenter configCenter;
    List<String> serviceGroup;
    static class ConfigCenter{
        String productName;
        String productIdc;
    }

}
