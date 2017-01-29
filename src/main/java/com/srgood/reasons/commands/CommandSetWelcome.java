package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by HiItsMe on 1/18/2017.
 */
public class CommandSetWelcome implements Command {

    private static final String HELP = "Changes the welcome message.  Use '" + ReasonsMain.prefix + "setwelcome [welcome]'.  Set to 'OFF' to turn off.  Use '@USER' to ping new user.";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return args.length >= 1;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        String arg = event.getMessage().getContent().substring(13);
        if(arg.trim().toLowerCase().equals("off")) {
            ConfigUtils.setGuildProperty(event.getGuild(), "welcome", "OFF");
            event.getChannel().sendMessage("Welcome message turned off").queue();
        } else {
            ConfigUtils.setGuildProperty(event.getGuild(), "welcome", arg);
            ConfigUtils.setGuildProperty(event.getGuild(), "welcomechannel", event.getChannel().getId());
            event.getChannel().sendMessage("Welcome message set to " + arg).queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {}

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"setwelcome"};
    }

}
