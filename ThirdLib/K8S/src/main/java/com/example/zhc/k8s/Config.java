package com.example.zhc.k8s;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@Slf4j
public class Config implements Serializable {
    public String namespace;
    String dsImageName;
    String imageRegistry;
    String imageDir;
    String imageUserName;
    String imagePassword;

    public void trimAndCheck(){
        if (StringUtils.isBlank(namespace) || StringUtils.isBlank(dsImageName)
                || StringUtils.isBlank(imageRegistry) || StringUtils.isBlank(imageDir)
                || StringUtils.isBlank(imageUserName) || StringUtils.isBlank(imagePassword)){
            log.error("dsConfig.json 有空字段");
            throw new RuntimeException("namespace or dsImageName is empty");
        }
        namespace = namespace.trim();
        dsImageName = dsImageName.trim();
        imageRegistry = imageRegistry.trim();
        imageDir = imageDir.trim();
        imageUserName = imageUserName.trim();
        imagePassword = imagePassword.trim();
    }
}
