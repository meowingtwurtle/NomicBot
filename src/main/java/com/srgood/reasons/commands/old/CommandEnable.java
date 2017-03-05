package com.srgood.reasons.commands.old;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.commands.upcoming.CommandDescriptor;
import com.srgood.reasons.utils.CommandUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandEnable implements Command {
    private static final String HELP = "Enables a command on this server. Use: '" + ReasonsMain.prefix + "enable <command>'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (args.length > 0) {
            CommandDescriptor mCommand = CommandUtils.getCommandDescriptorByName(args[0]);
            try {
                CommandUtils.setCommandEnabled(event.getGuild(), mCommand, true);
                event.getChannel().sendMessage(String.format("Command %s enabled.", CommandUtils.getNameFromCommandDescriptor(mCommand))).queue();
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage(String.format("Cannot enable command %s.", CommandUtils.getNameFromCommandDescriptor(mCommand))).queue();
            }
        } else {
            event.getChannel().sendMessage("Please specify a command to enable.").queue();
        }
    }

    @Override
    public String help() {
        
        return HELP;
    }


    @Override
    public boolean canSetEnabled() {
        return false;
    }

    @Override
    public String[] names() {
        return new String[] { "enable" };
    }

}
