package com.srgood.dbot.utils;

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
}
