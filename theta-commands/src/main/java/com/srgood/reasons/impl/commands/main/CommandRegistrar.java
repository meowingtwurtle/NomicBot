package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandManager;

public class CommandRegistrar {
    public static void registerCommands(CommandManager commandManager) {
        commandManager.registerCommand(new CommandHelpDescriptor());
    }
}
