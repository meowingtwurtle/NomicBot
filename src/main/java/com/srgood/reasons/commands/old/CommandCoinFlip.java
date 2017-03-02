package com.srgood.reasons.commands.old;

import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class CommandCoinFlip implements Command {

    private static final String HELP = "Flips a coin and prints the result. Use: '" + ReasonsMain.prefix + "flip'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        
        Random r = new Random();
        int n = r.nextInt(6002);
        if (n < 3000) {
            event.getChannel().sendMessage("Heads").queue();
        } else if (n > 3001) {
            event.getChannel().sendMessage("Tails").queue();
        } else {
            event.getChannel().sendMessage("Side").queue();
        }
    }

    @Override
    public String help() {
        
        return HELP;
    }

    @Override
    public String[] names() {
        return new String[] {"coinflip"};
    }

}
