package com.srgood.reasons.commands;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.impl.commands.CommandUtils;
import net.dv8tion.jda.core.entities.*;

import java.util.Collections;
import java.util.List;

public class CommandExecutionData {
    private final String rawData;
    private final String rawArgs;
    private final List<String> parsedArguments;
    private final MessageChannel channel;
    private final Guild guild;
    private final Member sender;
    private final BotManager botManager;

    public CommandExecutionData(Message message, BotManager botManager) {
        this(message.getRawContent(),
                CommandUtils.getCommandMessageArgsSection(message, botManager.getConfigManager().getGuildConfigManager(message.getGuild()).getPrefix()),
                CommandUtils.parseCommandMessageArguments(message, botManager.getConfigManager().getGuildConfigManager(message.getGuild()).getPrefix()),
                message.getChannel(),
                message.getGuild(),
                message.getAuthor(),
                botManager);
    }

    public CommandExecutionData(String rawData, String rawArgs, List<String> parsedArguments, MessageChannel channel, Guild guild, User sender, BotManager botManager) {
        this(rawData, rawArgs, parsedArguments, channel, guild, guild.getMember(sender), botManager);
    }

    public CommandExecutionData(String rawData, String rawArgs, List<String> parsedArguments, MessageChannel channel, Guild guild, Member sender, BotManager botManager) {
        this.rawData = rawData;
        this.rawArgs = rawArgs;
        this.parsedArguments = parsedArguments;
        this.channel = channel;
        this.guild = guild;
        this.sender = sender;
        this.botManager = botManager;
    }


    public String getRawData() {
        return rawData;
    }

    public String getRawArguments() {
        return rawArgs;
    }

    public List<String> getParsedArguments() {
        return Collections.unmodifiableList(parsedArguments);
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getSender() {
        return sender;
    }

    public BotManager getBotManager() {
        return botManager;
    }
}
