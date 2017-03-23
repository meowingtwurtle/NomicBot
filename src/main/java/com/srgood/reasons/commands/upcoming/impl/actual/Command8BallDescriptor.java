package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;

import java.util.Arrays;
import java.util.Random;

import static com.srgood.reasons.Reference.Strings.EIGHT_BALL;

public class Command8BallDescriptor extends BaseCommandDescriptor {
    public Command8BallDescriptor() {
        super(Executor::new, "Tells your fortune", "<>", Arrays.asList("8ball", "fortune", "magic", "idiothelper"));
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        private Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            Random random = new Random();
            int numChoices = EIGHT_BALL.length;
            int randIndex = random.nextInt(numChoices);
            sendOutput(EIGHT_BALL[randIndex]);
        }
    }
}
