package com.srgood.reasons.impl.base.commands;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.GuildConfigManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.srgood.reasons.impl.base.commands.CommandUtils.generatePossiblePrefixesForGuild;

public class CommandManagerImpl implements CommandManager {
    private final Lock channelThreadMapLock = new ReentrantLock();
    private final Map<String, CommandDescriptor> commands = new TreeMap<>();
    private final Map<String, ChannelCommandThread> channelThreadMap = new HashMap<>();

    private Future<BotManager> botManagerFuture;

    public CommandManagerImpl(Future<BotManager> botManagerFuture) {
        this.botManagerFuture = botManagerFuture;
    }

        @Override
        public void handleCommandMessage(Message cmd) {
            GuildConfigManager guildConfigManager = getBotManager().getConfigManager()
                                                              .getGuildConfigManager(cmd.getGuild());

            //if (BlacklistUtils.isBlacklisted(botManager.getConfigManager(), cmd.getMember(), GuildDataManager.getGuildBlacklist(botManager.getConfigManager(), cmd.getGuild()))) {
            //    botManager.getLogger().info("Ignoring command, sender was directly or indirectly blacklisted");
            //    return;
            //}

            String calledCommand = CommandUtils.getCalledCommand(cmd.getRawContent(), generatePossiblePrefixesForGuild(guildConfigManager, cmd.getGuild()));
            CommandDescriptor descriptor = getCommandByName(calledCommand);
            if (descriptor != null) {
                if (guildConfigManager.getCommandConfigManager(descriptor).isEnabled()) {
                    getChannelThreadMapLock().lock();
                    ChannelCommandThread channelCommandThread = channelThreadMap.computeIfAbsent(cmd.getChannel()
                                                                                                                                     .getId(), id -> new ChannelCommandThread(cmd
                            .getChannel(), this, getBotManager()));
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
        for (Map.Entry<String, CommandDescriptor> entry : commands.entrySet()) {
            if (name.matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
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
        commands.put(descriptor.getNameRegex(), descriptor);
        getBotManager().getLogger().info("Registered command by regex: " + descriptor.getNameRegex());
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
        getBotManager().getConfigManager().getGuildConfigManager(guild).getCommandConfigManager(cmd).setEnabled(enabled);
    }

    @Override
    public void close() {}

    private BotManager getBotManager() {
        try {
            return botManagerFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
