package org.example.zhc.docker;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CheckRemoteImage {

    public static void main(String[] args) {
        String account = "tkestack";
        String password = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE5ODA3NDcwMzUsImp0aSI6IjdhYTciLCJpYXQiOjE2NjUzODcwMzUsInVzciI6ImppYW50dSIsInRlZCI6ImRlZmF1bHQifQ.anEJJzQS1XYisFfMc1uWNMTo-ZKu7SDQh9X49sTnggvc3rUb-tusibjxBa2oittAOsRbgeL8d_7UD85bxiNIBaaKgrthMplZaU3ZuaRdziDE34mTkOZ8dH54gtpenpRAr7q45QtXAQ6ClE9PQaIs1Y21wb88mk5gzVVqOklXA9X0ZeHtMwZf-cZz-8y3bZX_bEnT6pqLO6XIaMA1FahYhyE8Qs2SXiHrayyYaBae1WMVBPwI2SdF8_fkpqAKnVe0OYUHvyq7IGAOMevusGuzYzvUNVafexwCC8jaBsU21bIPMhJVlYVLnX1BFwdsnR-ToGNuIX9u19azK6dEPLVIkA";
        String repository = "syyx-tpf/tpf-managercenter-client";
        String tag = "1.0.0.11";

        boolean imageExists = checkImageExists(account, password, repository, tag);
        System.out.println("Image exists: " + imageExists);
    }

    public static boolean checkImageExists(String account, String password, String repository, String tag) {
        String registryUrl = "http://default.registry.tke-syyx.com/v2/";
        String imageUrl = registryUrl + repository + "/manifests/" + tag;

        CloseableHttpClient httpClient = createInsecureHttpClient();
        HttpGet httpGet = new HttpGet(imageUrl);

        try {
            CloseableHttpResponse re = httpClient.execute(httpGet);
            int statusCode = re.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                // Get the WWW-Authenticate header value
                String wwwAuthenticate = re.getFirstHeader(HttpHeaders.WWW_AUTHENTICATE).getValue();
                re.close();
                // Extract the realm, service, and scope
                Pattern pattern = Pattern.compile("Bearer realm=\"(.*?)\",service=\"(.*?)\",scope=\"(.*?)\"");
                Matcher matcher = pattern.matcher(wwwAuthenticate);
                String realm = "";
                String service = "";
                String scope = "";

                if (matcher.find()) {
                    realm = matcher.group(1);
                    service = matcher.group(2);
                    scope = matcher.group(3);
                }

                // Request the access token
                scope = scope.replace("default-","");
                String authUrl = realm + "?service=" + service + "&scope=" + scope;
                HttpGet httpGetToken = new HttpGet(authUrl);
                httpGetToken.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((account + ":" + password).getBytes()));
                CloseableHttpResponse response = httpClient.execute(httpGetToken);
                int tokenStatusCode = response.getStatusLine().getStatusCode();
                if (tokenStatusCode == HttpStatus.SC_OK) {
                    String tokenResponse = EntityUtils.toString(response.getEntity());
                    JSONObject tokenJson = JSONObject.parseObject(tokenResponse);
                    String token = tokenJson.getString("token");
                    response.close();

                    // Retry the request with the access token
                    HttpGet httpGet2 = new HttpGet(imageUrl);
                    httpGet2.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                    statusCode = httpClient.execute(httpGet2).getStatusLine().getStatusCode();

                    return statusCode == HttpStatus.SC_OK;
                }
            }
            if (statusCode == HttpStatus.SC_OK){
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static CloseableHttpClient createInsecureHttpClient() {
        try {
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            return true; // Accept all certificates
                        }
                    })
                    .build();

            SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionFactory)
                    .build();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10*1000)
                    .setConnectionRequestTimeout(5*1000)
                    .setSocketTimeout(5*1000)
                    .build();
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
//            connectionManager.setMaxTotal(200);
//            connectionManager.setDefaultMaxPerRoute(100);

            return HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setConnectionManager(connectionManager)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
