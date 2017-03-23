package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.Reference;
import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.config.ConfigPersistenceUtils;

public class CommandShutdownDescriptor extends BaseCommandDescriptor {
    public CommandShutdownDescriptor() {
        super(Executor::new, "Shuts down the bot. You SHOULD be an developer to use this", "<>","shutdown");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (Reference.Other.BOT_DEVELOPERS.contains(executionData.getSender().getId())) {
                sendOutput("Shutting down! %s", executionData.getSender().getAsMention());

                try {
                    ConfigPersistenceUtils.writeXML();
                    ReasonsMain.getJda().shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                    sendOutput("Error, shutdown failed with an exception. Force exiting!");
                    System.exit(-1);
                }
            }
        }
    }
}
