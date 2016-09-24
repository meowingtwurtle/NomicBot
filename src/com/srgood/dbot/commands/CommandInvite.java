package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandInvite implements Command {
    private final String help = "Prints the link to add Reasons to another server. Use: '" + BotMain.prefix + "invite'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        event.getChannel().sendMessage("https://discordapp.com/oauth2/authorize?client_id=201810822131875840&scope=bot&permissions=0x23525237");
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
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"invite"};
    }

}
