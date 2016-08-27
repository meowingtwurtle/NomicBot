package com.srgood.dbot.utils;

public class GuildUtil {
    public static void initGuild(net.dv8tion.jda.entities.Guild guild) {

        com.srgood.dbot.utils.XMLHandler.initGuildXML(guild);

        try {
            for (com.srgood.dbot.utils.Permissions permission : com.srgood.dbot.utils.Permissions.values()) {
                com.srgood.dbot.utils.PermissionOps.createRole(permission, guild, true);
            }
        } catch (net.dv8tion.jda.exceptions.PermissionException e3) {
            net.dv8tion.jda.utils.SimpleLog.getLog("Reasons").warn("Could not create custom role! Possible permissions problem?");
        }

        com.srgood.dbot.utils.XMLHandler.initGuildCommands(guild);
    }
}
