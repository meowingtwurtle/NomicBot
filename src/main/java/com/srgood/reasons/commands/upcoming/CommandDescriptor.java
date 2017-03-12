package com.srgood.reasons.commands.upcoming;

import java.util.Collections;
import java.util.Set;

public interface CommandDescriptor {
    CommandExecutor getExecutor(CommandExecutionData executionData);

    String getHelp();

    String[] getNames();

    default boolean canSetEnabled() {
        return true;
    }

    default boolean hasSubCommands() {
        return !getSubCommands().isEmpty();
    }

    default Set<CommandDescriptor> getSubCommands() {
        return Collections.emptySet();
    }
}
