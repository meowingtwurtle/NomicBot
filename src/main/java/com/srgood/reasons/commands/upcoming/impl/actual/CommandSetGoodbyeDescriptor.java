package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.GreetingUtils;

public class CommandSetGoodbyeDescriptor extends BaseCommandDescriptor {
    public CommandSetGoodbyeDescriptor() {
        super(Executor::new, "Sets the goodbye message for the Guild, which will be sent in the current Channel. Set it to `OFF` to disable. Use `@USER` to use the name of the leaving user.", "setgoodbye");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            String message = executionData.getParsedArguments().get(0);

            ConfigUtils.setGuildProperty(executionData.getGuild(), GreetingUtils.formatPropertyName("goodbye"), message);
            ConfigUtils.setGuildProperty(executionData.getGuild(), GreetingUtils.formatPropertyName("goodbyechannel"),
                    executionData.getChannel().getId());

            if (message.trim().equalsIgnoreCase("OFF")) {
                sendOutput("Goodbye message turned off");
            } else {
                sendOutput("Goodbye message set. Messages will be sent in this channel.", message);
            }
        }
    }
}
