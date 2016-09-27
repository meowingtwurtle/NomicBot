package com.srgood.dbot.utils;

import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.awt.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by dmanl on 9/26/2016.
 */
public class VoteListener extends ListenerAdapter {
    private Channel voteChan;
    private LinkedHashMap<String,Integer> selections;
    private ArrayList<User> votedUsers = new ArrayList<>();

    public VoteListener(Channel channel, LinkedHashMap<String,Integer> choices) {
        voteChan = channel;
        selections = choices;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getChannel().equals(voteChan)) {
            String msg = event.getMessage().getContent().toLowerCase().trim();

            if (msg.startsWith("vote")) {
                msg = msg.replace("vote ","");
                for (String choice : selections.keySet()) {
                    if (choice.equals(msg)) {
                        if (votedUsers.contains(event.getAuthor())) {
                            event.getChannel().sendMessage("You have already voted " + event.getAuthor().getAsMention());
                        } else {
                            selections.replace(choice,selections.get(choice) + 1);
                            event.getChannel().sendMessage("Your vote has been counted " + event.getAuthor().getAsMention());
                            votedUsers.add(event.getAuthor());
                        }

                    }
                }
            }
        }
    }


}
