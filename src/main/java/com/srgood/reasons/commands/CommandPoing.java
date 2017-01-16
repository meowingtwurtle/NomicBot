package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by dmanl on 9/12/2016.
 */
public class CommandPoing implements Command {

    private static final String HELP = "Poing! Use: '" + ReasonsMain.prefix + "poing'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Plong " + event.getAuthor().getAsMention()).queue();
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] {"poing"};
    }
}
