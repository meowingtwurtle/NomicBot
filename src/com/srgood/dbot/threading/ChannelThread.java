package com.srgood.dbot.threading;

import com.srgood.dbot.commands.CommandParser;
import com.srgood.dbot.utils.CommandUtils;
import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.exceptions.RateLimitedException;

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
                try {
                    CommandItem commandItem = commandDeque.getFirst();
                    CommandParser.CommandContainer commandContainer = commandItem.getCommandContainer();
                    if (commandItem.shouldExecute()) {
                        //then run the command and its post execution code (see command)
                        CommandUtils.commands.get(commandContainer.invoke).action(commandContainer.args, commandContainer.event);
                        CommandUtils.commands.get(commandContainer.invoke).executed(true, commandContainer.event);
                    } else {
                        //else only run the execution code
                        CommandUtils.commands.get(commandContainer.invoke).executed(false, commandContainer.event);
                    }
                }  catch (RateLimitedException e) {
                    try {
                        Thread.sleep(1000L);

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } catch (Exception e) {
                    CommandItem commandItem = commandDeque.getFirst();
                    CommandParser.CommandContainer commandContainer = commandItem.getCommandContainer();
                    commandContainer.event.getChannel().sendMessage("***A FATAL exception occurred ( " + e.getMessage()+ ") , please notify us. If possible, store the date and time.***");
                    e.printStackTrace();

                }
                commandDeque.removeFirst();
            }
            if (!commandWasAdded) {
                endOfLife();
                return;
            }
            commandWasAdded = false;
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
