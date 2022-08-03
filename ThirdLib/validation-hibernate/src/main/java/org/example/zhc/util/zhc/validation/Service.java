package org.example.zhc.util.zhc.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@Slf4j
@Builder
@ServiceConstraint
public class Service {
    String name;
    @NotBlank(message = "namespace 不能为空!")
    String namespace;
    ServiceType type;
    String attachModule;

    List<@NotNull Port> ports;
    String slbName;
    String slbId;
    String externalTrafficPolicy;
    Boolean isIntranet;
    String externalName;

    public static final String ALI_SLB_ID = "service.beta.kubernetes.io/alicloud-loadbalancer-id";
    public static final String ALI_SLB_INTTANET = "service.beta.kubernetes.io/alicloud-loadbalancer-address-type";
    public void init(String namespace){
        this.namespace = namespace;
        assertAndInit();
    }

    public void assertAndInit(){
        log.info("service: name: {} namespace: {} type:{} attachModule: {} port: {} slbId: {} slbName: {} externalName:{}",name,namespace,type,attachModule,ports,slbId,slbName,externalName);
        //        Assertions.assertNotNull(type,"服务类型不能为空!");
//        Assertions.assertNotNull(ports,"端口不能为空!");
//        Assertions.assertTrue(name != null && !name.isEmpty(),"名字不能为空!");
//        if(type == ServiceType.SLB || type == ServiceType.ALISLB){
//            Assertions.assertTrue(attachModule != null && !attachModule.isEmpty(),"attachModule不能为空!");
//            for (Port p : ports){
//                p.init();
//            }
//            if(type == ServiceType.ALISLB){
//                Assertions.assertTrue(slbId != null && !slbId.isEmpty(),"slbId不能为空!");
//            }
//        }
//        if(type == ServiceType.EXTERNAL){
//            Assertions.assertTrue(externalName != null && !externalName.isEmpty(),"externalName不能为空!");
//        }
    }


}
