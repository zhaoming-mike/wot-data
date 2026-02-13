package com.mike.tanks.utils;

import java.util.HashMap;
import java.util.Map;

public class CountryCodeConverter {
    private static final Map<String, String> COUNTRY_MAP = new HashMap<>();
    
    static {
        // 初始化国家简称映射
        COUNTRY_MAP.put("china",    "中国");
        COUNTRY_MAP.put("czech",    "捷克");
        COUNTRY_MAP.put("france",   "法国");
        COUNTRY_MAP.put("germany",  "德国");
        COUNTRY_MAP.put("italy",    "意大利");
        COUNTRY_MAP.put("japan",    "日本");
        COUNTRY_MAP.put("poland",   "波兰");
        COUNTRY_MAP.put("sweden",   "瑞典");
        COUNTRY_MAP.put("uk",       "英国");
        COUNTRY_MAP.put("usa",      "美国");
        COUNTRY_MAP.put("ussr",     "苏联");
        // 继续添加其他国家的映射...
    }
    
    public static String convert(String shortName) {
        if (shortName == null) return "神秘国家";
        return COUNTRY_MAP.getOrDefault(shortName.toLowerCase(), shortName);
    }
    
    public static void main(String[] args) {
        String[] tests = {"ussr", "cezach", "cn", "unknown"};
        for (String test : tests) {
            System.out.println(test + " -> " + convert(test));
        }
    }
}