package org.example.zhc.apollo;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import org.junit.Before;
import org.junit.Test;

public class OpenApiApp {
    public static String COMPANY_PORTAL_URL = "http://192.168.10.142:28070";
    public static String COMPANY_TOKEN =  "5f9862364d15224e02ea877d9a3abddfa8951292";
    public static String APPID = "tpf-game-server-common";

    public static String ENV = "PRO";
    public static String NAMESPACE = "tpf.env.custom";
    public static String CLUSTER_AISEN = "wuhui-dev-aisen";
    public static String CLUSTER_DEFAULT = "default";


    public static ApolloOpenApiClient client;
    @Before
    public void createClient(){
        client = ApolloOpenApiClient.newBuilder()
                .withPortalUrl(COMPANY_PORTAL_URL)
                .withToken(COMPANY_TOKEN)
                .build();
    }

    @Test
    public void readNamespace(){
        OpenNamespaceDTO openNamespaceDTO = client.getNamespace(APPID,ENV,CLUSTER_AISEN,NAMESPACE);
        System.out.println(String.format("reading namespace_%s, cluster_%s",NAMESPACE, CLUSTER_DEFAULT));
        openNamespaceDTO.getItems().stream().forEach(item->{
            System.out.println(String.format("item key_%s, value_%s",item.getKey(),item.getValue()));
        });
    }

    @Test
    public void modifyItem(){
        OpenNamespaceDTO openNamespaceDTO = client.getNamespace(APPID,ENV,CLUSTER_DEFAULT,NAMESPACE);
        OpenItemDTO dto = openNamespaceDTO.getItems().get(0);
        String value = "1Gi";
        dto.setValue(value);

        System.out.println(String.format("modify Item_%s's  to %s",dto.getKey(), value));
        client.updateItem(APPID,ENV,CLUSTER_DEFAULT,NAMESPACE,dto);
    }

    @Test
    public void removeItem(){
        client.removeItem(APPID,ENV,CLUSTER_DEFAULT,NAMESPACE,"ip","tpf");
    }


    @Test
    public void publishNamespace(){
        NamespaceReleaseDTO releaseDTO = new NamespaceReleaseDTO();
        releaseDTO.setReleaseTitle("20210823202958-release");
        releaseDTO.setReleasedBy("tpf");
        client.publishNamespace(APPID,ENV,CLUSTER_DEFAULT,NAMESPACE,releaseDTO);
    }
}
