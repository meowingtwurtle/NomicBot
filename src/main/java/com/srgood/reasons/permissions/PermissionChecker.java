package com.srgood.reasons.permissions;

import com.srgood.reasons.Reference;
import net.dv8tion.jda.core.entities.Member;

/**
 * The methods in this class take a {@link net.dv8tion.jda.core.entities.Member} (and possibly some other arguments),
 * throwing a {@link com.srgood.reasons.permissions.InsufficientPermissionException} if the
 * {@link net.dv8tion.jda.core.entities.Member} does not have the permission required by the method.
 */
public class PermissionChecker {
    /**
     * Fails if the {@link net.dv8tion.jda.core.entities.Member} does not have the permission to perform the {@link com.srgood.reasons.permissions.PermissibleAction}.
     * The method will never fail if the {@link net.dv8tion.jda.core.entities.Member} is the owner of their {@link net.dv8tion.jda.core.entities.Guild}, as returned by {@link net.dv8tion.jda.core.entities.Member#isOwner()}
     *
     * @param member The {@link net.dv8tion.jda.core.entities.Member} to check the permission for
     * @param action The {@link com.srgood.reasons.permissions.PermissibleAction} to check
     *
     * @throws com.srgood.reasons.permissions.InsufficientPermissionException If the {@link net.dv8tion.jda.core.entities.Member} does not have the permission required
     */
    public static void checkMemberPermission(Member member, PermissibleAction action) throws InsufficientPermissionException {
        if (member.isOwner()) {
            return;
        }
        if (!GuildDataManager.getGuildPermissionSet(member.getGuild()).checkMemberPermission(member, action)) {
            throw new InsufficientPermissionException(String.format("%s does not have permission to perform the action %s", member
                    .getUser()
                    .getName(), action));
        }
    }

    /**
     * Fails if the {@link net.dv8tion.jda.core.entities.Member} is not an administrator for the bot.
     * Currently, the only way to be an administrator is to be a recognized developer, as in {@link com.srgood.reasons.Reference.Other#BOT_DEVELOPERS}.
     *
     * @param member The {@link net.dv8tion.jda.core.entities.Member} to check for being a bot admin.
     *
     * @throws com.srgood.reasons.permissions.InsufficientPermissionException If the {@link net.dv8tion.jda.core.entities.Member} is not an administrator for the bot
     */
    public static void checkBotAdmin(Member member) {
        if (!Reference.Other.BOT_DEVELOPERS.contains(member.getUser().getId())) {
            throw new InsufficientPermissionException(String.format("%s cannot perform the action because they are not a bot owner.", member
                    .getUser()
                    .getName()));
        }
    }
}
