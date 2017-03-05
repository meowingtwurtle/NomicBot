package com.srgood.reasons.commands.upcoming.impl.executor;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;

public abstract class DMOutputCommandExecutor extends BaseCommandExecutor {
    public DMOutputCommandExecutor(CommandExecutionData executionData) {
        super(executionData);
    }

    @Override
    protected void sendOutput(String message) {
        executionData.getMessage().getAuthor().openPrivateChannel().queue(chan -> chan.sendMessage(message).queue());
    }
}
