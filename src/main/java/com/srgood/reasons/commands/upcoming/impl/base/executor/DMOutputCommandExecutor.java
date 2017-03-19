package com.srgood.reasons.commands.upcoming.impl.base.executor;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;

public abstract class DMOutputCommandExecutor extends BaseCommandExecutor {
    public DMOutputCommandExecutor(CommandExecutionData executionData) {
        super(executionData);
    }

    @Override
    protected void sendOutput(String message) {
        executionData.getSender().openPrivateChannel().queue(chan -> chan.sendMessage(message).queue());
    }
}
