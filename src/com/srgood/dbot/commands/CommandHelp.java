package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.CommandUtils;
import com.srgood.dbot.utils.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collection;

public class CommandHelp implements Command {
    private final String help = "Lists all commands. Use: '" + BotMain.prefix + "help'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub

        Collection<Command> commands = CommandUtils.commands.values();
        PrivateChannel privateChannel = event.getAuthor().getPrivateChannel();
        StringBuilder message = new StringBuilder();
        message.append("All commands: ").append("\n\n");
        for (Command c : commands) {
            String cmdMessage = "**" + CommandUtils.getNameFromCommand(c) + ":** " + " `" + c.help() + "`\n\n";
            if (message.length() + cmdMessage.length() >= 2000) {
                privateChannel.sendMessage(message.toString());
                message = new StringBuilder().append("\n");
            }
            message.append(cmdMessage);
        }

        if (message.length() > 0) {
            privateChannel.sendMessage(message.toString());
        }

        event.getChannel().sendMessage("Commands were set to you in a private message");

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
        return ConfigUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"help"};
    }

}
