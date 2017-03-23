package com.srgood.reasons.commands.upcoming.impl.base.descriptor;

import com.srgood.reasons.commands.upcoming.CommandDescriptor;
import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.CommandExecutor;
import com.srgood.reasons.commands.upcoming.HelpData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BaseCommandDescriptor implements CommandDescriptor {
    protected final List<String> names;
    protected final HelpData help;
    protected final Function<CommandExecutionData, CommandExecutor> dataToExecutorFunction;

    public BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> dataToExecutorFunction, String help, String args, String name) {
        this.names = Collections.singletonList(name);
        this.help = new HelpDataImpl(args, help);
        this.dataToExecutorFunction = dataToExecutorFunction;
    }

    public BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> dataToExecutorFunction, String help, String args, List<String> names) {
        this.names = new ArrayList<>(names);
        this.help = new HelpDataImpl(args, help);
        this.dataToExecutorFunction = dataToExecutorFunction;
    }

    @Override
    public CommandExecutor getExecutor(CommandExecutionData executionData) {
        return dataToExecutorFunction.apply(executionData);
    }

    @Override
    public HelpData help() {
        return help;
    }

    @Override
    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }

    private static class HelpDataImpl implements HelpData {
        private final String args;
        private final String description;

        public HelpDataImpl(String args, String description) {

            this.args = args;
            this.description = description;
        }

        @Override
        public String args() {
            return args;
        }

        @Override
        public String description() {
            return description;
        }
    }
}
