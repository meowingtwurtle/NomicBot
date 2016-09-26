package com.srgood.dbot.utils.config;

import org.w3c.dom.Element;

import java.util.HashMap;

public class ConfigDangerUtils {
    static void resetServers() {
        ConfigGuildUtils.servers = new HashMap<>();
    }
    static void addServer(String guildID, Element guildElement) {
        ConfigGuildUtils.servers.put(guildID, guildElement);
    }
}
