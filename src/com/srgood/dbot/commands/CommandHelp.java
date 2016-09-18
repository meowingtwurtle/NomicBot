package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Set;

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

        Set<String> v = BotMain.commands.keySet();
        StringBuilder z = new StringBuilder();
        z.append("All commands: \n");


        for (String i : v) {
            String output = i.substring(0, 1).toUpperCase() + i.substring(1);
            z.append("**").append(output).append(":** ").append("  `").append(BotMain.commands.get(i).help()).append("`").append("\n\n");
        }


        event.getAuthor().getPrivateChannel().sendMessage(z.toString());
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
        return com.srgood.dbot.utils.XMLUtils.getCommandPermissionXML(guild, this);
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
