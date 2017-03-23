package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;

import java.util.Arrays;
import java.util.Random;

public class CommandCoinFlipDescriptor extends BaseCommandDescriptor {
    public CommandCoinFlipDescriptor() {
        super(Executor::new, "Flips a coin", "<>", Arrays.asList("coinflip", "flip", "flipcoin", "flipacoin"));
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            Random r = new Random();
            int n = r.nextInt(6002);
            if (n < 3000) {
                sendOutput("Heads");
            } else if (n > 3001) {
                sendOutput("Tails");
            } else {
                sendOutput("Side");
            }
        }
    }
}
