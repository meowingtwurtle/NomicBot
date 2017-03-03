package com.srgood.reasons.commands.upcoming;

@FunctionalInterface
public interface CommandExecutor {
    default boolean shouldExecute() { return true; }

    void execute();

    default void postExecution() {}
}
