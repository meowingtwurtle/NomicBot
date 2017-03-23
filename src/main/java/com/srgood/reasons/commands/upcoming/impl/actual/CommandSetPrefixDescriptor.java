package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.config.ConfigUtils;

public class CommandSetPrefixDescriptor extends BaseCommandDescriptor {
    public CommandSetPrefixDescriptor() {
        super(Executor::new, "Gets the prefix in the current Guild", "<prefix>", "setprefix");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() > 0) {
                ConfigUtils.setGuildPrefix(executionData.getGuild(), executionData.getParsedArguments().get(0));
                sendOutput("The prefix has been set.");
            } else {
                sendOutput("Please specify a prefix.");
            }
        }
    }
}
