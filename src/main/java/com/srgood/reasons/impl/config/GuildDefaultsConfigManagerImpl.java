package com.srgood.reasons.impl.config;

import com.srgood.reasons.config.GuildDefaultsConfigManager;

public class GuildDefaultsConfigManagerImpl extends BasicConfigManagerImpl implements GuildDefaultsConfigManager {

    private static final String DEFAULT_TAG_NAME = "default";

    public GuildDefaultsConfigManagerImpl(BotConfigManagerImpl botConfigManager) {
        super(botConfigManager.applyOnDocument(doc -> ConfigUtils.getOrCreateChildElement(doc.getDocumentElement(), DEFAULT_TAG_NAME)));
    }
}
