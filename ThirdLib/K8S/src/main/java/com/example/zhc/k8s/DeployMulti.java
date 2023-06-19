package com.example.zhc.k8s;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1StatefulSet;
import io.kubernetes.client.openapi.models.V1StatefulSetList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.zhc.util.FileUtil.readCharacterFileToStr;

/**
 *
 */
@Slf4j
public class DeployMulti {
    static AppsV1Api appsV1Api;
    static CoreV1Api coreV1Api;
    static Gson gson = new Gson();

    public static void main(String[] args){
        try {
            // 设置日志输出等级为 INFO
            Configurator.setRootLevel(org.apache.logging.log4j.Level.INFO);
            init();
            com.example.zhc.k8s.Config config = gson.fromJson(readCharacterFileToStr("dsConfig.json",false), com.example.zhc.k8s.Config.class);
            config.trimAndCheck();
            String namespace = config.getNamespace();
            String dsImageName = config.getDsImageName();
            String registry = config.getImageRegistry();
            String registryDir = config.getImageDir();
            String userName = config.getImageUserName();
            String password = config.getImagePassword();
            log.info("namespace: {}, dsImageName: {}",namespace,dsImageName);
            // 推送镜像
            String imageNameWithRep = String.format("%s%s%s",registry,registryDir,dsImageName);
            List<String> execResult = new ArrayList<>();
            List<String> execCommand = new ArrayList<>();
            execCommand.add(String.format("docker login -u %s -p \"%s\" \"%s\"",userName,password,registry));
            execCommand.add(String.format("docker build -t %s .",imageNameWithRep));
            execCommand.add(String.format("docker push %s",imageNameWithRep));
            boolean result = CommandUtil.executeCommand(execCommand, execResult);
            log.info("push image {} result: {}",imageNameWithRep,result);
            if(!result){
                log.error("push image {} failed!",imageNameWithRep);
                return;
            }
            V1StatefulSet statefulSet = readFileToObj("stateful-container-proxy.yaml",V1StatefulSet.class,false);
            V1Service statefulSetSvc = readFileToObj("stateful-container-proxy-service.yaml",V1Service.class,false);
            // 设置镜像名称
            statefulSet.getSpec().getTemplate().getSpec().getContainers().get(1).setImage(imageNameWithRep);
            //更新svc
            createService(namespace,statefulSetSvc,coreV1Api);
            //更新statefulSet
            updateStatefulSet(namespace,statefulSet,appsV1Api);
            log.info("{}更新{}成功!",namespace,dsImageName);
        }catch (Exception e){
            log.error("更新失败!",e);
        }
    }
    public static void init() throws IOException {
        Configuration.setDefaultApiClient(Config.fromConfig(KubeConfig.loadKubeConfig(new StringReader(readCharacterFileToStr("./k8sCertFile.ignore",false)))));
        appsV1Api = new AppsV1Api();
        coreV1Api = new CoreV1Api();
    }
//    @Test
//    public void sdf() throws UnknownHostException {
//        byte[] ipAddr = new byte[] { 127, 0, 0, 1 };
//        String canonicalHostName = InetAddress.getByAddress(ipAddr).getCanonicalHostName();
//    }
//    @Test
//    public void updateStatefulSet() throws IOException, ApiException {
//        try{
//            String name = "stateful-world-service";
//            String namespace = "tpfdemo-dev-aisen";
//            String fileSelector = String.format("metadata.name=%s",name);
//
//            V1StatefulSet statefulSet = readFileToObj("stateful-set.ignore",V1StatefulSet.class,false);
//            // 没有就报异常...
////            V1StatefulSet readStateful = appsV1Api.readNamespacedStatefulSet(name, namespace, null, true, null);
//            V1StatefulSetList listStatefuls = appsV1Api.listNamespacedStatefulSet(namespace, null, false, null, fileSelector, null, 1, null, null, false);
//            boolean compatiable = compatiable(statefulSet, listStatefuls.getItems().get(0));
//            V1StatefulSet currentStateful = appsV1Api.replaceNamespacedStatefulSet("stateful-world-service", "tpfdemo-dev-aisen", statefulSet, null, null, null);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

    public static <T> T  readFileToObj(String fileName, Class<T> obj,boolean isResources) throws IOException {
        String content = readCharacterFileToStr(fileName,isResources);
        return Yaml.loadAs(content, obj);
    }

    public static boolean compatiable(V1StatefulSet statefulSet1, V1StatefulSet statefulSet2){
        String podManagementPolicy1 = statefulSet1.getSpec().getPodManagementPolicy();
        String podManagementPolicy2 = statefulSet2.getSpec().getPodManagementPolicy();
        String matchLabels1 = statefulSet1.getSpec().getSelector().getMatchLabels().get("app");
        String matchLabels2 = statefulSet2.getSpec().getSelector().getMatchLabels().get("app");
        String serviceName1 = statefulSet1.getSpec().getServiceName();
        String serviceName2 = statefulSet2.getSpec().getServiceName();
        return Objects.equals(podManagementPolicy1,podManagementPolicy2)
                && Objects.equals(matchLabels1,matchLabels2)
                && Objects.equals(serviceName1,serviceName2);
    }

    public static void createService(String namespace,V1Service service, CoreV1Api coreV1Api) throws ApiException {
        String serviceName = service.getMetadata().getName();
        String fileSelector = String.format("metadata.name=%s", serviceName);
        V1ServiceList serviceList = coreV1Api.listNamespacedService(namespace, null, false, null, fileSelector, null, 1, null, null, false);
        log.info("createService begin: namespace:{},name: {}", namespace, serviceName);
        if (serviceList == null || serviceList.getItems().size() == 0) {
            log.info(String.format("create service finish: %s", serviceName));
            coreV1Api.createNamespacedService(namespace, service, null, null, null);
        } else {
            log.info(String.format("service exist skip it: %s", serviceName));
        }
    }
    public static void deleteServiceCatchError(String ns, String name, CoreV1Api coreV1Api) throws ApiException {
        try {
            coreV1Api.deleteNamespacedService(name,ns,null,null,null,null,null,null);
            log.info("Deleted service {} in ns:{}" ,name,  ns);
        }
        catch (JsonSyntaxException e) {
            if (e.getCause() instanceof IllegalStateException) {
                IllegalStateException ise = (IllegalStateException) e.getCause();
                if (ise.getMessage() != null && ise.getMessage().contains("Expected a string but was BEGIN_OBJECT"))
                    log.warn("Catching exception because of issue https://github.com/kubernetes-client/java/issues/86");
                else throw e;
            }
            else throw e;
        }
    }

    /**
     * 更新有状态服务
     */
    public static void updateStatefulSet(String namespace, V1StatefulSet statefulSet, AppsV1Api appsV1Api) throws ApiException {
        String statefulSetName = statefulSet.getMetadata().getName();
        String fileSelector = String.format("metadata.name=%s",statefulSetName);
        V1StatefulSetList statefulSetList = appsV1Api.listNamespacedStatefulSet(namespace,null,false,null,fileSelector,null,1,null,null,false);
        if(statefulSetList == null || statefulSetList.getItems().size() == 0){
            log.info(String.format("create statefulSet: %s", statefulSetName));
            appsV1Api.createNamespacedStatefulSet(namespace,statefulSet,null,null,null);
        }else{
            //是否为不兼容的更新
            if(compatiable(statefulSet,statefulSetList.getItems().get(0))){
                log.info(String.format("update statefulSet: %s",statefulSetName));
                appsV1Api.replaceNamespacedStatefulSet(statefulSetName,namespace,statefulSet,null,null,null);
            }else{
                log.info(String.format("delete statefulSet: %s",statefulSetName));
                deleteStatefulSetCatchError(namespace,statefulSetName,appsV1Api);
                log.info("delete response success!");
                log.info(String.format("create statefulSet: %s",statefulSetName));
                appsV1Api.createNamespacedStatefulSet(namespace,statefulSet,null,null,null);
            }

        }
    }
    public static void deleteStatefulSetCatchError(String ns, String name, AppsV1Api appsV1Api) throws ApiException {
        try {
            appsV1Api.deleteNamespacedStatefulSet(name, ns, null, null, 1, null, null, null);
            log.info("Deleted statefulSet:{}" ,name);
        }
        catch (JsonSyntaxException e) {
            if (e.getCause() instanceof IllegalStateException) {
                IllegalStateException ise = (IllegalStateException) e.getCause();
                if (ise.getMessage() != null && ise.getMessage().contains("Expected a string but was BEGIN_OBJECT"))
                    log.warn("Catching exception because of issue https://github.com/kubernetes-client/java/issues/86");
                else throw e;
            }
            else throw e;
        }
    }
}
