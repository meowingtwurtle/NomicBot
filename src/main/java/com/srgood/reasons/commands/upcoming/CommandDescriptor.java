package com.srgood.reasons.commands.upcoming;

public interface CommandDescriptor {
    CommandExecutor getExecutor(CommandExecutionData executionData);

    String getHelp();

    String[] getNames();

    default boolean canSetEnabled() { return true; }
}
