package com.srgood.reasons.commands.upcoming.impl.actual;

import static com.srgood.reasons.commands.upcoming.CommandManager.registerCommandDescriptor;

public class CommandRegistrar {
    public static void registerCommands() {
        registerCommandDescriptor(new Command8BallDescriptor());
        registerCommandDescriptor(new CommandCensorDescriptor());
        registerCommandDescriptor(new CommandCoinFlipDescriptor());
        registerCommandDescriptor(new CommandDebugDescriptor());
        registerCommandDescriptor(new CommandDiceRollDescriptor());
        registerCommandDescriptor(new CommandDisableDescriptor());
        registerCommandDescriptor(new CommandEnableDescriptor());
        registerCommandDescriptor(new CommandEvalDescriptor());
        registerCommandDescriptor(new CommandGetEnabledDescriptor());
        registerCommandDescriptor(new CommandGetPrefixDescriptor());
        registerCommandDescriptor(new CommandHelpDescriptor());
        registerCommandDescriptor(new CommandInfoDescriptor());
        registerCommandDescriptor(new CommandInviteDescriptor());
        registerCommandDescriptor(new CommandNotifyRandDescriptor());
        registerCommandDescriptor(new CommandPingDescriptor());
        registerCommandDescriptor(new CommandPongDescriptor());
        registerCommandDescriptor(new CommandSetGoodbyeDescriptor());
        registerCommandDescriptor(new CommandSetPrefixDescriptor());
        registerCommandDescriptor(new CommandSetWelcomeDescriptor());
        registerCommandDescriptor(new CommandShutdownDescriptor());
        registerCommandDescriptor(new CommandStatusDescriptor());
        registerCommandDescriptor(new CommandTicTacToeDescriptor());
        registerCommandDescriptor(new CommandVoteDescriptor());
    }
}
