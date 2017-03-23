package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.CommandDescriptor;
import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.CommandManager;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;

public class CommandDisableDescriptor extends BaseCommandDescriptor {
    public CommandDisableDescriptor() {
        super(Executor::new, "Disables a command in the current Guild", "<command>","disable");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() > 0) {
                CommandDescriptor mCommand = CommandManager.getCommandDescriptorByName(executionData.getParsedArguments()
                                                                                                    .get(0));
                try {
                    CommandManager.setCommandEnabled(executionData.getGuild(), mCommand, false);
                    sendOutput("Command disabled.", mCommand.getPrimaryName());
                } catch (IllegalArgumentException e) {
                    sendOutput("Cannot disable command %s.", mCommand.getPrimaryName());
                }
            } else {
                sendOutput("Please specify a command to disable.");
            }
        }
    }
}
