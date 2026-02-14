package com.mike.tanks.utils;

import java.util.HashMap;
import java.util.Map;

public class TankModelConverter {
    
    private static final Map<String, String> TANK_MODEL_MAP = new HashMap<>();
    
    static {
        // 初始化坦克车型映射关系
        TANK_MODEL_MAP.put("lightTank",     "轻坦");
        TANK_MODEL_MAP.put("mediumTank",    "中坦");
        TANK_MODEL_MAP.put("heavyTank",     "重坦");
        TANK_MODEL_MAP.put("SPG",           "火炮");
        TANK_MODEL_MAP.put("AT-SPG",        "坦歼");
    }
    
    /**
     * 将车型代码转换为中文名称
     * @param modelCode 车型代码
     * @return 对应的中文名称，如果找不到则返回原字符串
     */
    public static String convertToChinese(String modelCode) {
        if (modelCode == null || modelCode.trim().isEmpty()) {
            return modelCode;
        }
        
        String trimmedCode = modelCode.trim();
        return TANK_MODEL_MAP.getOrDefault(trimmedCode, trimmedCode);
    }
    
    /**
     * 批量转换车型代码
     * @param modelCodes 车型代码数组
     * @return 中文名称数组
     */
    public static String[] batchConvert(String[] modelCodes) {
        if (modelCodes == null) {
            return new String[0];
        }
        
        String[] results = new String[modelCodes.length];
        for (int i = 0; i < modelCodes.length; i++) {
            results[i] = convertToChinese(modelCodes[i]);
        }
        return results;
    }
    
    public static void main(String[] args) {
        // 测试代码
        String[] testModels = {"mediumTank", "heavyTank", "lightTank", "SPG", "AT-SPG"};
        
        System.out.println("=== 单个转换测试 ===");
        for (String model : testModels) {
            String chinese = convertToChinese(model);
            System.out.println(model + " -> " + chinese);
        }
        
        System.out.println("\n=== 批量转换测试 ===");
        String[] results = batchConvert(testModels);
        for (int i = 0; i < testModels.length; i++) {
            System.out.println(testModels[i] + " -> " + results[i]);
        }
    }
}