package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.PermissionLevels;
import com.srgood.dbot.utils.CommandUtils;
import com.srgood.dbot.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandGetEnabled implements Command {
    private static final String HELP = "Prints whether a command is enabled on this server. Use: '" + BotMain.prefix + "getenabled <command>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Command command = CommandUtils.getCommandByName(args[0]);
        ConfigUtils.initCommandConfigIfNotExists(event.getGuild(), command);
        event.getChannel().sendMessage(String.format("Command %s is %s.", CommandUtils.getNameFromCommand(command), ConfigUtils.isCommandEnabled(event.getGuild(), command) ? "enabled" : "disabled"));
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {

    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.STANDARD;
    }
    @Override
    public String[] names() {
        return new String[] {"getenabled", "isenabled", "commandisenbaled"};
    }
}
