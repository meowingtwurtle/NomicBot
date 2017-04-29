package com.srgood.reasons.impl.permissions;

import com.srgood.reasons.config.BotConfigManager;
import net.dv8tion.jda.core.entities.Guild;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GuildDataManager {
    private static final Map<String, GuildPermissionSet> guildPermissionSetMap = new HashMap<>();

    public static GuildPermissionSet getGuildPermissionSet(BotConfigManager botConfigManager, Guild guild) {
        if (guildPermissionSetMap.containsKey(guild.getId())) {
            return guildPermissionSetMap.get(guild.getId());
        }

        // No guild data found, need to load from XML
        return loadNewGuild(botConfigManager, guild);
    }

    private static GuildPermissionSet loadNewGuild(BotConfigManager botConfigManager, Guild guild) {
        GuildPermissionSet ret = botConfigManager.getGuildConfigManager(guild)
                                                 .getSerializedProperty(
                                                         "roleData",
                                                         GuildPermissionSet.class,
                                                         new GuildPermissionSet(guild),
                                                         false,
                                                         Collections.singletonMap("com.srgood.reasons.permissions", "com.srgood.reasons.impl.permissions"));

        ret.clean(guild.getJDA());

        guildPermissionSetMap.put(guild.getId(), ret);
        return ret;
    }

    public static void setGuildPermissionSet(BotConfigManager botConfigManager, Guild guild, GuildPermissionSet permissionSet) {
        guildPermissionSetMap.put(guild.getId(), permissionSet);
        botConfigManager.getGuildConfigManager(guild).setSerializedProperty("roleData", permissionSet);
    }
}
