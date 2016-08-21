package com.srgood.dbot.utils;

import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class CommandParser {
    //creates a new method of type CommandContainer which is used for parsing
    public CommandContainer parse(String rw, GuildMessageReceivedEvent event, String prefix) {
        ArrayList<String> split = new ArrayList<>();
        String beheaded;
        try {
            if (event.getJDA().getSelfInfo().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention())) {
                beheaded = event.getMessage().getRawContent().replaceFirst(event.getJDA().getSelfInfo().getAsMention() + " ", "");
            } else {
                beheaded = rw.replaceFirst(prefix, "");
            }
        } catch (IndexOutOfBoundsException ex) {
            beheaded = rw.replaceFirst(prefix, "");
        }


        String[] splitBeheaded = beheaded.split(" ");
        java.util.Collections.addAll(split, splitBeheaded);
        String invoke = split.get(0);
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new CommandContainer(rw, beheaded, splitBeheaded, invoke, args, event);
    }

    public class CommandContainer {
        public final String raw;
        public final String invoke;
        public final String[] args;
        public final GuildMessageReceivedEvent event;

        public CommandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, GuildMessageReceivedEvent event) {
            this.raw = rw;
            this.invoke = invoke;
            this.args = args;
            this.event = event;

        }
    }

}
