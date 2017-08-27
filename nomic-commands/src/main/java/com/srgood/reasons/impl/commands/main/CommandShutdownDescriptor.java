package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.nomic.DBUtil;

import java.util.Optional;

public class CommandShutdownDescriptor extends BaseCommandDescriptor {
    public CommandShutdownDescriptor() {
        super(Executor::new, "Shuts down the bot", "<>", false, "shutdown", "die");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            try {
                DBUtil.shutdownDB();
                executionData.getBotManager().close();
            } catch (Exception e) {
                sendOutput("Could not shutdown!");
            }
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return executionData.getMessage().getAuthor().getIdLong() == 164117897025683456L ? Optional.empty() : Optional.of("You are not the bot administrator?");
        }
    }
}
