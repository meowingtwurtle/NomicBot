package com.srgood.dbot.threading;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.commands.CommandParser;
import com.srgood.dbot.utils.CommandUtils;
import net.dv8tion.jda.entities.Channel;

import java.util.ArrayDeque;
import java.util.Deque;

public class ChannelThread extends Thread {

    private Channel channel;

    private final Deque<CommandItem> commandDeque = new ArrayDeque<>();

    private boolean commandWasAdded = false;

    public ChannelThread(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            //if the commands get method returns true (see command.class)...
            for (int i = 0; i < commandDeque.size(); i++) {
                CommandItem commandItem = commandDeque.getFirst();
                CommandParser.CommandContainer commandContainer = commandItem.getCommandContainer();
                if (commandItem.shouldExecute()) {
                    //then run the command and its post execution code (see command)
                    BotMain.commands.get(commandContainer.invoke).action(commandContainer.args, commandContainer.event);
                    BotMain.commands.get(commandContainer.invoke).executed(true, commandContainer.event);
                } else {
                    //else only run the execution code
                    BotMain.commands.get(commandContainer.invoke).executed(false, commandContainer.event);
                }
                commandDeque.removeFirst();
            }
            if (!commandWasAdded) { return; }
        }
    }

    private void endOfLife() {
        CommandUtils.deregisterThread(this);
    }

    public void addCommand(CommandItem commandItem) {
        commandDeque.addLast(commandItem);
        commandWasAdded = true;
    }

    public static class CommandItem {
        private final CommandParser.CommandContainer commandContainer;
        private final boolean shouldExecute;

        public CommandItem(CommandParser.CommandContainer commandContainer, boolean shouldExecute) {
            this.commandContainer = commandContainer;
            this.shouldExecute = shouldExecute;
        }

        public CommandParser.CommandContainer getCommandContainer() {
            return commandContainer;
        }

        public boolean shouldExecute() {
            return shouldExecute;
        }
    }

    public String getChannelID() {
        return channel.getId();
    }

}
