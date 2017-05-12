package com.srgood.reasons.impl.config;

import com.srgood.reasons.config.BasicConfigManager;
import org.w3c.dom.Element;

import java.io.*;
import java.util.Base64;
import java.util.Map;

public class BasicConfigManagerImpl implements BasicConfigManager {
    private final Element operationalElement;

    public BasicConfigManagerImpl(Element operationalElement) {
        this.operationalElement = operationalElement;
    }

    @Override
    public String getProperty(String property, String defaultValue, boolean setIfMissing) {
        Element propertyElement = parseProperty(property, setIfMissing);
        if (propertyElement == null) {
            return defaultValue;
        }
        return propertyElement.getTextContent();
    }

    private Element parseProperty(String property, boolean createIfMissing) {
        Element currentBaseElement = operationalElement;
        for (String propertyPart : property.split("/")) {
            final Element finalBaseElement = currentBaseElement;
            currentBaseElement = ConfigUtils.getDirectDescendants(currentBaseElement, propertyPart).findFirst().orElseGet(() -> {
                if (createIfMissing) {
                    return ConfigUtils.createChild(finalBaseElement, propertyPart);
                } else {
                    return null;
                }
            });
            if (currentBaseElement == null) {
                return null;
            }
        }
        return currentBaseElement;
    }

    @Override
    public void setProperty(String property, String value) {
        //noinspection ConstantConditions Because we create if missing, we will always get a non-null Element.
        parseProperty(property, true).setTextContent(value);
    }

    @Override
    public <T> T getSerializedProperty(String property, T defaultValue, boolean setIfMissing, Map<String, String> classReplacements) {
        String rawText = getProperty(property);
        if (rawText == null) {
            if (setIfMissing) {
                setSerializedProperty(property, defaultValue);
            }
            return defaultValue;
        }

        try {
            byte[] base64Decoded = Base64.getDecoder().decode(rawText);
            InputStream rawDataStream = new ByteArrayInputStream(base64Decoded);
            ObjectInputStream objectInputStream = new ObjectInputStream(rawDataStream) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    try {
                        return super.resolveClass(desc);
                    } catch (Exception e) {
                        String className = desc.getName();
                        for (Map.Entry<String, String> replacement : classReplacements.entrySet()) {
                            className = className.replace(replacement.getKey(), replacement.getValue());
                        }
                        return Class.forName(className);
                    }
                }

                @Override
                protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
                    ObjectStreamClass classDescriptor = super.readClassDescriptor();
                    String className = classDescriptor.getName();
                    for (Map.Entry<String, String> replacement : classReplacements.entrySet()) {
                        className = className.replace(replacement.getKey(), replacement.getValue());
                    }
                    return ObjectStreamClass.lookup(Class.forName(className));
                }
            };
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public void setSerializedProperty(String property, Object value) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            setProperty(property, Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
