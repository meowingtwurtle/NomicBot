package com.srgood.reasons.commands.upcoming.impl.descriptor;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.CommandExecutor;
import com.srgood.reasons.commands.upcoming.impl.executor.ChannelOutputCommandExecutor;

import java.util.function.Function;

public class SimpleTextCommandDescriptor extends BaseCommandDescriptor {

    protected final String outputText;

    public SimpleTextCommandDescriptor(String commandName, String outputText) {
        super(generateExecutorFunction(outputText), outputText, commandName);
        this.outputText = outputText;
    }

    private static Function<CommandExecutionData, CommandExecutor> generateExecutorFunction(String outputText) {
        return data -> new ChannelOutputCommandExecutor(data) {
            @Override
            public void execute() {
                sendOutput(outputText);
            }
        };
    }

    @Override
    public CommandExecutor getExecutor(CommandExecutionData executionData) {
        return new ChannelOutputCommandExecutor(executionData) {
            @Override
            public void execute() {
                sendOutput(outputText);
            }
        };
    }
}
