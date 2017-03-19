package com.srgood.reasons.commands.upcoming.impl.base.descriptor;

import com.srgood.reasons.commands.upcoming.CommandDescriptor;
import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.CommandExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BaseCommandDescriptor implements CommandDescriptor {
    protected final List<String> names;
    protected final String help;
    protected final Function<CommandExecutionData, CommandExecutor> dataToExecutorFunction;

    public BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> dataToExecutorFunction, String help, String name) {
        this.names = new ArrayList<String>() {
            {
                add(name);
            }
        };
        this.help = help;
        this.dataToExecutorFunction = dataToExecutorFunction;
    }

    public BaseCommandDescriptor(Function<CommandExecutionData, CommandExecutor> dataToExecutorFunction, String help, List<String> names) {
        this.names = new ArrayList<>(names);
        this.help = help;
        this.dataToExecutorFunction = dataToExecutorFunction;
    }

    @Override
    public CommandExecutor getExecutor(CommandExecutionData executionData) {
        return dataToExecutorFunction.apply(executionData);
    }

    @Override
    public String getHelp() {
        return help;
    }

    @Override
    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }
}
