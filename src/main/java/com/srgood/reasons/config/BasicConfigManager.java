package com.srgood.reasons.config;

import java.util.Collections;
import java.util.Map;

public interface BasicConfigManager {
    default String getProperty(String property) {
        return getProperty(property, null);
    }
    default String getProperty(String property, String defaultValue) {
        return getProperty(property, defaultValue, false);
    }
    String getProperty(String property, String defaultValue, boolean setIfMissing);
    void setProperty(String property, String value);

    default <T> T getSerializedProperty(String property) {
        return getSerializedProperty(property,  null);
    }
    default <T> T getSerializedProperty(String property, T defaultValue) {
        return getSerializedProperty(property, defaultValue, false);
    }
    default<T> T getSerializedProperty(String property, T defaultValue, boolean setIfMissing) {
        return getSerializedProperty(property, defaultValue, setIfMissing, Collections.emptyMap());
    }
    <T> T getSerializedProperty(String property, T defaultValue, boolean setIfMissing, Map<String, String> classReplacements);
    void setSerializedProperty(String property, Object value);
}
