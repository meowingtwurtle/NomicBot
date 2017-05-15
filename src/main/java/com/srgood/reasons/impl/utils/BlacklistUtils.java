package com.srgood.reasons.impl.utils;

import com.srgood.reasons.config.BotConfigManager;
import com.srgood.reasons.impl.permissions.Permission;
import com.srgood.reasons.impl.permissions.PermissionChecker;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class BlacklistUtils {
    public static boolean isBlacklisted(BotConfigManager botConfigManager, Member member, List<String> blacklist) {
        return PermissionChecker.checkBotAdmin(member).isPresent()
                && PermissionChecker.checkMemberPermission(botConfigManager, member, Permission.MANAGE_BLACKLIST).isPresent()
                && (member.getRoles().stream().map(Role::getId).anyMatch(blacklist::contains)
                        || blacklist.contains(member.getUser().getId()));
    }
}
