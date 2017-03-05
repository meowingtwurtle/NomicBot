package com.srgood.reasons.commands.upcoming.impl.executor;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.CommandExecutor;
import com.srgood.reasons.permissions.InsufficientPermissionException;
import net.dv8tion.jda.core.exceptions.PermissionException;

public abstract class BaseCommandExecutor implements CommandExecutor {

    protected final CommandExecutionData executionData;

    public BaseCommandExecutor(CommandExecutionData executionData) {
        this.executionData = executionData;
    }

    protected abstract void sendOutput(String message);

    protected void checkCallerPermissions() {} // No need to override if there is nothing to do

    protected void checkBotPermissions() {} // No need to override if there is nothing to do

    protected boolean customPreExecuteCheck() {
        return true; // No need to override if there is nothing to do
    }

    @Override
    public boolean shouldExecute() {
        try {
            checkBotPermissions();
        } catch (PermissionException e) {
            return false; // If bot has insufficient permissions to run command, don't run it.
        }

        try {
            checkCallerPermissions();
        } catch (InsufficientPermissionException e) {
            // TODO Send failure
            return false; // Caller has no permission to run command, so don't run it.
        }
        return customPreExecuteCheck();
    }
}
