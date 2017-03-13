package com.srgood.reasons.commands.upcoming.impl.executor;

import com.srgood.reasons.commands.upcoming.CommandExecutor;

public enum EmptyCommandExecutor implements CommandExecutor {
    INSTANCE;

    @Override
    public boolean shouldExecute() {
        return true;
    }

    @Override
    public void execute() {}
}
