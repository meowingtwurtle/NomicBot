package com.srgood.reasons.impl.commands;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.GuildConfigManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommandManagerImpl implements CommandManager {
    private final Lock channelThreadMapLock = new ReentrantLock();
    private final Map<String, CommandDescriptor> commands = new TreeMap<>();
    private final Map<String, ChannelCommandThread> channelThreadMap = new java.util.HashMap<>();

    private final BotManager botManager;

    public static class CommandComparator implements Comparator<CommandDescriptor>, Serializable {

        @Override
        public int compare(CommandDescriptor command0, CommandDescriptor command1) {
            return command0.getPrimaryName().compareTo(command1.getPrimaryName());
        }
    }

    public CommandManagerImpl(BotManager botManager) {
        this.botManager = botManager;
    }

        @Override
        public void handleCommandMessage(Message cmd) {
            GuildConfigManager guildConfigManager = botManager.getConfigManager()
                                                              .getGuildConfigManager(cmd.getGuild());
            String calledCommand = CommandUtils.getCalledCommand(cmd, guildConfigManager.getPrefix());
            CommandDescriptor descriptor = getCommandByName(calledCommand);
            if (descriptor != null) {
                if (guildConfigManager.getCommandConfigManager(descriptor).isEnabled()) {
                    getChannelThreadMapLock().lock();
                    ChannelCommandThread channelCommandThread = channelThreadMap.computeIfAbsent(cmd.getChannel()
                                                                                                    .getId(), id -> new ChannelCommandThread(cmd
                            .getChannel(), this, botManager));
                    getChannelThreadMapLock().unlock();
                    channelCommandThread.addCommand(cmd);
                    if (channelCommandThread.getState() == Thread.State.NEW) {
                        channelCommandThread.start();
                    }
                } else {
                    cmd.getChannel().sendMessage("This command is disabled").queue();
                }
            } else {
                cmd.getChannel().sendMessage(String.format("Unknown command `%s`", calledCommand)).queue();
            }
    }

    @Override
    public CommandDescriptor getCommandByName(String name) {
        return commands.get(name);
    }

    Lock getChannelThreadMapLock() {
        return channelThreadMapLock;
    }

    void deregisterThread(ChannelCommandThread thread) {
        getChannelThreadMapLock().lock();
        if (channelThreadMap.containsKey(thread.getChannelID())) {
            channelThreadMap.remove(thread.getChannelID());
        }
        getChannelThreadMapLock().unlock();
    }

    @Override
    public void registerCommand(CommandDescriptor descriptor) {
        List<String> names = descriptor.getNames();
        for (String name : names) {
            commands.put(name, descriptor);
            botManager.getLogger().info("Registered command " + name);
        }
    }

    @Override
    public Set<CommandDescriptor> getRegisteredCommands() {
        return new HashSet<>(commands.values());
    }

    @Override
    public void setCommandEnabled(Guild guild, CommandDescriptor cmd, boolean enabled) {
        if (!cmd.canSetEnabled()) {
            throw new IllegalArgumentException("Cannot toggle this command");
        }
        botManager.getConfigManager().getGuildConfigManager(guild).getCommandConfigManager(cmd).setEnabled(enabled);
    }
}