package com.srgood.reasons.impl.base.commands;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandExecutionData;
import net.dv8tion.jda.core.entities.*;

import java.util.Collections;
import java.util.List;

public class CommandExecutionDataImpl implements CommandExecutionData {
    private final Message message;
    private final String rawData;
    private final String rawArgs;
    private final List<String> parsedArguments;
    private final MessageChannel channel;
    private final BotManager botManager;

    public CommandExecutionDataImpl(Message message, BotManager botManager) {
        this(message, message.getRawContent(),
                CommandUtils.getCommandMessageArgsSection(message.getRawContent(), ""),
                CommandUtils.parseCommandMessageArguments(message.getRawContent(), ""),
                message.getChannel(),
                botManager);
    }

    public CommandExecutionDataImpl(Message message, String rawData, String rawArgs, List<String> parsedArguments, MessageChannel channel, BotManager botManager) {
        this.message = message;
        this.rawData = rawData;
        this.rawArgs = rawArgs;
        this.parsedArguments = parsedArguments;
        this.channel = channel;
        this.botManager = botManager;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override public String getRawData() {
        return rawData;
    }

    @Override public String getRawArguments() {
        return rawArgs;
    }

    @Override public List<String> getParsedArguments() {
        return Collections.unmodifiableList(parsedArguments);
    }

    @Override public MessageChannel getChannel() {
        return channel;
    }

    @Override public BotManager getBotManager() {
        return botManager;
    }
}
