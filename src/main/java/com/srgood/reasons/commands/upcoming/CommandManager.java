package com.srgood.reasons.commands.upcoming;

import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommandManager {
    private static final Lock channelThreadMapLock = new ReentrantLock();
    private static Map<String, CommandDescriptor> commands = new TreeMap<>();
    private static Map<String, ChannelCommandThread> channelThreadMap = new java.util.HashMap<>();

    public static class CommandComparator implements Comparator<CommandDescriptor> {

        @Override
        public int compare(CommandDescriptor command0, CommandDescriptor command1) {
            return getNameFromCommandDescriptor(command0).compareTo(getNameFromCommandDescriptor(command1));
        }
    }

    public static void handleCommand(Message cmd) {
        CommandExecutionData executionData = new CommandExecutionData(cmd);
        String calledCommand = CommandUtils.getCalledCommand(cmd);
        CommandDescriptor descriptor = getCommandDescriptorByName(calledCommand);
        CommandExecutor executor = descriptor.getExecutor(executionData);

        // checks if the referenced command is in the command list
        if (commands.containsKey(calledCommand)) {
            ConfigUtils.initCommandConfigIfNotExists(cmd.getGuild(), calledCommand);
            //if the command is enabled for the message's guild...
            //if the message author has the required permission level...
            if (ConfigUtils.isCommandEnabled(cmd.getGuild(), descriptor))
                if (true) {
                    boolean shouldExecute = executor.shouldExecute();
                    getChannelThreadMapLock().lock();
                    ChannelCommandThread channelCommandThread =
                            channelThreadMap.containsKey(cmd.getChannel().getId())
                                    ? channelThreadMap.get(cmd.getChannel().getId())
                                    : new ChannelCommandThread(cmd.getChannel());
                    channelThreadMap.put(cmd.getChannel().getId(), channelCommandThread);
                    getChannelThreadMapLock().unlock();
                    channelCommandThread.addCommand(cmd);
                    if (channelCommandThread.getState() == Thread.State.NEW) {
                        channelCommandThread.start();
                    }
                } else {
                    cmd.getChannel().sendMessage("You lack the required permission to preform this action").queue();
                }
            else {
                cmd.getChannel().sendMessage("This command is disabled").queue();
            }
        } else {
            cmd.getChannel().sendMessage(String.format("Unknown command `%s`", calledCommand)).queue();
        }
    }

    public static void deregisterThread(ChannelCommandThread thread) {
        getChannelThreadMapLock().lock();
        if (channelThreadMap.containsKey(thread.getChannelID())) {
            channelThreadMap.remove(thread.getChannelID());
        }
        getChannelThreadMapLock().unlock();
    }

    public static String getNameFromCommandDescriptor(CommandDescriptor cmd) {
        return cmd.getNames()[0];
    }

    public static String getPrimaryCommandAlias(String name) {
        return getNameFromCommandDescriptor(getCommandDescriptorByName(name));
    }

    public static CommandDescriptor getCommandDescriptorByName(String name) {
        return commands.get(name);
    }

    public static void registerCommandDescriptor(CommandDescriptor descriptor) {
        String[] names = descriptor.getNames();
        for (String name : names) {
            commands.put(name, descriptor);
        }
    }

    public static Set<CommandDescriptor> getRegisteredCommandDescriptors() {
        return new HashSet<>(commands.values());
    }

    public static Lock getChannelThreadMapLock() {
        return channelThreadMapLock;
    }

    public static void setCommandEnabled(Guild guild, CommandDescriptor cmd, boolean enabled) {
        if (!cmd.canSetEnabled()) {
            throw new IllegalArgumentException("Cannot toggle this command");
        }
        ConfigUtils.setCommandEnabled(guild, cmd, enabled);
    }
}
