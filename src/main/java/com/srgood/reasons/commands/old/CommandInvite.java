package com.srgood.reasons.commands.old;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.Reference;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandInvite implements Command {
    private static final String HELP = "Prints the link to add Reasons to another server. Use: '" + ReasonsMain.prefix + "invite'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(String.format("Reasons authorization link: %s", Reference.Strings.INVITE_LINK)).queue();
    }

    @Override
    public String help() {
        
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] {"invite"};
    }

}
