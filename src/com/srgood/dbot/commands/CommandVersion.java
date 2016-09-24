package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandVersion implements Command {

    private final String help = "Prints the version of Reasons. Use: '" + BotMain.prefix + "version'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub

        //see http://stackoverflow.com/questions/396429/how-do-you-know-what-version-number-to-use
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("notes")) {
                //TODO add an XML field for past and current Release notes. I.E. <notes ver= 0.1.2>Added Version command</Notes>
            }
        } else {
            event.getChannel().sendMessage("The current version is: " + com.srgood.dbot.Reference.Strings.VERSION);
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
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"version"};
    }

}
