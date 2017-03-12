package com.srgood.reasons.commands.upcoming.impl.descriptor;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.CommandExecutor;
import com.srgood.reasons.commands.upcoming.CommandDescriptor;

import java.util.*;
import java.util.function.Function;

public abstract class MultiTierCommandDescriptor extends BaseCommandDescriptor {

    private final Set<CommandDescriptor> subCommands;

    public MultiTierCommandDescriptor(Set<CommandDescriptor> subCommands, String help, String... names) {
        super(generateDataToExecutorFunction(subCommands), help, names);
        this.subCommands = new HashSet<>(subCommands);
    }

    private static Function<CommandExecutionData, CommandExecutor> generateDataToExecutorFunction(Collection<CommandDescriptor> subCommandDescriptors) {
        return executionData -> {
            if (executionData.getParsedArguments().isEmpty()) {
                return null;
            }

            String targetName = executionData.getParsedArguments().get(0);

            for (CommandDescriptor subDescriptor : subCommandDescriptors) {
                if (Arrays.stream(subDescriptor.getNames()).anyMatch(targetName::equals)) {
                    return subDescriptor.getExecutor(patchExecutionDataForSubCommand(executionData));
                }
            }

            return null;
        };
    }

    private static CommandExecutionData patchExecutionDataForSubCommand(CommandExecutionData data) {
        List<String> oldParsedArguments = data.getParsedArguments();
        List<String> newParsedArguments = oldParsedArguments.subList(1, oldParsedArguments.size());

        return new CommandExecutionData(data.getRawData(), data.getRawArguments(), newParsedArguments, data.getChannel(),
                data.getGuild(), data.getSender());
    }

    @Override
    public boolean hasSubCommands() {
        return true;
    }

    @Override
    public Set<CommandDescriptor> getSubCommands() {
        return Collections.unmodifiableSet(subCommands);
    }
}
