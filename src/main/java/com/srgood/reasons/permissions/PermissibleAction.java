package com.srgood.reasons.permissions;

public enum PermissibleAction {

    // Basic one-off chat commands
    GET_HELP,
    GET_INFO,
    GET_AUTH_LINK,
    CHECK_PING,
    DO_CHANCE_GAME,
    EVALUATE_MATH,

    // Games
    START_CHESS_GAME,
    START_TIC_TAC_TOE__GAME,

    // Moderation commands
    DELETE_MESSAGES,
    SET_COMMAND_ENABLED,
    SET_GUILD_PREFIX,
    START_VOTE,
    PARTICIPATE_IN_VOTE,
    NOTIFY_MEMBER,

    // Getter commands
    GET_COMMAND_ENABLED,
    GET_GUILD_PREFIX,
    GET_GUILD_ROLES;
}
