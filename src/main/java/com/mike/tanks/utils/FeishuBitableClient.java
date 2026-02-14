package com.mike.tanks.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FeishuBitableClient {
    
    // 飞书开放平台域名
    private static final String FEISHU_HOST = "https://open.feishu.cn";
    
    // Jackson ObjectMapper
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // OkHttpClient
    private final OkHttpClient httpClient = new OkHttpClient();
    
    // 从飞书开发者后台获取
    private String appId;
    private String appSecret;
    private String appToken;        // 多维表格的App Token
    private String tableId;          // 数据表的ID
    
    public FeishuBitableClient(String appId, String appSecret, String appToken, String tableId) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.appToken = appToken;
        this.tableId = tableId;
    }
    
    /**
     * 获取tenant_access_token
     */
    private String getTenantAccessToken() throws IOException {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("app_id", appId);
        requestBody.put("app_secret", appSecret);
        
        Request request = new Request.Builder()
                .url(FEISHU_HOST + "/open-apis/auth/v3/tenant_access_token/internal")
                .post(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        objectMapper.writeValueAsString(requestBody)))
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("获取token失败: " + response);
            }
            
            String responseBody = response.body().string();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            
            if (jsonResponse.has("code") && jsonResponse.get("code").asInt() == 0) {
                return jsonResponse.get("tenant_access_token").asText();
            } else {
                String msg = jsonResponse.has("msg") ? jsonResponse.get("msg").asText() : "未知错误";
                throw new IOException("获取token失败: " + msg);
            }
        }
    }
    
    /**
     * 向多维表格添加记录
     * @param fields 字段数据，key为字段名，value为字段值
     * @return 添加的记录ID
     */
    public String addRecord(Map<String, Object> fields) throws IOException {
        String token = getTenantAccessToken();
        
        // 构建请求体
        ObjectNode requestBody = objectMapper.createObjectNode();
        
        // 添加字段
        ObjectNode fieldsNode = objectMapper.createObjectNode();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                fieldsNode.put(entry.getKey(), (String) value);
            } else if (value instanceof Integer) {
                fieldsNode.put(entry.getKey(), (Integer) value);
            } else if (value instanceof Long) {
                fieldsNode.put(entry.getKey(), (Long) value);
            } else if (value instanceof Float) {
                fieldsNode.put(entry.getKey(), (Float) value);
            } else if (value instanceof Double) {
                fieldsNode.put(entry.getKey(), (Double) value);
            } else if (value instanceof Boolean) {
                fieldsNode.put(entry.getKey(), (Boolean) value);
            } else if (value == null) {
                fieldsNode.putNull(entry.getKey());
            } else if (value instanceof Map) {
                ObjectNode objNode = objectMapper.createObjectNode();
                Map<String, String> objMap = (Map<String, String>) value;
                for (Map.Entry<String, String> next : objMap.entrySet()) {
                    objNode.put(next.getKey(), next.getValue());
                }
                fieldsNode.put(entry.getKey(), objNode);
            } else {
                fieldsNode.put(entry.getKey(), value.toString());
            }
        }
        requestBody.set("fields", fieldsNode);
        
        // 构建请求
        String url = String.format("%s/open-apis/bitable/v1/apps/%s/tables/%s/records",
                FEISHU_HOST, appToken, tableId);
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        objectMapper.writeValueAsString(requestBody)))
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("添加记录失败: " + response);
            }
            
            String responseBody = response.body().string();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            
            if (jsonResponse.has("code") && jsonResponse.get("code").asInt() == 0) {
                JsonNode data = jsonResponse.get("data");
                if (data != null && data.has("record")) {
                    JsonNode record = data.get("record");
                    if (record.has("record_id")) {
                        return record.get("record_id").asText();
                    }
                }
                throw new IOException("添加记录成功但未返回记录ID");
            } else {
                String msg = jsonResponse.has("msg") ? jsonResponse.get("msg").asText() : "未知错误";
                throw new IOException("添加记录失败: " + msg);
            }
        }
    }
    
    /**
     * 批量添加记录
     * @param recordsList 多条记录的数据列表，每条记录是一个字段Map
     * @return 添加的记录ID列表
     */
    public java.util.List<String> batchAddRecords(java.util.List<Map<String, Object>> recordsList) throws IOException {
        String token = getTenantAccessToken();
        
        // 构建请求体
        ObjectNode requestBody = objectMapper.createObjectNode();
        ArrayNode recordsArray = objectMapper.createArrayNode();
        
        for (Map<String, Object> fields : recordsList) {
            ObjectNode recordNode = objectMapper.createObjectNode();
            ObjectNode fieldsNode = objectMapper.createObjectNode();
            
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    fieldsNode.put(entry.getKey(), (String) value);
                } else if (value instanceof Integer) {
                    fieldsNode.put(entry.getKey(), (Integer) value);
                } else if (value instanceof Long) {
                    fieldsNode.put(entry.getKey(), (Long) value);
                } else if (value instanceof Float) {
                    fieldsNode.put(entry.getKey(), (Float) value);
                } else if (value instanceof Double) {
                    fieldsNode.put(entry.getKey(), (Double) value);
                } else if (value instanceof Boolean) {
                    fieldsNode.put(entry.getKey(), (Boolean) value);
                } else if (value == null) {
                    fieldsNode.putNull(entry.getKey());
                } else if (value instanceof Map) {
                    ObjectNode objNode = objectMapper.createObjectNode();
                    Map<String, String> objMap = (Map<String, String>) value;
                    for (Map.Entry<String, String> next : objMap.entrySet()) {
                        objNode.put(next.getKey(), next.getValue());
                    }
                    fieldsNode.put(entry.getKey(), objNode);
                } else {
                    fieldsNode.put(entry.getKey(), value.toString());
                }
            }
            
            recordNode.set("fields", fieldsNode);
            recordsArray.add(recordNode);
        }
        
        requestBody.set("records", recordsArray);
        
        // 构建请求（批量添加接口）
        String url = String.format("%s/open-apis/bitable/v1/apps/%s/tables/%s/records/batch_create",
                FEISHU_HOST, appToken, tableId);
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        objectMapper.writeValueAsString(requestBody)))
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("批量添加记录失败: " + response);
            }
            
            String responseBody = response.body().string();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            
            if (jsonResponse.has("code") && jsonResponse.get("code").asInt() == 0) {
                JsonNode data = jsonResponse.get("data");
                if (data != null && data.has("records")) {
                    java.util.List<String> recordIds = new java.util.ArrayList<>();
                    JsonNode recordsResult = data.get("records");
                    
                    for (JsonNode record : recordsResult) {
                        if (record.has("record_id")) {
                            recordIds.add(record.get("record_id").asText());
                        }
                    }
                    
                    return recordIds;
                }
                throw new IOException("批量添加成功但未返回记录ID");
            } else {
                String msg = jsonResponse.has("msg") ? jsonResponse.get("msg").asText() : "未知错误";
                throw new IOException("批量添加记录失败: " + msg);
            }
        }
    }
    
    // 使用示例
    public static void main(String[] args) {

        // 请替换为您的实际配置
        String appId = "cli_a6cf3875673e900c";
        String appSecret = "5BiHt0nbF6It10jlnneTMha1eaLz2t2Z";

        //多维表格链接中 base 和 ?table 之间的那串字符
        String appToken = "Z3GxbAzWKawbxCsqHp1clPkMnfe";

        //多维表格链接中 ?table 等号后面的那串字符
        String tableId = "tblDS2MBxhAFY4pp";

        //例子：https://microming.feishu.cn/base/Z3GxbAzWKawbxCsqHp1clPkMnfe?table=tblDS2MBxhAFY4pp&view=vewp8CtDzq
        
        FeishuBitableClient client = new FeishuBitableClient(appId, appSecret, appToken, tableId);
        
        try {
            // 示例1：添加单条记录
            Map<String, Object> fields = new HashMap<>();
            fields.put("TANK ID", 123);
            fields.put("国家", "美国");
            fields.put("等级", "V");
            fields.put("金币车", "是");
            fields.put("默认炮火均伤", 400);
            fields.put("百米散布", .34);
            fields.put("俯角（度°）", 10);
            fields.put("坦克名称", "PTZ-78");

            Map<String, Object> link = new HashMap<>();
            link.put("text", "点击查看");
            link.put("link", "http://api.worldoftanks.asia/static/2.77.0/wot/encyclopedia/vehicle/ussr-R04_T-34.png");
            fields.put("预览", link);

            String recordId = client.addRecord(fields);
             System.out.println("添加记录成功，记录ID: " + recordId);
            
            // 示例2：批量添加多条记录
            java.util.List<Map<String, Object>> recordsList = new java.util.ArrayList<>();
            
            Map<String, Object> record1 = new HashMap<>();
            record1.put("文本", "李四");
            recordsList.add(record1);
            
            Map<String, Object> record2 = new HashMap<>();
            record2.put("文本", "王五");
            recordsList.add(record2);
            
//            java.util.List<String> recordIds = client.batchAddRecords(recordsList);
//            System.out.println("批量添加成功，记录ID列表: " + recordIds);
            
        } catch (IOException e) {
            System.err.println("操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}