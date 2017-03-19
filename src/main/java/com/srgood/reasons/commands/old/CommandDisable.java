package com.srgood.reasons.commands.old;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.commands.upcoming.CommandDescriptor;
import com.srgood.reasons.commands.upcoming.CommandManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandDisable implements Command {
    private static final String HELP = "Disables a command on this server. Use: '" + ReasonsMain.prefix + "disable <command>'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (args.length > 0) {
            CommandDescriptor mCommand = CommandManager.getCommandDescriptorByName(args[0]);
            try {
                CommandManager.setCommandEnabled(event.getGuild(), mCommand, false);
                event.getChannel().sendMessage(String.format("Command %s disabled.", mCommand.getPrimaryName())).queue();
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage(String.format("Cannot disable command %s.",mCommand.getPrimaryName())).queue();
            }
        } else {
            event.getChannel().sendMessage("Please specify a command to toggle").queue();
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
        return new String[] { "disable" };
    }

}
