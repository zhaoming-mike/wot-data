package com.mike.tanks.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mike.tanks.config.YamlConfig;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.cert.X509Certificate;

public class APIUtils {

    private static SSLContext sslContext;
    private static TrustManager[] trustAllCerts;

    private static OkHttpClient client;

    static ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private static YamlConfig config = YamlConfig.of("params.yaml");
    private static String wg_host = config.getString("wg-host");
    private static String wx_host = config.getString("wx-host");
    private static String wg_application_id = config.getString("wg-application-id");
    private static String wx_webhook_tank_sheet_key = config.getString("wx-webhook.tank-sheet-key");

    static {
        try {
            // 创建信任所有证书的 TrustManager（空实现）
            trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    }
            };

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)  // 同时绕过主机名验证
                .build();
    }

    public static Object getAllVehicles(String tier, String nation) {
        Object result = null;
        String logicCall = config.getString("wg-api.vehicles");

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .addPathSegment(logicCall)
                .addQueryParameter("tier", tier)
                .addQueryParameter("nation", nation);

        result = makeCall(urlBuilder, result);
        return result;
    }

    public static JsonNode getAllVehiclesNode(String tier) {
        JsonNode result = null;
        String logicCall = config.getString("wg-api.vehicles");

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .addPathSegment(logicCall)
                .addQueryParameter("tier", tier);

        result = makeCall2Node(urlBuilder, result);
        return result;
    }

    public static JsonNode getAllVehiclesNode(String tier, String nation) {
        JsonNode result = null;
        String logicCall = config.getString("wg-api.vehicles");

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .addPathSegment(logicCall)
                .addQueryParameter("tier", tier)
                .addQueryParameter("nation", nation);

        result = makeCall2Node(urlBuilder, result);
        return result;
    }

    public static Object getAllVehicles(String tier) {
        Object result = null;
        String logicCall = config.getString("wg-api.vehicles");

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .addPathSegment(logicCall)
                .addQueryParameter("tier", tier);

        result = makeCall(urlBuilder, result);
        return result;
    }

    public static Object findPlayer(String nickname) {
        Object result = null;
        String logicCall = config.getString("wg-api.account.list");

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .addPathSegment(logicCall)
                .addQueryParameter("search", nickname);

        result = makeCall(urlBuilder, result);
        return result;
    }

    private static JsonNode makeCall2Node(HttpUrl.Builder urlBuilder, JsonNode result) {
        urlBuilder.scheme("https")
                .addQueryParameter("application_id", wg_application_id)
                .host(wg_host);
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();

            result = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Object makeCall(HttpUrl.Builder urlBuilder, Object result) {
        urlBuilder.scheme("https")
                .addQueryParameter("application_id", wg_application_id)
                .host(wg_host);
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();

            result = mapper.readValue(json, Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void pushData2WX(String jsonData) {
        String logicCall = config.getString("wx-api.smartsheet-webhook");

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .addPathSegment(logicCall);

        push2WX(urlBuilder, jsonData);
    }

    private static void push2WX(HttpUrl.Builder urlBuilder, String jsonData) {
        urlBuilder.scheme("https")
                .addQueryParameter("key", wx_webhook_tank_sheet_key)
                .host(wx_host);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                jsonData
        );

        Request request = new Request.Builder()
                .url(URLDecoder.decode(urlBuilder.build().toString())) // 腾讯的接口不能正确解码，需要手工处理。
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            byte[] bytes = response.body().bytes();
            System.out.println(new String(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
