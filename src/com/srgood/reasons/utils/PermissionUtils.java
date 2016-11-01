package com.srgood.reasons.utils;

import com.srgood.reasons.PermissionLevels;
import com.srgood.reasons.Reference;
import com.srgood.reasons.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.managers.RoleManager;

import java.util.Collection;

public class PermissionUtils {

    public static Collection<com.srgood.reasons.PermissionLevels> getPermissions(Guild guild, User user) {
        return rolesToPermissions(guild, guild.getRolesForUser(user));
    }

    public static com.srgood.reasons.PermissionLevels userPermissionLevel(Guild guild, User user) {
        if (Reference.Other.BOT_DEVELOPERS.contains(user.getId())) {
            return com.srgood.reasons.PermissionLevels.DEVELOPER;
        } else {
            return getHighestPermission(guild, getPermissions(guild, user));
        }
    }


    public static com.srgood.reasons.PermissionLevels getHighestPermission(Guild guild, Collection<com.srgood.reasons.PermissionLevels> Roles) {

        com.srgood.reasons.PermissionLevels maxFound = com.srgood.reasons.PermissionLevels.STANDARD;

        for (com.srgood.reasons.PermissionLevels permLevel : com.srgood.reasons.PermissionLevels.values()) {
            if ((permLevel.getLevel() > maxFound.getLevel())) {
                if (containsAny(rolesToPermissions(guild, ConfigUtils.getGuildRolesFromPermission(guild, permLevel)), Roles)) {
                    maxFound = permLevel;
                    break;
                }
            }
        }
        return maxFound;
    }

    private static Collection<com.srgood.reasons.PermissionLevels> rolesToPermissions(Guild guild, Collection<? extends net.dv8tion.jda.entities.Role> roles) {
        return roles.stream().map(role -> ConfigUtils.roleToPermission(role)).collect(java.util.stream.Collectors.toList());
    }

    private static boolean containsAny(Collection<?> container, Collection<?> checkFor) {
        for (Object o : checkFor) {
            if (container.contains(o)) {
                return true;
            }
        }
        return false;
    }

    public static com.srgood.reasons.PermissionLevels intToEnum(int level) {
        for (com.srgood.reasons.PermissionLevels p : com.srgood.reasons.PermissionLevels.values()) {
            if (p.level == level) {
                return p;
            }
        }
        return null;
    }

    /**
     * @deprecated
     */
    public static int enumToInt (PermissionLevels level) {
        return level.getLevel();
    }


    public static Role createRole(com.srgood.reasons.PermissionLevels roleLevel, Guild guild, boolean addToXML) {
        if (!roleLevel.isVisible()) return null;

        RoleManager roleMgr = guild.createRole();
        roleMgr.setName(roleLevel.getReadableName());
        roleMgr.setColor(roleLevel.getColor());

        roleMgr.update();

        Role role = roleMgr.getRole();

        if (addToXML) {
            ConfigUtils.registerRoleConfig(guild, role, roleLevel);
        }


        return role;
    }

    public static PermissionLevels stringToRole(String uRole) {
        System.out.println("" + uRole);

        for (PermissionLevels p : PermissionLevels.values()) {
            if (p.getXMLName().toLowerCase().equals(uRole.toLowerCase())) {
                return p;
            }
        }
        return PermissionLevels.STANDARD;
    }
}
