package com.srgood.reasons.commands.old;


import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.Reference;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

/**
 * Created by dmanl on 9/13/2016.
 */
public class Command8Ball implements Command {

    private static final String HELP = "Prints a random string from our magic 8Ball machine. Use: '" + ReasonsMain.prefix + "8ball'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(Reference.Strings.EIGHT_BALL[new Random().nextInt(Reference.Strings.EIGHT_BALL.length)]).queue();
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] {"8ball"};
    }
}
