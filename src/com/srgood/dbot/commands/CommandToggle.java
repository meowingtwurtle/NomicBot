package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.CommandUtils;
import com.srgood.dbot.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandToggle implements Command {
    private static final String HELP = "Toggles a command's state to on or off on this server. Use: '" + BotMain.prefix + "toggle <command>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (args.length > 0) {
            Command mCommand = CommandUtils.getCommandByName(args[0]);
            if (mCommand instanceof CommandToggle) {
                event.getChannel().sendMessage("ERROR: Cannot toggle the toggle command!");
            } else {
                boolean oldStatus = ConfigUtils.isCommandEnabled(event.getGuild(), mCommand);
                ConfigUtils.setCommandEnabled(event.getGuild(), mCommand, !oldStatus);
                event.getChannel().sendMessage(String.format("Command %s %s", CommandUtils.getNameFromCommand(mCommand), oldStatus ? "DISABLED" : "ENABLED"));
            }
        } else {
            event.getChannel().sendMessage("Please specify a command to toggle");
        }
    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.ADMINISTRATOR;
    }

    @Override
    public String[] names() {
        return new String[] {"toggle"};
    }

}
