package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandPong implements Command {

    private final String help = "Pong! Use: '" + BotMain.prefix + "pong'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        event.getChannel().sendMessage("Ping " + event.getAuthor().getAsMention());

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
        return com.srgood.dbot.utils.XMLUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

}
