package com.srgood.reasons.impl.base.commands;


import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.CommandExecutor;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayDeque;
import java.util.Deque;

public class ChannelCommandThread extends Thread {

    private MessageChannel channel;
    private final CommandManagerImpl commandManager;
    private final BotManager botManager;

    private final Deque<Message> commandDeque = new ArrayDeque<>();

    private boolean commandWasAdded = false;

    public ChannelCommandThread(MessageChannel channel, CommandManagerImpl commandManager, BotManager botManager) {
        this.channel = channel;
        this.commandManager = commandManager;
        this.botManager = botManager;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < commandDeque.size(); i++) {
                Message message = commandDeque.getFirst();
                try {
                    String calledCommad = CommandUtils.getCalledCommand(message.getRawContent(), "");
                    CommandDescriptor descriptor = commandManager.getCommandByName(calledCommad);
                    CommandExecutionData executionData = new CommandExecutionDataImpl(message, botManager);
                    CommandExecutor executor = descriptor.getExecutor(executionData);
                    if (executor.shouldExecute()) {
                        executor.execute();
                        executor.postExecution();
                    }
                } catch (Exception e) {
                    message.getChannel().sendMessage("A ***FATAL*** exception occurred ( `" + e.getMessage()+ "` ) , please notify us. If possible, store the date and time.").queue();
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
        commandManager.getChannelThreadMapLock().lock();
        boolean shouldEnd = commandDeque.size() == 0;
        if (shouldEnd) {
            commandManager.deregisterThread(this);
        }
        commandManager.getChannelThreadMapLock().unlock();
        return shouldEnd;
    }

    public void addCommand(Message commandMessage) {
        commandDeque.addLast(commandMessage);
        commandWasAdded = true;
    }


    public String getChannelID() {
        return channel.getId();
    }

}
