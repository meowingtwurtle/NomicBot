package com.srgood.reasons.commands;

import com.srgood.reasons.BotMain;
import com.srgood.reasons.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandPing implements Command {

    private static final String HELP = "Ping! Use: '" + BotMain.prefix + "ping'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        event.getChannel().sendMessage("Pong " + event.getAuthor().getAsMention());


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
    public com.srgood.reasons.PermissionLevels permissionLevel(Guild guild) {

        return ConfigUtils.getCommandPermission(guild, this);


    }

    @Override
    public com.srgood.reasons.PermissionLevels defaultPermissionLevel() {
        return com.srgood.reasons.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"ping"};
    }

}
