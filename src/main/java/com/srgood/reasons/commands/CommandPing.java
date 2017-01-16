package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandPing implements Command {

    private static final String HELP = "Ping! Use: '" + ReasonsMain.prefix + "ping'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Pong " + event.getAuthor().getAsMention()).queue();
    }

    @Override
    public String help() {
        
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] {"ping"};
    }

}
