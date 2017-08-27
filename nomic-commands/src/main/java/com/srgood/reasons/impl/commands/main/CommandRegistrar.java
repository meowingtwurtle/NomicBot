package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandManager;

public class CommandRegistrar {
    public static void registerCommands(CommandManager commandManager) {
        commandManager.registerCommand(new CommandAbstainDescriptor());
        commandManager.registerCommand(new CommandAmendDescriptor());
        commandManager.registerCommand(new CommandAyeDescriptor());
        commandManager.registerCommand(new CommandHelpDescriptor());
        commandManager.registerCommand(new CommandNayDescriptor());
        commandManager.registerCommand(new CommandProposeDescriptor());
        commandManager.registerCommand(new CommandRepealDescriptor());
        commandManager.registerCommand(new CommandRewriteDescriptor());
        commandManager.registerCommand(new CommandShutdownDescriptor());
        commandManager.registerCommand(new CommandTransmuteDescriptor());

    }
}