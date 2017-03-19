package com.srgood.reasons.commands.old;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.commands.upcoming.CommandManager;
import com.srgood.reasons.utils.MessageUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandHelp implements Command {
    private static final String HELP = "Lists all commands (only primary aliases). Use: '" + ReasonsMain.prefix + "HELP'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            StringBuilder message = new StringBuilder();
            message.append("All commands: ").append("\n\n");

            CommandManager.getRegisteredCommandDescriptors().stream()
                          .sorted(new CommandManager.CommandComparator())
                          .distinct()
                          .map(command ->
                                "**" + command.getPrimaryName() + ":** " + " `" + command.getHelp() + "`\n\n")
                          .forEach(message::append);

            MessageUtils.sendMessageSafeSplitOnChar(privateChannel, message.toString(), '\n');
            event.getChannel().sendMessage("Commands were set to you in a private message").queue();
        });

    }

    @Override
    public String help() {
        
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] { "help" };
    }

}
