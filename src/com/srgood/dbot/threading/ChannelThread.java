package com.srgood.dbot.threading;

import com.srgood.dbot.commands.CommandParser;
import net.dv8tion.jda.entities.Channel;

import java.util.ArrayDeque;
import java.util.Deque;

public class ChannelThread extends Thread {

    private Channel channel;

    private final Deque<CommandParser.CommandContainer> commandDeque = new ArrayDeque<>();

    public ChannelThread(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
    }

    public void addCommand(CommandParser.CommandContainer commandContainer) {
        commandDeque.addLast(commandContainer);
    }

}
