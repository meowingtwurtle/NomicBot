package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.GreetingUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;

/**
 * Created by HiItsMe on 1/18/2017.
 */
public class CommandSetGoodbye implements Command {

    private static final String HELP = "Changes the goodbye message.  Use '" + ReasonsMain.prefix + "setgoodbye [goodbye]'.  Set to 'OFF' to turn off.  Use '@USER' to ping new user.";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return args.length >= 1;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        String message;
        {
            String[] argsWithCase = Arrays.copyOfRange(event.getMessage().getContent().split(" "), 1, args.length + 1);
            message = String.join(" ", argsWithCase);
        }

        if(message.trim().equalsIgnoreCase("OFF")) {
            ConfigUtils.setGuildProperty(event.getGuild(), GreetingUtils.formatPropertyName("goodbye"), "OFF");
            event.getChannel().sendMessage("Goodbye message turned off").queue();
        } else {
            ConfigUtils.setGuildProperty(event.getGuild(), GreetingUtils.formatPropertyName("goodbye"), message);
            ConfigUtils.setGuildProperty(event.getGuild(), GreetingUtils.formatPropertyName("goodbyechannel"), event.getChannel().getId());
            event.getChannel().sendMessage(String.format("Goodbye message set to **`%s`**. Messages will be sent in this channel.", message)).queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {}

    @Override
    public String[] names() {
        return new String[] {"setgoodbye"};
    }

}
