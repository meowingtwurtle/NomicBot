package com.srgood.reasons.commands.old;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.commands.upcoming.CommandDescriptor;
import com.srgood.reasons.commands.upcoming.CommandManager;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandGetEnabled implements Command {
    private static final String HELP = "Prints whether a command is enabled on this server. Use: '" + ReasonsMain.prefix + "getenabled <command>'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        CommandDescriptor command = CommandManager.getCommandDescriptorByName(args[0]);
        //ConfigUtils.initCommandConfigIfNotExists(event.getGuild(), command);
        event.getChannel().sendMessage(String.format("Command %s is %s.", command.getPrimaryName(), ConfigUtils.isCommandEnabled(event.getGuild(), command) ? "enabled" : "disabled")).queue();
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] {"getenabled", "isenabled", "commandisenbaled"};
    }
}
