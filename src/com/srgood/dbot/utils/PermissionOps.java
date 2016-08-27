package com.srgood.dbot.utils;

import com.srgood.dbot.Reference;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.managers.RoleManager;

import java.util.Collection;

public class PermissionOps {

    public static Collection<Permissions> getPermissions(Guild guild, User user) {
        return rolesToPermissions(guild, guild.getRolesForUser(user));
    }

    public static Permissions userPermissionLevel(Guild guild, User user) {
        if (Reference.Other.BOT_DEVELOPERS.contains(user.getId())) {
            return Permissions.DEVELOPER;
        } else {
            return getHighestPermission(guild, getPermissions(guild, user));
        }
    }


    public static Permissions getHighestPermission(Guild guild, Collection<com.srgood.dbot.utils.Permissions> Roles) {

        Permissions maxFound = Permissions.STANDARD;

        for (Permissions permLevel : Permissions.values()) {
            if ((permLevel.getLevel() > maxFound.getLevel())) {
                if (containsAny(rolesToPermissions(guild, XMLHandler.getGuildRolesFromInternalName(permLevel.getXMLName(), guild)), Roles)) {
                    maxFound = permLevel;
                    break;
                }
            }
        }
        return maxFound;
    }

    private static Collection<Permissions> rolesToPermissions(Guild guild, Collection<? extends net.dv8tion.jda.entities.Role> roles) {
        return roles.stream().map(role -> XMLHandler.roleToPermission(role, guild)).collect(java.util.stream.Collectors.toList());
    }

    private static boolean containsAny(Collection<?> container, Collection<?> checkFor) {
        for (Object o : checkFor) {
            if (container.contains(o)) {
                return true;
            }
        }
        return false;
    }

    public static Permissions intToEnum(int level) {
        for (Permissions p : Permissions.values()) {
            if (p.level == level) {
                return p;
            }
        }
        return null;
    }

    public static Role createRole(Permissions roleLevel, Guild guild, boolean addToXML) {
        if (!roleLevel.isVisible()) return null;

        RoleManager roleMgr = guild.createRole();
        roleMgr.setName(roleLevel.getReadableName());
        roleMgr.setColor(roleLevel.getColor());

        roleMgr.update();

        Role role = roleMgr.getRole();

        if (addToXML) {
            XMLHandler.registerRole(guild, role, roleLevel);
        }


        return role;
    }

}
