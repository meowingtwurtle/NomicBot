package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.PermissionLevels;
import com.srgood.dbot.utils.Vote;
import com.srgood.dbot.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dmanl on 9/26/2016.
 */
public class CommandVote implements Command{

    private final static String HELP = "Begins a vote with the specified options that ends after the specified amount of seconds. Use: '" + BotMain.prefix + "vote <duration (seconds)> <option 1> <option 2> [option 3 [option 4 [option 5 [option 6 [...]]]]]'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {

        if (args.length >= 3) {
            Map<String,Integer> voteMap = new LinkedHashMap<>();

            for (int i = 1; i < args.length; i++) {
                voteMap.put(args[i],0);
            }

            new Vote(voteMap, Integer.parseInt(args[0]),event.getChannel(), () -> {
                StringBuilder sb = new StringBuilder();

                for (String st : voteMap.keySet()) {
                    sb.append(st).append(": ").append(voteMap.get(st)).append("\n");
                }
                event.getChannel().sendMessage(sb.toString());
            });
        } else {
            event.getChannel().sendMessage("Incorrect arguments");
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {

    }

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.ADMINISTRATOR;
    }
}
