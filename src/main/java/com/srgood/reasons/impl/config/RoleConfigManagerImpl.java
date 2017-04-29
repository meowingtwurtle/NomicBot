package com.srgood.reasons.impl.config;

import com.srgood.reasons.config.RoleConfigManager;
import org.w3c.dom.Element;

public class RoleConfigManagerImpl extends BasicConfigManagerImpl implements RoleConfigManager {
    public RoleConfigManagerImpl(Element roleElement) {
        super(roleElement);
    }
}
