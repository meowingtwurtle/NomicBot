package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.Reference;
import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;

import java.util.Arrays;

public class CommandInfoDescriptor extends BaseCommandDescriptor {
    public CommandInfoDescriptor() {
        super(Executor::new, "Returns information about the bot, including the current version","<>", Arrays.asList("info", "version", "about"));
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            sendOutput("The current version is: %s%n%s", Reference.Strings.VERSION, Reference.Strings.CREDITS);
        }
    }
}
