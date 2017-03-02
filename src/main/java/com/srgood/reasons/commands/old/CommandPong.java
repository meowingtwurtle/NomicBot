package com.srgood.reasons.commands.old;

import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandPong implements Command {

    private static final String HELP = "Pong! Use: '" + ReasonsMain.prefix + "pong'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Ping " + event.getAuthor().getAsMention()).queue();
    }

    @Override
    public String help() {
        
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] {"pong"};
    }

}
