package com.srgood.dbot.commands;

import com.srgood.app.BotMain;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLHandler;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandPing implements Command {

    private final String help = "Ping! Use: '" + BotMain.prefix + "ping'";

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
        return help;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public Permissions permissionLevel(Guild guild) {

        return XMLHandler.getCommandPermissionXML(guild, this);


    }

    @Override
    public Permissions defaultPermissionLevel() {
        return Permissions.STANDARD;
    }

}
