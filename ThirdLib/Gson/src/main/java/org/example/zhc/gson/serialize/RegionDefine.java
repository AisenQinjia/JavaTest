package org.example.zhc.gson.serialize;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 区服定义对应object
 * @author aisen
 */
@Getter
public class RegionDefine {

    String apolloIdc;
    ConfigCenter configCenter;
    List<String> serviceGroup;
    public RegionDefine(@NotNull String apolloIdc, ConfigCenter configCenter, List<String> serviceGroup){
        this.apolloIdc = apolloIdc;
        this.configCenter = configCenter;
        this.serviceGroup = serviceGroup;
    }
    @Getter
    public static class ConfigCenter{
        String productName;
        String productIdc;
    }

}
