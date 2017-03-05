package com.srgood.reasons.commands.old;


import com.srgood.reasons.commands.upcoming.CommandDescriptor;
import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.CommandExecutor;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.CommandUtils;
import net.dv8tion.jda.core.entities.Channel;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.srgood.reasons.utils.CommandUtils.getChannelThreadMapLock;

public class ChannelCommandThread extends Thread {

    private Channel channel;

    private final Deque<CommandParser.CommandContainer> commandDeque = new ArrayDeque<>();

    private boolean commandWasAdded = false;

    public ChannelCommandThread(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            //if the commands get method returns true (see command.class)...
            for (int i = 0; i < commandDeque.size(); i++) {
                try {
                    CommandParser.CommandContainer commandContainer = commandDeque.getFirst();
                    CommandDescriptor descriptor = CommandUtils.getCommandDescriptorByName(commandContainer.invoke);
                    CommandExecutionData executionData = new CommandExecutionData(commandContainer.event.getMessage());
                    CommandExecutor executor = descriptor.getExecutor(executionData);
                    if (ConfigUtils.isCommandEnabled(commandContainer.event.getGuild(), descriptor)) {
                        if (executor.shouldExecute()) {
                            //then run the command and its post execution code (see command)
                            executor.execute();
                            executor.postExecution();
                        }
                        // Otherwise do nothing
                    } else {
                        commandContainer.event.getChannel().sendMessage("This command is not enabled.");
                    }
                } catch (Exception e) {
                    CommandParser.CommandContainer commandContainer = commandDeque.getFirst();
                    commandContainer.event.getChannel().sendMessage("A ***FATAL*** exception occurred ( `" + e.getMessage()+ "` ) , please notify us. If possible, store the date and time.").queue();
                    e.printStackTrace();

                }
                commandDeque.removeFirst();
            }
            if (!commandWasAdded) {
                boolean threadLifeOver = attemptEndOfLife();
                if (threadLifeOver) {
                    return;
                }
            }
            commandWasAdded = false;
        }
    }

    private boolean attemptEndOfLife() {
        getChannelThreadMapLock().lock();
        boolean shouldEnd = commandDeque.size() == 0;
        if (shouldEnd) {
            CommandUtils.deregisterThread(this);
        }
        getChannelThreadMapLock().unlock();
        return shouldEnd;
    }

    public void addCommand(CommandParser.CommandContainer commandItem) {
        commandDeque.addLast(commandItem);
        commandWasAdded = true;
    }


    public String getChannelID() {
        return channel.getId();
    }

}
