package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandSetPrefix implements Command {
    private final String help = "Sets the prefix on this server. Use: '" + BotMain.prefix + "setprefix <prefix>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        try {
            ConfigUtils.setGuildPrefix(event.getGuild(), args[0]);
            event.getChannel().sendMessage("Prefix set to: " + args[0]);
        } catch (Exception e) {
            event.getChannel().sendMessage(help());
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        return help;
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
        return new String[] {"setprefix"};
    }

}
