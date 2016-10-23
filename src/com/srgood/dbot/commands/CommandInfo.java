package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.Reference;
import com.srgood.dbot.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandInfo implements Command {

    private static final String HELP = "Prints the version of Reasons. Use: '" + BotMain.prefix + "version'";

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
            event.getChannel().sendMessage(String.format("The current version is: %s%n%s", Reference.Strings.VERSION, Reference.Strings.CREDITS));
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] { "info", "version" };
    }

}
