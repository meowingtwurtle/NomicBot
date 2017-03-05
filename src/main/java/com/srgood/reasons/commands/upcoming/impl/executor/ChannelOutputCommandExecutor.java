package com.srgood.reasons.commands.upcoming.impl.executor;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;

public abstract class ChannelOutputCommandExecutor extends BaseCommandExecutor {
    public ChannelOutputCommandExecutor(CommandExecutionData executionData) {
        super(executionData);
    }

    @Override
    protected void sendOutput(String message) {
        executionData.getChannel().sendMessage(message).queue();
    }
}
