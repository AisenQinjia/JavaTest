package com.example.zhc.k8s;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1StatefulSet;
import io.kubernetes.client.openapi.models.V1StatefulSetList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

import static org.example.FileUtil.readCharacterFileToStr;

/**
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OfficialApi {
    AppsV1Api appsV1Api;
    CoreV1Api coreV1Api;
    @BeforeAll
    public void beforeAll() throws IOException {
        Configuration.setDefaultApiClient(Config.fromConfig(KubeConfig.loadKubeConfig(new StringReader(readCharacterFileToStr("cert-config.ignore",false)))));
        appsV1Api = new AppsV1Api();
        coreV1Api = new CoreV1Api();

    }
    @Test
    public void updateStatefulSet() throws IOException, ApiException {
        try{
            String name = "stateful-world-service";
            String namespace = "tpfdemo-dev-aisen";
            String fileSelector = String.format("metadata.name=%s",name);

            V1StatefulSet statefulSet = readFileToObj("stateful-set.ignore",V1StatefulSet.class,false);
            // 没有就报异常...
//            V1StatefulSet readStateful = appsV1Api.readNamespacedStatefulSet(name, namespace, null, true, null);
            V1StatefulSetList listStatefuls = appsV1Api.listNamespacedStatefulSet(namespace, null, false, null, fileSelector, null, 1, null, null, false);
            boolean compatiable = compatiable(statefulSet, listStatefuls.getItems().get(0));
            V1StatefulSet currentStateful = appsV1Api.replaceNamespacedStatefulSet("stateful-world-service", "tpfdemo-dev-aisen", statefulSet, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

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
}
