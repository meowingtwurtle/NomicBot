package com.srgood.reasons.commands;

import com.srgood.reasons.utils.CommandUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Collections;
import java.util.List;

public class CommandExecutionData {
    private final String rawData;
    private final String rawArgs;
    private final List<String> parsedArguments;
    private final Message message;
    private final MessageChannel channel;
    private final Guild guild;

    public CommandExecutionData(Message message) {
        this.rawData = message.getRawContent();
        this.rawArgs = CommandUtils.getCommandMessageArgsSection(message);
        this.parsedArguments = CommandUtils.parseCommandMessageArguments(message);
        this.message = message;
        this.channel = message.getChannel();
        this.guild = message.getGuild();
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

    public Message getMessage() {
        return message;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }
}
