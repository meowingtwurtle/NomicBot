package com.srgood.dbot.utils;

import com.srgood.dbot.PermissionLevels;
import net.dv8tion.jda.entities.Guild;

import java.util.Set;

public class GuildUtils {
    public static void initGuild(net.dv8tion.jda.entities.Guild guild) {

        ConfigUtils.initGuildConfig(guild);

        try {
            for (com.srgood.dbot.PermissionLevels permission : com.srgood.dbot.PermissionLevels.values()) {
                com.srgood.dbot.utils.PermissionUtils.createRole(permission, guild, true);
            }
        } catch (net.dv8tion.jda.exceptions.PermissionException e3) {
            net.dv8tion.jda.utils.SimpleLog.getLog("Reasons").warn("Could not create custom role! Possible permissions problem?");
        }

        ConfigUtils.initGuildConfig(guild);
    }

    public static void doPreMessageGuildCheck(Guild guild) {
        checkForRoles(guild);
    }
    private static void checkForRoles(Guild guild) {
        deregisterPhantomRoles(guild);
        createMissingRoles(guild);
    }
    private static void deregisterPhantomRoles(Guild guild) {
        for (PermissionLevels permLevel : PermissionLevels.values()) {
            if (!permLevel.isVisible()) {
                continue;
            }
            Set<String> roleIDs = ConfigUtils.getGuildRoleIDsFromPermissionName(guild, permLevel);
            for (String id : roleIDs) {
                if (guild.getRoleById(id) == null) {
                    ConfigUtils.deregisterRoleConfig(guild, id);
                }
            }
        }
    }
    private static void createMissingRoles(Guild guild) {
        for (PermissionLevels permLevel : PermissionLevels.values()) {
            if (!permLevel.isVisible()) {
                continue;
            }
            if (!ConfigUtils.guildHasRoleForPermission(guild, permLevel)) {
                PermissionUtils.createRole(permLevel, guild, true);
            }
        }
    }
}
