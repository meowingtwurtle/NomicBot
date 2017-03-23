package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.Reference;
import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;

import java.util.Arrays;

public class CommandInviteDescriptor extends BaseCommandDescriptor {
    public CommandInviteDescriptor() {
        super(Executor::new, "Prints the authlink for the bot","<>", Arrays.asList("invite", "link", "authlink"));
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            sendOutput("Theta authorization link: %s", Reference.Strings.INVITE_LINK);
        }
    }
}
