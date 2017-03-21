package com.srgood.reasons.commands.upcoming.impl.base.executor;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import net.dv8tion.jda.core.entities.Message;

public abstract class ChannelOutputCommandExecutor extends BaseCommandExecutor {
    public ChannelOutputCommandExecutor(CommandExecutionData executionData) {
        super(executionData);
    }

    protected void sendOutput(String format, Object... arguments) {
        String formatted = String.format(format, (Object[]) arguments); // Prevent confusing varargs call
        executionData.getChannel().sendMessage(formatted).queue();
    }

    protected void sendOutput(Message message) {
        executionData.getChannel().sendMessage(message).queue();
    }
}
