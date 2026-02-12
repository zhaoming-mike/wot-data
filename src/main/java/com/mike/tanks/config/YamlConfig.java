package com.mike.tanks.config;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YamlConfig {
    
    private final Map<String, Object> config;
    private static final Map<String, YamlConfig> CACHE = new ConcurrentHashMap<>();
    
    private YamlConfig(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream is = YamlConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("配置文件不存在: " + fileName);
            }
            this.config = yaml.load(is);
        } catch (Exception e) {
            throw new RuntimeException("加载配置失败: " + fileName, e);
        }
    }
    
    // 静态工厂方法
    public static YamlConfig of(String fileName) {
        return CACHE.computeIfAbsent(fileName, YamlConfig::new);
    }
    
    // 链式取值
    public String getString(String key) {
        return getString(key, null);
    }
    
    public String getString(String key, String defaultValue) {
        Object value = getValue(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    public Integer getInt(String key) {
        return getInt(key, null);
    }
    
    public Integer getInt(String key, Integer defaultValue) {
        Object value = getValue(key);
        if (value == null) return defaultValue;
        if (value instanceof Integer) return (Integer) value;
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }
    
    public Boolean getBoolean(String key, Boolean defaultValue) {
        Object value = getValue(key);
        if (value == null) return defaultValue;
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.parseBoolean(value.toString());
    }
    
    public Double getDouble(String key) {
        return getDouble(key, null);
    }
    
    public Double getDouble(String key, Double defaultValue) {
        Object value = getValue(key);
        if (value == null) return defaultValue;
        if (value instanceof Double) return (Double) value;
        if (value instanceof Integer) return ((Integer) value).doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public List<String> getList(String key) {
        Object value = getValue(key);
        if (value instanceof List) {
            return (List<String>) value;
        }
        return new ArrayList<>();
    }
    
    public Map<String, Object> getMap(String key) {
        Object value = getValue(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return new HashMap<>();
    }
    
    // 核心方法：通过点号获取嵌套值
    @SuppressWarnings("unchecked")
    private Object getValue(String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> current = config;
        
        for (int i = 0; i < keys.length - 1; i++) {
            Object value = current.get(keys[i]);
            if (!(value instanceof Map)) {
                return null;
            }
            current = (Map<String, Object>) value;
        }
        
        return current.get(keys[keys.length - 1]);
    }
    
    // 获取原始Map
    public Map<String, Object> getRawConfig() {
        return config;
    }
}