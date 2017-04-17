package com.srgood.reasons.config;

import java.io.Serializable;

public interface BasicConfigManager {
    default String getProperty(String property) {
        return getProperty(property, null);
    }
    default String getProperty(String property, String defaultValue) {
        return getProperty(property, defaultValue, false);
    }
    String getProperty(String property, String defaultValue, boolean setIfMissing);
    void setProperty(String property, String value);

    default <T extends Serializable> T getSerializedProperty(String property, Class<? extends T> propertyClass) {
        return getSerializedProperty(property, propertyClass, null);
    }
    default <T extends Serializable> T getSerializedProperty(String property, Class<? extends T> propertyClass, T defaultValue) {
        return getSerializedProperty(property, propertyClass, defaultValue, false);
    }
    default<T extends Serializable> T getSerializedProperty(String property, Class<? extends T> propertyClass, T defaultValue, boolean setIfMissing) {
        return getSerializedProperty(property, propertyClass, defaultValue, setIfMissing, Collections.emptyMap());
    }
    <T extends Serializable> T getSerializedProperty(String property, Class<? extends T> propertyClass, T defaultValue, boolean setIfMissing, Map<String, String> classReplacements);
    void setSerializedProperty(String property, Serializable value);
}
