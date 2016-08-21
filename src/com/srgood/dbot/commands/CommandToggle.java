package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLHandler;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandToggle implements Command {
    private final String help = "Toggles a commands state to on or off use:'" + BotMain.prefix + "toggle'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        Command mCommand = BotMain.getCommandByName(args[0]);
        if (args.length > 0) {
            XMLHandler.setCommandIsEnabled(event.getGuild(), mCommand, !XMLHandler.commandIsEnabled(event.getGuild(), mCommand));
        } else {
            event.getChannel().sendMessage("Please specify a command to toggle");
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
    public Permissions permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return XMLHandler.getCommandPermissionXML(guild, this);
    }

    @Override
    public Permissions defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return Permissions.ADMINISTRATOR;
    }

}
